package com.google.code.lightssh.project.security.shiro;

import java.net.URLEncoder;
import java.util.Date;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.lightssh.common.config.SystemConfig;
import com.google.code.lightssh.common.web.SessionKey;
import com.google.code.lightssh.project.log.service.LoginLogManager;
import com.google.code.lightssh.project.security.entity.LoginAccount;
import com.google.code.lightssh.project.security.service.LoginAccountManager;
import com.google.code.lightssh.project.security.service.SecurityUtil;
import com.google.code.lightssh.project.util.RequestUtil;

/**
 * 扩展“验证码”
 * @author YangXiaojin
 *
 */
public class CaptchaFormAuthenticationFilter extends FormAuthenticationFilter{
	
	private static Logger log = LoggerFactory.getLogger(CaptchaFormAuthenticationFilter.class);
	
	public static final String DEFAULT_CAPTCHA_PARAM = "captcha";
	
	/**
	 * 日志警告登录失败次数
	 */
	public static final int WARN_LOGIN_COUNT = 10;
	
	private String captchaParam = DEFAULT_CAPTCHA_PARAM;
	
	/** 登录日志 */
	private LoginLogManager loginLogManager;
	
	/** 登录帐号 */
	private LoginAccountManager accountManager;
	
	/** 系统参数 */
	private SystemConfig systemConfig;
	
	public String getCaptchaParam() {
        return captchaParam;
    }

	public void setCaptchaParam(String captchaParam) {
		this.captchaParam = captchaParam;
	}

	protected String getCaptcha(ServletRequest request) {
        return WebUtils.getCleanParam(request, getCaptchaParam());
    }
	
    public void setLoginLogManager(LoginLogManager loginLogManager) {
		this.loginLogManager = loginLogManager;
	}

	public void setAccountManager(LoginAccountManager accountManager) {
		this.accountManager = accountManager;
	}

	public void setSystemConfig(SystemConfig systemConfig) {
		this.systemConfig = systemConfig;
	}

	protected CaptchaUsernamePasswordToken createToken(ServletRequest request, ServletResponse response) {
        String username = getUsername(request);
        String password = getPassword(request);
        String captcha = getCaptcha(request);
        boolean rememberMe = isRememberMe(request);
        String host = getHost(request);
                
        return new CaptchaUsernamePasswordToken(username, password, rememberMe, host,captcha);
    }
	
	/**
	 * 验证码认证
	 */
	protected void doCaptchaValidate( HttpServletRequest request ,CaptchaUsernamePasswordToken token ){
		if( SecurityUtil.isCheckCaptcha( request ) && 
			!SecurityUtil.isPassedCaptchaValidate(request, token.getCaptcha() )){
			throw new ShiroBadCaptchaException("验证码错误！");
		}
	}
	/**
	 * 取 LoginAccount
	 * @param username
	 * @return
	 */
	protected LoginAccount fetchLoginAccount( String username ){
		if( username == null )
			return null;
		
		LoginAccount account = accountManager.getWithParty( username );
		
		/*
		if( account != null && account.getParty() != null ){
    		if( account.getParty() instanceof Member ){
    			Member member = (Member)account.getParty();
    			if( PartyStatus.FREEZE.equals(member.getPartyStatus()))
        			throw new AuthenticationException("会员已被冻结！"); 
    		}
		}
		*/
		
		return account;
	}
	/**
	 * 写Cookie
	 */
	protected void writeCookie(HttpServletRequest request,HttpServletResponse response,
		String cookieName,String cookieValue ){
		if( cookieName == null )
			return ;
		
		try{
			if( cookieValue != null )
				cookieValue = URLEncoder.encode(cookieValue,"utf-8");
		}catch( Exception e ){}
		
		Cookie cookie = new Cookie(cookieName,cookieValue);
    	cookie.setPath( request.getContextPath() );
    	response.addCookie( cookie );
	}
    
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
    	CaptchaUsernamePasswordToken token = createToken(request, response);
        if (token == null) {
            String msg = "方法createToken()返回空值，登录操作必须获得非空的AuthenticationToken！";
            throw new IllegalStateException(msg);
        }
        
        LoginAccount account = fetchLoginAccount( token.getUsername() );;
        try {
        	doCaptchaValidate( (HttpServletRequest)request,token );
        	
            Subject subject = getSubject(request, response);
            subject.login(token);
            
            SecurityUtil.clearFailedCount((HttpServletRequest)request );//清除失败次数
            
            //更换用户登录后，刷新页头
            Cookie cookie = new Cookie("REFRESH-HEADER","TRUE");
        	cookie.setPath( ((HttpServletRequest)request).getContextPath() );
        	((HttpServletResponse)response).addCookie( cookie );
            
        	//登录日志
        	try{
        		loginLogManager.login(new Date(),
        				RequestUtil.getIpAddr((HttpServletRequest)request),account );
        	}catch( Exception e ){
        		log.error("写登录日志出现异常:", e );
        	}
			
        	writeSession( account,(HttpServletRequest)request );
            return onLoginSuccess(token, subject, request, response);
        } catch (AuthenticationException e) {
        	SecurityUtil.incFailedCount((HttpServletRequest)request);//登录失败次数
        	
        	int failed_count = SecurityUtil.getFailedCount((HttpServletRequest)request);
        	if( (failed_count % WARN_LOGIN_COUNT) ==0 ){
        		log.warn("登录失败次数超过"+ failed_count +"次:username=" + token.getPrincipal()
        				+ ",ip=" + RequestUtil.getIpAddr((HttpServletRequest)request));
        	}
            return onLoginFailure(token, e, request, response);
        }
    }
    
    protected void writeSession( LoginAccount account ,HttpServletRequest request ){
    	request.getSession().setAttribute(SessionKey.LOGIN_ACCOUNT, account);
    }
    
    /**
     * 登录失败用于前端信息提示
     */
    protected void setFailureAttribute(ServletRequest request, AuthenticationException ae) {
        request.setAttribute(getFailureKeyAttribute(), ae);
    }
    /**
     * 登录成功跳转地址
     */
    public String getSuccessUrl() {
    	//String domain = StringUtil.clean( getDynamicDomain() );
        //return (domain== null?"":domain)+ super.getSuccessUrl();
        
        return super.getSuccessUrl();
    }
    
	/**
	 * 取登录地址
	 * 两情况，常规地址和CAS Server 地址
	 */
    public String getLoginUrl() {
    	if( systemConfig == null || !"true".equalsIgnoreCase(
    			systemConfig.getProperty( ConfigConstants.CAS_ENABLED_KEY, "false")) )
    		return super.getLoginUrl();
    		
        return systemConfig.getProperty( ConfigConstants.CAS_SERVER_URL_PREFIX_KEY )
        	+ "?service=" + systemConfig.getProperty( ConfigConstants.CAS_SERVICE_KEY );
    }

}

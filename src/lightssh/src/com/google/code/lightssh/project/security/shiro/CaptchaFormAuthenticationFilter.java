package com.google.code.lightssh.project.security.shiro;

import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.lightssh.common.config.SystemConfig;
import com.google.code.lightssh.common.support.shiro.CaptchaUsernamePasswordToken;
import com.google.code.lightssh.common.support.shiro.ShiroBadCaptchaException;
import com.google.code.lightssh.common.support.shiro.TimeLockedException;
import com.google.code.lightssh.common.web.SessionKey;
import com.google.code.lightssh.project.log.service.LoginLogManager;
import com.google.code.lightssh.project.security.entity.LoginAccount;
import com.google.code.lightssh.project.security.entity.OnlineUser;
import com.google.code.lightssh.project.security.service.LoginAccountManager;
import com.google.code.lightssh.project.security.service.LoginFailureManager;
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
	
	/**
	 * 在线登录用户缓存
	 */
	private Cache onlineUserCache; 

	/** 登录日志 */
	private LoginLogManager loginLogManager;
	
	/** 登录帐号 */
	private LoginAccountManager accountManager;
	
	/** 登录失败记录*/
	private LoginFailureManager loginFailureManager;
	
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

	public void setLoginFailureManager(LoginFailureManager loginFailureManager) {
		this.loginFailureManager = loginFailureManager;
	}
	
	public void setOnlineUserCache(Cache cache) {
		this.onlineUserCache = cache;
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
		if( isCheckCaptcha( request )){
			String captcha = (String)request.getSession().getAttribute(
					com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
			
			if(captcha!=null && !captcha.equalsIgnoreCase(token.getCaptcha()))
				throw new ShiroBadCaptchaException("验证码错误！");
		}
	}
	
	/**
	 * 业务验证
	 */
	protected void doBusinessValidate( LoginAccount account ){
		if( account == null )
			return;
		
		if( !account.isEnabled() )
			throw new LockedAccountException("用户账号已禁用！");
		
		if( !account.isExpired() )
			throw new ExpiredCredentialsException("用户账号已过期！");
		
		Calendar now = Calendar.getInstance();
		Calendar lockedTime = account.getLastLoginLockTime();
		if( lockedTime != null ){
			int minutes = 10;
			try{
				minutes = Integer.parseInt(systemConfig.getProperty(
					ConfigConstants.LOGIN_FAILURE_LOCK_MINUTES_KEY,"10"));
			}catch(Exception e){
				log.warn("登录锁定时间设置为10分钟。");
			}
			
			lockedTime.add(Calendar.MINUTE, minutes );
			int remain = lockedTime.get( Calendar.MINUTE ) - now.get( Calendar.MINUTE );
			if( now.compareTo(lockedTime) < 1 )
				throw new TimeLockedException("用户账号已被锁定，" +
						(remain>0?remain+"分钟后重试":"请稍后重试")+"！");
		}
	}
	
	/**
	 * 取用户信息
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
	 * 设置登录失败次数
	 */
	private void setFailedCount( HttpServletRequest request,int count ){
		request.getSession().setAttribute(SessionKey.LOGIN_FAILURE_COUNT,count);
	}
	
	/**
	 * 取得登录失败次数
	 */
	private int getFailedCount( HttpServletRequest request ){
		Integer failed_count = (Integer) request.getSession().getAttribute(
				SessionKey.LOGIN_FAILURE_COUNT);
		return (failed_count == null) ? 0 : failed_count;
	}
	
	/**
	 * 添加失败次数
	 */
	protected void incFailedCount( HttpServletRequest request){
		setFailedCount(request,getFailedCount( request )+1);
	}
	
	/**
	 * 清除失败次数
	 */
	protected void clearFailedCount( HttpServletRequest request){
		setFailedCount(request,0);
	}
	
	/**
	 * 锁定登录帐户
	 */
	protected boolean doLock( int failed_count,LoginAccount la ){
		boolean locked = false;
		
		int times = 3;
		try{
			times = Integer.parseInt(systemConfig.getProperty(
				ConfigConstants.LOGIN_FAILURE_LOCK_TIMES_KEY,"3"));
		}catch(Exception e){
			//ignore
		}
		
		if( failed_count >= times ){
			try{
				accountManager.updateLockTime(la);
				locked = true;
			}catch( Exception e ){
				log.warn("更新登录失败锁定时间异常：",e);
			}
		}
		
		return locked;
	}
	
	/**
	 * 是否做验证码检查
	 */
	protected boolean isCheckCaptcha( HttpServletRequest request ){
		boolean enabled = "true".equalsIgnoreCase(systemConfig.getProperty(
				ConfigConstants.CAPTCHA_ENABLED_KEY,"false"));
		int times = 0;
		try{
			times = Integer.parseInt(systemConfig.getProperty(
				ConfigConstants.CAPTCHA_LOGIN_IGNORE_TIMES_KEY,"0"));
		}catch(Exception e){
			//ignore
		}
		
		return enabled && getFailedCount( request ) >= times;
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
        	doBusinessValidate( account ); //业务检查
        	
            Subject subject = getSubject(request, response);
            subject.login(token);
            
            clearFailedCount((HttpServletRequest)request );//清除失败次数
            
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
        	if( !(e instanceof TimeLockedException) ){
	        	incFailedCount((HttpServletRequest)request);//登录失败次数
	        	
	        	//登录失败记录
	        	String ip = RequestUtil.getIpAddr((HttpServletRequest)request);
	        	loginFailureManager.save(account,token.getUsername(),ip
	        			,((HttpServletRequest)request).getSession().getId());
	        	
	        	int failed_count = getFailedCount((HttpServletRequest)request);
	        	boolean locked = doLock( failed_count, account );
	        	if( locked ){
	        		log.warn("登录帐户[{}]在用户[{}]登录时被锁定。"
	        				,account.getIdentity(),token.getUsername());
	        	}
        	}
        	
            return onLoginFailure(token, e, request, response);
        } 
    }
    
    protected void writeSession( LoginAccount account ,HttpServletRequest request ){
    	//写session
    	request.getSession().setAttribute(SessionKey.LOGIN_ACCOUNT, account);
    	
    	//写在线缓存
    	if( this.onlineUserCache != null ){
    		String username = account.getLoginName();
    		onlineUserCache.put(new Element(username
    				,new OnlineUser(username,request) ));
    	}
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

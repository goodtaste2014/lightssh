package com.google.code.lightssh.project.web.action;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.code.lightssh.common.config.SystemConfig;
import com.google.code.lightssh.common.web.SessionKey;
import com.google.code.lightssh.common.web.action.BaseAction;
import com.google.code.lightssh.project.security.service.SecuirtyFramework;
import com.google.code.lightssh.project.security.shiro.ConfigConstants;

/**
 * 
 * @author YangXiaojin
 *
 */
@Component( "securityAction" )
@Scope("prototype")
public class SecurityAction extends BaseAction{

	private static final long serialVersionUID = 1L;
	
	public static final String SECUIRTY_FRAMEWORK_PARAM ="secuirtyFramework";
	
	/** 系统参数 */
	@Resource(name="systemConfig")
	private SystemConfig systemConfig;
	
	/**
	 * 是否显示验证码
	 */
	public boolean isShowCaptcha( ){
		boolean enabled = "true".equalsIgnoreCase(systemConfig.getProperty(
				ConfigConstants.CAPTCHA_ENABLED_KEY,"false"));
		int times = 0;
		try{
			times = Integer.parseInt(systemConfig.getProperty(
				ConfigConstants.CAPTCHA_LOGIN_IGNORE_TIMES_KEY,"0"));
		}catch(Exception e){
			//ignore
		}
		
		Integer failed_count = (Integer) request.getSession().getAttribute(
				SessionKey.LOGIN_FAILURE_COUNT);
		failed_count=(failed_count == null) ? 0 : failed_count;
		
		return enabled && failed_count>=times;
	}
	
	/**
	 * 登出
	 */
	public String logout(){
		try{
			//Spring Security 的退出不会执行到这里
			SecurityUtils.getSubject().logout();
		}catch( Exception e ){
			//e.printStackTrace();
		}
		
		return SUCCESS;
	}
	
	/**
	 * 登录
	 */
	public String login(){		
		String security = ServletActionContext.getServletContext()
			.getInitParameter( SECUIRTY_FRAMEWORK_PARAM );
		
		//如果登录成功(session未失效),再次登录会一直停留在登录页面
		if( SecuirtyFramework.SHIRO.getValue().equalsIgnoreCase( security )
				&& SecurityUtils.getSubject().isAuthenticated() ){
			return SUCCESS;
		}
		
		if( SecuirtyFramework.SHIRO.getValue().equalsIgnoreCase( security ) )
			request.setAttribute("secuirtyFramework", "shiro");
		
		return INPUT;
	}

}

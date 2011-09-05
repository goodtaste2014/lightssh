package com.google.code.lightssh.project.web.action;

import org.apache.shiro.SecurityUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.code.lightssh.common.web.action.BaseAction;
import com.google.code.lightssh.project.security.service.SecuirtyFramework;

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

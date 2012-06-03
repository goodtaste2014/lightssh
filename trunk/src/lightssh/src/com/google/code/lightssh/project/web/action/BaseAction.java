package com.google.code.lightssh.project.web.action;

import com.google.code.lightssh.common.web.SessionKey;
import com.google.code.lightssh.project.security.entity.LoginAccount;

public class BaseAction extends com.google.code.lightssh.common.web.action.BaseAction{

	private static final long serialVersionUID = 1L;
	
	/**
	 * 登录帐户
	 */
	public LoginAccount getLoginAccount( ){
		return (LoginAccount)request.getSession().getAttribute( SessionKey.LOGIN_ACCOUNT );
	}
	
	/**
	 * 登录用户名
	 */
	public String getLoginUser( ){
		LoginAccount la = getLoginAccount();
		return la==null?null:la.getLoginName();
	}

}

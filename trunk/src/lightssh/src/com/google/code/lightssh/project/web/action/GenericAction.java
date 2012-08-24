package com.google.code.lightssh.project.web.action;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.apache.catalina.Globals;

import com.google.code.lightssh.common.entity.Persistence;
import com.google.code.lightssh.common.web.SessionKey;
import com.google.code.lightssh.project.security.entity.LoginAccount;

public class GenericAction<T extends Persistence<?>> extends com.google.code.lightssh.common.web.action.CrudAction<T>{

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
	
	/**
	 * 缓存参数
	 */
	@SuppressWarnings("unchecked")
	protected void cacheRequestParams( ){
		Map<String,Map> cachedParamMap = (Map<String,Map>)request.getSession()
			.getAttribute( SessionKey.REQUEST_PARAMETERS );
		
		if( cachedParamMap == null )
			cachedParamMap = new HashMap<String,Map>();
		
		Map<String,String[]> oneRequestParams = new HashMap<String,String[]>();
		Enumeration names = request.getParameterNames();
		while( names.hasMoreElements() ){
			String name = names.nextElement().toString();
			oneRequestParams.put(name, request.getParameterValues(name));
		}
			
		cachedParamMap.put(request.getRequestURI(),oneRequestParams);
		request.getSession().setAttribute(SessionKey.REQUEST_PARAMETERS, cachedParamMap);
		
	}
	
	/**
	 * 获取缓存的参数
	 */
	@SuppressWarnings("unchecked")
	public Map getCachedParams( ){
		Map<String,Map> cachedParamMap = (Map<String,Map>)request.getSession()
			.getAttribute( SessionKey.REQUEST_PARAMETERS );
		
		if( cachedParamMap == null )
			return null;
		
		return cachedParamMap.get(request.getAttribute( Globals.FORWARD_REQUEST_URI_ATTR ));
	}

}

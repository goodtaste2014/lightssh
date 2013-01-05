package com.google.code.lightssh.project.security.shiro;

import org.apache.shiro.authc.AuthenticationException;

import com.google.code.lightssh.project.security.entity.OnlineUser;

/**
 * 存在登录在线用户
 * @author YangXiaojin
 *
 */
public class OnlineUserExistException extends AuthenticationException{

	private static final long serialVersionUID = -3071547857552085391L;
	
	private OnlineUser user;

	public OnlineUserExistException() {
		super();
	}

	public OnlineUserExistException(OnlineUser user) {
		this.user = user;
	}

	public OnlineUser getUser() {
		return user;
	}
	
}

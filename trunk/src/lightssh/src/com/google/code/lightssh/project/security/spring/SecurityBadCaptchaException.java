package com.google.code.lightssh.project.security.spring;

import org.springframework.security.authentication.AuthenticationServiceException;

import com.google.code.lightssh.project.security.service.BadCaptchaException;

/**
 * Spring Security 验证码异常
 * @author YangXiaojin
 *
 */
public class SecurityBadCaptchaException extends AuthenticationServiceException 
	implements BadCaptchaException{

	private static final long serialVersionUID = 3719616463845582702L;

	public SecurityBadCaptchaException(String msg, Throwable t) {
		super(msg, t);
	}

	public SecurityBadCaptchaException(String msg) {
		super(msg);
	}

}

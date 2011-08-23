package com.google.code.lightssh.project.security.shiro;

import org.apache.shiro.authc.AuthenticationException;

import com.google.code.lightssh.project.security.service.BadCaptchaException;

/**
 * SHIRO验证码异常
 * @author YangXiaojin
 *
 */
public class ShiroBadCaptchaException extends AuthenticationException
	implements BadCaptchaException{

	private static final long serialVersionUID = -4987144327107616457L;

	public ShiroBadCaptchaException() {
		super();
	}

	public ShiroBadCaptchaException(String message, Throwable cause) {
		super(message, cause);
	}

	public ShiroBadCaptchaException(String message) {
		super(message);
	}

	public ShiroBadCaptchaException(Throwable cause) {
		super(cause);
	}

}

package com.google.code.lightssh.project.mail;

/**
 * 邮件发送
 *
 */
public interface MailSenderManager{
	
	/**
	 * 忘记用户名
	 */
	public void forgotUsername(String title, String email );
	
}

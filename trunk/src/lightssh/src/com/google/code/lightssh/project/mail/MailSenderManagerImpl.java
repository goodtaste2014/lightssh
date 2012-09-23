package com.google.code.lightssh.project.mail;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.google.code.lightssh.common.config.SystemConfig;
import com.google.code.lightssh.common.mail.Address;
import com.google.code.lightssh.common.mail.MailAddress;
import com.google.code.lightssh.common.mail.MailContent;
import com.google.code.lightssh.common.mail.sender.MailSender;
import com.google.code.lightssh.common.model.ConnectionConfig;

/**
 * 邮件发送
 *
 */
@Component("mailSenderManager")
public class MailSenderManagerImpl implements MailSenderManager{
	
	/** 
	 * 系统参数 
	 */
	@Resource(name="systemConfig")
	private SystemConfig systemConfig;
	

	@Resource(name="mailSender")
	private MailSender mailSender;
	
	protected ConnectionConfig getEmailConnectionConfig( ){
		if( systemConfig == null )
			return null;
		
		ConnectionConfig config = new ConnectionConfig();
		
		config.setHost( this.systemConfig.getProperty( 
				MailConfigConstants.EMAIL_HOST_KEY ) );
		
		config.setPort( this.systemConfig.getProperty( 
				MailConfigConstants.EMAIL_PORT_KEY ) );
		
		config.setUsername( this.systemConfig.getProperty( 
				MailConfigConstants.EMAIL_USERNAME_KEY ) );
		
		config.setPassword( this.systemConfig.getProperty( 
				MailConfigConstants.EMAIL_PASSWORD_KEY ) );
		
		return config;
	}

	/**
	 * 忘记用户名
	 */
	public void forgotUsername(String title, String email ){
		ConnectionConfig config = getEmailConnectionConfig();
		Address to = new Address(email,title);
		Address from = new Address(config.getUsername(),config.getUsername());
		MailAddress mailAddress = new MailAddress( to,from );
		
		StringBuffer content = new StringBuffer();
		content.append("您在系统的登录帐号为：" + title);
		
		mailSender.send(config,mailAddress
				,new MailContent("用户帮助",content.toString()));
	}
}

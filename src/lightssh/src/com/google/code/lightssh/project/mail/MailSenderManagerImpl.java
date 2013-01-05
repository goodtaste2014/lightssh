package com.google.code.lightssh.project.mail;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.google.code.lightssh.common.config.SystemConfig;
import com.google.code.lightssh.common.mail.Address;
import com.google.code.lightssh.common.mail.MailAddress;
import com.google.code.lightssh.common.mail.MailContent;
import com.google.code.lightssh.common.mail.sender.MailSender;
import com.google.code.lightssh.common.model.ConnectionConfig;
import com.google.code.lightssh.project.security.entity.LoginAccount;

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
		
		config.setSsl( "true".equalsIgnoreCase(systemConfig.getProperty( 
				MailConfigConstants.EMAIL_SSL_KEY ) ));
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
	
	/**
	 * 忘记密码
	 */
	public void forgotPassword(String retrieveUrl, LoginAccount account ){
		if( account == null || StringUtils.isEmpty(account.getEmail()) )
			return ;
		
		ConnectionConfig config = getEmailConnectionConfig();
		Address to = new Address(account.getEmail(),account.getLoginName());
		Address from = new Address(config.getUsername(),config.getUsername());
		MailAddress mailAddress = new MailAddress( to,from );
		
		StringBuffer content = new StringBuffer();
		content.append("<html>");
		//content.append("<img src=\"http://www.gstatic.com/codesite/ph/images/defaultlogo.png\"></br>");
		content.append("\n要重设您系统帐户 "+account.getLoginName()+" 的密码，请点击以下链接：" );
		content.append( "<a href=\""+retrieveUrl+"\">点击重设密码</a>" );
		content.append("\n如果点击以上链接没有反应，请将该网址复制并粘贴到新的浏览器窗口中。" );
		content.append("\n"+retrieveUrl);
		content.append("</html>");
		
		mailSender.sendHtml(config,mailAddress
				,new MailContent("用户帮助-找回登录密码",content.toString()));

	}
}

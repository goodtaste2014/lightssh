package com.google.code.lightssh.project.security.web;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.apache.cxf.common.util.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.struts2.json.annotations.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.code.lightssh.common.model.page.ListPage;
import com.google.code.lightssh.common.util.CryptographyUtil;
import com.google.code.lightssh.common.util.StringUtil;
import com.google.code.lightssh.common.web.SessionKey;
import com.google.code.lightssh.project.mail.MailSenderManager;
import com.google.code.lightssh.project.security.entity.LoginAccount;
import com.google.code.lightssh.project.security.entity.Role;
import com.google.code.lightssh.project.security.service.LoginAccountManager;
import com.google.code.lightssh.project.web.action.GenericAction;

/**
 * LoginAccount Action 
 * @author YangXiaojin
 *
 */
@Component( "loginAccountAction" )
@Scope("prototype")
public class LoginAccountAction extends GenericAction<LoginAccount>{
	
	private static final long serialVersionUID = 2391150894472042768L;

	private static Logger log = LoggerFactory.getLogger(LoginAccountAction.class);
	
	@Resource(name="mailSenderManager")
	private MailSenderManager mailSenderManager;
	
	private LoginAccount account;
	
	private List<String> passwords = new ArrayList<String>();
	
	private String ids;
	
	private boolean passed;

	/**
	 * 安全值 
	 */
	private String safeMessage;
	
	@Resource( name="loginAccountManager" )
	public void setLoginAccountManager( LoginAccountManager manager ){
		super.manager = manager;
	}
	
	public LoginAccountManager getManager( ){
		return (LoginAccountManager)super.manager;
	}

	public List<String> getPasswords() {
		return passwords;
	}

	public void setPasswords(List<String> passwords) {
		this.passwords = passwords;
	}

	public LoginAccount getAccount() {
		this.account = super.model;
		return account;
	}

	public void setAccount(LoginAccount account) {
		this.account = account;
		super.model = this.account;
	}
	
	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getSafeMessage() {
		return safeMessage;
	}

	public void setSafeMessage(String safeMessage) {
		this.safeMessage = safeMessage;
	}

	@JSON(name="passed")
	public boolean isPassed() {
		return passed;
	}

	public void setPassed(boolean passed) {
		this.passed = passed;
	}

	public String list() {
		if ( page == null )
			page = new ListPage<LoginAccount>();
		page.addAscending( "createDate" );

		if ( this.isPost() )
			cacheRequestParams();
		return super.list();
	}
	
	public String edit( ){
		if( request.getParameter("password") != null )
    		return "password";
		else if( request.getParameter("role") != null 
				&& account != null && account.getId() != null ){
			setAccount( this.getManager().get(account) );
			if( account == null ){
				saveErrorMessage("添加角色异常，系统找不到相关账号！");
				return ERROR;
			}
			
			return "role";
		}
		
		return super.edit();
	}
	
	/**
	 * 显示登录账户信息
	 */
	public String view( ){
		if( account == null || (account.getIdentity() == null 
				&& StringUtil.clean(account.getLoginName()) == null) )
			return INPUT;
		
		if( account.getIdentity() != null )
			setAccount( getManager().get( account ));
		else
			setAccount(getManager().get( account.getLoginName() ));
		
		if( account == null )
			return INPUT;
		
		return SUCCESS;
	}
	
	public String save( ){
		if( this.account != null && this.account.getPeriod() != null 
				&& this.account.getPeriod().getEnd() != null ){
			//结束时间 yyyy-MM-dd 23:59:59
			Calendar calendar = Calendar.getInstance();
			calendar.setTime( account.getPeriod().getEnd() );
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			calendar.add(Calendar.MILLISECOND, -1);
			this.account.getPeriod().setEnd( calendar.getTime() );
		}
		
		if( account != null && account.getParty() != null 
				&& StringUtils.isEmpty(account.getParty().getId())){
			account.setParty(null);
		}else{
			account.setPartyId( account.getParty().getId() );
		}
		try{
			this.getManager().save(account);
		}catch( Exception e ){
			return INPUT;
		}
		
		return SUCCESS;
	}
	
	/**
	 * update password
	 */
	public String password(){
		if( super.isGet() )
			return INPUT;
		
		String loginName = SecurityUtils.getSubject().getPrincipal().toString();
		
		if( loginName == null )
			return LOGIN;
		
		try{
			this.getManager().updatePassword( loginName,
					passwords.get( 0 ), passwords.get( 1 ) );
			super.saveSuccessMessage("修改密码成功！");
		}catch( Exception e ){
			super.saveErrorMessage( e.getMessage() );
		}
		
		return INPUT;
	}
	
	/**
	 * 修改用户角色
	 * @return
	 */
	public String role( ){
		if( account != null && account.getId() != null ){
			if( ids != null ){
				for( String id:ids.split(",") ){
					Role role = new Role();
					role.setId( id );
					account.addRole( role );
				}//end for
			}
			
			try{
				getManager().updateRole(account);
				saveSuccessMessage("成功更改用户角色！");
			}catch( Exception e ){
				saveErrorMessage( "更新角色异常：" + e.getMessage() );
			}
		}
		
		return SUCCESS;
	}
	
	/**
	 * 查询当前登录帐号信息
	 */
	public String myprofile( ){
		if( this.getLoginUser() == null )
			return LOGIN;
		
		this.setAccount( getManager().get( getLoginUser() ) );
		if( this.account == null )
			return LOGIN;
		
		return SUCCESS;
	}
	
	/**
	 * 重置密码
	 */
	public String prereset(){
		if( account == null || account.getLoginName() == null ){
			this.saveErrorMessage("重置密码参数错误！");
			return INPUT;
		}
		
		return SUCCESS;
	}
	
	/**
	 * 重置密码
	 */
	public String resetpassword(){
		if( account == null || account.getLoginName() == null
				|| passwords == null || passwords.isEmpty() ){
			this.addActionError("请求参数错误！");
			return INPUT;
		}
		
		if( this.getLoginUser().equals(account.getLoginName()) ){
			this.addActionError("不能重置当前登录用户的密码！");
			return INPUT;
		}
		
		try{
			getManager().resetPassword(account.getLoginName(),passwords.get(0));
		}catch( Exception e ){
			super.addActionError("重置密码异常："+e.getMessage());
		}
		
		saveSuccessMessage("帐号["+account.getLoginName()+"]登录密码已被成功重置！");
		log.info("操作员[{}]成功重置帐号[{}]登录密码！"
				,getLoginUser(),account.getLoginName());
		
		return SUCCESS;
	}
	
	/**
	 * 帐号恢复
	 */
	public String recovery(){
		SecurityUtils.getSubject().logout();
		
		//clean Session
		request.getSession().setAttribute(
				SessionKey.LOGIN_ACCOUNT,null);
		return SUCCESS;
	}
	
	/**
	 * 忘记密码
	 */
	public String forgotpassword(){
		if( this.isGet() )
			return INPUT;
		
		if( account == null || account.getLoginName() == null )
			return INPUT;
		
		String subject = account.getLoginName();
		if( subject.indexOf("@") > 0 )
			setAccount( getManager().getByEmail(subject) );
		else
			setAccount( getManager().get(subject) );
		
		if( account ==  null ){
			this.saveErrorMessage("没有找到与该登录用户名或电子邮件地址关联的帐户信息！");
			return INPUT;
		}
		
		return SUCCESS;
	}
	
	/**
	 * 通过邮箱找回密码
	 */
	public String emailrecovery( ){
		if( account == null || account.getLoginName() == null )
			return INPUT;
		
		cleanCaptcha();//防止重复提交
		
		LoginAccount la = this.getManager().get(account.getLoginName());
		if( la == null ){
			this.addActionError("无法找回与用户"+account.getLoginName()+"匹配的帐号信息！");
			return INPUT;
		}
		
		this.setAccount(la);
		StringBuffer url = new StringBuffer();
		url.append("HTTP/1.1".equals(request.getProtocol())?"http://":"https://");
		url.append( request.getRemoteHost());
		url.append( request.getLocalPort()!=80?":"+request.getLocalPort():"");
		url.append( request.getContextPath() );
		
		url.append("/security/recovery/emailretrieve.do");
		url.append("?account.loginName="+account.getLoginName());
		url.append("&message=");
		url.append(CryptographyUtil.hashSha1Hex(account.getEmail()+account.getPassword()));
		try{
			mailSenderManager.forgotPassword(url.toString(),account);
		}catch( Exception e ){
			this.addActionError("找回密码异常："+e.getMessage());
			return INPUT;
		}
		
		return SUCCESS;
	}
	
	/**
	 * 邮件重置密码
	 */
	public String emailretrieve(){
		String message = request.getParameter("message");
		if( account == null || account.getLoginName() == null || message == null ){
			this.addActionError("错误的链接地址！");
			return INPUT;
		}
		
		LoginAccount la = this.getManager().get(account.getLoginName());
		if( la == null ){
			this.addActionError("非法或过期的链接地址！");
			return INPUT;
		}
		this.setAccount(la);
		
		String checkMsg = CryptographyUtil.hashSha1Hex(
				account.getEmail()+account.getPassword());
		if( !message.equals(checkMsg) ){
			this.addActionError("非法或过期的链接地址！");
			return INPUT;
		}
		
		//用于重置密码的"安全值"
		safeMessage = CryptographyUtil.hashSha1Hex(
				request.getSession().getId() 
				+ account.getLoginName() + account.getPassword());
		
		return SUCCESS;
	}
	
	/**
	 * 重置密码
	 */
	public String retrieve( ){
		if( safeMessage == null || account == null 
				|| account.getLoginName() == null )
			return ERROR;
		
		setAccount( this.getManager().get(account.getLoginName()) );
		if( account == null )
			return ERROR;
		
		if( passwords == null || passwords.isEmpty() ){
			this.addFieldError("passwords[0]","未设置新密码！");
			return INPUT;
		}

		//用于重置密码的"安全值"
		String checkValue = CryptographyUtil.hashSha1Hex(
					request.getSession().getId() 
					+ account.getLoginName() + account.getPassword());
		
		if( !safeMessage.equals(checkValue) ){
			return ERROR;
		}
		
		try{
			getManager().resetPassword(account.getLoginName(),passwords.get(0));
			
			login(account);//登录
			this.cleanCaptcha(); //防止重复登录
		}catch( Exception e ){
			log.error("重设帐号[{}]密码异常：",account.getLoginName(),e);
			return ERROR;
		}
		
		return SUCCESS;
	}
	
	/**
	 * 忘记用户名
	 */
	public String forgotusername(){
		if( this.isGet() )
			return INPUT;
		
		cleanCaptcha();//防止重复提交
		
		if( account == null || account.getEmail() == null )
			return INPUT;
		
		try{
			
			String email = account.getEmail();
			setAccount(getManager().getByEmail( email ));
			
			if( account != null ){
				//send username to email address
				mailSenderManager.forgotUsername(account.getUsername(),email);
			}
		}catch( Exception e ){
			log.warn("找回用户名过程出现异常：",e);
		}
		
		return SUCCESS;
	}
	
	/**
	 * 释放登录锁
	 */
	public String releaselock( ){
		if( account == null || account.getIdentity() == null )
			return INPUT;
		
		try{
			this.getManager().releaseLockTime(account);
		}catch( Exception e ){
			this.saveErrorMessage("释放登录锁异常："+e.getMessage());
			return INPUT;
		}
		
		return SUCCESS;
	}
	/**
	 * 校验当前登录用户密码
	 */
	public String validatePassword(){
		String password = request.getParameter("password");
		if( getLoginAccount() != null && getLoginAccount().getPassword() != null 
				&& getLoginAccount().getPassword().equals( password ) ){
			this.passed = true;
		}
		
		return SUCCESS;
	}
}

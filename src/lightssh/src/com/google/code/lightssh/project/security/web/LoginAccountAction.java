package com.google.code.lightssh.project.security.web;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.code.lightssh.common.model.page.ListPage;
import com.google.code.lightssh.common.web.action.CrudAction;
import com.google.code.lightssh.project.security.entity.LoginAccount;
import com.google.code.lightssh.project.security.entity.Role;
import com.google.code.lightssh.project.security.service.LoginAccountManager;
import com.google.code.lightssh.project.security.service.SecurityUtil;

/**
 * LoginAccount Action 
 * @author YangXiaojin
 *
 */
@Component( "loginAccountAction" )
@Scope("prototype")
public class LoginAccountAction extends CrudAction<LoginAccount>{
	
	private static final long serialVersionUID = 2391150894472042768L;
	
	private LoginAccount account;
	
	private List<String> passwords = new ArrayList<String>();
	
	private String ids;
	
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

	public String list(){
		if( page == null )
			page = new ListPage<LoginAccount>( );
		page.addAscending("createDate");
		
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
		
		return super.save();
	}
	
	/**
	 * update password
	 */
	public String password(){
		if( super.isGet() )
			return INPUT;
		
		String loginName = SecurityUtil.getPrincipal();
		
		if( loginName == null )
			return LOGIN;
		
		try{
			this.getManager().updatePassword( loginName,passwords.get(0),passwords.get(1));
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
	
	public String myprofile( ){
		String loginName = SecurityUtil.getPrincipal();
		if( loginName == null )
			return LOGIN;
		
		this.setAccount( getManager().get( loginName ) );
		if( this.account == null )
			return LOGIN;
		
		return SUCCESS;
	}
	
}

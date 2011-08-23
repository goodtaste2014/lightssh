package com.google.code.lightssh.project.security.service;

import com.google.code.lightssh.common.service.BaseManager;
import com.google.code.lightssh.project.security.entity.LoginAccount;

/**
 * LoginAccount Manager
 * @author YangXiaojin
 *
 */
public interface LoginAccountManager extends BaseManager<LoginAccount>{
	
	/**
	 * 根据名称查询登录账号
	 */
	public LoginAccount get( String name );
	
	/**
	 * 初始化系统管理员登录账号
	 */
	public void initLoginAccount( );
	
	/**
	 * 更新密码
	 */
	public void updatePassword( String name,String password,String newPassword );
	
	/**
	 * 更新角色
	 */
	public void updateRole( LoginAccount account );

}

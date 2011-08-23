package com.google.code.lightssh.project.security.dao;

import com.google.code.lightssh.common.dao.Dao;
import com.google.code.lightssh.project.security.entity.LoginAccount;

/**
 * LoginAccount Dao
 * @author YangXiaojin
 *
 */
public interface LoginAccountDao extends Dao<LoginAccount>{
	
	/**
	 * get by login name
	 */
	public LoginAccount get( String loginName );
	
	/**
	 * 更新角色
	 */
	public void updateRole( LoginAccount account );

}

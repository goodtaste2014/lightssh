package com.google.code.lightssh.project.security.dao;

import java.util.List;

import com.google.code.lightssh.common.dao.Dao;
import com.google.code.lightssh.common.model.page.ListPage;
import com.google.code.lightssh.project.security.entity.LoginAccount;
import com.google.code.lightssh.project.security.entity.Permission;

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

	/**
	 * 分页查询LoginAccount id 和登录名
	 */
	public ListPage<LoginAccount> listLight(ListPage<LoginAccount> page,LoginAccount t );
	
	/**
	 * 根据权限查询登录账户
	 */
	public List<LoginAccount> listByPermission(Permission p );
	
	/**
	 * 查询带Party ID
	 */
	public LoginAccount getWithPartyIdentity( String loginName);
	
}

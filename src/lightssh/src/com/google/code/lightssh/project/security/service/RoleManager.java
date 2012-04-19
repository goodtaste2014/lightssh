package com.google.code.lightssh.project.security.service;

import java.util.Collection;

import com.google.code.lightssh.common.service.BaseManager;
import com.google.code.lightssh.project.log.entity.Access;
import com.google.code.lightssh.project.security.entity.Navigation;
import com.google.code.lightssh.project.security.entity.Role;

/**
 * role Manager
 * @author YangXiaojin
 *
 */
public interface RoleManager extends BaseManager<Role>{
	
	/**
	 * 角色添加权
	 */
	public void addPermission( Role role, Collection<Navigation> colls );
	
	/**
	 * 角色添加权 并添加日志
	 */
	public void addPermission(Role role, Collection<Navigation> colls,Access log);
	
	/**
	 * 初始化系统管理员角色
	 */
	public Role initRole( boolean forceUpdatePermission );

}

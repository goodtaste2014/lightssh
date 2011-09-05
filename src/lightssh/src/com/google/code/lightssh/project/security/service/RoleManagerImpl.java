package com.google.code.lightssh.project.security.service;

import java.util.Collection;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.google.code.lightssh.common.ApplicationException;
import com.google.code.lightssh.common.dao.DaoException;
import com.google.code.lightssh.common.model.page.ListPage;
import com.google.code.lightssh.common.service.BaseManager;
import com.google.code.lightssh.common.service.BaseManagerImpl;
import com.google.code.lightssh.project.security.dao.RoleDao;
import com.google.code.lightssh.project.security.entity.Navigation;
import com.google.code.lightssh.project.security.entity.Permission;
import com.google.code.lightssh.project.security.entity.Role;

/**
 * Role Manager 实现
 * @author YangXiaojin
 *
 */
@Component("roleManager")
public class RoleManagerImpl extends BaseManagerImpl<Role> implements RoleManager{
	
	/** 系统超级管理员角色  */
	public static final String SUPER_ROLE = "Super Role";
	
	@Resource(name="navigationManager")
	private NavigationManager navigationManager;
	
	@Resource(name="permissionManager")
	private BaseManager<Permission> permissionManager;
	
	@Resource(name="roleDao")
	public void setRoleDao( RoleDao roleDao ){
		super.dao = roleDao;
	}
	
	public RoleDao getRoleDao(){
		return (RoleDao)super.dao;
	}
	
	public void setNavigationManager(NavigationManager navigationManager) {
		this.navigationManager = navigationManager;
	}
	
	public void setPermissionManager(BaseManager<Permission> permissionManager) {
		this.permissionManager = permissionManager;
	}

	/**
	 * 只针对名称及描述进行修改
	 */
	public void save( Role role ){
		boolean update = (role != null && role.getId() != null);
		
		Role exist = getRoleDao().get( role.getName() );
		if( exist != null && !exist.getIdentity().equals( role.getIdentity()))
			throw new SecurityException( "该角色名称'"+role.getName()+"'已经存在！" );
		
		if( update ){
			Role db_role = get( role );
			if( db_role == null )
				throw new DaoException( "该角色已不存在，不能进行修改操作！" );
			
			db_role.setName( role.getName() );
			db_role.setDescription( role.getDescription() );
			dao.update( db_role );
		}else{
			dao.create( role );
		}
	}

	@Override
	public void addPermission(Role role, Collection<Navigation> colls) {
		if( role == null )
			return;
		
		Role db_role = get(role);
		if( db_role == null )
			throw new ApplicationException("角色信息在系统不存在！");
		
		//update permission
		Collection<Permission> pers = navigationManager.listPermission(colls);
		db_role.setPermissions( null );
		db_role.addPermission(pers);
		
		//db_role.setName( role.getName() );
		
		save( db_role );
	}

	@Override
	public Role initRole( boolean forceUpdatePermission ) {
		Role role = getRoleDao().get(SUPER_ROLE);
		if( role == null ){
			role = new Role();
			role.setCreateDate( new Date() );
			role.setDescription("系统初始化创建.");
			role.setName(SUPER_ROLE);
			role.setReadonly( Boolean.TRUE );
		}
		//是否更新角色权限
		
		//list all permission
		ListPage<Permission> page = new ListPage<Permission>(Integer.MAX_VALUE);
		page = permissionManager.list(page);
		if( role.getId() == null || forceUpdatePermission ){
			role.addPermission( page.getList() );
		}
		
		getRoleDao().create( role );
		
		return role;
	}


}

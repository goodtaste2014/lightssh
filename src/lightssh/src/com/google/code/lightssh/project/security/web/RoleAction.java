package com.google.code.lightssh.project.security.web;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.json.annotations.JSON;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.code.lightssh.common.model.page.ListPage;
import com.google.code.lightssh.common.web.action.CrudAction;
import com.google.code.lightssh.project.security.entity.Navigation;
import com.google.code.lightssh.project.security.entity.Role;
import com.google.code.lightssh.project.security.service.NavigationManager;
import com.google.code.lightssh.project.security.service.RoleManager;

/**
 * Role Action
 * @author YangXiaojin
 *
 */
@Component( "roleAction" )
@Scope("prototype")
public class RoleAction extends CrudAction<Role>{

	private static final long serialVersionUID = 1L;
	
	private Role role;
	
	private List<Navigation> p_list;
	
	private RoleManager roleManager;
	
	@Resource( name="navigationManager" )
	private NavigationManager navigationManager;
	
	@JSON(name="page")
	public ListPage<Role> getPage( ){
		return super.getPage();
	}
	
	@Resource( name="roleManager" )
	public void setRoleManager( RoleManager roleManager ){
		this.roleManager = roleManager;
		super.manager = this.roleManager;
	}

	public void setNavigationManager(NavigationManager navigationManager) {
		this.navigationManager = navigationManager;
	}

	public Role getRole() {
		role = super.model;
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
		super.model = role;
	}
	
	public List<Navigation> getP_list() {
		return p_list;
	}

	public void setP_list(List<Navigation> pList) {
		p_list = pList;
	}

	public String save( ){
		if( role != null ){
			role.setReadonly( Boolean.FALSE );
			role.setCreateDate( new Date() );
		}
		
		String result = super.save();
		if( request.getParameter("saveAndAuthorize") != null )
			result = "authorize";
		
		return result;
	}
	
	public String list( ){
		if( page == null )
			page = new ListPage<Role>( );
		page.addAscending("createDate");
		
		return super.list();
	}
	
	public String permission( ){
		if( role == null || role.getId() == null ){
			saveErrorMessage("请选择'角色'！");
			return INPUT;
		}
		
		setRole( roleManager.get(role) );
		if( role == null ){
			saveErrorMessage("找不到相关'角色'！");
			return INPUT;
		}
		
		//导航
		List<Navigation> list = navigationManager.listTop();
		request.setAttribute("nav_list", list);
		
		return SUCCESS;
	}
	
	/**
	 * 保存角色权限
	 */
	public String addPermission( ){
		if( role == null || role.getId() == null )
			return INPUT;
		
		try{
			//roleManager.save( role ,p_list );
			roleManager.addPermission( role,p_list );
		}catch( Exception e ){
			saveErrorMessage("角色添加权限异常：" + e.getMessage() );
			return INPUT;
		}
		
		role = roleManager.get(role);
		if( role == null ){
			return INPUT;
		}
		
		return SUCCESS;
	}
	
}

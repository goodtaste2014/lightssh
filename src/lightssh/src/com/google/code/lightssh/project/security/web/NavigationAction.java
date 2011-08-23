package com.google.code.lightssh.project.security.web;

import java.util.List;

import com.google.code.lightssh.common.web.action.CrudAction;
import com.google.code.lightssh.project.security.entity.Navigation;
import com.google.code.lightssh.project.security.service.NavigationManager;

/**
 * Navigation Action
 * @author YangXiaojin
 *
 */
public class NavigationAction extends CrudAction<Navigation>{

	private static final long serialVersionUID = 9168395737762383186L;
	
	private NavigationManager navigationManager;
	
	public void setNavigationManager(NavigationManager navigationManager) {
		this.navigationManager = navigationManager;
		super.manager = this.navigationManager;
	}

	public String list( ){
		List<Navigation> list = this.navigationManager.listTop();
		request.setAttribute("nav_list", list);
		
		return SUCCESS;
	}

}

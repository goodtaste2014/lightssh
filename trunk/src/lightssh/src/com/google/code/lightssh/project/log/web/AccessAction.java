package com.google.code.lightssh.project.log.web;

import com.google.code.lightssh.common.model.page.ListPage;
import com.google.code.lightssh.common.web.action.CrudAction;
import com.google.code.lightssh.project.log.entity.Access;
import com.google.code.lightssh.project.log.entity.History;
import com.google.code.lightssh.project.log.service.AccessManager;

/**
 * 
 * @author YangXiaojin
 *
 */
public class AccessAction extends CrudAction<Access>{

	private static final long serialVersionUID = -1040098371572994529L;
	
	private Access access;
	
	private AccessManager accessManager;

	public void setAccessManager(AccessManager accessManager) {
		this.accessManager = accessManager;
		super.manager  = this.accessManager;
	}

	public Access getAccess() {
		this.access = super.model;
		return access;
	}

	public void setAccess(Access access) {
		this.access = access;
		super.model = this.access;
	}
	
	public String list( ){
		if( page == null )
			page = new ListPage<Access>();
		
		page.addDescending("time");
		
		return super.list();
	}
	
	public String compare( ){
		History history = accessManager.getHistory(access);
		request.setAttribute("history", history );
		
		return SUCCESS;
	}

}

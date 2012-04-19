package com.google.code.lightssh.project.param.web;

import javax.annotation.Resource;

import org.apache.struts2.json.annotations.JSON;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.code.lightssh.common.model.page.ListPage;
import com.google.code.lightssh.common.web.action.CrudAction;
import com.google.code.lightssh.project.param.entity.SystemParam;
import com.google.code.lightssh.project.param.service.SystemParamManager;

/**
 * 系统参数Action
 * @author YangXiaojin
 *
 */
@Component( "systemParamAction" )
@Scope("prototype")
public class SystemParamAction extends CrudAction<SystemParam>{
	
	private static final long serialVersionUID = 1L;

	private SystemParam param;

	@Resource( name="systemParamManager" )
	public void setSystemParamManager(SystemParamManager systemParamManager) {
		super.manager = systemParamManager;
	}
	
	public SystemParamManager getManager(){
		return (SystemParamManager)this.manager;
	}

	public SystemParam getParam() {
		param = super.model;
		return param;
	}

	public void setParam(SystemParam param) {
		this.param = param;
		super.model = this.param;
	}
	
	@JSON(name="unique")
	public boolean isUnique() {
		return unique;
	}
	
  	public String unique( ){
		this.unique = this.getManager().isUniqueGroupAndName( param );
		return SUCCESS;
	}
	
	public String list(){
		if( page == null )
			page = new ListPage<SystemParam>();
		
		page.addDescending("createdTime");
		
		return super.list();
	}

}

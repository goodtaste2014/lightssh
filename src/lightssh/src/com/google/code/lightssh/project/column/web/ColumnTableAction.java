package com.google.code.lightssh.project.column.web;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.code.lightssh.project.column.entity.ColumnTable;
import com.google.code.lightssh.project.column.service.ColumnTableManager;
import com.google.code.lightssh.project.web.action.GenericAction;

/**
 * 
 * @author Aspen
 * @date 2013-10-30
 * 
 */
@Component( "columnTableAction" )
@Scope("prototype")
public class ColumnTableAction extends GenericAction<ColumnTable>{

	private static final long serialVersionUID = 8196447291429366339L;
	
	private ColumnTable table;
	
	@Resource(name="columnTableManager")
	public void setManager(ColumnTableManager mgr){
		this.manager = mgr;
	}
	
	public ColumnTable getTable() {
		this.table = this.model;
		return table;
	}

	public void setTable(ColumnTable table) {
		this.table = table;
		this.model = this.table;
	}

}

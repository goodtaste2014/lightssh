package com.google.code.lightssh.project.column.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.google.code.lightssh.common.service.BaseManagerImpl;
import com.google.code.lightssh.project.column.dao.ColumnTableDao;
import com.google.code.lightssh.project.column.entity.ColumnTable;

/**
 * 
 * @author Aspen
 * @date 2013-10-30
 * 
 */
@Component("columnTableManager")
public class ColumnTableManagerImpl extends BaseManagerImpl<ColumnTable> implements ColumnTableManager{

	private static final long serialVersionUID = 1101857819919107633L;
	
	@Resource(name="columnTableDao")
	public void setDao(ColumnTableDao dao){
		this.dao = dao;
	}
	
	public ColumnTableDao getDao( ){
		return (ColumnTableDao)this.dao;
	}

}

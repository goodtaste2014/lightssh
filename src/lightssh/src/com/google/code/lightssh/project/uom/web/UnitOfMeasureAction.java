package com.google.code.lightssh.project.uom.web;

import com.google.code.lightssh.common.web.action.CrudAction;
import com.google.code.lightssh.project.uom.entity.UnitOfMeasure;
import com.google.code.lightssh.project.uom.service.UnitOfMeasureManager;

/**
 * 
 * @author YangXiaojin
 *
 */
public class UnitOfMeasureAction extends CrudAction<UnitOfMeasure>{

	private static final long serialVersionUID = 1L;
	
	private UnitOfMeasure uom;

	public void setUomManager(UnitOfMeasureManager uomManager) {
		super.manager = uomManager;
	}
	
	public UnitOfMeasureManager getManager( ){
		return (UnitOfMeasureManager)super.manager;
	}

	public UnitOfMeasure getUom() {
		this.uom = super.model;
		return uom;
	}

	public void setUom(UnitOfMeasure uom) {
		this.uom = uom;
		super.model = this.uom;
	}
	
	public String toggle( ){
		try{
			getManager().toggleActive(uom);
		}catch( Exception e ){
			this.saveErrorMessage(e.getMessage());
		}
		
		return SUCCESS;
	}

}

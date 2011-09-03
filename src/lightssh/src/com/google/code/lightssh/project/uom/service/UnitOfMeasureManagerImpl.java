package com.google.code.lightssh.project.uom.service;

import java.util.List;

import com.google.code.lightssh.common.model.page.ListPage;
import com.google.code.lightssh.common.service.BaseManagerImpl;
import com.google.code.lightssh.project.uom.entity.UnitOfMeasure;
import com.google.code.lightssh.project.uom.entity.UnitOfMeasure.UomType;

/**
 * 
 * @author YangXiaojin
 *
 */
public class UnitOfMeasureManagerImpl extends BaseManagerImpl<UnitOfMeasure> implements UnitOfMeasureManager{

	private List<UnitOfMeasure> list(UomType type,Boolean active) {
		ListPage<UnitOfMeasure> page = new ListPage<UnitOfMeasure>( Integer.MAX_VALUE );
		UnitOfMeasure uom = new UnitOfMeasure( );
		uom.setType(type);
		uom.setActive( active );
		page = super.dao.list(page, uom);
		
		return page.getList();
	}
	
	@Override
	public List<UnitOfMeasure> list(UomType type){
		return list( type,null );
	}

	@Override
	public List<UnitOfMeasure> listActivity(UomType type) {
		return list( type, Boolean.TRUE );
	}

	@Override
	public void toggleActive(UnitOfMeasure uom) {
		if( uom == null || uom.getIdentity() == null )
			return;
		
		UnitOfMeasure db_uom = this.get( uom );
		if( db_uom != null ){
			db_uom.setActive( !db_uom.isActive() );
			dao.update( db_uom );
		}
	}
	
}

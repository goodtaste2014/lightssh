package com.google.code.lightssh.project.uom.dao;

import java.util.ArrayList;
import java.util.List;

import com.google.code.lightssh.common.dao.hibernate.HibernateDao;
import com.google.code.lightssh.common.model.page.ListPage;
import com.google.code.lightssh.common.util.StringUtil;
import com.google.code.lightssh.project.uom.entity.UnitOfMeasure;

/**
 * 
 * @author YangXiaojin
 *
 */
public class UnitOfMeasureDaoHibernate extends HibernateDao<UnitOfMeasure>{
	
	public ListPage<UnitOfMeasure> list(ListPage<UnitOfMeasure> page,UnitOfMeasure t ){
		if( t == null )
			return list( page );
		
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder( );
		
		hql.append( " FROM " + entityClass.getName() + " AS m " );
		hql.append( " WHERE 1=1 ");
		if( t.getType() != null ){
			hql.append( " AND m.type = ? " );
			params.add( t.getType() );
		}
		
		if( t.getActive() != null ){
			hql.append( " AND ( m.active = ? "
					+ (Boolean.FALSE.equals(t.getActive())?"OR m.active IS NULL":"")+")" );
			params.add( t.getActive() );
		}
		
		if( StringUtil.clean( t.getIsoCode() ) != null ){
			hql.append( " AND m.isoCode like ? " );
			params.add( "%" + t.getIsoCode().trim() + "%");
		}
		
		return super.query(page, hql.toString(), params.toArray( ) );
	}

}

package com.google.code.lightssh.project.geo.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.google.code.lightssh.common.dao.hibernate.HibernateAnnotationDao;
import com.google.code.lightssh.common.model.page.ListPage;
import com.google.code.lightssh.common.util.StringUtil;
import com.google.code.lightssh.project.geo.entity.GeographicBoundary;

/**
 * 
 * @author YangXiaojin
 *
 */
@Repository("geoDao")
public class GeographicBoundaryDaoHibernate extends HibernateAnnotationDao<GeographicBoundary>{
	
	public ListPage<GeographicBoundary> list(ListPage<GeographicBoundary> page,GeographicBoundary t ){
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
		
		if( StringUtil.clean( t.getName() ) != null ){
			hql.append( " AND m.name like ? " );
			params.add( "%" + t.getName().trim() + "%");
		}
		
		return super.query(page, hql.toString(), params.toArray( ) );
	}

}

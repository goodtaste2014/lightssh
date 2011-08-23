package com.google.code.lightssh.project.party.dao;

import java.util.ArrayList;
import java.util.List;

import com.google.code.lightssh.common.dao.hibernate.HibernateDao;
import com.google.code.lightssh.common.model.page.ListPage;
import com.google.code.lightssh.project.party.entity.Party;

public class PartyDaoHibernate extends HibernateDao<Party>{
	
	public ListPage<Party> list(ListPage<Party> page,Party t ){
		if( t == null )
			return list( page );
		
		List<Object> params = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer( );
		
		hql.append( " FROM " + entityClass.getName() + " AS m " );
		hql.append( " WHERE 1=1 ");
		if( t.getName() != null && t.getName().trim() != null 
				&& !"".equals(t.getName().trim())){
			hql.append( " AND m.name like ? " );
			params.add( "%" + t.getName().trim() + "%");
		}
		
		return super.query(page, hql.toString(), params.toArray( ) );
	}

}

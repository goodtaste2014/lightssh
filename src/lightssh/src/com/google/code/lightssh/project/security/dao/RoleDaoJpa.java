package com.google.code.lightssh.project.security.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.google.code.lightssh.common.dao.jpa.JpaAnnotationDao;
import com.google.code.lightssh.common.model.page.ListPage;
import com.google.code.lightssh.project.security.entity.Role;

@Repository("roleDao")
public class RoleDaoJpa extends JpaAnnotationDao<Role> implements RoleDao{
	
	public ListPage<Role> list(ListPage<Role> page,Role t ){
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
	
	@SuppressWarnings("unchecked")
	public Role get(String name ) {
		String hql = " SELECT m FROM " + entityClass.getName() + " AS m WHERE m.name = ? ";
		List<Role> results = getJpaTemplate().find(hql, name );
		
		return (results==null||results.isEmpty())?null:results.get(0);
	}

}

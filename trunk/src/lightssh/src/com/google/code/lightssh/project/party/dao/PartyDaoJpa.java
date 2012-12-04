package com.google.code.lightssh.project.party.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.google.code.lightssh.common.dao.jpa.JpaAnnotationDao;
import com.google.code.lightssh.common.model.page.ListPage;
import com.google.code.lightssh.common.util.ReflectionUtil;
import com.google.code.lightssh.project.party.entity.Organization;
import com.google.code.lightssh.project.party.entity.Party;
import com.google.code.lightssh.project.party.entity.Person;

@Repository("partyDao")
public class PartyDaoJpa extends JpaAnnotationDao<Party> implements PartyDao{
	
	private static final long serialVersionUID = -2223709423588592533L;

	public ListPage<Party> list(ListPage<Party> page,Party t ){
		return this.list( super.entityClass, page, t );
	}
	
	public ListPage<Party> list(Class<?> clazz,ListPage<Party> page,Party t ){
		List<Object> params = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer( );
		
		hql.append( " FROM " + clazz.getName() + " AS m " );
		hql.append( " WHERE 1=1 ");
		
		if( t != null ){
			if( t.getName() != null && t.getName().trim() != null 
					&& !"".equals(t.getName().trim())){
				hql.append( " AND m.name like ? " );
				params.add( "%" + t.getName().trim() + "%");
			}
			
			if( t instanceof Person ){
				
			}
		}
		
		return super.query(page, hql.toString(), params.toArray( ) );
	}
	
	public ListPage<Party> list(Class<?> clazz,ListPage<Party> page, Party t,Collection<String> properties ){
		if( t == null || page == null || properties == null)
			return null;
		
		HashSet<String> set = new HashSet<String>( properties );
		if( set.isEmpty() )
			return null;
		
		List<Object> params = new ArrayList<Object>( set.size() );
		StringBuffer sb = new StringBuffer( " FROM " + clazz.getName() + " AS m WHERE 1 = 1 " );
		for( String property:set ){
			Object value = ReflectionUtil.reflectGetValue(t, property);
			sb.append( " and m." + property + " = ? ");
			params.add( value );
		}
		
		return this.query(page, sb.toString(), params.toArray());
	}
	
	public ListPage<Party> list(ListPage<Party> page, Party t,Collection<String> properties ){
		Class<?> clazz = Organization.class;
		if( t instanceof Person )
			clazz = Person.class;
		return super.list(clazz, page, t, properties);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Party read(Class clazz, Party party) {
		if( party == null )
			return null;
		
		return this.getEntityManager().find(clazz,party.getIdentity());
	}

}

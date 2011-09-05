package com.google.code.lightssh.project.identity.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.google.code.lightssh.common.dao.hibernate.HibernateAnnotationDao;
import com.google.code.lightssh.project.identity.entity.Identity;
import com.google.code.lightssh.project.identity.entity.IdentityType;

@Repository("identityDao")
public class IdentityDaoHibernate extends HibernateAnnotationDao<Identity> implements IdentityDao{
	
	public IdentityDaoHibernate() {
		super();
	}

	@SuppressWarnings("unchecked")
	public Identity getByValue(String value) {
		String hql = " SELECT m FROM " + entityClass.getName()
			+ " AS m WHERE m.value = ? ";
		
		List<Identity> results = getHibernateTemplate().find(hql,value );
		
		return (results==null||results.isEmpty())?null:results.get(0);
	}

	@SuppressWarnings("unchecked")
	public Identity get(IdentityType type, String value) {
		String hql = " SELECT m FROM " + entityClass.getName()
			+ " AS m WHERE m.value = ? AND m.type = ? ";
		
		List<Identity> results = getHibernateTemplate().find(
				hql, new Object[]{value,type} );
		
		return (results==null||results.isEmpty())?null:results.get(0);
	}

}

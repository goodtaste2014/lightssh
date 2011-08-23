package com.google.code.lightssh.project.party.dao;

import java.util.List;

import com.google.code.lightssh.common.dao.hibernate.HibernateDao;
import com.google.code.lightssh.project.party.entity.Organization;

public class OrganizationDaoHibernate extends HibernateDao<Organization> implements OrganizationDao{

	/**
	 * 查询最上层组织
	 */
	@SuppressWarnings("unchecked")
	public List<Organization> listTop(  ){
		String hql = " SELECT m FROM " + entityClass.getName()
			+ " AS m WHERE m.parent is null ORDER BY m.sequence ASC ";
		
		return getHibernateTemplate().find(hql );
	}
}

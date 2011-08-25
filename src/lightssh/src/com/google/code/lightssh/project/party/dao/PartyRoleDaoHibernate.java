package com.google.code.lightssh.project.party.dao;

import java.util.List;

import com.google.code.lightssh.common.dao.hibernate.HibernateDao;
import com.google.code.lightssh.project.party.entity.PartyRole;
import com.google.code.lightssh.project.party.entity.PartyRole.RoleType;

/**
 * 
 * @author YangXiaojin
 *
 */
public class PartyRoleDaoHibernate extends HibernateDao<PartyRole> implements PartyRoleDao{

	@SuppressWarnings("unchecked")
	@Override
	public List<PartyRole> list(RoleType type) {
		String hql = " SELECT m FROM " + entityClass.getName()
			+ " AS m WHERE m.type = ? ";
	
		return getHibernateTemplate().find(hql,type );
	}
	
	

}

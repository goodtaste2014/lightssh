package com.google.code.lightssh.project.party.dao;

import java.util.List;

import com.google.code.lightssh.common.dao.hibernate.HibernateDao;
import com.google.code.lightssh.project.party.entity.Party;
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

	@SuppressWarnings("unchecked")
	@Override
	public List<Party> listParty(RoleType type) {
		String hql = " SELECT m.party FROM " + entityClass.getName()
			+ " AS m WHERE m.type = ? ";

		return getHibernateTemplate().find(hql,type );
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PartyRole> list(Party party) {
		String hql = " SELECT m FROM " + entityClass.getName()
			+ " AS m WHERE m.party = ? ";

		return getHibernateTemplate().find(hql,party );
	}
	
	@Override
	public void remove( Party party ){
		String hql = " DELETE m FROM " + entityClass.getName()
			+ " AS m WHERE m.party = ? ";
		
		getHibernateTemplate().update(hql,party);
	}

}

package com.google.code.lightssh.project.party.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.google.code.lightssh.common.dao.hibernate.HibernateAnnotationDao;
import com.google.code.lightssh.project.party.entity.Party;
import com.google.code.lightssh.project.party.entity.PartyRole;
import com.google.code.lightssh.project.party.entity.PartyRole.RoleType;

/**
 * 
 * @author YangXiaojin
 *
 */
@Repository("partyRoleDao")
public class PartyRoleDaoHibernate extends HibernateAnnotationDao<PartyRole> implements PartyRoleDao{

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
			+ " AS m WHERE m.party.id = ? ";
		
		return getHibernateTemplate().find(hql,party.getIdentity() );
	}
	
	@Override
	public void remove( Party party ){
		String hql = " DELETE m FROM " + entityClass.getName()
			+ " AS m WHERE m.party = ? ";
		
		getHibernateTemplate().update(hql,party);
	}
	
	@SuppressWarnings("unchecked")
	public List<PartyRole> list( Party party ,RoleType[] inTypes ){
		if( inTypes == null || inTypes.length == 0 )
			return list( party );
		
		StringBuffer hql = new StringBuffer( 
				" SELECT m FROM " + entityClass.getName()
			+ " AS m WHERE m.party = ? " );
		
		for( int i=0;i<inTypes.length;i++ ){
			if( i== 0 )
				hql.append(" AND m.type in ( ");
			hql.append( ((i==0)?"":",") + "'"+ inTypes[i].name() +"'" );
			if( i==inTypes.length-1 )
				hql.append(" )");
		}

		return getHibernateTemplate().find(hql.toString(),party );
	}

}

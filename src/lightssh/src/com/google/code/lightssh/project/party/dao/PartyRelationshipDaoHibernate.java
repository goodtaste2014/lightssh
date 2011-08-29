package com.google.code.lightssh.project.party.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.code.lightssh.common.dao.hibernate.HibernateDao;
import com.google.code.lightssh.project.party.entity.Party;
import com.google.code.lightssh.project.party.entity.PartyRelationship;
import com.google.code.lightssh.project.party.entity.PartyRole;
import com.google.code.lightssh.project.party.entity.PartyRelationship.RelationshipType;

/**
 * 
 * @author YangXiaojin
 *
 */
public class PartyRelationshipDaoHibernate extends HibernateDao<PartyRelationship> 
implements PartyRelationshipDao{

	@SuppressWarnings("unchecked")
	@Override
	public List<PartyRelationship> list( RelationshipType type ) {
		String hql = " SELECT m FROM " + entityClass.getName()
			+ " AS m WHERE m.type = ? "
			+ "  AND ( m.period.end IS NULL OR m.period.end >= ? )"
			;
	
		return getHibernateTemplate().find(hql,new Object[]{type,new Date()} );
	}
	
	@Override
	public void removeByFromRole( PartyRole partyRole ){
		String hql = " DELETE FROM " + entityClass.getName()
			+ " AS m WHERE m.from = ? ";
		
		getHibernateTemplate().delete(hql,partyRole);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PartyRelationship> list(RelationshipType type, Party from,Party to ) {
		List<Object> params = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer( );
		
		hql.append( " FROM " + entityClass.getName() + " AS m " );
		hql.append( " WHERE m.type = ? ");
		params.add( RelationshipType.ORG_ROLLUP );
		
		if( from != null ){
			hql.append( " AND m.from.party = ? " );
			params.add( from );
		}
		
		if( to != null ){
			hql.append( " AND m.to.party = ? " );
			params.add( to );
		}
		
		return getHibernateTemplate().find(hql.toString(), params.toArray( ) );
	}

}

package com.google.code.lightssh.project.party.dao;

import java.util.Date;
import java.util.List;

import com.google.code.lightssh.common.dao.hibernate.HibernateDao;
import com.google.code.lightssh.project.party.entity.PartyRelationship;
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
	
	

}

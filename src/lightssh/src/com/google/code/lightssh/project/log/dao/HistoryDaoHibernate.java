package com.google.code.lightssh.project.log.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.google.code.lightssh.common.dao.hibernate.HibernateAnnotationDao;
import com.google.code.lightssh.project.log.entity.Access;
import com.google.code.lightssh.project.log.entity.History;


@Repository("historyDao")
public class HistoryDaoHibernate extends HibernateAnnotationDao<History> implements HistoryDao{

	@SuppressWarnings("unchecked")
	@Override
	public History getByAccess(Access access) {
		String hql = " SELECT m FROM " + entityClass.getName()
		+ " AS m WHERE m.access = ? ";
	
		List<History> results = getHibernateTemplate().find(hql,access );
		
		return (results==null||results.isEmpty())?null:results.get(0);
	}

}

package com.google.code.lightssh.project.sequence.dao;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.orm.jpa.JpaCallback;
import org.springframework.stereotype.Repository;

import com.google.code.lightssh.common.dao.jpa.JpaAnnotationDao;
import com.google.code.lightssh.common.util.StringUtil;
import com.google.code.lightssh.project.sequence.entity.Sequence;

@Repository("sequenceDao")
public class SequenceDaoJpa extends JpaAnnotationDao<Sequence> implements SequenceDao {

	@SuppressWarnings("unchecked")
	@Override
	public long nextDatabaseSequenceNumber(final String seqName ) {
		final String sql = "select "+seqName+".nextval  from dual";
		Long results = (Long) getJpaTemplate().execute(
				new JpaCallback() {
					public Object doInJpa(EntityManager entityManager) {
						Object object = entityManager.createNativeQuery(sql,seqName).getSingleResult();
		        		return (object instanceof BigDecimal)?((BigDecimal)object).longValue():0L;
		        	}// end doInJpa
	        	}); 
		return results;
	}

	@SuppressWarnings("unchecked")
	public Sequence readWithLock(final String key){
		if( StringUtil.clean( key ) == null )
			return null;
		
		final String sql = "select t.* from T_SEQUENCE t where t.id = ? for update ";
		
		List<Sequence> results = (List<Sequence>) getJpaTemplate().execute(
				new JpaCallback() {
					public Object doInJpa(EntityManager entityManager) {
						return entityManager.createNativeQuery( sql, Sequence.class)
							.setParameter(0, StringUtil.clean( key )).getResultList();
		        	}// end doInHibernate
	        	}); 
		
		return (results==null||results.isEmpty())?null:results.get(0);
	}

}

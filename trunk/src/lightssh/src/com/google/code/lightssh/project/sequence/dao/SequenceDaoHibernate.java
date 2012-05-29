package com.google.code.lightssh.project.sequence.dao;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.google.code.lightssh.common.dao.hibernate.HibernateAnnotationDao;
import com.google.code.lightssh.common.util.StringUtil;
import com.google.code.lightssh.project.sequence.entity.Sequence;

//@Repository("sequenceDao")
public class SequenceDaoHibernate extends HibernateAnnotationDao<Sequence> implements SequenceDao {

	@SuppressWarnings("unchecked")
	@Override
	public long nextDatabaseSequenceNumber( String seqName ) {
		final String sql = "select "+seqName+".nextval  from dual";
		Long results = (Long) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session) throws HibernateException {
						Query query = session.createSQLQuery(sql);
						Object object = query.uniqueResult();
		        		return (object instanceof BigDecimal)?((BigDecimal)object).longValue():0L;
		        	}// end doInHibernate
	        	}); 
		return results;
	}

	@SuppressWarnings("unchecked")
	public Sequence readWithLock(final String key){
		if( StringUtil.clean( key ) == null )
			return null;
		
		final String sql = "select t.* from T_SEQUENCE t where t.id = ? for update ";
		
		List<Sequence> results = (List<Sequence>) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session) throws HibernateException {
						Query query = session.createSQLQuery(sql ).addEntity(Sequence.class);
						query.setParameter(0, StringUtil.clean( key ) );
						
		        		List<Sequence> list = query.list();
		        		return list;
		        	}// end doInHibernate
	        	}); 
		
		return (results==null||results.isEmpty())?null:results.get(0);
	}

}

package com.google.code.lightssh.project.geo.dao;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.google.code.lightssh.common.dao.hibernate.HibernateAnnotationDao;
import com.google.code.lightssh.common.model.page.ListPage;
import com.google.code.lightssh.project.geo.entity.GeoAssociation;
import com.google.code.lightssh.project.geo.entity.GeographicBoundary;

/**
 * 
 * @author YangXiaojin
 *
 */
@Repository("geoAssociationDao")
public class GeoAssociationDaoHibernate extends HibernateAnnotationDao<GeoAssociation>
implements GeoAssociationDao{
	
	@SuppressWarnings("unchecked")
	@Override
	public ListPage<GeographicBoundary> listGeoByParent(
			final ListPage<GeographicBoundary> page,final GeographicBoundary geo) {
		if( geo == null || geo.getIdentity() == null )
			return page;
		
		final StringBuilder hql = new StringBuilder(" FROM ");
		hql.append( super.entityClass.getName() );
		hql.append(  " AS m WHERE m.toGeo = ? " );
		final Object[] params = new Object[]{geo};

		return (ListPage<GeographicBoundary>) getHibernateTemplate().execute(
				new HibernateCallback() {
		        	public Object doInHibernate(Session session) throws HibernateException {
		        		String count_hql = " SELECT COUNT(m.fromGeo) " + hql.toString();
		        		page.setAllSize( rowCount( session,count_hql, params ) ); //all size
		        			
		        		if( page.getNumber() > page.getAllPage() )
		        			page.setNumber(Math.min(page.getNumber(), page.getAllPage()));
		        		
		        		if( page.getAllSize() > 0 && page.getSize() > 0 ){
		        			String jpql = " SELECT m.fromGeo " + hql.toString();
		        			jpql += addOrderBy( page );//add order
		        			
		        			Query query = session.createQuery( jpql )
		        				.setFirstResult(page.getStart()-1)
		        				.setMaxResults(page.getSize());
		        			
		        			addQueryParams( query,params );
		        			
		        			page.setList( query.list());
		        		}//end if
		        		
		        		return page;
	        		}// end doInHibernate
	        	}); 
	}

}

package com.google.code.lightssh.project.geo.dao;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.orm.jpa.JpaCallback;
import org.springframework.stereotype.Repository;

import com.google.code.lightssh.common.dao.jpa.JpaAnnotationDao;
import com.google.code.lightssh.common.model.page.ListPage;
import com.google.code.lightssh.project.geo.entity.GeoAssociation;
import com.google.code.lightssh.project.geo.entity.GeographicBoundary;

/**
 * 
 * @author YangXiaojin
 *
 */
@Repository("geoAssociationDao")
public class GeoAssociationDaoJpa extends JpaAnnotationDao<GeoAssociation>
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

		return (ListPage<GeographicBoundary>) getJpaTemplate().execute(
				new JpaCallback() {
		        	public Object doInJpa( EntityManager entityManager) {
		        		String count_hql = " SELECT COUNT(m.fromGeo) " + hql.toString();
		        		page.setAllSize( rowCount( count_hql, params ) ); //all size
		        			
		        		if( page.getNumber() > page.getAllPage() )
		        			page.setNumber(Math.min(page.getNumber(), page.getAllPage()));
		        		
		        		if( page.getAllSize() > 0 && page.getSize() > 0 ){
		        			String jpql = " SELECT m.fromGeo " + hql.toString();
		        			jpql += addOrderBy( page );//add order
		        			
		        			Query query = entityManager.createQuery( jpql )
		        				.setFirstResult(page.getStart()-1)
		        				.setMaxResults(page.getSize());
		        			
		        			addQueryParams( query,params );
		        			
		        			page.setList( query.getResultList());
		        		}//end if
		        		
		        		return page;
	        		}// end doInHibernate
	        	}); 
	}

}

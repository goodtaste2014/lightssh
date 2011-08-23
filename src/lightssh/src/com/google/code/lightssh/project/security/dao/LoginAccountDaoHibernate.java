package com.google.code.lightssh.project.security.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.google.code.lightssh.common.dao.hibernate.HibernateDao;
import com.google.code.lightssh.common.model.page.ListPage;
import com.google.code.lightssh.project.security.entity.LoginAccount;

/**
 * LoginAccount Dao Hibernate implement
 * @author YangXiaojin
 *
 */
public class LoginAccountDaoHibernate extends HibernateDao<LoginAccount> 
	implements LoginAccountDao{

	@SuppressWarnings("unchecked")
	@Override
	public LoginAccount get(String loginName) {
		String hql = " SELECT m FROM " + entityClass.getName() + " AS m WHERE m.loginName = ? ";
		List<LoginAccount> results = getHibernateTemplate().find(hql, loginName );
		
		return (results==null||results.isEmpty())?null:results.get(0);
	}

	public ListPage<LoginAccount> list(ListPage<LoginAccount> page,LoginAccount t ){
		if( t == null )
			return list( page ); 
		
		List<Object> params = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer( );
		
		hql.append( " FROM " + entityClass.getName() + " AS m " );
		hql.append( " WHERE 1=1 ");
		if( t.getLoginName() != null && t.getLoginName().trim() != null 
				&& !"".equals(t.getLoginName().trim())){
			hql.append( " AND m.loginName like ? " );
			params.add( "%" + t.getLoginName().trim() + "%");
		}
		
		return super.query(page, hql.toString(), params.toArray( ) );
	}
	
	@SuppressWarnings("unchecked")
	public void updateRole( final LoginAccount account ){
		getHibernateTemplate().execute(
			new HibernateCallback() {
	        	public Object doInHibernate(Session session) throws HibernateException {
	        		LoginAccount old = (LoginAccount)session.load( 
	        				LoginAccount.class, account.getIdentity() );
	        		old.setRoles( account.getRoles() );
	        		
	        		return 1;
	    		}// end doInHibernate
	    	}); 
	}
}

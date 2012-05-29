package com.google.code.lightssh.project.security.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.google.code.lightssh.common.ApplicationException;
import com.google.code.lightssh.common.dao.jpa.JpaAnnotationDao;
import com.google.code.lightssh.common.model.page.ListPage;
import com.google.code.lightssh.common.util.StringUtil;
import com.google.code.lightssh.project.party.entity.Organization;
import com.google.code.lightssh.project.party.entity.Party;
import com.google.code.lightssh.project.security.entity.LoginAccount;
import com.google.code.lightssh.project.security.entity.Permission;

/**
 * LoginAccount Dao Hibernate implement
 * @author YangXiaojin
 *
 */
@Repository("loginAccountDao")
public class LoginAccountDaoJpa extends JpaAnnotationDao<LoginAccount> 
	implements LoginAccountDao{

	@SuppressWarnings("unchecked")
	@Override
	public LoginAccount get(String loginName) {
		String hql = " SELECT m FROM " + entityClass.getName() + " AS m WHERE m.loginName = ? ";
		List<LoginAccount> results = getJpaTemplate().find(hql, loginName );
		
		return (results==null||results.isEmpty())?null:results.get(0);
	}
	
	/**
	 * 组装LoginAccount对象
	 */
	private LoginAccount buildObject( ResultSet rs ){
		if( rs == null )
			return null;
		
		LoginAccount la = new LoginAccount();
		try{
			la.setId( rs.getLong("ID"));
			//la.setParty_id( rs.getString("PARTY_ID"));
			la.setLoginName(rs.getString("LOGIN_NAME"));
			la.setPassword( rs.getString("PASSWORD"));
			la.setEnabled( rs.getBoolean("ENABLED"));
			la.setPeriod( rs.getDate("PERIOD_START"), rs.getDate("PERIOD_END"));
			//la.setUseCa( rs.getBoolean("USE_CA") );
			//la.setType( LoginAccount.LoginAccountType.valueOf( rs.getString("TYPE") ));
		}catch( SQLException e ){
			e.printStackTrace();
		}
		
		return la;
	}
	
	@SuppressWarnings("deprecation")
	public LoginAccount getWithPartyIdentity(final String loginName){
		final String sql_loginaccount = " select * from T_SECURITY_LOGINACCOUNT t where t.login_name = ? ";
		LoginAccount account = null;
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			//conn = getEntityManager().unwrap( java.sql.Connection.class );
			Session hibernateSession = getEntityManager().unwrap(Session.class);
			conn = hibernateSession.connection(); 
			
			ps = conn.prepareStatement(sql_loginaccount);
			ps.setString(1, loginName);
			rs = ps.executeQuery();
			while(rs.next()){
				account = buildObject( rs );
				break;
			}
		}catch (SQLException e){
			e.printStackTrace();
		}finally{
			try{
				if( rs != null )
					rs.close();
				if( ps != null )
					ps.close();
				if( conn != null )
					conn.close();
			}catch( Exception e ){
				
			}
		}
		
		return account;
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
		
		if( t.getType() != null ){
			hql.append( " AND m.type = ? " );
			params.add( t.getType() );
		}
		
		int subquery_flag = params.size();
		if(t.getParty()!=null){
			String entityClass = Party.class.getName();
			StringBuffer subQuery = new StringBuffer( );
			
			if( StringUtil.clean(t.getParty().getIdentity()) != null ){
				subQuery.append(" AND n.id like ?");
				params.add("%"+StringUtil.clean(t.getParty().getIdentity())+"%");
			}
			
			String name=StringUtil.clean(t.getParty().getName());
			if( name !=null){
				subQuery.append(" AND n.name like ?");
				params.add("%"+name+"%");
			}
			
			/*
			if(t.getParty() instanceof Member ){
				int member_flag = params.size();
				Member member = (Member)t.getParty();
				if( member.getPartyStatus() != null ){
					subQuery.append(" AND n.partyStatus = ? " );
					params.add( member.getPartyStatus() );
				}
				
				if( StringUtil.clean(member.getIdentity()) != null ){
					subQuery.append(" AND n.id = ? " );
					params.add( StringUtil.clean(member.getIdentity()) );
				}
				
				if( params.size() > member_flag )
					entityClass = Member.class.getName();
			}else */
			if( t.getParty() instanceof Organization ){
				int member_flag = params.size();
				//Query
				if( params.size() > member_flag )
					entityClass = Organization.class.getName();
			}
			
			if( params.size() > subquery_flag ){
				String sub_hql = " SELECT n.id FROM " + entityClass + " As n WHERE 1=1 ";
				hql.append( " AND m.party.id in ( " + sub_hql + subQuery.toString() + "  ) ");
			}
		}
		
		return super.query(page, hql.toString(), params.toArray( ) );
	}
	
	public void updateRole( final LoginAccount account ){
		throw new ApplicationException("DAO未实现！");
	}
	
	public ListPage<LoginAccount> listLight(ListPage<LoginAccount> page,LoginAccount t ){
		StringBuffer hql = new StringBuffer();

		List<Object> params = new ArrayList<Object>();
		String select = " SELECT new " + entityClass.getName() + "(m.id,m.loginName) ";
		hql.append(" FROM " + entityClass.getName() + " AS m WHERE 1=1 ");
		if( t != null ){
			if( t.getType() != null ){
				hql.append(" AND m.type = ? " );
				params.add( t.getType() );
			}
		}
		
		return super.query(page,select,hql.toString(),params.toArray() );
	}
	
	@SuppressWarnings("unchecked")
	public List<LoginAccount> listByPermission(final Permission p ){
		if( p == null || p.getIdentity() == null )
			return null;

		String tokens = "select sp.token from T_SECURITY_PERMISSION sp where sp.TOKEN = ? ";
		
		String roles = "select sr.ID from T_SECURITY_ROLE sr "
			+ " left join T_REF_ROLE_PERMISSION ref_rp " 
			+ " on ref_rp.role_id = sr.id where ref_rp.permission_id in " 
			+ "( " + tokens + " ) ";
		
		final String sql = "select distinct sla.* from T_SECURITY_LOGINACCOUNT sla " 
			+ " left join T_REF_LOGINACCOUNT_ROLE ref_lr on sla.id = ref_lr.loginaccount_id " 
			+ " where ref_lr.role_id in( " + roles + " ) order by sla.LOGIN_NAME asc ";
		
		return getEntityManager().createNativeQuery( sql, super.entityClass ).getResultList();
	}
}

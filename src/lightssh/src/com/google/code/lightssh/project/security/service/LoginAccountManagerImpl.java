package com.google.code.lightssh.project.security.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.google.code.lightssh.common.ApplicationException;
import com.google.code.lightssh.common.dao.Dao;
import com.google.code.lightssh.common.dao.DaoException;
import com.google.code.lightssh.common.model.page.ListPage;
import com.google.code.lightssh.common.service.BaseManagerImpl;
import com.google.code.lightssh.common.util.CryptographyUtil;
import com.google.code.lightssh.project.log.entity.Access;
import com.google.code.lightssh.project.log.service.AccessManager;
import com.google.code.lightssh.project.party.entity.Party;
import com.google.code.lightssh.project.security.dao.LoginAccountDao;
import com.google.code.lightssh.project.security.entity.LoginAccount;
import com.google.code.lightssh.project.security.entity.Permission;
import com.google.code.lightssh.project.security.entity.Role;
import com.google.code.lightssh.project.security.entity.LoginAccount.LoginAccountType;

/**
 * LoginAccount Manager implement
 * @author YangXiaojin
 *
 */
@Component("loginAccountManager")
public class LoginAccountManagerImpl extends BaseManagerImpl<LoginAccount>
	implements LoginAccountManager,UserDetailsService{
	
	private static Logger log = LoggerFactory.getLogger(LoginAccountManagerImpl.class);
	
	/** 管理员账号 */
	public static final String ROOT_LOGIN_NAME="root";
	
	public static final String DEFAULT_PASSWORD = "123456";
	
	@Resource(name="roleManager")
	private RoleManager roleManager;
	
	@Resource(name="accessManager")
	private AccessManager accessManager;
	
	/**
	 * 存放已删除用户名，用于强制退出在线用户
	 */
	@Resource(name="sessionInvalidateUserCache")
	private Cache sessionInvalidateUserCache;
	
	@Resource(name="loginAccountDao")
	public void setDao(Dao<LoginAccount> dao) {
		this.dao = dao;
	}
	
	public void setSessionInvalidateUserCache(Cache sessionInvalidateUserCache) {
		this.sessionInvalidateUserCache = sessionInvalidateUserCache;
	}

	private LoginAccountDao getDao(){
		return (LoginAccountDao)super.dao;
	}

	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}
	
	public LoginAccount get( String name ){
		return getDao().get( name );
	}
	
	public LoginAccount getLight( String name ){
		return getDao().getWithPartyIdentity( name ); //TODO
	}
	
	public LoginAccount getWithParty( String name ){
		LoginAccount result = getDao().getWithPartyIdentity( name );
		/*
		if( result != null && result.getParty_id() != null ){
			Member member = memberManager.getLightMember( result.getParty_id() );
			Party party = member;
			if( party == null ){
				party = partyManager.getOrganization(result.getParty_id());
			}else{
				//取会员业务角色
				member.setPartyRoles( partyRoleManager.list( member ));
			}
			result.setParty(party);
		}
		*/
		return result;
	}

	@Override
	public UserDetails loadUserByUsername(String loginName)
			throws UsernameNotFoundException {
		return get( loginName );
	}

	@Override
	public void initLoginAccount() {
		LoginAccount root = getDao().get( ROOT_LOGIN_NAME );
		if( root == null ){
			root = new LoginAccount( );
			root.setCreateDate( new Date() );
			root.setLoginName(ROOT_LOGIN_NAME);
			root.setEnabled(Boolean.TRUE);
			root.setType(LoginAccountType.ADMIN);
			Role superRole = roleManager.initRole(true); //TODO
			root.addRole(superRole); 
			root.setPassword(CryptographyUtil.hashSha1Hex(DEFAULT_PASSWORD ) );
			root.setDescription("系统初始化自动创建。");
			
			try{
				save(root);
				log.info("成功初始化系统账户！" );
			}catch( Exception e ){
				String msg = "初始化系统账户异常："+e.getMessage();
				log.error( msg );
				throw new ApplicationException( msg );
			}
		}
	}
	
	/**
	 * 更新密码
	 */
	public void updatePassword( String name,String password,String newPassword ){
		if( password == null || newPassword == null  )
			throw new ApplicationException("原密码或新密码为空！");
		
		LoginAccount account = getDao().get(name);
		if( account == null )
			throw new ApplicationException("找不到名称为"+name+"的账号！");
		
		String hash_pwd =  CryptographyUtil.hashMd5Hex( password );
		String hash_new_pwd = CryptographyUtil.hashMd5Hex( newPassword );
		
		if( !account.getPassword().equals( hash_pwd ))
			throw new ApplicationException("原密码不正确！");
		
		account.setPassword( hash_new_pwd  );
		super.dao.update( account );
	}
	
	public void save( LoginAccount account ,Access access ){
		this.save(account);
		if( access != null ){
			//access.setType(AccessType.SECURITY_ACCOUNT_ADD);
			//this.accessManager.save(access);
		}
	}
	
	public void save( LoginAccount account ){
		if( account == null )
			throw new SecurityException( "数据不完整，LoginAccount 为空！" );
		
		boolean inserted = (account.getId() == null);
		if( inserted ){
			account.setCreateDate( new Date() );
			account.setPassword( CryptographyUtil.hashMd5Hex( DEFAULT_PASSWORD ) );
		}
		
		LoginAccount exist = getDao().get( account.getLoginName() );
		if( exist == null && !inserted )
			throw new DaoException( "登录账号已不存在，不能进行修改操作！" );
		
		if( exist != null && !exist.getIdentity().equals(account.getIdentity()))
			throw new SecurityException( "登录账号名'"+account.getLoginName()+"'已存在！" );
		
		if( exist != null ){
			exist.setParty( account.getParty() );
			if( exist.getParty()!=null && exist.getParty().getId() == null )
				exist.setParty( null );
			exist.setDescription( account.getDescription() );
			exist.setPeriod( account.getPeriod() );
			exist.setEnabled( account.getEnabled() );
			getDao().update( exist );
		}else
			getDao().create( account );
	}
	
	public void remove( Serializable identity ){
		LoginAccount db_account = dao.read(identity);
		if( db_account != null && ROOT_LOGIN_NAME.equals(db_account.getLoginName()) )
			throw new ApplicationException("系统超级管理员账户不允许删除！");

		String name = db_account.getLoginName();
		super.remove(identity);
		if( sessionInvalidateUserCache != null )
			sessionInvalidateUserCache.put( new Element(name,name) );
	}
	
	public void remove( LoginAccount account ){
		if( account != null )
			remove( account.getIdentity() );
	}
	
	public void remove(LoginAccount t,Access log) {
		LoginAccount la = get( t );
		if( la != null ){
			dao.delete(la);
			if( log != null ){
				//log.setType( AccessType.SECURITY_ACCOUNT_DELETE );
				//log.setDescription( "删除的登录账户id=" + la.getIdentity() 
				//		+ ",名称=" + la.getLoginName() );
				//this.accessManager.save(log);
			}
		}
	}
	public void updateRole( LoginAccount account ){
		if( account == null )
			throw new SecurityException( "数据不完整，LoginAccount 为空！" );
		
		LoginAccount old = this.get( account );
		if( old != null ){
			old.setRoles( account.getRoles() );
			//getDao().updateRole(old);
			getDao().update(old);
		}
	}
	
	public void updateRole( LoginAccount account, Access log ){
		updateRole( account );
		
		if( log != null ){
			accessManager.save(log);
		}
	}
	
	public List<LoginAccount> listAdmin( ){
		ListPage<LoginAccount> page = new ListPage<LoginAccount>( 1024 );
		LoginAccount t = new LoginAccount();
		t.setType(LoginAccountType.ADMIN);
		page = getDao().listLight(page,t);
		return page.getList();
	}

	@Override
	public List<LoginAccount> listByPermission(Permission permission) {
		return getDao().listByPermission(permission);
	}
	
	public List<LoginAccount> listByPermission(String token ){
		Permission p = new Permission(token);
		return listByPermission( p );
	}
	@Override
	public ListPage<LoginAccount> list(ListPage<LoginAccount> page,
			LoginAccount la) {
		return getDao().list(page, la);
	}
	
	public void toggleCa( LoginAccount account ,Access access ){
		if( account == null || account.getIdentity() == null )
			return ;
		
		LoginAccount old = this.get(account);
		if( old == null ){
			log.warn("开启或禁用CA，找不到相关登录账户！");
			throw new ApplicationException("找不到相关数据！");
		}
		
		boolean close = old.isUseCa();
		old.setUseCa( !close );
		
		this.dao.update( old );
		
		String desc = "成功"+(close?"禁用":"启用")+"帐号["+old.getLoginName()+"]CA登录！";
		log.info( desc );
		if( access != null ){
			//access.setType( AccessType.SECURITY_ACCOUNT_CA );
			//access.setDescription(desc);
			//this.accessManager.save( access );
		}
	}
	@Override
	public List<LoginAccount> listByParty(Party party) {
		LoginAccount account = new LoginAccount();
		account.setParty( party );
		
		ListPage<LoginAccount> page = new ListPage<LoginAccount>();
		page.setSize( Integer.MAX_VALUE );
		dao.list(page,account);
		
		return page.getList();
	}
}

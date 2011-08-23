package com.google.code.lightssh.project.security.service;

import java.io.Serializable;
import java.util.Date;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.google.code.lightssh.common.ApplicationException;
import com.google.code.lightssh.common.dao.DaoException;
import com.google.code.lightssh.common.service.BaseManagerImpl;
import com.google.code.lightssh.common.util.CryptographyUtil;
import com.google.code.lightssh.project.security.dao.LoginAccountDao;
import com.google.code.lightssh.project.security.entity.LoginAccount;
import com.google.code.lightssh.project.security.entity.Role;

/**
 * LoginAccount Manager implement
 * @author YangXiaojin
 *
 */
public class LoginAccountManagerImpl extends BaseManagerImpl<LoginAccount>
	implements LoginAccountManager,UserDetailsService{
	
	private static Log log = LogFactory.getLog(LoginAccountManagerImpl.class);
	
	/** 管理员账号 */
	public static final String ROOT_LOGIN_NAME="root";
	
	public static final String DEFAULT_PASSWORD = "123456";
	
	private RoleManager roleManager;
	
	/**
	 * 存放已删除用户名，用于强制退出在线用户
	 */
	private Cache sessionInvalidateUserCache;
	
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
			Role superRole = roleManager.initRole(false); //TODO
			root.addRole(superRole); 
			root.setPassword( CryptographyUtil.hashMd5Hex( DEFAULT_PASSWORD ) );
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
	
	public void updateRole( LoginAccount account ){
		if( account == null )
			throw new SecurityException( "数据不完整，LoginAccount 为空！" );
		
		LoginAccount old = this.get( account );
		if( old != null ){
			old.setRoles( account.getRoles() );
			getDao().updateRole(old);
			//getDao().update(old);
		}
	}
}

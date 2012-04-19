package com.google.code.lightssh.project.log.service;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.google.code.lightssh.common.ApplicationException;
import com.google.code.lightssh.common.dao.Dao;
import com.google.code.lightssh.common.entity.Persistence;
import com.google.code.lightssh.common.service.BaseManagerImpl;
import com.google.code.lightssh.project.identity.entity.Identity;
import com.google.code.lightssh.project.identity.service.IdentityManager;
import com.google.code.lightssh.project.log.dao.HistoryDao;
import com.google.code.lightssh.project.log.entity.Access;
import com.google.code.lightssh.project.log.entity.AccessType;
import com.google.code.lightssh.project.log.entity.History;
import com.google.code.lightssh.project.security.entity.LoginAccount;
import com.google.code.lightssh.project.security.service.LoginAccountManager;

/**
 * 
 * @author YangXiaojin
 *
 */
@Component("accessManager")
public class AccessManagerImpl extends BaseManagerImpl<Access> implements AccessManager{
	
	protected Dao<Access> dao;
	
	@Resource(name="historyDao")
	protected HistoryDao historyDao;
	
	@Resource(name="identityManager")
	private IdentityManager identityManager;
	
	@Resource(name="loginAccountManager")
	private LoginAccountManager loginAccountManager;
	
	public AccessManagerImpl() {
		super();
	}
		
	public void setLoginAccountManager(LoginAccountManager loginAccountManager) {
		this.loginAccountManager = loginAccountManager;
	}

	@Resource(name="accessDao")
	public void setDao(Dao<Access> dao) {
		this.dao = dao;
		super.dao = this.dao;
	}
	
	public void setHistoryDao(HistoryDao historyDao) {
		this.historyDao = historyDao;
	}

	public void setIdentityManager(IdentityManager identityManager) {
		this.identityManager = identityManager;
	}

	public void logLogin(Date date,String ip, String loginName ) {
		LoginAccount la = loginAccountManager.get( loginName );
		if( la == null )
			return;
		
		Access access = new Access();
		Identity classIdentity = identityManager.getClassIdentity( la );
		if( classIdentity == null )
			classIdentity = new Identity( la );
		
		access.setOperator(loginName);
		access.setClassIdentity(classIdentity);
		access.setIp(ip);
		access.setTime(date);
		access.setType( AccessType.LOGIN );
		dao.create(access);		
	}

	public void log(Access access, Persistence<?> originalModel,
			Persistence<?> newModel) {
		if( access == null )
			throw new ApplicationException("access is null!");
		
		if( originalModel != null && newModel != null ){
			access.setType( AccessType.UPDATE );
		}else if( originalModel == null && newModel == null ){
			throw new ApplicationException("original model and new model is null!");
		}else if( originalModel == null ){
			access.setType( AccessType.CREATE );
		}else{
			access.setType( AccessType.DELETE );
		}
		
		Persistence<?> p = originalModel;
		if(p==null) 
			p = newModel;
		Identity classIdentity = identityManager.getClassIdentity( p );
		if( classIdentity == null ){
			classIdentity = new Identity( p );
			this.identityManager.create(classIdentity);
		}
		access.setClassIdentity(classIdentity);
		if( access.getDescription()==null )
			access.setDescription( p.getClass().getCanonicalName() );
		
		History history = new History(access,originalModel,newModel);
		history.setCreateDate( access.getTime() );		
		//dao.create(access);	
		historyDao.create( history );
	}

	@Override
	public History getHistory(Access access) {
		if( access == null || access.getIdentity() == null )
			return null;
		
		return this.historyDao.getByAccess(access);
	}

}


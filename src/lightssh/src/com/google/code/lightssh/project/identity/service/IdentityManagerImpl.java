package com.google.code.lightssh.project.identity.service;

import com.google.code.lightssh.common.entity.Persistence;
import com.google.code.lightssh.common.service.BaseManagerImpl;
import com.google.code.lightssh.project.identity.dao.IdentityDao;
import com.google.code.lightssh.project.identity.entity.Identity;
import com.google.code.lightssh.project.identity.entity.IdentityType;

/**
 * 
 * @author YangXiaojin
 *
 */
public class IdentityManagerImpl extends BaseManagerImpl<Identity> implements IdentityManager{
	
	protected IdentityDao dao;
		
	public IdentityManagerImpl() {
		super();
	}

	public void setIdentityDao(IdentityDao dao) {
		super.dao = dao;
		this.dao = dao;
	}
	
	public Identity get(String value) {
		return dao.getByValue( value );
	}

	public Identity get(IdentityType type, String value) {
		return dao.get(type, value);
	}

	public Identity getClassIdentity(Persistence<?> model) {
		Identity i = new Identity( model );
		return dao.get( i.getType(),i.getValue() );
	}

	public void saveClassIdentity(Persistence<?> model) {
		Identity i = new Identity( model );
		dao.create(i);
	}
	
}

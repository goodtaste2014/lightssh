package com.google.code.lightssh.project.party.service;

import java.util.ArrayList;
import java.util.List;

import com.google.code.lightssh.common.ApplicationException;
import com.google.code.lightssh.common.model.page.ListPage;
import com.google.code.lightssh.common.service.BaseManagerImpl;
import com.google.code.lightssh.project.party.dao.OrganizationDao;
import com.google.code.lightssh.project.party.entity.Organization;
import com.google.code.lightssh.project.sequence.service.SequenceManager;

public class OrganizationManagerImpl extends BaseManagerImpl<Organization> implements OrganizationManager{
	
	private SequenceManager sequenceManager;
	
	public void setOrganizationDao( OrganizationDao dao ){
		super.dao = dao;
	}
	
	public void setSequenceManager(SequenceManager sequenceManager) {
		this.sequenceManager = sequenceManager;
	}

	public OrganizationDao getDao(){
		return (OrganizationDao)super.dao;
	}

	@Override
	public List<Organization> listTop() {
		return getDao().listTop();
	}

	@Override
	public boolean isUniqneName(Organization org) {
		if( org == null )
			return true;
		
		ListPage<Organization> page = new ListPage<Organization>(1);
		List<String> properties = new ArrayList<String>(1);
		properties.add("name");
		page = getDao().list(page, org, properties);
		
		Organization exists = (page.getList()==null||page.getList().isEmpty())
			?null:page.getList().get(0);
		
		return exists == null || exists.getIdentity().equals( org.getIdentity() );
	}
	
	public void save( Organization org ){
		if( org == null )
			throw new ApplicationException("数据为空，不能进行保存！");
		
		boolean inserted = (org.getIdentity() == null) 
			|| ("".equals(org.getIdentity()));
		if( inserted ){
			org.setId( sequenceManager.nextSequenceNumber( org ) );
			this.getDao().create(org);
		}else
			this.getDao().update(org);
	}
}

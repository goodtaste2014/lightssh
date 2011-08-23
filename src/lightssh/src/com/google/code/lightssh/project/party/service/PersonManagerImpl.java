package com.google.code.lightssh.project.party.service;

import java.io.Serializable;

import com.google.code.lightssh.common.ApplicationException;
import com.google.code.lightssh.common.dao.Dao;
import com.google.code.lightssh.common.service.BaseManagerImpl;
import com.google.code.lightssh.common.util.ReflectionUtil;
import com.google.code.lightssh.project.log.entity.Access;
import com.google.code.lightssh.project.log.service.AccessManager;
import com.google.code.lightssh.project.party.entity.Person;
import com.google.code.lightssh.project.sequence.service.SequenceManager;

public class PersonManagerImpl extends BaseManagerImpl<Person> implements PersonManager{
	
	private SequenceManager sequenceManager;
	
	private AccessManager accessManager;
	
	public void setSequenceManager(SequenceManager sequenceManager) {
		this.sequenceManager = sequenceManager;
	}
	
	public void setAccessManager(AccessManager accessManager) {
		this.accessManager = accessManager;
	}

	public Dao<Person> getDao(){
		return super.dao;
	}
	
	public void save( Person person ){
		this.save(person,null);
	}
	
	public void save( Person person , Access access ){
		if( person == null )
			throw new ApplicationException("数据为空，不能进行保存！");
		
		Person original = null;
		boolean inserted = (person.getIdentity() == null) 
			|| ("".equals(person.getIdentity()));
		if( inserted ){
			person.setId( sequenceManager.nextSerialNumber( person ) );
			this.getDao().create(person);
		}else{
			Person old = this.getDao().read( person );
			if( old != null ){
				original = old.clone();
				ReflectionUtil.assign(person, old);
				this.getDao().update( old );
			}
		}
		
		if( access != null ){
			accessManager.log(access, original, person);
		}
	}

	public void remove(Serializable identity, Access access) {
		Person person = dao.read(identity);
		if( person != null ){
			dao.delete(person);
			
			if( access != null )
				accessManager.log(access, person, null);
		}
	}
}

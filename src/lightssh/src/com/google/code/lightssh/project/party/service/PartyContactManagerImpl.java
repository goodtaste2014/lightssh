package com.google.code.lightssh.project.party.service;

import java.util.Collection;

import com.google.code.lightssh.common.service.BaseManagerImpl;
import com.google.code.lightssh.project.contact.entity.ContactMechanism;
import com.google.code.lightssh.project.contact.entity.ContactMechanism.ContactMechanismType;
import com.google.code.lightssh.project.party.dao.PartyContactDao;
import com.google.code.lightssh.project.party.entity.Party;
import com.google.code.lightssh.project.party.entity.PartyContact;

/**
 * 
 * @author YangXiaojin
 *
 */
public class PartyContactManagerImpl extends BaseManagerImpl<PartyContact>
implements PartyContactManager{
	
	public void setDao( PartyContactDao dao ){
		this.dao = dao;
	}
	
	public PartyContactDao getDao(){
		return (PartyContactDao)super.dao;
	}

	@Override
	public Collection<ContactMechanism> list(Party party,
			ContactMechanismType type) {
		return getDao().list(party, type);
	}

	@Override
	public Collection<ContactMechanism> list(Party party) {
		return getDao().list(party );
	}

	@Override
	public void remove(ContactMechanism contact) {
		getDao().remove(contact);
	}

	@Override
	public void save(Party party, ContactMechanism contact) {
		if( party != null && party.getIdentity() != null && contact != null ){
			if( contact.isInsert() )
				contact.preInsert();
			super.save( new PartyContact(party,contact) );
		}
	}

	@Override
	public void save(Party party, Collection<ContactMechanism> contacts) {
		if( contacts == null || contacts.isEmpty() )
			return;
		
		for( ContactMechanism contact:contacts )
			this.save(party, contact);
	}

}

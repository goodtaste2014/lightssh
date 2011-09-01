package com.google.code.lightssh.project.party.web;

import org.apache.struts2.json.annotations.JSON;

import com.google.code.lightssh.common.web.action.CrudAction;
import com.google.code.lightssh.project.contact.entity.ContactMechanism;
import com.google.code.lightssh.project.party.entity.Party;
import com.google.code.lightssh.project.party.entity.PartyContact;
import com.google.code.lightssh.project.party.service.PartyContactManager;

/**
 * PartyContact Action
 * @author YangXiaojin
 *
 */
public class PartyContactAction extends CrudAction<PartyContact>{
	
	private static final long serialVersionUID = 1L;
	
	private ContactMechanism contact;
	
	private Party party;
	
	private PartyContactManager partyContactManager;
	
	private boolean exe_result;

	@JSON( name="contact" )
	public ContactMechanism getContact() {
		return contact;
	}

	public void setContact(ContactMechanism contact) {
		this.contact = contact;
	}

	public Party getParty() {
		return party;
	}

	public void setParty(Party party) {
		this.party = party;
	}

	@JSON( name="result" )
	public boolean isExe_result() {
		return exe_result;
	}

	public void setExe_result(boolean exeResult) {
		exe_result = exeResult;
	}

	public void setPartyContactManager(PartyContactManager partyContactManager) {
		this.partyContactManager = partyContactManager;
		super.manager = this.partyContactManager;
	}
	
	public String save( ){
		try{
			partyContactManager.save(party, contact);
			exe_result = true;
		}catch( Exception e ){
			exe_result = false;
			return INPUT;
		}
		
		return SUCCESS;
	}
	
	public String remove( ){
		try{
			partyContactManager.remove(contact);
		}catch( Exception e ){
			super.saveErrorMessage( e.getMessage() );
		}
		
		return SUCCESS;
	}

}

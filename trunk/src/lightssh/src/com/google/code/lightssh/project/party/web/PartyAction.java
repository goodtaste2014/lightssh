package com.google.code.lightssh.project.party.web;

import com.google.code.lightssh.common.model.page.ListPage;
import com.google.code.lightssh.common.service.BaseManager;
import com.google.code.lightssh.common.web.action.CrudAction;
import com.google.code.lightssh.project.party.entity.Organization;
import com.google.code.lightssh.project.party.entity.Party;

/**
 * Party Action
 * @author YangXiaojin
 *
 */
public class PartyAction extends CrudAction<Party>{

	private static final long serialVersionUID = 669140342947692813L;
	
	private Organization party;
	
	public void setPartyManager( BaseManager<Party> manager ){
		super.manager = manager;
	}

	public Organization getParty() {
		this.party = (Organization)super.model;
		return party;
	}

	public void setParty(Organization party) {
		this.party = party;
		super.model = this.party;
	}
	
    /**
    * list
    * @return
    */
    public String list( ){       
        if( page == null ){
            page = new ListPage<Party>(); //TODO
        }
        
        page.setSize(10);
       
        return super.list();
    }

}

package com.google.code.lightssh.project.party.web;

import java.util.HashSet;
import java.util.Set;

import com.google.code.lightssh.common.model.page.ListPage;
import com.google.code.lightssh.common.util.StringUtil;
import com.google.code.lightssh.common.web.action.CrudAction;
import com.google.code.lightssh.project.log.entity.Access;
import com.google.code.lightssh.project.party.entity.Organization;
import com.google.code.lightssh.project.party.entity.PartyRole.RoleType;
import com.google.code.lightssh.project.party.service.PartyManager;
import com.google.code.lightssh.project.party.service.PartyRelationshipManager;

public class OrganizationAction extends CrudAction<Organization>{

	private static final long serialVersionUID = 1L;
	
	private PartyRelationshipManager partyRelationshipManager;
	
	private PartyManager partyManager;
	
	private Organization party;
	
	private String party_role_type;
	
	private ListPage<Organization> page;

	public void setPartyRelationshipManager(
			PartyRelationshipManager partyRelationshipManager) {
		this.partyRelationshipManager = partyRelationshipManager;
	}

	public PartyManager getPartyManager() {
		return partyManager;
	}

	public void setPartyManager(PartyManager partyManager) {
		super.manager = this.manager;
		this.partyManager = partyManager;
	}

	public Organization getParty() {
		return party;
	}

	public void setParty(Organization party) {
		this.party = party;
	}
	
	public String getParty_role_type() {
		return party_role_type;
	}

	public void setParty_role_type(String partyRoleType) {
		party_role_type = partyRoleType;
	}

	public ListPage<Organization> getPage() {
		return page;
	}

	public void setPage(ListPage<Organization> page) {
		this.page = page;
	}
	
	/**
	 * popup
	 */
	public String popup( ){
		Organization root = partyRelationshipManager.listRollup();
		request.setAttribute("popup_org_rollup", root );
		
		return SUCCESS;
	}
	
	public String save( ){
		if( party == null )
			return INPUT;
		
		Set<RoleType> types = new HashSet<RoleType>( );
		types.add(RoleType.valueOf( this.party_role_type ) );
		
        Access access = new Access(  );
        access.init(request);
        //access.setOperator( SecurityUtil.getPrincipal() );
        
        try{
        	partyManager.save(party, types, access);
        }catch( Exception e ){ //other exception
            addActionError( e.getMessage() );
            return INPUT;
        } 
        
        String hint =  "保存(id="+ party.getIdentity() +")成功！" ;
        saveSuccessMessage( hint );
        String saveAndNext = request.getParameter("saveAndNext");
        if( saveAndNext != null && !"".equals( saveAndNext.trim() ) ){
        	return NEXT;
        }else{        	
        	return SUCCESS;
        }
	}

	
    /**
     * delete
     * @return
     */
     public String delete( ){
         if( party == null || StringUtil.clean(party.getIdentity()) == null ){
             return INPUT;
         }
        
         Access access = new Access(  );
         access.init(request);
         
         try{
        	 this.getPartyManager().remove( party,access );
        	 saveSuccessMessage( "成功删除数据(id=" + party.getIdentity() + ")！" );
         }catch( Exception e ){ //other exception
             saveErrorMessage( "删除发生异常：" + e.getMessage() );
             return INPUT;
         } 
        
         return SUCCESS;
     }

}

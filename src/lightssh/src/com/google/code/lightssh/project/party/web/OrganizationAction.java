package com.google.code.lightssh.project.party.web;

import java.util.List;

import com.google.code.lightssh.common.model.page.ListPage;
import com.google.code.lightssh.common.web.action.CrudAction;
import com.google.code.lightssh.project.party.entity.Organization;
import com.google.code.lightssh.project.party.service.OrganizationManager;

/**
 * Organization Action
 * @author YangXiaojin
 *
 */
public class OrganizationAction extends CrudAction<Organization>{

	private static final long serialVersionUID = -8796060311970284330L;
	
	private Organization organization;
	
	private boolean unique;
	
	public void setOrganizationManager( OrganizationManager manager ){
		super.manager = manager;
	}
	
	public OrganizationManager getManager( ){
		return (OrganizationManager)super.manager;
	}

	public boolean isUnique() {
		return unique;
	}

	public void setUnique(boolean unique) {
		this.unique = unique;
	}

	public Organization getOrganization() {
		this.organization = super.model;
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
		super.model = this.organization;
	}
	
	public String save( ){
		if( organization != null && organization.getParent() != null 
				&& (organization.getParent().getIdentity() == null || 
						"".equals(organization.getParent().getIdentity()) ))
			organization.setParent( null );
		
		return super.save();
	}
	
	public String list( ){
		if( page == null ){
            page = new ListPage<Organization>();
        }
       
        request.setAttribute( "list", this.getManager().listTop() );
       
		return SUCCESS;
	}
	
	/**
	 * popup
	 */
	public String popup( ){
		List<Organization> list = this.getManager().listTop();
		request.setAttribute("popup_list", list );
		
		return SUCCESS;
	}
	
	public String unique( ){
		this.unique = this.getManager().isUniqneName(organization);
		return SUCCESS;
	}
	
}

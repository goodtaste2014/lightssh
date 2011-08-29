package com.google.code.lightssh.project.party.service;

import com.google.code.lightssh.common.service.BaseManager;
import com.google.code.lightssh.project.party.entity.Organization;
import com.google.code.lightssh.project.party.entity.PartyRelationship;
import com.google.code.lightssh.project.party.entity.PartyRole;

/**
 * 
 * @author YangXiaojin
 *
 */
public interface PartyRelationshipManager extends BaseManager<PartyRelationship>{
	
	/**
	 * 查询所有隶属关系
	 */
	public Organization listRollup( );
	
	/**
	 * delete PartyRelationship by PartyRole
	 */
	public void remove( PartyRole partyRole );

}

package com.google.code.lightssh.project.party.service;

import java.util.List;

import com.google.code.lightssh.common.service.BaseManager;
import com.google.code.lightssh.project.party.entity.Organization;
import com.google.code.lightssh.project.party.entity.Party;
import com.google.code.lightssh.project.party.entity.PartyRelationship;
import com.google.code.lightssh.project.party.entity.PartyRole;

/**
 * 
 * @author YangXiaojin
 *
 */
public interface PartyRelationshipManager extends BaseManager<PartyRelationship>{
	
	/**
	 * 根据条件查询
	 */
	public PartyRelationship getRollupByFromParty( Party from );
	
	/**
	 * 根据条件查询
	 */
	public List<PartyRelationship> listRollupByToParty( Party to );
	
	/**
	 * 查询所有隶属关系
	 */
	public Organization listRollup( );
	
	/**
	 * delete PartyRelationship by PartyRole
	 */
	public void removeByFromRole( PartyRole partyRole );

}

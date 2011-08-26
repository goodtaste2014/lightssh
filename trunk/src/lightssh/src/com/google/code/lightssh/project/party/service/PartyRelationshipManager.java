package com.google.code.lightssh.project.party.service;

import com.google.code.lightssh.common.service.BaseManager;
import com.google.code.lightssh.project.party.entity.Organization;
import com.google.code.lightssh.project.party.entity.PartyRelationship;

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

}

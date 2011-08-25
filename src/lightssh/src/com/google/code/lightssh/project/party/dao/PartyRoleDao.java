package com.google.code.lightssh.project.party.dao;

import java.util.List;

import com.google.code.lightssh.common.dao.Dao;
import com.google.code.lightssh.project.party.entity.PartyRole;
import com.google.code.lightssh.project.party.entity.PartyRole.RoleType;

/**
 * PartyRole Dao
 * @author YangXiaojin
 *
 */
public interface PartyRoleDao extends Dao<PartyRole>{
	
	/**
	 * list by RoleType
	 */
	public List<PartyRole> list( RoleType type );

}

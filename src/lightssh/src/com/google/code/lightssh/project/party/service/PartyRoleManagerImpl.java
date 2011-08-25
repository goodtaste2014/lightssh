package com.google.code.lightssh.project.party.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.code.lightssh.common.service.BaseManagerImpl;
import com.google.code.lightssh.project.party.entity.Party;
import com.google.code.lightssh.project.party.entity.PartyRole;
import com.google.code.lightssh.project.party.entity.PartyRole.RoleType;

/**
 * @author YangXiaojin
 *
 */
public class PartyRoleManagerImpl extends BaseManagerImpl<PartyRole> implements PartyRoleManager{

	@Override
	public List<PartyRole> list(RoleType type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(Party party, Collection<RoleType> types) {
		if( party == null || party.getIdentity() == null 
				|| types == null || types.isEmpty())
			throw new IllegalArgumentException(
					"保存PartyRole参数错误，Party和RoleType不能为空。");
		
		List<PartyRole> entities = new ArrayList<PartyRole>( types.size() );
		for( RoleType type:types ){
			entities.add( new PartyRole(party,type));
		}
		
		super.dao.create(entities);
	}

}

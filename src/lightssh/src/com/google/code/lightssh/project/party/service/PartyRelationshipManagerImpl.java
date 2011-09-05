package com.google.code.lightssh.project.party.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.google.code.lightssh.common.service.BaseManagerImpl;
import com.google.code.lightssh.project.party.dao.PartyRelationshipDao;
import com.google.code.lightssh.project.party.entity.Organization;
import com.google.code.lightssh.project.party.entity.Party;
import com.google.code.lightssh.project.party.entity.PartyRelationship;
import com.google.code.lightssh.project.party.entity.PartyRole;
import com.google.code.lightssh.project.party.entity.PartyRelationship.RelationshipType;
import com.google.code.lightssh.project.party.entity.PartyRole.RoleType;

/**
 * 
 * @author YangXiaojin
 *
 */
@Component("partyRelationshipManager")
public class PartyRelationshipManagerImpl extends BaseManagerImpl<PartyRelationship> 
implements PartyRelationshipManager{
	
	@Resource(name="partyRoleManager")
	private PartyRoleManager partyRoleManager;
	
	public void setPartyRoleManager(PartyRoleManager partyRoleManager) {
		this.partyRoleManager = partyRoleManager;
	}

	@Resource(name="partyRelationshipDao")
	public void setDao( PartyRelationshipDao dao ){
		super.dao = dao;
	}
	
	public PartyRelationshipDao getDao(){
		return (PartyRelationshipDao)super.dao;
	}
	
	@Override
	public Organization listRollup( ) {
		List<PartyRelationship> list = getDao().list( RelationshipType.ORG_ROLLUP );
		if( list == null || list.isEmpty() ){
			List<Party> parties = partyRoleManager.listParty( RoleType.PARENT_ORG );
			if( parties == null || parties.isEmpty() )
				return null;
			
			for( Party party:parties)
				if( party instanceof Organization )
					return (Organization)party;
			
			return null;
		}
		
		Organization root = null;
		Set<Party> set = new HashSet<Party>( );
		for( PartyRelationship pr:list ){
			Party to = pr.getTo().getParty();
			Party from = pr.getFrom().getParty();
			if( to instanceof Organization && from instanceof Organization ){
				set.add( to );
				set.add( from );
				if( RoleType.PARENT_ORG.equals( pr.getTo().getType() ) ){
					//if( root != null )
					//	throw new ApplicationException("数据异常，存在多个'最上级组织'！");
					root = (Organization)to;
				}
			}
		}
		
		if( root != null ){
			for( PartyRelationship pr:list ){
				Party to = pr.getTo().getParty();
				Party from = pr.getFrom().getParty();
				if( set.contains( to ) ) //构造树
					((Organization)to).addChild( (Organization)from );
			}
		}
		
		return root;
	}

	@Override
	public void removeByFromRole(PartyRole partyRole) {
		getDao().removeByFromRole(partyRole);
	}

	@Override
	public PartyRelationship getRollupByFromParty(Party from) {
		List<PartyRelationship> results = getDao().list(
				RelationshipType.ORG_ROLLUP, from, null);
		return (results==null||results.isEmpty())?null:results.get(0);
	}

	@Override
	public List<PartyRelationship> listRollupByToParty(Party to) {
		return getDao().list( RelationshipType.ORG_ROLLUP, null, to);
	}

	
}

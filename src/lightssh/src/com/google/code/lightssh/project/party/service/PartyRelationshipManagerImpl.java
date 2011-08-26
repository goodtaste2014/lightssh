package com.google.code.lightssh.project.party.service;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.code.lightssh.common.service.BaseManagerImpl;
import com.google.code.lightssh.project.party.dao.PartyRelationshipDao;
import com.google.code.lightssh.project.party.entity.Organization;
import com.google.code.lightssh.project.party.entity.Party;
import com.google.code.lightssh.project.party.entity.PartyRelationship;
import com.google.code.lightssh.project.party.entity.PartyRelationship.RelationshipType;
import com.google.code.lightssh.project.party.entity.PartyRole.RoleType;

/**
 * 
 * @author YangXiaojin
 *
 */
public class PartyRelationshipManagerImpl extends BaseManagerImpl<PartyRelationship> 
implements PartyRelationshipManager{
	
	private PartyRoleManager partyRoleManager;
	
	public void setPartyRoleManager(PartyRoleManager partyRoleManager) {
		this.partyRoleManager = partyRoleManager;
	}

	public void setDao( PartyRelationshipDao dao ){
		super.dao = dao;
	}
	
	public PartyRelationshipDao getDao(){
		return (PartyRelationshipDao)super.dao;
	}
	
	public void save( PartyRelationship t ){
		if( t == null )
			return;
		
		if( t.getCreatedTime() == null ){
			t.setCreatedTime(Calendar.getInstance());
			super.create(t);
		}else 
			super.update(t);
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

}

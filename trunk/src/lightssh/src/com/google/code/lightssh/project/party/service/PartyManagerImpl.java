package com.google.code.lightssh.project.party.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import com.google.code.lightssh.common.ApplicationException;
import com.google.code.lightssh.common.model.page.ListPage;
import com.google.code.lightssh.common.service.BaseManagerImpl;
import com.google.code.lightssh.common.util.ReflectionUtil;
import com.google.code.lightssh.common.util.StringUtil;
import com.google.code.lightssh.project.log.entity.Access;
import com.google.code.lightssh.project.log.service.AccessManager;
import com.google.code.lightssh.project.party.dao.PartyDao;
import com.google.code.lightssh.project.party.entity.Organization;
import com.google.code.lightssh.project.party.entity.Party;
import com.google.code.lightssh.project.party.entity.PartyRelationship;
import com.google.code.lightssh.project.party.entity.PartyRole;
import com.google.code.lightssh.project.party.entity.Person;
import com.google.code.lightssh.project.party.entity.PartyRelationship.RelationshipType;
import com.google.code.lightssh.project.party.entity.PartyRole.RoleType;
import com.google.code.lightssh.project.sequence.service.SequenceManager;

/**
 * 
 * @author YangXiaojin
 *
 */
public class PartyManagerImpl extends BaseManagerImpl<Party> implements PartyManager{
	
	private SequenceManager sequenceManager;
	
	private AccessManager accessManager;
	
	private PartyRoleManager partyRoleManager;
	
	private PartyRelationshipManager partyRelationshipManager;
	
	public void setDao( PartyDao dao ){
		super.dao = dao;
	}
	
	public PartyDao getDao(){
		return (PartyDao)super.dao;
	}
	
	public void setSequenceManager(SequenceManager sequenceManager) {
		this.sequenceManager = sequenceManager;
	}
	
	public void setAccessManager(AccessManager accessManager) {
		this.accessManager = accessManager;
	}

	public void setPartyRoleManager(PartyRoleManager partyRoleManager) {
		this.partyRoleManager = partyRoleManager;
	}

	public void setPartyRelationshipManager(
			PartyRelationshipManager partyRelationshipManager) {
		this.partyRelationshipManager = partyRelationshipManager;
	}

	public void remove(Party t, Access access) {
		Class<?> clazz = Person.class;
		if( t instanceof Organization )
			clazz = Organization.class;
		
		Party party = getDao().read(clazz,t);
		if( party != null ){
			dao.delete(party);
			
			if( access != null && isDoLog(party))
				accessManager.log(access, party, null);
		}
		
	}

	@Override
	public void save(Party party, Access access) {
		if( party == null )
			throw new ApplicationException("数据为空，不能进行保存！");
		
		Party original = null;
		boolean inserted = StringUtil.clean( party.getIdentity() ) == null;
		
		if( party instanceof Organization && !isUniqneName( party ) )
			throw new ApplicationException("名称["+party.getName()+"]已经存在！");
		
		if( inserted ){
			party.setId( sequenceManager.nextSequenceNumber( party ) );
			dao.create( party );
		}else{
			Party old = dao.read( party );
			if( old != null ){
				original = old.clone();
				ReflectionUtil.assign(party, old);
				dao.update( old );
			}
		}
		
		if( access != null && isDoLog(party)){
			accessManager.log(access, original, party);
		}
	}

	/**
	 * 是否做日志记录
	 */
	protected boolean isDoLog( Party party ){
		//TODO 参数设置是否进行日志记录
		return true;
	}

	@Override
	public ListPage<Party> listOrganization(ListPage<Party> page,Party party) {
		return getDao().list( Organization.class, page, party);
	}

	@Override
	public ListPage<Party> listPerson(ListPage<Party> page, Party party) {
		return getDao().list( Person.class, page, party);
	}
	
	@Override
	public boolean isUniqneName(Party party) {
		if( party == null )
			return true;
		
		ListPage<Party> page = new ListPage<Party>(1);
		List<String> properties = new ArrayList<String>(1);
		properties.add("name");
		page = getDao().list(page,party,properties);
		
		Party exists = (page.getList()==null||page.getList().isEmpty())
			?null:page.getList().get(0);
		
		return exists == null || exists.getIdentity().equals( party.getIdentity() );
	}

	@Override
	public Party getParentCorporation() {
		List<PartyRole> list = partyRoleManager.list(RoleType.PARENT_ORG);
		if( list == null || list.isEmpty() )
			throw new ApplicationException("总公司/最上级组织 未设定！");
		
		return list.get(0).getParty();
	}

	@Override
	public void save(Organization party,Collection<RoleType> types, Access access) {
		if( party == null )
			throw new IllegalArgumentException("内部组织为空！");
			
		if( types == null || types.isEmpty() )
			throw new IllegalArgumentException("内部组织("+party.getName()+")角色类型为空！");
		
		Set<RoleType> roleTypes = new HashSet<RoleType>( types );
		roleTypes.add( RoleType.INTERNAL_ORG );
		
		List<PartyRole> partyRoles = partyRoleManager.list(RoleType.INTERNAL_ORG);
		if( partyRoles == null || partyRoles.isEmpty() ) 
			roleTypes.add( RoleType.PARENT_ORG ); //第一个保存的组织设定为'最高上级'
		
		this.save(party, access);
		
		PartyRole fromRole = null ; //下级隶属关系
		Set<RoleType> interalOrgSet = new HashSet<RoleType>( );
		CollectionUtils.addAll(interalOrgSet, RoleType.valuesOfInternalOrg());
		
		partyRoles = new ArrayList<PartyRole>( roleTypes.size() );
		for( RoleType type:roleTypes ){
			PartyRole newRole = new PartyRole(party,type);
			partyRoles.add( newRole );
			
			if( interalOrgSet.contains( type ) )
				fromRole = newRole;
		}
		//partyRoleManager.save(party, roleTypes);
		partyRoleManager.save( partyRoles );
		
		if( party.getParent() == null && party.getParent().getId() == null )
			return;
		
		PartyRole toRole = null; //上级隶属关系
		List<PartyRole> toRoles = partyRoleManager.list( party.getParent() );
		for( PartyRole role:toRoles ){
			if( RoleType.PARENT_ORG.equals( role.getType() ) ){
				toRole = role;
				break;
			}
				
			if( interalOrgSet.contains( role.getType() ) )
				toRole = role;
		}
		
		PartyRelationship pr = new PartyRelationship( 
				RelationshipType.ORG_ROLLUP,fromRole,toRole );
		partyRelationshipManager.save( pr );
	}
	
}

package com.google.code.lightssh.project.party.service;

import java.util.Collection;

import com.google.code.lightssh.common.model.page.ListPage;
import com.google.code.lightssh.common.service.BaseManager;
import com.google.code.lightssh.project.log.entity.Access;
import com.google.code.lightssh.project.party.entity.Organization;
import com.google.code.lightssh.project.party.entity.Party;
import com.google.code.lightssh.project.party.entity.PartyRole.RoleType;

/**
 * 
 * @author YangXiaojin
 *
 */
public interface PartyManager extends BaseManager<Party>{
	
	/**
	 * 带条件的分页查询
	 */
	public ListPage<Party> listPerson(ListPage<Party> page,Party party );
	
	/**
	 * 带条件的分页查询
	 */
	public ListPage<Party> listOrganization(ListPage<Party> page,Party party );
	
	/**
	 * 保存时记录日志
	 */
	public void save( Party party ,Access access );
	
	/**
	 * 保存内部组织
	 * @param party 内部组织
	 * @param types 角色类型
	 * @param access 日志记录
	 */
	public void save( Organization party,Collection<RoleType> types,Access access );
	
	/**
	 * 带日志的删除
	 */
	public void remove(Party party, Access access);
	
	/**
	 * 名称是否唯一
	 */
	public boolean isUniqneName( Party party );
	
	/**
	 * 查询总公司/最上层组织
	 */
	public Party getParentCorporation( );

}

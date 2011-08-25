package com.google.code.lightssh.project.party.service;

import com.google.code.lightssh.common.model.page.ListPage;
import com.google.code.lightssh.common.service.BaseManager;
import com.google.code.lightssh.project.log.entity.Access;
import com.google.code.lightssh.project.party.entity.Party;

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
	 * 带日志的删除
	 */
	public void remove(Party party, Access access);
	
	/**
	 * 名称是否唯一
	 */
	public boolean isUniqneName( Party party );

}

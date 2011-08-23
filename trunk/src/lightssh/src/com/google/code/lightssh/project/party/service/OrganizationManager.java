package com.google.code.lightssh.project.party.service;

import java.util.List;

import com.google.code.lightssh.common.service.BaseManager;
import com.google.code.lightssh.project.party.entity.Organization;

public interface OrganizationManager extends BaseManager<Organization>{
	
	/**
	 * 查询最上层组织
	 */
	public List<Organization> listTop(  );
	
	/**
	 * 名称是否唯一
	 */
	public boolean isUniqneName( Organization org );

}

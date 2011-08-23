package com.google.code.lightssh.project.party.dao;

import java.util.List;

import com.google.code.lightssh.common.dao.Dao;
import com.google.code.lightssh.project.party.entity.Organization;

public interface OrganizationDao extends Dao<Organization>{
	
	/**
	 * 查询最上层组织
	 */
	public List<Organization> listTop(  );

}

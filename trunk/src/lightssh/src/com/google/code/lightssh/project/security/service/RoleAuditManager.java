package com.google.code.lightssh.project.security.service;

import com.google.code.lightssh.common.service.BaseManager;
import com.google.code.lightssh.project.security.entity.RoleAudit;
import com.google.code.lightssh.project.security.entity.RoleChange;

/**
 * 
 * @author YangXiaojin
 *
 */
public interface RoleAuditManager extends BaseManager<RoleAudit>{
	
	/**
	 * 审核
	 */
	public void audit(RoleAudit ra,RoleChange rc );

}

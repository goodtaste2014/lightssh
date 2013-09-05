package com.google.code.lightssh.project.message.service;

import com.google.code.lightssh.common.service.BaseManager;
import com.google.code.lightssh.project.message.entity.Publish;
import com.google.code.lightssh.project.security.entity.LoginAccount;

/**
 * 
 * @author Aspen
 * @date 2013-9-5
 * 
 */
public interface PublishManager extends BaseManager<Publish>{
	
	/**
	 * 标记为已读
	 */
	public boolean markToRead( String id ,LoginAccount user);

}

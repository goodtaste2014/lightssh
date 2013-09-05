package com.google.code.lightssh.project.message.dao;

import com.google.code.lightssh.common.dao.Dao;
import com.google.code.lightssh.project.message.entity.Publish;
import com.google.code.lightssh.project.security.entity.LoginAccount;

/**
 * 
 * @author Aspen
 * 
 */
public interface PublishDao extends Dao<Publish>{
	
	/**
	 * 标记为已读
	 */
	public int markToRead( String id,LoginAccount user );

}

package com.google.code.lightssh.project.message.dao;

import com.google.code.lightssh.common.dao.Dao;
import com.google.code.lightssh.project.message.entity.Message;

/**
 * 
 * @author Aspen
 * 
 */
public interface MessageDao extends Dao<Message>{
	
	public void save( Message t );

}

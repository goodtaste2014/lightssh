package com.google.code.lightssh.project.message.service;

import com.google.code.lightssh.common.service.BaseManager;
import com.google.code.lightssh.project.message.entity.Message;

/**
 * 
 * @author Aspen
 * 
 */
public interface MessageManager extends BaseManager<Message>{
	
	/**
	 * 带锁的查询
	 */
	public Message getWithLock(String id );
	
	/**
	 * 增加删除数
	 */
	public boolean incDeletedCount( String id );

}

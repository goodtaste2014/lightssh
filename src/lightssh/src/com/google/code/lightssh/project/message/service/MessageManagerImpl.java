package com.google.code.lightssh.project.message.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.google.code.lightssh.common.service.BaseManagerImpl;
import com.google.code.lightssh.project.message.dao.MessageDao;
import com.google.code.lightssh.project.message.entity.Message;

/**
 * 
 * @author Aspen
 * 
 */
@Component("messageManager")
public class MessageManagerImpl extends BaseManagerImpl<Message> implements MessageManager{

	private static final long serialVersionUID = -4158212432035375331L;
	
	@Resource(name="messageDao")
	public void setDao(MessageDao dao){
		this.dao = dao;
	}

	public MessageDao getDao(){
		return (MessageDao)this.dao;
	}
	
}

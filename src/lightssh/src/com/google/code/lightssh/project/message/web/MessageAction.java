package com.google.code.lightssh.project.message.web;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.code.lightssh.project.message.entity.Message;
import com.google.code.lightssh.project.message.service.MessageManager;
import com.google.code.lightssh.project.web.action.GenericAction;

/**
 * 
 * @author YangXiojin
 * @date 2013-9-4
 * 
 */
@Component( "messageAction" )
@Scope("prototype")
public class MessageAction extends GenericAction<Message>{

	private static final long serialVersionUID = 3443312151707430333L;
	
	private Message message;
	
	@Resource(name="messageManager")
	public void setManager(MessageManager mgr){
		this.manager = mgr;
	}
	
	public MessageManager getManager(){
		return (MessageManager)this.manager;
	}

	public Message getMessage() {
		this.message = this.model;
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
		this.model = this.message;
	}
	

}

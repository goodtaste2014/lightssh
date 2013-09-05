package com.google.code.lightssh.project.message.service;

import java.util.Calendar;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.google.code.lightssh.common.ApplicationException;
import com.google.code.lightssh.common.service.BaseManagerImpl;
import com.google.code.lightssh.project.message.dao.MessageDao;
import com.google.code.lightssh.project.message.entity.Catalog;
import com.google.code.lightssh.project.message.entity.Message;

/**
 * 
 * @author Aspen
 * 
 */
@Component("messageManager")
public class MessageManagerImpl extends BaseManagerImpl<Message> implements MessageManager{

	private static final long serialVersionUID = -4158212432035375331L;
	
	@Resource(name="catalogManager")
	private CatalogManager catalogManager;
	
	//@Resource(name="publishManager")
	//private PublishManager publishManager;
	
	@Resource(name="messageDao")
	public void setDao(MessageDao dao){
		this.dao = dao;
	}

	public MessageDao getDao(){
		return (MessageDao)this.dao;
	}
	
	public void save( Message t ){
		if( t == null )
			throw new ApplicationException("参数为空！");
		
		Catalog catalog = catalogManager.getDefaultInfo();
		if( catalog == null )
			throw new ApplicationException("默认信息分类未初始化！");
		
		t.setCatalog( catalog );
		t.setCreatedTime( Calendar.getInstance() );
		t.setLinkable(false);
		t.setPublishedCount(0);//TODO
		t.setDeletedCount(0);
		t.setHitCount(0);
		t.setReaderCount(0);
		t.setTodoClean(false);
		
		getDao().save(t);
	}
	
}

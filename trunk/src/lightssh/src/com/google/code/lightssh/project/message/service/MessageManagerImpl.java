package com.google.code.lightssh.project.message.service;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.google.code.lightssh.common.dao.SearchCondition;
import com.google.code.lightssh.common.model.page.ListPage;
import com.google.code.lightssh.common.service.BaseManagerImpl;
import com.google.code.lightssh.project.message.dao.MessageDao;
import com.google.code.lightssh.project.message.entity.Message;

/**
 * 
 * @author YangXiojin
 * @date 2013-9-4
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
	
	public ListPage<Message> list( ListPage<Message> page, Message t ){
		SearchCondition sc = new SearchCondition();
		
		if( t != null ){
			if( StringUtils.isNotEmpty(t.getTitle())){
				sc.like("title", t.getTitle().trim());
			}
			
			if( StringUtils.isNotEmpty(t.getContent())){
				sc.like("content", t.getContent().trim());
			}
			
			if( StringUtils.isNotEmpty(t.getCreator())){
				sc.equal("creator", t.getCreator().trim());
			}
			
			if( t.getCatalog() != null ){
				if( StringUtils.isNotEmpty(t.getCatalog().getId()) )
					sc.equal("catalog.id", t.getCatalog().getId().trim());
			}
		}
		
		return dao.list(page, sc);
	}
}

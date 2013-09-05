package com.google.code.lightssh.project.message.service;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.google.code.lightssh.common.dao.SearchCondition;
import com.google.code.lightssh.common.model.page.ListPage;
import com.google.code.lightssh.common.service.BaseManagerImpl;
import com.google.code.lightssh.project.message.dao.PublishDao;
import com.google.code.lightssh.project.message.entity.Message;
import com.google.code.lightssh.project.message.entity.Publish;
import com.google.code.lightssh.project.security.entity.LoginAccount;

/**
 * 
 * @author Aspen
 * @date 2013-9-5
 * 
 */
@Component("publishManager")
public class PublishManagerImpl extends BaseManagerImpl<Publish> implements PublishManager{

	private static final long serialVersionUID = -4659284392763441005L;
	
	@Resource(name="publishDao")
	public void setDao(PublishDao dao){
		this.dao = dao;
	}
	
	public PublishDao getDao(){
		return (PublishDao)this.dao;
	}
	

	/**
	 * 标记为已读
	 */
	public boolean markToRead( String id ,LoginAccount user){
		if( StringUtils.isEmpty(id) || user == null )
			return false;
		
		return getDao().markToRead(id, user) > 0;
	}
	
	public ListPage<Publish> list(ListPage<Publish> page ,Publish t){
		SearchCondition sc = new SearchCondition();
		if( t != null ){
			if( t.getUser() != null && t.getUser().getId() != null ){
				sc.equal("user.id", t.getUser().getId() );
			}
			
			if( t.getPeriod() != null ){
				Calendar cal = Calendar.getInstance();
				Date start = t.getPeriod().getStart();
				Date end = t.getPeriod().getEnd();
				
				if( start != null ){
					cal.setTime(start);
					sc.greateThanOrEqual("createdTime",cal);
				}
				
				if( end != null ){
					Calendar cal_end = Calendar.getInstance();
					cal_end.setTime(end);
					cal_end.add(Calendar.DAY_OF_MONTH, 1);
					cal_end.add(Calendar.SECOND, -1);
					sc.lessThanOrEqual("createdTime",cal_end);
				}
			}
			
			if( t.getMessage() != null ){
				Message msg = t.getMessage();
				
				if( StringUtils.isNotEmpty( msg.getTitle() ) ){
					sc.like("message.title", msg.getTitle().trim() );
				}
			}
		}
		
		return dao.list(page, sc);
	}

}

package com.google.code.lightssh.project.message.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.google.code.lightssh.common.service.BaseManagerImpl;
import com.google.code.lightssh.project.message.dao.SubscriptionDao;
import com.google.code.lightssh.project.message.entity.Subscription;

/**
 * 
 * @author YangXiojin
 * @date 2013-9-3
 * 
 */
@Component("subscriptionManager")
public class SubscriptionManagerImpl extends BaseManagerImpl<Subscription> implements SubscriptionManager{

	private static final long serialVersionUID = 4433581949519273589L;
	
	@Resource(name="subscriptionDao")
	public void setDao(SubscriptionDao dao){
		this.dao = dao;
	}
	
	public SubscriptionDao getDao(){
		return (SubscriptionDao)this.dao;
	}

}

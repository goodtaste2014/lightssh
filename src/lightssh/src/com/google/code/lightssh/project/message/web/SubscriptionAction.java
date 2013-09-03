package com.google.code.lightssh.project.message.web;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.code.lightssh.project.message.entity.Subscription;
import com.google.code.lightssh.project.message.service.SubscriptionManager;
import com.google.code.lightssh.project.web.action.GenericAction;

/**
 * 
 * @author YangXiojin
 * @date 2013-9-3
 * 
 */
@Component( "subscriptionAction" )
@Scope("prototype")
public class SubscriptionAction extends GenericAction<Subscription>{

	private static final long serialVersionUID = 2395680064090502873L;
	
	@Resource( name="subscriptionManager" )
	public void setManager(SubscriptionManager subscriptionManager) {
		super.manager = subscriptionManager;
	}

}

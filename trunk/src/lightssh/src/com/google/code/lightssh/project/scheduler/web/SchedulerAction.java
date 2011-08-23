package com.google.code.lightssh.project.scheduler.web;

import java.util.List;

import com.google.code.lightssh.common.web.action.BaseAction;
import com.google.code.lightssh.project.scheduler.entity.TriggerWrap;
import com.google.code.lightssh.project.scheduler.service.SchedulerManager;

/**
 * scheduler action
 * @author YangXiaojin
 *
 */
public class SchedulerAction extends BaseAction{

	private static final long serialVersionUID = 6483507951968694317L;
	
	private SchedulerManager schedulerManager;

	public void setSchedulerManager(SchedulerManager schedulerManager) {
		this.schedulerManager = schedulerManager;
	}
	
	/**
	 * 列出所有定时任务
	 */
	public String listTriggers(){
		List<TriggerWrap> triggers = schedulerManager.listAllTrigger();
		request.setAttribute("list", triggers);
		return SUCCESS;
	}
	
	/**
	 * 停用/启用 Trigger
	 */
	public String toggle(){
		String name = request.getParameter("name");
		
		try{
			schedulerManager.toggleTrigger(name);
		}catch( Exception e ){
			this.addActionError("任务操作失败：" + e.getMessage() );
		}
		
		return SUCCESS;
	}

}

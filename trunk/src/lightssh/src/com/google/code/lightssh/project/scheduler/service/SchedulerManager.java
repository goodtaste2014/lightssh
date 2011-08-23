package com.google.code.lightssh.project.scheduler.service;

import java.util.List;

import com.google.code.lightssh.common.service.Manager;
import com.google.code.lightssh.project.scheduler.entity.TriggerWrap;

/**
 * 
 * @author YangXiaojin
 *
 */
public interface SchedulerManager extends Manager{
	
	/**
	 * 初始化CronTrigger
	 */
	public void initCronTrigger( );
	
	/**
	 * 更新Trigger
	 */
	public void changeCronTrigger( String triggerName,String cronExpression);
	
	/**
	 * 启动/停止Trigger
	 */
	public void toggleTrigger( String triggerName );
	
	/**
	 * 列出所有Trigger
	 */
	public List<TriggerWrap> listAllTrigger( );

}

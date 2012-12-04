package com.google.code.lightssh.project.scheduler.service;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;


/** 
 * 定时任务接口
 */
public abstract class AbstractSchedulerService extends QuartzJobBean implements SchedulerService{
	
	private static final long serialVersionUID = 2057666129105043334L;
	
	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		execute();
	}

}

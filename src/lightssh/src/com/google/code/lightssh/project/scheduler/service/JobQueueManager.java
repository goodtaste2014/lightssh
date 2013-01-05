package com.google.code.lightssh.project.scheduler.service;

import com.google.code.lightssh.common.model.page.ListPage;
import com.google.code.lightssh.common.service.BaseManager;
import com.google.code.lightssh.project.scheduler.entity.JobQueue;

/**
 * 
 * @author YangXiaojin
 *
 */
public interface JobQueueManager extends BaseManager<JobQueue>{
	
	/**
	 * 任务入队列
	 */
	public void jobInQueue( String jobType,String refId );
	
	/**
	 * 任务入队列
	 */
	public void jobInQueue( String jobType,String refId,int maxSendCount );
	
	/**
	 * 任务入队列
	 */
	public void jobInQueue( String jobType,int maxSendCount,String[] refIds );
	
	/**
	 * 任务入队列
	 */
	public void jobInQueue( String jobType,String jobName,int maxSendCount,String[] refIds );
	
	/**
	 * 工作任务队列大小
	 */
	public int getJobQueueSize( );
	
	/**
	 * 工作任务队列大小
	 * @param jobTypeId 工作任务类型ID
	 */
	public int getJobQueueSize(String jobTypeId);
	
	/**
	 * 查询工作队列
	 */
	public ListPage<JobQueue> list( ListPage<JobQueue> page,String jobTypeId );

}

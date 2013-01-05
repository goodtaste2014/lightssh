package com.google.code.lightssh.project.scheduler.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.google.code.lightssh.common.ApplicationException;
import com.google.code.lightssh.common.dao.SearchCondition;
import com.google.code.lightssh.common.model.page.ListPage;
import com.google.code.lightssh.common.service.BaseManagerImpl;
import com.google.code.lightssh.project.scheduler.dao.JobQueueDao;
import com.google.code.lightssh.project.scheduler.entity.JobQueue;
import com.google.code.lightssh.project.scheduler.entity.SchedulerType;

/**
 * 
 * @author YangXiaojin
 *
 */
@Component("jobQueueManager")
public class JobQueueManagerImpl extends BaseManagerImpl<JobQueue> implements JobQueueManager{

	private static final long serialVersionUID = 3580422383287494752L;
	
	/**默认队列最大执行次数*/
	public static final int DEFAULT_MAX_EXECUTE_COUNT = 10;
	
	@Resource(name="schedulerTypeManager")
	private SchedulerTypeManager schedulerTypeManager;
	
	@Resource(name="jobQueueDao")
	public void setDao(JobQueueDao dao){
		this.dao = dao;
	}
	
	public JobQueueDao getDao(){
		return (JobQueueDao)this.dao;
	}

	/**
	 * 工作任务队列大小
	 */
	@Override
	public int getJobQueueSize( ){
		return getJobQueueSize(null);
	}
	
	/**
	 * 工作任务队列大小
	 */
	@Override
	public int getJobQueueSize( String jobTypeId ){
		ListPage<JobQueue> page = new ListPage<JobQueue>(0);
		SearchCondition sc = new SearchCondition();
		if( !StringUtils.isEmpty(jobTypeId) ){
			sc.equal("type.id", jobTypeId.trim() );
		}
		page = dao.list(page, sc);
		
		return page.getAllSize();
	}
	
	/**
	 * delete all
	 */
	public void remove( Collection<JobQueue> entities ){
		for( JobQueue item:entities )
			dao.delete(item.getIdentity());
	}
	
	/**
	 * 查询工作队列
	 */
	public ListPage<JobQueue> list( ListPage<JobQueue> page,JobQueue t ){
		if( page == null )
			page = new ListPage<JobQueue>();
		
		SearchCondition sc = new SearchCondition();
		if( t != null ){
			if( t.getType() != null ){
				if( !StringUtils.isEmpty(t.getType().getId()) ){
					sc.equal("type.id", t.getType().getId().trim() );
				}
			}
			
			//...
		}
		
		return dao.list(page, sc);
	}
	
	/**
	 * 查询工作队列
	 */
	public ListPage<JobQueue> list( ListPage<JobQueue> page,String jobTypeId ){
		if( StringUtils.isEmpty(jobTypeId) )
			return page;
		
		if( page == null )
			page = new ListPage<JobQueue>();
		
		page.addAscending("createdTime");
		JobQueue t = new JobQueue();
		SchedulerType type = new SchedulerType();
		type.setId(jobTypeId);
		t.setType( type );
		
		return list(page, t);
	}

	/**
	 * 任务入队列
	 */
	public void jobInQueue( String jobType,String refId ){
		jobInQueue( jobType,refId,DEFAULT_MAX_EXECUTE_COUNT);
	}
	
	/**
	 * 任务入队列
	 */
	public void jobInQueue( String jobType,String refId,int maxSendCount ){
		if( StringUtils.isEmpty(refId) || StringUtils.isEmpty(jobType) )
			return;
		
		SchedulerType type = schedulerTypeManager.get(jobType);
		if( type == null )
			throw new ApplicationException("工作任务类型["+jobType+"]不存在！");
		
		JobQueue queue = new JobQueue();
		queue.setType(type);
		queue.setRefId(refId);
		queue.setCreatedTime( Calendar.getInstance() );
		queue.setMaxSendCount(maxSendCount);
		queue.setFailureCount(0);
		queue.setStatus(JobQueue.Status.NEW);
		
		getDao().create(queue);
	}
	
	/**
	 * 任务入队列
	 */
	public void jobInQueue( String jobType,int maxSendCount,String[] refIds ){
		jobInQueue(jobType,null,maxSendCount,refIds);
	}
	
	/**
	 * 任务入队列
	 */
	public void jobInQueue( String jobType,String jobName,int maxSendCount,String[] refIds ){
		SchedulerType type = null;
		if( !StringUtils.isEmpty(jobType) && !StringUtils.isEmpty(jobName) ){
			type = schedulerTypeManager.safeGet(jobType,jobName);
		}else{
			type = schedulerTypeManager.get(jobType);
		}
		
		if( type == null )
			throw new ApplicationException("工作任务类型["+jobType+"]不存在！");
		
		List<JobQueue> queues = new ArrayList<JobQueue>();
		for(String refId:refIds ){
			JobQueue queue = new JobQueue();
			queue.setType(type);
			queue.setRefId(refId);
			queue.setCreatedTime( Calendar.getInstance() );
			queue.setMaxSendCount(maxSendCount);
			queue.setFailureCount(0);
			queue.setStatus(JobQueue.Status.NEW);
			
			queues.add(queue);
		}
		
		if( !queues.isEmpty() )
			getDao().create(queues);
	}
}

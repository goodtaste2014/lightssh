package com.google.code.lightssh.project.scheduler.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.Trigger.TriggerState;
import org.quartz.impl.StdScheduler;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.lightssh.common.ApplicationException;
import com.google.code.lightssh.common.util.StringUtil;
import com.google.code.lightssh.project.scheduler.entity.JobInterval;
import com.google.code.lightssh.project.scheduler.entity.TriggerWrap;

public class SchedulerManagerImpl implements SchedulerManager{
	
	private static final long serialVersionUID = 544811394387288846L;

	private static Logger log = LoggerFactory.getLogger(SchedulerManagerImpl.class);
	
	private JobIntervalManager jobIntervalManager;
	
	private StdScheduler scheduler;

	public void setJobIntervalManager(JobIntervalManager jobIntervalManager) {
		this.jobIntervalManager = jobIntervalManager;
	}

	public void setQuartzScheduler(
			StdScheduler scheduler) {
		this.scheduler = scheduler;
	}
	
	/**
	 * 获得系统Trigger
	 */
	private List<Trigger> listTriggers( ){
		List<Trigger> triggers = new ArrayList<Trigger>( );
		
		List<String> groups  = new ArrayList<String>();
		groups.add( Scheduler.DEFAULT_GROUP );
		
		try{
			groups = scheduler.getTriggerGroupNames();
		}catch( Exception e ){}
		
		try{
			for( String group:groups ){
				Set<TriggerKey> keys = scheduler.getTriggerKeys(
						GroupMatcher.triggerGroupContains(group));
				
				for( TriggerKey key:keys )
					triggers.add( scheduler.getTrigger( key ) );
			}
		}catch( Exception e ){
			log.warn("获取系统定时任务异常："+e.getMessage());
		}
		
		return triggers;
	}
	
	/**
	 * 定时器设置
	 */
	private Map<String,JobInterval> getJobIntervalMap( ){
		List<JobInterval> list = jobIntervalManager.listAvailable();
		if( list == null || list.isEmpty() )
			return null;
		
		Map<String,JobInterval> jiMap = new HashMap<String,JobInterval>();
		for( JobInterval ji:list )
			jiMap.put(ji.getTriggerName(), ji);
		
		return jiMap;
	}

	@Override
	public void initCronTrigger() {
		if( scheduler == null || jobIntervalManager == null )
			return;
		
		Map<String,JobInterval> jiMap = getJobIntervalMap();
		if( jiMap == null || jiMap.isEmpty() )
			return;
		
		List<Trigger> triggers = listTriggers( );
		if( triggers == null || triggers.isEmpty() )
			return;
		
		//针对每个Trigger 设置Cron表示达
		/*
		for( Trigger trigger:triggers ){
			JobInterval jobInterval = jiMap.get( trigger.getName() );
			if( trigger instanceof CronTrigger && jobInterval != null ){
				try{
					if( jobInterval.isEnabled() ){ //重设置trigger
						((CronTrigger)trigger).setCronExpression( jobInterval.getCronExpression() );
						scheduler.rescheduleJob(trigger.getName(), trigger.getGroup(), trigger);
					}else{//停用
						scheduler.pauseTrigger(trigger.getName(), trigger.getGroup());
					}
				}catch( Exception e ){
					//e.printStackTrace();
				}
			}
		}//end for
		*/
	}

	@Override
	public void changeCronTrigger(String triggerName, String cronExpression) {
		if( triggerName == null || cronExpression == null )
			return;
		
		JobInterval jobInterval = jobIntervalManager.get( triggerName );
		if( jobInterval == null )
			return;
		jobInterval.setCronExpression(cronExpression);
		jobIntervalManager.save(jobInterval);
		
		List<Trigger> triggers = listTriggers( );
		if( triggers == null || triggers.isEmpty() )
			return;
		
		/*
		for( Trigger trigger:triggers ){
			if( triggerName.equals( trigger.getName() ) && trigger instanceof CronTrigger ){
				try{
					((CronTrigger)trigger).setCronExpression( jobInterval.getCronExpression() );
					scheduler.rescheduleJob(trigger.getName(), trigger.getGroup(), trigger);
				}catch( Exception e ){
					//e.printStackTrace();
				}
				break;
			}
		}//end for
		*/
	}
	
	/**
	 * 定时器状态
	 */
	protected TriggerState getTriggerState( TriggerKey key ){
		try {
			return scheduler.getTriggerState( key );
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		
		return TriggerState.NONE;
	}

	@Override
	public List<TriggerWrap> listAllTrigger() {
		List<TriggerWrap> result = null;
		List<Trigger> list = listTriggers();
		
		if( list != null && !list.isEmpty() ){
			result = new ArrayList<TriggerWrap>();
			
			//Map<String,JobInterval> jiMap = getJobIntervalMap();
			
			for(Trigger trigger:list ){
				TriggerWrap wrap = new TriggerWrap( trigger );
				wrap.setState(getTriggerState(trigger.getKey()));
				
				result.add(wrap);
			}
		}
		return result;
	}
	
	/**
	 * 启动或暂停定时器
	 */
	public void toggleTrigger(String triggerName ){
		if( StringUtil.clean(triggerName)==null )
			return;
		
		String cronExpression = null;
		JobInterval jobInterval = jobIntervalManager.get( triggerName );
		if( jobInterval != null ){
			jobInterval.setEnabled( Boolean.valueOf( !jobInterval.isEnabled() ) );
			cronExpression = StringUtil.clean(jobInterval.getCronExpression());
		}
		
		Trigger target = null;
		try {
			target = scheduler.getTrigger(TriggerKey.triggerKey(triggerName));
		} catch (SchedulerException e) {
			log.warn("获取定时任务[{}]异常：{}",triggerName,e.getMessage());
			throw new ApplicationException( e );
		}
		
		if( target == null )
			throw new ApplicationException( "找不到相关定时任务["+triggerName+"]" );
			
		boolean enabled = false;
		
		try {
			TriggerState state = scheduler.getTriggerState( target.getKey() );
			
			if( target instanceof CronTrigger){ 
				cronExpression = ((CronTrigger)target).getCronExpression();
			}
			
			if( TriggerState.PAUSED.equals(state) ){
				scheduler.resumeTrigger( target.getKey() );//启动定时器
				enabled = true;
			}else if( TriggerState.NORMAL.equals(state) ){
				scheduler.pauseTrigger(target.getKey());//暂停定时器
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		
		//-----------------------------------------------------------------------------
		List<Trigger> triggers = listTriggers( );
		if( triggers == null || triggers.isEmpty() )
			return;
		
		
		/*
		for( Trigger trigger:triggers ){
			if( triggerName.equals( trigger.getName() ) ){
				try{
					int state = getTriggerState(trigger.getName(), trigger.getGroup());
					if( Trigger.STATE_PAUSED == state ){
						if( trigger instanceof CronTrigger){ 
							if(cronExpression != null){
								((CronTrigger)trigger).setCronExpression(cronExpression);
								scheduler.rescheduleJob(trigger.getName(), trigger.getGroup(), trigger);
							}else{
								cronExpression = ((CronTrigger)trigger).getCronExpression();
							}
						}
						scheduler.resumeTrigger(trigger.getName(), trigger.getGroup());
						enabled = true;
					}else
						scheduler.pauseTrigger(trigger.getName(), trigger.getGroup());
				}catch( Exception e ){
					log.error("操作定时任务[{}]异常:",trigger.getName(),e);
					throw new ApplicationException( e );
				}
				break;
			}
		}//end for
		*/
		
		if( jobInterval == null )
			jobInterval = new JobInterval();

		jobInterval.setTriggerName(triggerName);
		jobInterval.setEnabled( enabled );
		jobInterval.setCronExpression(cronExpression);
		jobIntervalManager.save(jobInterval);
	}
	
}

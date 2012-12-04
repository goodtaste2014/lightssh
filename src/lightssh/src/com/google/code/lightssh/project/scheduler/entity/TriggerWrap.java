package com.google.code.lightssh.project.scheduler.entity;

import java.util.Date;

import org.quartz.CronTrigger;
import org.quartz.Trigger;

/**
 * 定时任务包裹类
 * @author YangXiaojin
 *
 */
public class TriggerWrap {

	private static final long serialVersionUID = -6368089938691033645L;
	
	private Trigger trigger;
	
	private boolean pause;

	public boolean isPause() {
		return pause;
	}

	public void setPause(boolean pause) {
		this.pause = pause;
	}
	
	public TriggerWrap( Trigger trigger ){
		this.trigger = trigger;
	}
	
	public String getName(){
		return trigger.getKey().getName();
	}
	
	public String getCronExpression(){
		if( trigger instanceof CronTrigger )
			return ((CronTrigger)trigger).getCronExpression();
		
		return null;
	}
	
	public Date getPreviousFireTime(){
		return trigger.getPreviousFireTime();
	}
	
	public Date getNextFireTime(){
		return trigger.getNextFireTime();
	}
	
	public Date getEndTime(){
		return trigger.getEndTime();
	}

	public String getGroup(){
		return trigger.getKey().getGroup();
	}
	
	public String getDescription(){
		return trigger.getDescription();
	}

}

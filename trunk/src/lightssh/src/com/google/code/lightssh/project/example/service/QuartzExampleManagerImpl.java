package com.google.code.lightssh.project.example.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.google.code.lightssh.project.scheduler.service.AbstractSchedulerService;

public class QuartzExampleManagerImpl extends AbstractSchedulerService  {
	
	private static final long serialVersionUID = 4012188759799974113L;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd mm:HH:ss");
	
	public void execute( ){
		System.out.println("running ... 定时任务示例！" 
				+ sdf.format(Calendar.getInstance().getTime()));
	}

}

package com.google.code.lightssh.project.scheduler.service;

import java.util.List;

import com.google.code.lightssh.common.service.BaseManagerImpl;
import com.google.code.lightssh.project.scheduler.entity.JobInterval;

/**
 * 
 * @author YangXiaojin
 *
 */
public class JobIntervalManagerImpl extends BaseManagerImpl<JobInterval> implements JobIntervalManager{

	@Override
	public List<JobInterval> listAvailable() {
		//TODO 
		return super.dao.listAll();
	}

}

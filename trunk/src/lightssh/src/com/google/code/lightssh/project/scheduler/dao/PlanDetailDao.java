package com.google.code.lightssh.project.scheduler.dao;

import java.util.List;

import com.google.code.lightssh.common.dao.Dao;
import com.google.code.lightssh.project.scheduler.entity.PlanDetail;


/**
 * 
 * @author YangXiaojin
 *
 */
public interface PlanDetailDao extends Dao<PlanDetail>{
	
	/**
	 * 查询依赖未完成任务
	 */
	public List<PlanDetail> listRelyOnUnsuccessful( String id );
	
	/**
	 * 更新计划任务明细
	 */
	public int update(String id,PlanDetail.Status originalStatus,PlanDetail.Status newStatus );

}

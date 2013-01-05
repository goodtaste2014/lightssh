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
import com.google.code.lightssh.common.model.Result;
import com.google.code.lightssh.common.model.page.ListPage;
import com.google.code.lightssh.common.service.BaseManagerImpl;
import com.google.code.lightssh.common.util.TextFormater;
import com.google.code.lightssh.project.scheduler.dao.PlanDao;
import com.google.code.lightssh.project.scheduler.dao.PlanDetailDao;
import com.google.code.lightssh.project.scheduler.entity.Plan;
import com.google.code.lightssh.project.scheduler.entity.PlanDetail;
import com.google.code.lightssh.project.scheduler.entity.SchedulerType;
import com.google.code.lightssh.project.sequence.service.SequenceManager;

/**
 * 执行计划业务实现
 * @author YangXiaojin
 *
 */
@Component("planManager")
public class PlanManagerImpl extends BaseManagerImpl<Plan> implements PlanManager{

	private static final long serialVersionUID = -6376825556178324639L;
	
	@Resource(name="planDetailDao")
	private PlanDetailDao planDetailDao;
	
	@Resource(name="sequenceManager")
	private SequenceManager sequenceManager;
	
	@Resource(name="jobQueueManager")
	private JobQueueManager jobQueueManager;
	
	@Resource(name="planDao")
	public void setDao(PlanDao dao ){
		this.dao = dao;
	}
	
	public PlanDao getDao(){
		return (PlanDao)this.dao;
	}
	
	/**
	 * 计划明细
	 */
	public PlanDetail getDetail( String id ){
		if( StringUtils.isEmpty(id) )
			return null;
		
		return this.planDetailDao.read(id);
	}
	
	/**
	 * 执行计划明细
	 */
	public List<PlanDetail> listDetail(String planId){
		if( StringUtils.isEmpty(planId) )
			return null;
		
		ListPage<PlanDetail> page = new ListPage<PlanDetail>(Integer.MAX_VALUE);
		SearchCondition sc = new SearchCondition();
		sc.equal("plan.id", planId );
		
		page.addAscending("sequence");
		page = this.planDetailDao.list(page, sc);
		
		return page.getList();
	}
	
	/**
	 * 执行计划明细
	 */
	public List<PlanDetail> listDetail(Plan plan){
		return listDetail(plan.getId());
	}
	
	/**
	 * 查询执行计划明细
	 */
	public List<PlanDetail> listDetail( Object[] ids ){
		ListPage<PlanDetail> page = new ListPage<PlanDetail>(Integer.MAX_VALUE);
		SearchCondition sc = new SearchCondition();
		sc.in("id",ids);
		
		page.addAscending("sequence");
		this.planDetailDao.list(page,sc);
		
		return page.getList();
	}
	
	/**
	 * 取创建时间最新记录
	 */
	public Plan getLast(SchedulerType type){
		if( type == null || type.getId() == null )
			return null;
		
		return getLastByType( type.getId() );
	}
	
	/**
	 * 取创建时间最新记录
	 */
	public Plan getLastByType(String type){
		if(  StringUtils.isEmpty(type) )
			return null;
		
		ListPage<Plan> page = new ListPage<Plan>(1);
		SearchCondition sc = new SearchCondition();
		sc.equal("type.id", type );
		
		page.addDescending("createdTime");
		
		return dao.list(page, sc).getFirst();
	}
	
	/**
	 * 保存执行计划
	 */
	public void save( Plan plan,List<PlanDetail> details ){
		if( plan == null || details == null )
			throw new ApplicationException("参数为空！");
		
		plan.setId( sequenceManager.nextSequenceNumber(plan)
				.replaceFirst(Plan.SEQUENCE_KEY,"") );
		dao.create(plan);
		
		List<String> refIds = new ArrayList<String>();
		for( PlanDetail item:details ){
			item.setId( plan.getId() + "-" + item.getSequence() );
			item.setPlan(plan);
			refIds.add(item.getId());
		}
		planDetailDao.create(details);
		
		jobQueueManager.jobInQueue(plan.getType().getId()
				,1,refIds.toArray(new String[0]));
	}
	
	/**
	 * 明细入任务队列
	 */
	public void detailInQueue(PlanDetail pd){
		PlanDetail detail = this.getDetail( pd.getId() );
		if( detail == null )
			throw new ApplicationException("明细数据["+pd.getId()+"]不存在！");
		
		if( detail.getPlan() == null || detail.getPlan().getType() == null )
			throw new ApplicationException("明细数据["+pd.getId()+"]不完整！");
		
		jobQueueManager.jobInQueue(
				detail.getPlan().getType().getId(),detail.getId(),1);
	}
	
	/**
	 * 更新执行时间
	 */
	public void updateFireTime(String id,Calendar fireTime){
		if( StringUtils.isEmpty(id) )
			return;
		
		if( fireTime == null )
			fireTime = Calendar.getInstance();
		
		Plan plan = dao.read(id);
		if( plan != null ){
			plan.setFireTime(fireTime);
			dao.update(plan);
		}
	}
	
	/**
	 * 更新完成时间
	 */
	public void updateFinishTime(String id,Calendar finishTime){
		if( StringUtils.isEmpty(id) )
			return;
		
		if( finishTime == null )
			finishTime = Calendar.getInstance();
		
		Plan plan = dao.read(id);
		if( plan != null ){
			if( plan.getFinishTime() != null )
				plan.setFinishTime(finishTime);
			plan.setFinished(Boolean.TRUE);
			
			dao.update(plan);
		}
	}
	
	/**
	 * 更新数据状态
	 */
	public void updateStatus(Collection<Result> results){
		if( results == null || results.isEmpty() )
			return;
		
		for( Result result:results ){
			PlanDetail detail = planDetailDao.read( result.getKey() );
			if( detail != null ){
				if( detail.getFireTime() == null )
					detail.setFireTime(Calendar.getInstance());
				
				if( result.isSuccess() ){
					detail.setFinishTime( Calendar.getInstance() );
					detail.setStatus(PlanDetail.Status.SUCCESS);
				}else{
					detail.setStatus(PlanDetail.Status.FAILURE);
					detail.setErrMsg( TextFormater.format(result.getMessage(),197,true) );
					detail.incFailureCount( );
				}
				planDetailDao.update(detail);
			}
		}//end for
	}

}

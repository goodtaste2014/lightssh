package com.google.code.lightssh.project.scheduler.dao;

import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.google.code.lightssh.common.dao.jpa.JpaDao;
import com.google.code.lightssh.project.scheduler.entity.PlanDetail;


/**
 * 
 * @author YangXiaojin
 *
 */
@Repository("planDetailDao")
public class PlanDetailDaoJpa extends JpaDao<PlanDetail> implements PlanDetailDao{

	private static final long serialVersionUID = 2299825707757948995L;
	
	/**
	 * 查询依赖未完成任务
	 */
	@SuppressWarnings("unchecked")
	public List<PlanDetail> listRelyOnUnsuccessful( String id ){
		if( StringUtils.isEmpty(id) )
			return null;
		
		StringBuffer sb = new StringBuffer(" FROM " + entityClass.getName());
		sb.append(" AS m WHERE m.precondition.id = ? AND m.status != ? ");
		
		Query query = getEntityManager().createQuery(sb.toString());
		addQueryParams(query,new Object[]{id,PlanDetail.Status.SUCCESS});
		
		return query.getResultList();
	}
	
	/**
	 * 更新计划任务明细
	 */
	public int update(String id,PlanDetail.Status originalStatus,PlanDetail.Status newStatus ){
		String hql = " UPDATE PlanDetail AS m SET m.status = '" +newStatus.name()
			+ "' WHERE m.id = '"+id+"' AND m.status = '"+originalStatus.name()+"'" ;
		Query query = getEntityManager().createQuery(hql);
		return query.executeUpdate();
	}

}

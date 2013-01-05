package com.google.code.lightssh.project.scheduler.dao;

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
	
	

}

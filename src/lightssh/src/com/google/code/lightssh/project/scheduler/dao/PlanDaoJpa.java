package com.google.code.lightssh.project.scheduler.dao;

import org.springframework.stereotype.Repository;

import com.google.code.lightssh.common.dao.jpa.JpaDao;
import com.google.code.lightssh.project.scheduler.entity.Plan;

/**
 * 
 * @author YangXiaojin
 *
 */
@Repository("planDao")
public class PlanDaoJpa extends JpaDao<Plan> implements PlanDao{

	private static final long serialVersionUID = -1286910597664170719L;

}

package com.google.code.lightssh.project.scheduler.dao;

import org.springframework.stereotype.Repository;

import com.google.code.lightssh.common.dao.jpa.JpaDao;
import com.google.code.lightssh.project.scheduler.entity.JobQueue;

/**
 * JobQueue JPA 实现
 * @author YangXiaojin
 */
@Repository("jobQueueDao")
public class JobQueueDaoJpa extends JpaDao<JobQueue> implements JobQueueDao{

	private static final long serialVersionUID = 1929954400169139057L;

}

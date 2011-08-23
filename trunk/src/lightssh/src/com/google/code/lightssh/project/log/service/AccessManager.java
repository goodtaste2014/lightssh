package com.google.code.lightssh.project.log.service;

import java.util.Date;

import com.google.code.lightssh.common.entity.Persistence;
import com.google.code.lightssh.common.service.BaseManager;
import com.google.code.lightssh.project.log.entity.Access;
import com.google.code.lightssh.project.log.entity.History;

/**
 * Access Manager
 * @author YangXiaojin
 *
 */
public interface AccessManager extends BaseManager<Access>{
	
	/**
	 * 登录日志
	 */
	public void logLogin( Date date,String ip,String loginName );
	
	/**
	 * 记录实体变动
	 * @param access 操作记录
	 * @param originalModel 原实体
	 * @param newModel 新实体
	 */
	public void log( Access access, Persistence<?> originalModel,Persistence<?> newModel );
	
	/**
	 * 查询历史记录
	 * @param access
	 * @return
	 */
	public History getHistory( Access access );

}

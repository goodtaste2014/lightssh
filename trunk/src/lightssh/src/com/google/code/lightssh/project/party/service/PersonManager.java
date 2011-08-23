package com.google.code.lightssh.project.party.service;

import java.io.Serializable;

import com.google.code.lightssh.common.service.BaseManager;
import com.google.code.lightssh.project.log.entity.Access;
import com.google.code.lightssh.project.party.entity.Person;

/**
 * 
 * @author YangXiaojin
 *
 */
public interface PersonManager extends BaseManager<Person>{
	
	/**
	 * 保存时记录日志
	 */
	public void save( Person person ,Access access );
	
	/**
	 * 带日志的删除
	 * @param identity
	 * @param access
	 */
	public void remove(Serializable identity, Access access);

}

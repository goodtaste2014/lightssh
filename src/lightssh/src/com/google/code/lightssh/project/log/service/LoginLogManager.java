package com.google.code.lightssh.project.log.service;

import java.util.Date;

import com.google.code.lightssh.common.service.BaseManager;
import com.google.code.lightssh.project.log.entity.LoginLog;
import com.google.code.lightssh.project.security.entity.LoginAccount;

/**
 * 登录日志业务接口
 * @author YangXiaojin
 *
 */
public interface LoginLogManager extends BaseManager<LoginLog>{
	
	/**
	 * 登录日志
	 */
	public void login(Date date,String ip, LoginAccount la );

}

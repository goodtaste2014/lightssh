package com.google.code.lightssh.project.config;

import com.google.code.lightssh.common.config.SystemConfig;

/**
 * ftportal 参数配置
 * @author YangXiaojin
 *
 */
public class ProjectConfig extends SystemConfig{
	
	/** 系统名称  */
	public static final String PROJECT_NAME = "project";
	
	/** ICP */
	public static final String CQFAE_ICP = "cqfae.icp";
	
	/** 电话 */
	public static final String CQFAE_TEL = "cqfae.tel";
	
	/** 传真 */
	public static final String CQFAE_FAX = "cafae.fax";
	
	
	public ProjectConfig( ){
		super();
	}

	@Override
	public String getProjectName() {
		return PROJECT_NAME;
	}

}

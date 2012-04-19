package com.google.code.lightssh.project.param.service;

import java.util.List;

import com.google.code.lightssh.common.model.ConnectionConfig;
import com.google.code.lightssh.common.service.BaseManager;
import com.google.code.lightssh.project.param.entity.SystemParam;

/**
 * 系统参数业务接口
 * @author YangXiaojin
 *
 */
public interface SystemParamManager extends BaseManager<SystemParam>{
	
	/**
	 * 通过名称查参数
	 */
	public SystemParam getByName( String name );
	
	/**
	 * 通过组名查参数
	 */
	public List<SystemParam> listByGroup( String group );
	
	/**
	 * 检查组名与参数名是否重复
	 */
	public boolean isUniqueGroupAndName( SystemParam param );
	
	/**
	 * 获取结算门户WebService参数
	 */
	public ConnectionConfig getPortalWebServiceParam();

}

package com.google.code.lightssh.project.remoting.hessian;

import javax.annotation.Resource;

import org.springframework.remoting.caucho.HessianProxyFactoryBean;

import com.google.code.lightssh.common.config.SystemConfig;

/**
 * 扩展HessianProxyFactoryBean
 * @author YangXiaojin
 *
 */
public class MyHessianProxyFactoryBean extends HessianProxyFactoryBean{
	
	/**
	 * 远程服务系统名称前缀
	 */
	public static final String REMOTING_SYSTEM_PREFIX = "remoting.system.";
	
	/**
	 * 子系统名称
	 */
	private String system;
	
	/** 
	 * 系统参数 
	 */
	@Resource
	private SystemConfig systemConfig;
	
	/**
	 * 系统远程服务地址前缀
	 */
	protected String getServiceUrlPrefix( ){
		if( systemConfig != null ){
			return systemConfig.getProperty(REMOTING_SYSTEM_PREFIX + system );
		}
		
		return null;
	}
	
	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public String getServiceUrl() {
		String serviceAction = super.getServiceUrl();
		if( serviceAction != null && serviceAction.startsWith("http"))
			return serviceAction;
		
		String prefix = getServiceUrlPrefix();
		if( prefix != null && serviceAction != null ){
			if( prefix.endsWith("/") && serviceAction.startsWith("/") )
				prefix = prefix.substring(0, prefix.length()-1);
			
			if( !prefix.endsWith("/") && !serviceAction.startsWith("/") )
				prefix = prefix + "/";
		}
		
		return (prefix==null?"":prefix) + serviceAction;
	}

}

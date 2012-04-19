package com.google.code.lightssh.project.security.shiro;

/**
 * CAS 常量
 * @author YangXiaojin
 *
 */
public interface ConfigConstants {
	
	/** 是否运行CAS */
	public static final String CAS_RUNNING_KEY = "cas.running";
	
	/** CAS服务URL地址Key */
	public static final String CAS_SERVER_URL_PREFIX_KEY = "cas.server.url.prefix";
	
	/** CAS服务Key */
	public static final String CAS_SERVICE_KEY = "cas.service";
	
	
	/** 是否根据用户进行跳转  */
	public static final String DOMAIN_RUNNING_KEY = "domain.running";
	
	/** 管理员跳转URL Key*/
	public static final String DOMAIN_ADMIN_URL_PREFIX_KEY = "domain.admin.url.prefix";
	
	/** 买方跳转URL Key*/
	public static final String DOMAIN_BUYER_URL_PREFIX_KEY = "domain.buyer.url.prefix";
	
	/** 卖方跳转URL Key*/
	public static final String DOMAIN_SELLER_URL_PREFIX_KEY = "domain.seller.url.prefix";
	
	/** 配送方跳转URL Key*/
	public static final String DOMAIN_LOGISTICS_URL_PREFIX_KEY = "domain.logistics.url.prefix";

}

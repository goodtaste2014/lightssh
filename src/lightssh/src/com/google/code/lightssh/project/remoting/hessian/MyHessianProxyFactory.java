package com.google.code.lightssh.project.remoting.hessian;

import com.caucho.hessian.client.HessianConnectionFactory;
import com.caucho.hessian.client.HessianProxyFactory;
import com.caucho.hessian.client.HessianURLConnectionFactory;

/**
 * 扩展HessianProxyFactory
 * @author YangXiaojin
 *
 */
public class MyHessianProxyFactory extends HessianProxyFactory{
	
	protected HessianConnectionFactory createHessianConnectionFactory(){
		HessianURLConnectionFactory hcf = (HessianURLConnectionFactory)super.createHessianConnectionFactory();
	    return hcf;
	}
	

}

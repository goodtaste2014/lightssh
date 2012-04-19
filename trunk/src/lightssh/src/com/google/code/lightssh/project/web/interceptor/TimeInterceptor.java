package com.google.code.lightssh.project.web.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.lightssh.common.util.StringUtil;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * Action 执行时间拦截器
 * @author YangXiaojin
 *
 */
public class TimeInterceptor extends AbstractInterceptor{
	
	private static final long serialVersionUID = 1L;
	
	public static final long MAX_EXE_MILLIS = 2000;
	
	private static Logger log = LoggerFactory.getLogger(TimeInterceptor.class);
	
	public String intercept(ActionInvocation invocation) throws Exception{
		long startTime = System.currentTimeMillis(); //开始执行时间
		String result = invocation.invoke();
		long executionTime = System.currentTimeMillis() - startTime; //执行时间
		
        StringBuffer message = new StringBuffer(100);
        message.append("执行的Action[");
        String namespace = invocation.getProxy().getNamespace();
        if ( StringUtil.clean(namespace) != null ) {
            message.append(namespace);
            if( !namespace.equals("/"))
            	message.append("/");
        }
        message.append(invocation.getProxy().getActionName());
        message.append("!");
        message.append(invocation.getProxy().getMethod());
        message.append("] 耗时 ").append(executionTime).append(" 毫秒.");
        
        if( executionTime > MAX_EXE_MILLIS )
        	log.info( message.toString() );
        
		return result;
	}

}

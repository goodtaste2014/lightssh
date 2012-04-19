package com.google.code.lightssh.project.web.interceptor;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.lightssh.common.config.SystemConfig;
import com.google.code.lightssh.common.util.StringUtil;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * Action 执行时间拦截器
 * @author YangXiaojin
 *
 */
public class RunningTimeInterceptor extends AbstractInterceptor{
	
	private static final long serialVersionUID = 1L;
	
	public static final long MAX_EXE_MILLIS = 2000;
	
	private long runningTime = 0;
	
	private static Logger log = LoggerFactory.getLogger(RunningTimeInterceptor.class);
	
	public static final String RUNNING_TIME_KEY = "log.running.time";
	
	@Resource( name="systemConfig" )
	private SystemConfig systemConfig;
	
    public void init() {
    	if( systemConfig != null && systemConfig.getProperty(RUNNING_TIME_KEY) != null ){
    		String conf = systemConfig.getProperty(RUNNING_TIME_KEY);
    		try{
    			runningTime = Long.valueOf( conf );
    		}catch( NumberFormatException e ){
    			log.warn("设置执行时间日志参数[{}]异常：{}",conf,e.getMessage());
    		}
    	}
    }
	
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
        
        if( executionTime > getMaxExeMillis() )
        	log.info( message.toString() );
        
		return result;
	}
	
	protected long getMaxExeMillis( ){
		return Math.max(this.runningTime,MAX_EXE_MILLIS);
	}

}

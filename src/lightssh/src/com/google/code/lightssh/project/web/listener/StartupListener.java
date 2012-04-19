package com.google.code.lightssh.project.web.listener;

import javax.servlet.ServletContextEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.google.code.lightssh.project.scheduler.service.SchedulerManager;
import com.google.code.lightssh.project.security.service.LoginAccountManager;

/**
 * container startup listener
 * @author Yangxiaojin
 */
public class StartupListener extends ContextLoaderListener{
	
	private static Logger log = LoggerFactory.getLogger(StartupListener.class);
	
	public void contextDestroyed(ServletContextEvent sce) {
		super.contextDestroyed(sce); //Spring
	}

	/**
	 * init system resource
	 */
	public void contextInitialized(ServletContextEvent sce) {
		super.contextInitialized(sce); //Spring 
		
		chooseFreemarkLoger();
		
        ApplicationContext ctx = WebApplicationContextUtils
        	.getRequiredWebApplicationContext(sce.getServletContext());
        
		log.debug( "系统初始化..." );
		initScheduler( ctx );
		initRootUser( ctx );
	}

	/**
	 * 初始系统账户
	 */
	private void initRootUser( ApplicationContext ctx ){
        try{
	        LoginAccountManager laMgr = (LoginAccountManager) ctx.getBean(
	        		"loginAccountManager");
	        laMgr.initLoginAccount();
        }catch( Exception e ){
        	log.error( e.getMessage() );
        }
	}
	
	/**
	 * 初始化定时任务
	 */
	private void initScheduler( ApplicationContext ctx ){
		try{
			SchedulerManager schedulerManager = (SchedulerManager) ctx.getBean(
	        		"schedulerManager");
			if( schedulerManager == null )
				return;
			
			schedulerManager.initCronTrigger();
        }catch( Exception e ){
        	log.error( e.getMessage() );
        }
		
	}
	
	
	/**
	 * FreeMarker调试日志
	 */
	public void chooseFreemarkLoger( ){
		try{
			//freemarker.log.Logger.selectLoggerLibrary( freemarker.log.Logger.LIBRARY_NONE );
			freemarker.log.Logger.selectLoggerLibrary( freemarker.log.Logger.LIBRARY_SLF4J );
		}catch( Exception e ){
			log.warn("选择Freemarker日志框架出错:" + e.getMessage() );
		}
	}
}

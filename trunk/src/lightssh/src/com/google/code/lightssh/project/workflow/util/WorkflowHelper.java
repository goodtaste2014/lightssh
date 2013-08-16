package com.google.code.lightssh.project.workflow.util;

import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;

import com.google.code.lightssh.project.util.SpringContextHelper;
import com.google.code.lightssh.project.workflow.service.WorkflowManager;

/**
 * 
 * @author YangXiojin
 * @date 2013-8-15
 * 
 */
public class WorkflowHelper {
	
	public static final String WORKFLOW_MANAGER_NAME = "workflowManager";

	/**
	 * 根据Key查询流程定义
	 */
	public static ProcessDefinition getProcessDefinition(String key ){
		if( StringUtils.isEmpty(key))
			return null;
		
		WorkflowManager bean = (WorkflowManager)
				SpringContextHelper.getBean(WORKFLOW_MANAGER_NAME);
		
		return bean.getProcessDefinition(key);
	}
	
	/**
	 * 根据ID查询流程实例
	 */
	public static ProcessInstance getProcessInstance(String id ){
		if( StringUtils.isEmpty(id))
			return null;
		
		WorkflowManager bean = (WorkflowManager)
				SpringContextHelper.getBean(WORKFLOW_MANAGER_NAME);
		
		return bean.getProcessInstance( id );
	}
	
	/**
	 * 根据ID查询任务
	 */
	public static Task getTask(String id ){
		if( StringUtils.isEmpty(id))
			return null;
		
		WorkflowManager bean = (WorkflowManager)
				SpringContextHelper.getBean(WORKFLOW_MANAGER_NAME);
		
		return bean.getTask( id );
	}
	
	/**
	 * 根据流程实例ID查询任务
	 */
	public static Task getTaskByProcessId(String id ){
		if( StringUtils.isEmpty(id))
			return null;
		
		WorkflowManager bean = (WorkflowManager)
				SpringContextHelper.getBean(WORKFLOW_MANAGER_NAME);
		
		return bean.getTaskByProcessId( id );
	}
	
}

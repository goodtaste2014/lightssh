package com.google.code.lightssh.project.workflow.service;

import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import com.google.code.lightssh.common.model.page.ListPage;
import com.google.code.lightssh.common.service.Manager;

/**
 * 工作流业务接口
 *
 */
public interface WorkflowManager extends Manager{
	
	/**
	 * 查询流程定义
	 */
	public ListPage<ProcessDefinition> listProcessDefinition( ListPage<ProcessDefinition> page );
	
	/**
	 * 查询流程实例
	 */
	public ListPage<ProcessInstance> listProcessInstance( ListPage<ProcessInstance> page );
	
	/**
	 * 查询任务
	 */
	public ListPage<Task> listTask( ListPage<Task> page );
	
	/**
	 * 启动流程
	 */
	public ProcessInstance start( String processKey);
	
	/**
	 * 认领任务
	 */
	public void claim( String taskId,String userId );
	
	/**
	 * 完成任务
	 */
	public void complete( String taskId );

}

package com.google.code.lightssh.project.workflow.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.activiti.engine.form.FormData;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;

import com.google.code.lightssh.common.model.page.ListPage;
import com.google.code.lightssh.common.service.Manager;
import com.google.code.lightssh.project.workflow.entity.MyProcess;

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
	 * 查询流程定义
	 * @param key 流程定义Key
	 */
	public ProcessDefinition getProcessDefinition( String key );
	
	/**
	 * 查询流程实例
	 */
	public ListPage<ProcessInstance> listProcessInstance( ListPage<ProcessInstance> page );
	
	/**
	 * 查询流程实例
	 */
	public ProcessInstance getProcessInstance( String id );
	
	/**
	 * 历史流程实例
	 */
	public HistoricProcessInstance getProcessHistory(String procId );
	
	/**
	 * 历史流程示例
	 */
	public ListPage<HistoricProcessInstance> listProcessHistory(
			MyProcess process,ListPage<HistoricProcessInstance> page );
	
	/**
	 * 查询与我相关的流程
	 */
	public ListPage<HistoricProcessInstance> listMyProcess(
			MyProcess process,ListPage<HistoricProcessInstance> page );
	
	/**
	 * 查询任务
	 */
	public Task getTask( String taskId );
	
	/**
	 * 查询流程中活动的任务
	 */
	public Task getTaskByProcessId( String processId );
	
	/**
	 * 查询任务
	 */
	public ListPage<Task> listTask(Task task, ListPage<Task> page );
	
	/**
	 * 查询流程的上一执行任务
	 * @param processInstanceId 流程实例
	 */
	public HistoricTaskInstance getLastTask(String processInstanceId);
	
	/**
	 * 查询任务之前表单数据
	 */
	public List<HistoricDetail> listHistoricDetail( String taskId );
	
	/**
	 * 查询部署信息
	 */
	public ListPage<Deployment> listDeployment( ListPage<Deployment> page );
	
	/**
	 * 部署流程
	 */
	public void deploy( String resourceName,InputStream inputStream);
	
	/**
	 * 取消部署
	 */
	public void undeploy( String deploymentId );
	
	/**
	 * 启动流程
	 */
	public ProcessInstance start( String processKey);
	
	/**
	 * 启动流程
	 * @param processKey 流程key
	 * @param bizKey 业务key
	 */
	public ProcessInstance start( String processKey,String bizKey,String userId,Map<String,Object> variables );
	
	/**
	 * 认领任务
	 */
	public void claim( String taskId,String userId );
	
	/**
	 * 变更认领人
	 */
	public void changeAssignee( String taskId,String userId );
	
	/**
	 * 完成任务
	 */
	public void complete( String taskId );
	
	/**
	 * 完成任务
	 */
	public void complete( String taskId,String processInstanceId,String user,Boolean passed,String message );
	
	/**
	 * 流程注释
	 */
	public List<Comment> getProcessInstanceComments(String processInstanceId);
	
	/**
	 * 任务注释
	 */
	public List<Comment> getTaskComments(String taskId);
	
	/**
	 * 任务表单数据
	 */
	public TaskFormData getTaskFormData(String taskId );
	
	/**
	 * 开始事件表单数据
	 */
	public StartFormData getStartFormData(String processDefinitionId );
	
	/**
	 * 表单数据
	 * @param id 流程类型ID 或  任务ID
	 * @return FormData
	 */
	public FormData getFormData(String id );
	
	/**
	 * 提交数据
	 */
	public ProcessInstance submitStartFormData(String processDefinitionId
			,String businessKey,Map<String,String> properties  );
	
	/**
	 * 提交数据
	 */
	public void submitFormData(String taskId,Map<String,String> properties );

}

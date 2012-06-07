package com.google.code.lightssh.project.workflow.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormData;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricDetailQuery;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.query.Query;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.stereotype.Component;

import com.google.code.lightssh.common.ApplicationException;
import com.google.code.lightssh.common.model.page.ListPage;
import com.google.code.lightssh.common.model.page.OrderBy;
import com.google.code.lightssh.common.util.StringUtil;

/**
 * 工作流业务实现
 *
 */
@Component("workflowManager")
public class WorkflowManagerImpl implements WorkflowManager{
	
	@Resource(name="runtimeService")
	private RuntimeService runtimeService;
	
	@Resource(name="taskService")
	private TaskService taskService;
	
	@Resource(name="repositoryService")
	private RepositoryService repositoryService;
	
	//@Resource(name="identityService")
	//private IdentityService identityService;
	
	@Resource(name="historyService")
	private HistoryService historyService;
	
	@Resource(name="formService")
	private FormService formService;
	
	/**
	 * 查询
	 */
	@SuppressWarnings("unchecked")
	protected ListPage<?> query(Query query, ListPage<?> page ){
		if( page == null )
			page = new ListPage();
		
		OrderBy orderBy = page.getOrderBy();
		if( orderBy != null ){
			if( ListPage.OrderType.DESCENDING.equals(orderBy.getType()) )
				query.desc();
			else
				query.asc();
		}
		
		int count = (int)query.count();
		page.setAllSize(count);
		int start = page.getStart()-1;
		if( start > count ){
			start = (page.getAllPage()-1)*page.getSize() 
				+ ((count>page.getSize())?((count%page.getSize())-1):0);
			page.setNumber( page.getAllPage() );
		}
		
		page.setList( query.listPage(start,page.getSize()) );
		
		return page;
	}
	
	/**
	 * 查询部署信息
	 */
	@SuppressWarnings("unchecked")
	public ListPage<Deployment> listDeployment( ListPage<Deployment> page ){
		if( page == null )
			page = new ListPage<Deployment>();
		
		DeploymentQuery query = repositoryService.createDeploymentQuery();
		
		OrderBy orderBy = page.getOrderBy();
		if( orderBy != null ){
			if( "id".equals(orderBy.getProperty()) )
				query.orderByDeploymentId();
			else if( "deploymentTime".equals(orderBy.getProperty()) )
				query.orderByDeploymenTime();
			else if( "name".equals(orderBy.getProperty()) )
				query.orderByDeploymentName();
		}
		
		return (ListPage<Deployment>)query(query,page);
	}
	
	/**
	 * 部署流程
	 */
	public void deploy( String resourceName,InputStream inputStream){
		repositoryService.createDeployment()
			.addInputStream(resourceName, inputStream)
			.enableDuplicateFiltering()
			.name( "lightssh" ).deploy();
	}
	
	/**
	 * 取消部署
	 */
	public void undeploy( String deploymentId ){
		this.repositoryService.deleteDeployment(deploymentId,true);
	}
	
	/**
	 * 查询流程定义
	 */
	@SuppressWarnings("unchecked")
	public ListPage<ProcessDefinition> listProcessDefinition( ListPage<ProcessDefinition> page ){
		if( page == null )
			page = new ListPage<ProcessDefinition>();
		
		ProcessDefinitionQuery query = repositoryService
			.createProcessDefinitionQuery().latestVersion();
		
		OrderBy orderBy = page.getOrderBy();
		if( orderBy != null ){
			if( "name".equals(orderBy.getProperty()) )
				query.orderByProcessDefinitionName();
			else if( "key".equals(orderBy.getProperty()) )
				query.orderByProcessDefinitionKey();
			else if( "id".equals(orderBy.getProperty()) )
				query.orderByProcessDefinitionId();
			else if( "category".equals(orderBy.getProperty()) )
				query.orderByProcessDefinitionCategory();
			else if( "version".equals(orderBy.getProperty()) )
				query.orderByProcessDefinitionVersion();
			else if( "deploymentId".equals(orderBy.getProperty()) )
				query.orderByDeploymentId();
		}
		
		return (ListPage<ProcessDefinition>)query(query,page);
	}
	
	/**
	 * 查询流程实例
	 */
	@SuppressWarnings("unchecked")
	public ListPage<ProcessInstance> listProcessInstance( ListPage<ProcessInstance> page ){
		if( page == null )
			page = new ListPage<ProcessInstance>();
		
		ProcessInstanceQuery query = runtimeService
			.createProcessInstanceQuery();
		
		OrderBy orderBy = page.getOrderBy();
		if( orderBy != null ){
			if( "businessKey".equals(orderBy.getProperty()) )
				query.orderByProcessDefinitionKey();
			else if( "processDefinitionId".equals(orderBy.getProperty()) )
				query.orderByProcessDefinitionId();
			else if( "processInstanceId".equals(orderBy.getProperty()) )
				query.orderByProcessInstanceId();
		}
		
		return (ListPage<ProcessInstance>)query(query,page);
	}
	
	/**
	 * 历史流程示例
	 */
	@SuppressWarnings("unchecked")
	public ListPage<HistoricProcessInstance> listProcessHistory( ListPage<HistoricProcessInstance> page ){
		if( page == null )
			page = new ListPage<HistoricProcessInstance>();
		
		HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery();
		
		query.finished();
		//query.startedBy(userId);
		
		OrderBy orderBy = page.getOrderBy();
		if( orderBy != null ){
			if( "processDefinitionId".equals(orderBy.getProperty()) )
				query.orderByProcessDefinitionId();
			else if( "processInstanceId".equals(orderBy.getProperty()) )
				query.orderByProcessInstanceId();
			else if( "businessKey".equals(orderBy.getProperty()) )
				query.orderByProcessInstanceBusinessKey();
			else if( "duration".equals(orderBy.getProperty()) )
				query.orderByProcessInstanceDuration();
			else if( "startTime".equals(orderBy.getProperty()) )
				query.orderByProcessInstanceStartTime();
			else if( "endTime".equals(orderBy.getProperty()) )
				query.orderByProcessInstanceEndTime();
			
		}
		
		return (ListPage<HistoricProcessInstance>)query(query,page);
	}
	
	/**
	 * 启动流程
	 * @param processKey 流程key
	 */
	public ProcessInstance start( String processKey){
		return runtimeService.startProcessInstanceByKey( processKey );
	}
	
	/**
	 * 查询任务
	 */
	public Task getTask( String taskId ){
		return taskService.createTaskQuery().taskId(taskId).singleResult();
	}
	
	/**
	 * 查询任务
	 */
	@SuppressWarnings("unchecked")
	public ListPage<Task> listTask( ListPage<Task> page ){
		if( page == null )
			page = new ListPage<Task>();
		
		TaskQuery query = taskService.createTaskQuery();
		OrderBy orderBy = page.getOrderBy();
		if( orderBy != null ){
			if( "name".equals(orderBy.getProperty()) )
				query.orderByTaskName();
			else if( "id".equals(orderBy.getProperty()) )
				query.orderByTaskId();
			else if( "createTime".equals(orderBy.getProperty()) )
				query.orderByTaskCreateTime();
			else if( "priority".equals(orderBy.getProperty()) )
				query.orderByTaskPriority();
			
			/*
			query.orderByDueDate();
			query.orderByExecutionId();
			query.orderByProcessInstanceId();
			query.orderByTaskAssignee();
			query.orderByTaskDescription();
			*/
		}
		
		return (ListPage<Task>)query(query,page);
	}
	
	/**
	 * 查询历史任务
	 */
	@SuppressWarnings("unchecked")
	public ListPage<HistoricTaskInstance> listHistoryTask( ListPage<HistoricTaskInstance> page ){
		if( page == null )
			page = new ListPage<HistoricTaskInstance>();
		
		HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery();
		
		query.finished(); //已完成任务
		
		OrderBy orderBy = page.getOrderBy();
		if( orderBy != null ){
			if( "endTime".equals(orderBy.getProperty()) )
				query.orderByHistoricTaskInstanceEndTime();
			else if( "startTime".equals(orderBy.getProperty()) )
				query.orderByHistoricActivityInstanceStartTime();
		}
		
		return (ListPage<HistoricTaskInstance>)query(query,page);
	}
	
	/**
	 * 查询任务之前表单数据
	 */
	public List<HistoricDetail> listHistoricDetail( String taskId ){
		Task task = this.getTask(taskId);
		if( task == null )
			return null;
		
		List<HistoricTaskInstance> historyTaskList = historyService.createHistoricTaskInstanceQuery()
			.processInstanceId(task.getProcessInstanceId())
			.finished()
			.orderByTaskId().desc()
			.listPage(0,1);
		
		if( historyTaskList == null || historyTaskList.isEmpty() )
			return null;
		HistoricTaskInstance historyTask = historyTaskList.get(0);
		
		HistoricDetailQuery query = historyService.createHistoricDetailQuery().formProperties();
		query.taskId( historyTask.getId() );
		
		return query.list();
	}
	
	/**
	 * 认领流程
	 */
	public void claim( String taskId,String userId ){
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		if( task == null )
			throw new ApplicationException("任务["+taskId+"]不存在！");
		
		if( StringUtil.hasText( task.getAssignee() ) )
			throw new ApplicationException("任务["+taskId+"]已被用户["+task.getAssignee()+"]认领！");
		
		taskService.claim(taskId, userId);
	}
	
	/**
	 * 完成任务
	 */
	public void complete( String taskId ){
		taskService.complete(taskId);
	}
	
	/**
	 * 提交数据
	 */
	public ProcessInstance submitStartFormData(String processDefinitionId,String businessKey,Map<String,String> properties  ){
		return formService.submitStartFormData(processDefinitionId,businessKey,properties);
	}
	
	/**
	 * 提交数据
	 */
	public void submitFormData(String taskId,Map<String,String> properties ){
		this.formService.submitTaskFormData(taskId, properties);
	}
	
	/**
	 * 任务表单数据
	 */
	public TaskFormData getTaskFormData(String taskId ){
		return formService.getTaskFormData(taskId);
	}
	
	/**
	 * 开始事件表单数据
	 */
	public StartFormData getStartFormData(String processDefinitionId ){
		return formService.getStartFormData(processDefinitionId);
	}
	
	/**
	 * 表单数据
	 * @param id 流程类型ID 或  任务ID
	 * @return FormData
	 */
	public FormData getFormData(String id ){
		HistoricTaskInstance task = historyService
			.createHistoricTaskInstanceQuery().taskId(id).singleResult();
		if( task != null )
			return getTaskFormData(id);
		return getStartFormData(id);
	}

}

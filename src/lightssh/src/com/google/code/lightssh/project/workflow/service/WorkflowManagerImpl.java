package com.google.code.lightssh.project.workflow.service;

import javax.annotation.Resource;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.query.Query;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.stereotype.Component;

import com.google.code.lightssh.common.model.page.ListPage;
import com.google.code.lightssh.common.model.page.OrderBy;

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
	
	//@Resource(name="historyService")
	//private HistoryService historyService;
	
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
		
		page.setAllSize( (int)query.count() );
		page.setList( query.listPage(page.getStart()-1,page.getEnd()) );
		
		return page;
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
	 * 启动流程
	 * @param processKey 流程key
	 */
	public ProcessInstance start( String processKey){
		return runtimeService.startProcessInstanceByKey( processKey );
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
			
			query.orderByDueDate();
			query.orderByExecutionId();
			query.orderByProcessInstanceId();
			query.orderByTaskAssignee();
			query.orderByTaskCreateTime();
			query.orderByTaskDescription();
			query.orderByTaskPriority();
		}
		
		return (ListPage<Task>)query(query,page);
	}

}

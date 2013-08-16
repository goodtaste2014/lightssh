package com.google.code.lightssh.project.workflow.service;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
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
import org.activiti.engine.history.NativeHistoricProcessInstanceQuery;
import org.activiti.engine.query.Query;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.google.code.lightssh.common.ApplicationException;
import com.google.code.lightssh.common.model.page.ListPage;
import com.google.code.lightssh.common.model.page.OrderBy;
import com.google.code.lightssh.common.util.StringUtil;
import com.google.code.lightssh.project.workflow.entity.MyProcess;

/**
 * 工作流业务实现
 *
 */
@Component("workflowManager")
public class WorkflowManagerImpl implements WorkflowManager{

	private static final long serialVersionUID = -263409561222061490L;
	
	private static final String SQL_SELECT_COUNT = "SELECT COUNT(1) ";
	
	private static final String SQL_SELECT = "SELECT * ";

	@Resource(name="runtimeService")
	private RuntimeService runtimeService;
	
	@Resource(name="taskService")
	private TaskService taskService;
	
	@Resource(name="repositoryService")
	private RepositoryService repositoryService;
	
	@Resource(name="identityService")
	private IdentityService identityService;
	
	@Resource(name="historyService")
	private HistoryService historyService;
	
	@Resource(name="formService")
	private FormService formService;
	
	@Resource(name="managementService")
	private ManagementService managementService;
	
	/**
	 * 查询
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
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
	 * 查询流程定义
	 * @param key 流程定义Key
	 */
	public ProcessDefinition getProcessDefinition( String key ){
		ProcessDefinitionQuery query = repositoryService
				.createProcessDefinitionQuery();
		
		query.processDefinitionId(key);
		
		return query.singleResult();
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
	 * 查询流程实例
	 */
	public ProcessInstance getProcessInstance( String id ){
		ProcessInstanceQuery query = runtimeService
			.createProcessInstanceQuery();
		
		query.processInstanceId(id);
		
		return query.singleResult();
	}
	
	/**
	 * 历史流程实例
	 */
	@SuppressWarnings("unchecked")
	public ListPage<HistoricProcessInstance> listProcessHistory(MyProcess process,ListPage<HistoricProcessInstance> page ){
		if( page == null )
			page = new ListPage<HistoricProcessInstance>();
		
		HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery();
		
		if(process != null ){
			//流程创建者
			if( StringUtils.isNotEmpty(process.getOwner()) ){
				query.startedBy(process.getOwner().trim());
			}
			
			//是否完成
			if( process.getFinish() != null ){
				if( process.getFinish() )
					query.finished();
				else
					query.unfinished();
			}
			
			//流程定义ID
			if( StringUtils.isNotEmpty(process.getProcessDefinitionId()) ){
				query.processDefinitionId(process.getProcessDefinitionId().trim());
			}
			
			//流程实例ID
			if( StringUtils.isNotEmpty(process.getProcessInstanceId()) ){
				query.processInstanceId(process.getProcessInstanceId().trim());
			}
			
			//流程开始时间
			if( process.getStartPeriod() != null ){
				Date start = process.getStartPeriod().getStart();
				Date end = process.getStartPeriod().getEnd();
				
				if( start != null ){
					query.startedAfter( start );
				}
				
				if( end != null ){
					Calendar cal_end = Calendar.getInstance();
					cal_end.setTime(end);
					cal_end.add(Calendar.DAY_OF_MONTH, 1);
					cal_end.add(Calendar.SECOND, -1);
					
					query.startedBefore( cal_end.getTime() );
				}
			}
			
			//流程结束时间
			if( process.getEndPeriod() != null ){
				Date start = process.getEndPeriod().getStart();
				Date end = process.getEndPeriod().getEnd();
				
				if( start != null ){
					query.finishedAfter( start );
				}
				
				if( end != null ){
					Calendar cal_end = Calendar.getInstance();
					cal_end.setTime(end);
					cal_end.add(Calendar.DAY_OF_MONTH, 1);
					cal_end.add(Calendar.SECOND, -1);
					
					query.finishedBefore( cal_end.getTime() );
				}
			}
		}
		
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
	 * 查询与我相关的流程
	 */
	public ListPage<HistoricProcessInstance> listMyProcess(MyProcess process,ListPage<HistoricProcessInstance> page ){
		if( page == null )
			page = new ListPage<HistoricProcessInstance>();
		
		NativeHistoricProcessInstanceQuery query = historyService.createNativeHistoricProcessInstanceQuery();
		StringBuffer sql = new StringBuffer(" FROM " + managementService.getTableName(HistoricProcessInstance.class) + " t where 1=1 ");
		if( process != null ){
			boolean includeOwner = StringUtils.isNotEmpty(process.getOwner());
			
			//与“操作人”相关的流水
			if( StringUtils.isNotEmpty(process.getAssignee()) ){ //优化，与owner相同时，不执行子查询
				sql.append( " AND ( t.PROC_INST_ID_ IN( SELECT DISTINCT PROC_INST_ID_ FROM ");
				sql.append( managementService.getTableName(HistoricTaskInstance.class) );
				sql.append(" WHERE ASSIGNEE_ = #{assignee}");
				sql.append( " ) OR "+(includeOwner?" 1=1 ":" t.START_USER_ID_ = #{owner} " )+")" );
				
				if(!includeOwner)
					query.parameter("owner", process.getAssignee().trim());
				
				query.parameter("assignee", process.getAssignee().trim());
			}
			
			//流程创建者
			if( includeOwner ){
				sql.append(" AND t.START_USER_ID_ = #{owner}");
				query.parameter("owner", process.getOwner().trim());
			}
		}
		
		page.setAllSize( (int)query.sql( SQL_SELECT_COUNT + sql.toString() ).count() );
		page.setList( query.sql( SQL_SELECT + sql.toString() ).list() );
		
		return page;
	}
	
	/**
	 * 启动流程
	 * @param processKey 流程key
	 */
	public ProcessInstance start( String processKey){
		return runtimeService.startProcessInstanceByKey( processKey );
	}
	
	/**
	 * 启动流程
	 * @param processKey 流程key
	 * @param bizKey 业务key
	 */
	public ProcessInstance start( String processKey,String bizKey,String userId,Map<String,Object> variables){
		//用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
		if( !StringUtils.isEmpty(userId) )
			identityService.setAuthenticatedUserId(userId);
	    
		return runtimeService.startProcessInstanceByKey( processKey,bizKey,variables );
	}
	
	/**
	 * 查询任务
	 */
	public Task getTask( String taskId ){
		return taskService.createTaskQuery().taskId(taskId).singleResult();
	}
	
	/**
	 * 查询流程中活动的任务
	 */
	public Task getTaskByProcessId( String processId ){
		return taskService.createTaskQuery().processInstanceId(processId).singleResult();
	}
	
	/**
	 * 查询任务
	 */
	@SuppressWarnings("unchecked")
	public ListPage<Task> listTask(Task task, ListPage<Task> page ){
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
		
		if( task != null ){
			if( !StringUtils.isEmpty(task.getAssignee()) )
				query.taskAssignee(task.getAssignee());
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
				query.orderByHistoricTaskInstanceStartTime();
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
		
		if( StringUtil.hasText( task.getAssignee() ) ){
			//throw new ApplicationException("任务["+taskId+"]已被用户["+task.getAssignee()+"]认领！");
		
			taskService.setAssignee(taskId, userId);
		}
		//taskService.claim(taskId, userId);
	}
	
	/**
	 * 完成任务
	 */
	public void complete( String taskId ){
		taskService.complete(taskId);
	}
	
	/**
	 * 完成任务
	 */
	public void complete( String taskId,String processInstanceId,String message ){
		taskService.addComment(taskId, processInstanceId, message);
		
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

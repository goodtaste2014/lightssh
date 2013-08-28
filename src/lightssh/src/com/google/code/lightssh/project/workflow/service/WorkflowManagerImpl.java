package com.google.code.lightssh.project.workflow.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.bpmn.model.BpmnModel;
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
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricDetailQuery;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.history.NativeHistoricProcessInstanceQuery;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.query.NativeQuery;
import org.activiti.engine.query.Query;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.google.code.lightssh.common.ApplicationException;
import com.google.code.lightssh.common.model.page.ListPage;
import com.google.code.lightssh.common.model.page.ListPage.OrderType;
import com.google.code.lightssh.common.model.page.OrderBy;
import com.google.code.lightssh.common.util.StringUtil;
import com.google.code.lightssh.project.workflow.entity.TaskLog;
import com.google.code.lightssh.project.workflow.model.MyProcess;
import com.google.code.lightssh.project.workflow.model.MyTask;

/**
 * 工作流业务实现
 *
 */
@Component("workflowManager")
public class WorkflowManagerImpl implements WorkflowManager{

	private static final long serialVersionUID = -263409561222061490L;
	
	private static final String SQL_SELECT_COUNT = "SELECT COUNT(1) ";
	
	private static final String SQL_SELECT_DATA = "SELECT * ";
	
	private static String ORACLE_PAGINATION_SQL = " SELECT * FROM ( SELECT A.*, ROWNUM RN FROM ( #{SQL}) A WHERE ROWNUM <= #{row_end} ) WHERE RN >= #{row_start}";
	
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
	
	@Resource(name="taskLogManager")
	private TaskLogManager taskLogManager;
	
	/**
	 * 本地查询
	 * TODO 考虑其它数据库
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected ListPage<?> query(String from_sql,NativeQuery query, ListPage<?> page ){
		if( page == null )
			page = new ListPage();
		
		//TODO 只实现oracle，待完善
		String pagenation_sql = ORACLE_PAGINATION_SQL.replace("#{SQL}"
				,SQL_SELECT_DATA + from_sql + addOrderBy(page));
		
		int count = (int)query.sql( SQL_SELECT_COUNT + from_sql.toString() ).count();
		int start = page.getStart();
		if( page.getStart() > count ){
			start = (page.getAllPage()-1)*page.getSize() 
				+ ((count>page.getSize())?((count%page.getSize())-1):0);
			page.setNumber( page.getAllPage() );
		}
		
		query.parameter("row_start", start );
		query.parameter("row_end", page.getEnd() - 1 );
		
		page.setAllSize( count  );
		page.setList( query.sql( pagenation_sql ).list() );
		
		return page;
	}
	
	protected String addOrderBy( ListPage<?> page ){
		//add order
		StringBuffer orderby = new StringBuffer("");
		List<OrderBy> orderByList = page.listAllOrderBy();
		if( orderByList != null && !orderByList.isEmpty() ){
			for(OrderBy each:orderByList ){
				if( each == null )
					continue;
				orderby.append("".equals(orderby.toString())?" ORDER BY ":" ,");
				orderby.append( " " + each.getProperty() 
					+ (OrderType.ASCENDING.equals( each.getType() )?" ASC ":" DESC ") );
			}
		}
		
		return orderby.toString();
	}
	
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
	 * @param ID 流程定义Id
	 */
	public ProcessDefinition getProcessDefinition( String id ){
		ProcessDefinitionQuery query = repositoryService
				.createProcessDefinitionQuery();
		
		query.processDefinitionId(id);
		
		return query.singleResult();
	}

	/**
	 * 获取BpmnModel 通过流程定义ID
	 */
	public BpmnModel getBpmnModel(String procDefId ){
		return repositoryService.getBpmnModel(procDefId);
	}
	
	/**
	 * 根据流程实例ID查询活动的Activity ID
	 */
	public List<String> getActiveActivityIds(String procInstId ){
		return this.runtimeService.getActiveActivityIds( procInstId );
	}
	
	/**
	 * 根据流程实例ID查询结束的Activity ID
	 */
	public List<String> getHighLightedFlows(String procInstId ){
		//流程实例
		HistoricProcessInstance procInst = this.getProcessHistory(procInstId);
		if( procInst == null )
			return null;
		
		//流程定义
		ProcessDefinitionEntity procDef = (ProcessDefinitionEntity)
				((RepositoryServiceImpl) repositoryService)
                .getDeployedProcessDefinition( procInst.getProcessDefinitionId() );
		if( procDef == null )
			return null;
		
		List<HistoricActivityInstance> haiList = historyService.createHistoricActivityInstanceQuery()
				.processInstanceId(procInstId)
				.orderByHistoricActivityInstanceStartTime().asc()
				.list();
		
		Map<String,HistoricActivityInstance> haiMap = new HashMap<String,HistoricActivityInstance>();
		for (HistoricActivityInstance hai : haiList) {
			haiMap.put(hai.getActivityId(),hai);
		}
		
		return getHighLightedFlows(haiMap,procDef.getActivities() );
	}
	
	/**
	 * 获取高亮流程节点
	 */
	protected List<String> getHighLightedFlows(Map<String,HistoricActivityInstance> haiMap,List<ActivityImpl> activities ){
		if( activities == null || activities.isEmpty() 
				|| haiMap == null || haiMap.isEmpty() )
			return null;
		
		List<String> results = new ArrayList<String>();
		
		for(ActivityImpl item:activities){
			Object type = item.getProperty("type");
			if( type.equals("subProcess") ){
				List<String> subs = getHighLightedFlows(haiMap,item.getActivities() );
				if( subs != null && !subs.isEmpty() )
					results.addAll(subs);
			}
			
			String srcId = item.getId();
			if( haiMap.containsKey( srcId ) ){
				List<PvmTransition> pvmTrans = item.getOutgoingTransitions(); //某个节点出来的所有线路
				if( pvmTrans == null || pvmTrans.isEmpty() )
					continue;
				
				for( PvmTransition pvm:pvmTrans){
					String destFlowId = pvm.getDestination().getId();
					if( haiMap.containsKey( destFlowId ) ){
						//判断进入了哪个节点
	                    if ("exclusiveGateway".equals( type )){
	                    	//TODO 待实现
	                    }else{
	                    	results.add( pvm.getId() );
	                    }
					}
				}
			}//end if 
		}
		
		return results;
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
	public HistoricProcessInstance getProcessHistory(String procId ){
		if( StringUtils.isEmpty(procId) )
			return null;
		
		return historyService.createHistoricProcessInstanceQuery()
				.processInstanceId(procId).singleResult();
	}
	
	/**
	 * 历史流程实例
	 * TODO 重构，使用本地查询
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
	@SuppressWarnings("unchecked")
	public ListPage<HistoricProcessInstance> listMyProcess(MyProcess process,ListPage<HistoricProcessInstance> page ){
		if( page == null )
			page = new ListPage<HistoricProcessInstance>();
		
		NativeHistoricProcessInstanceQuery query = historyService.createNativeHistoricProcessInstanceQuery();
		StringBuffer sql = new StringBuffer(" FROM " + managementService.getTableName(HistoricProcessInstance.class) + " t where 1=1 ");
		if( process != null ){
			
			//流程实例ID
			if( StringUtils.isNotEmpty(process.getProcessInstanceId()) ){
				sql.append(" AND t.PROC_INST_ID_ = #{proc_inst_id}");
				query.parameter("proc_inst_id", process.getProcessInstanceId().trim());
			}
			
			//流程类型ID
			if( StringUtils.isNotEmpty(process.getProcessDefinitionId()) ){
				sql.append(" AND t.PROC_DEF_ID_ = #{proc_def_id}");
				query.parameter("proc_def_id", process.getProcessDefinitionId().trim());
			}
			
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
			
			//是否完成
			if( process.getFinish() != null ){
				if( process.getFinish() )
					sql.append(" AND t.END_TIME_ IS NOT NULL ");
				else
					sql.append(" AND t.END_TIME_ IS NULL ");
			}
			
			//流程开始时间
			if( process.getStartPeriod() != null ){
				Date start = process.getStartPeriod().getStart();
				Date end = process.getStartPeriod().getEnd();
				
				if( start != null ){
					sql.append(" AND t.START_TIME_ >= #{st_start} ");
					query.parameter("st_start", start);
				}
				
				if( end != null ){
					Calendar cal_end = Calendar.getInstance();
					cal_end.setTime(end);
					cal_end.add(Calendar.DAY_OF_MONTH, 1);
					cal_end.add(Calendar.SECOND, -1);
					
					sql.append(" AND t.START_TIME_ <= #{st_end} ");
					query.parameter("st_end", cal_end.getTime());
				}
			}
		}
		
		if( page.getOrderByList() != null && !page.getOrderByList().isEmpty() ){
			
		}
		
		//page.setAllSize( (int)query.sql( SQL_SELECT_COUNT + sql.toString() ).count() );
		//page.setList( query.sql( SQL_SELECT + sql.toString() ).list() );
		
		return (ListPage<HistoricProcessInstance>) query(sql.toString(),query,page);
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
	@SuppressWarnings("unchecked")
	public ListPage<HistoricTaskInstance> listHistoricTask(
			MyTask task,ListPage<HistoricTaskInstance> page){
		if( page == null )
			page = new ListPage<HistoricTaskInstance>();
		
		HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery();
		if( task != null ){
			//流程实例ID
			if( StringUtils.isNotEmpty(task.getProcessInstanceId()) ){
				query.processInstanceId(task.getProcessInstanceId().trim());
			}
			
			//流程定义ID
			if( StringUtils.isNotEmpty(task.getProcessDefinitionId()) ){
				query.processDefinitionId(task.getProcessDefinitionId().trim());
			}
			
			//签收人
			if( !StringUtils.isEmpty(task.getAssignee()) )
				query.taskAssignee(task.getAssignee());
			
			//任务是否完成
			if( task.getFinish() != null ){
				if( task.getFinish() )
					query.finished();
				else
					query.unfinished();
			}
		} 
		
		return (ListPage<HistoricTaskInstance>) query(query,page);
	}
	
	/**
	 * 查询流程中活动的任务
	 */
	public List<Task> listTaskByProcessId( String processId ){
		TaskQuery query = taskService.createTaskQuery();
		query.active();
		query.processInstanceId(processId);
		
		return query.list();
	}
	
	/**
	 * 查询任务
	 */
	@SuppressWarnings("unchecked")
	public ListPage<Task> listTask(MyTask task, ListPage<Task> page ){
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
			if( StringUtils.isNotEmpty(task.getCandidateUser()) )
				query.taskCandidateUser( task.getCandidateUser().trim() );
			
			if( StringUtils.isNotEmpty(task.getAssignee()) )
				query.taskAssignee(task.getAssignee().trim());
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
	 * 查询流程的上一执行任务
	 * @param processInstanceId 流程实例
	 */
	@SuppressWarnings("unchecked")
	public HistoricTaskInstance getLastTask(String processInstanceId){
		ListPage<HistoricTaskInstance> page = new ListPage<HistoricTaskInstance>(1);
		HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery();
		query.finished(); //已完成任务
		query.processInstanceId(processInstanceId);
		query.orderByHistoricTaskInstanceEndTime().desc();
		
		return ((ListPage<HistoricTaskInstance>)query(query,page)).getFirst();
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
	 * 变更认领人
	 */
	public void changeAssignee( String taskId,String userId ){
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		if( task == null )
			throw new ApplicationException("任务["+taskId+"]不存在！");
		
		taskService.setAssignee(taskId, userId);
	}
	
	/**
	 * 添加会签人
	 */
	public void addAssignee( String taskId,List<String> userIds ){
		if( userIds == null || userIds.isEmpty() )
			throw new ApplicationException("添加的会签人不能为空！");
		
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		if( task == null )
			throw new ApplicationException("任务["+taskId+"]不存在！");
		
		if( StringUtils.isNotEmpty(task.getParentTaskId()) )
			throw new ApplicationException("任务["+task.getName()+"]已是会签任务！");
		
		//taskService.
		
		for( String user:userIds ){
			TaskEntity newTask = (TaskEntity) taskService.newTask(); //TODO 更改ID规则
			
			newTask.setAssignee(user);  
			newTask.setName( task.getName() + "-[会签]");  
			newTask.setProcessDefinitionId(task.getProcessDefinitionId());  
			newTask.setProcessInstanceId(task.getProcessInstanceId() );  
			newTask.setParentTaskId(taskId);  
			newTask.setDescription( task.getName() + "-添加会签人["+user+"]");  
            taskService.saveTask(newTask); 
		}
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
	public void complete( String taskId,String user,Boolean passed,String message ){
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		if( task == null )
			throw new ApplicationException("任务["+taskId+"]不存在！");
		
		//task.getParentTaskId();
		//identityService.setAuthenticatedUserId( user ); 
		
		TaskLog.Type type = Boolean.TRUE.equals(passed)?TaskLog.Type.SUBMIT:TaskLog.Type.REVOKE;
		taskLogManager.save(task.getProcessInstanceId(),task.getId(),type,user, message);
		
		Map<String,Object> variables = new HashMap<String,Object>();
		variables.put("passed", passed);
		
		taskService.complete(taskId,variables); //提交
	}
	
	/**
	 * 流程注释
	 */
	public List<Comment> getProcessInstanceComments(String processInstanceId){
		return taskService.getProcessInstanceComments(processInstanceId);
	}
	
	/**
	 * 任务注释
	 */
	public List<Comment> getTaskComments(String taskId){
		return taskService.getTaskComments(taskId);
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

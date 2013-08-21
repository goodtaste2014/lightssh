package com.google.code.lightssh.project.workflow.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.task.Task;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.code.lightssh.common.model.page.ListPage;
import com.google.code.lightssh.common.util.StringUtil;
import com.google.code.lightssh.project.web.action.GenericAction;
import com.google.code.lightssh.project.workflow.service.WorkflowManager;

/**
 * 
 * @author YangXiojin
 * @date 2013-8-15
 * 
 */
@SuppressWarnings("rawtypes")
@Component( "workflowTaskAction" )
@Scope("prototype")
public class WorkflowTaskAction extends GenericAction{

	private static final long serialVersionUID = 394568619904313580L;
	
	private TaskEntity task;
	
	private ListPage<Task> taskPage;
	
	private String taskId;

	@Resource(name="workflowManager")
	private WorkflowManager workflowManager;

	public ListPage<Task> getTaskPage() {
		return taskPage;
	}

	public void setTaskPage(ListPage<Task> taskPage) {
		this.taskPage = taskPage;
	}
	
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public TaskEntity getTask() {
		return task;
	}

	public void setTask(TaskEntity task) {
		this.task = task;
	}

	/**
	 * 所有待办任务
	 */
	public String list(){
		taskPage = workflowManager.listTask(task,taskPage);
		return SUCCESS;
	}
	
	/**
	 * 我的待办任务
	 */
	public String myTodoList(){
		task = new TaskEntity();
		task.setAssignee( getLoginUser() ); //登录用户
		
		taskPage = workflowManager.listTask(task,taskPage);
		
		return SUCCESS;
	}
	
	/**
	 * 认领流程
	 */
	public String claim( ){
		try{
			String userId = request.getParameter("userId");
			workflowManager.claim( taskId ,StringUtil.hasText(userId)?userId:getLoginUser());
		}catch( Exception e ){
			this.saveErrorMessage( e.getMessage() );
		}
		return SUCCESS;
	}
	
	/**
	 * 代办认领人
	 */
	public String proxyClaim( ){
		try{
			String userId = request.getParameter("userId");
			workflowManager.changeAssignee(taskId ,userId);
		}catch( Exception e ){
			this.saveErrorMessage( e.getMessage() );
		}
		return SUCCESS;
	}
	
	/**
	 * 预做任务
	 */
	public String prepare( ){
		TaskFormData data = workflowManager.getTaskFormData(taskId);
		if( data != null && data.getFormProperties() != null 
				&& !data.getFormProperties().isEmpty() ){
			request.setAttribute("task_form_data", data);
			return INPUT;
		}
		
		return SUCCESS;
	}
	
	/**
	 * 渲染表单
	 */
	public String render( ){
		TaskFormData data = (TaskFormData)request.getAttribute("task_form_data");
		if( data == null )
			return ERROR;
		
		List<HistoricDetail> historicTasks  = workflowManager.listHistoricDetail(taskId);
		if( historicTasks != null ){
			request.setAttribute("history_task_form_data",historicTasks);
		}
		
		return SUCCESS;
	}
	
	/**
	 * 提交表单数据
	 */
	public String submit( ){
		TaskFormData data = workflowManager.getTaskFormData(taskId);
		if( data != null && data.getFormProperties() != null ){
			Map<String,String> properties = new HashMap<String,String>();
			for(FormProperty item: data.getFormProperties() )
				properties.put(item.getId(),request.getParameter(item.getId()));
			
			//workflowManager.submitFormData(taskId,properties);
		}
		
		return complete();
	}
	
	/**
	 * 查看任务
	 * TODO
	 */
	public String view( ){
		return SUCCESS;
	}
	
	/**
	 * 完成流程
	 */
	public String complete( ){
		if( this.getLoginAccount() == null )
			return LOGIN;
		
		Task task = workflowManager.getTask(taskId);
		if( task == null )
			this.saveErrorMessage("任务["+taskId+"]不存在！");
		
		Boolean passed =null;
		if(request.getParameter("passed") !=null)
			passed = "true".equalsIgnoreCase(request.getParameter("passed"));
		
		String message = request.getParameter("messsage");
		
		//TODO 检查提交流程是否与assignee相同
		try{
			workflowManager.complete( taskId ,task.getProcessInstanceId()
					,getLoginUser(),passed,message);
		}catch( Exception e ){
			this.saveErrorMessage( e.getMessage() );
		}
		
		return SUCCESS;
	}
}

package com.google.code.lightssh.project.workflow.web;

import javax.annotation.Resource;

import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.code.lightssh.common.model.page.ListPage;
import com.google.code.lightssh.common.web.action.BaseAction;
import com.google.code.lightssh.project.workflow.service.WorkflowManager;

/**
 * workflow action
 * @author YangXiaojin
 *
 */
@Component( "workflowAction" )
@Scope("prototype")
public class WorkflowAction extends BaseAction{

	private static final long serialVersionUID = 1793759911301918645L;
	
	@Resource(name="workflowManager")
	private WorkflowManager workflowManager;
	
	private ListPage<ProcessDefinition> pd_page;
	
	private ListPage<ProcessInstance> pi_page;
	
	private ListPage<Task> task_page;
	
	public ListPage<ProcessDefinition> getPd_page() {
		return pd_page;
	}

	public void setPd_page(ListPage<ProcessDefinition> pdPage) {
		pd_page = pdPage;
	}

	public ListPage<Task> getTask_page() {
		return task_page;
	}

	public void setTask_page(ListPage<Task> taskPage) {
		task_page = taskPage;
	}

	public ListPage<ProcessInstance> getPi_page() {
		return pi_page;
	}

	public void setPi_page(ListPage<ProcessInstance> piPage) {
		pi_page = piPage;
	}

	/**
	 * 开始工作流
	 */
	public String start( ){
		String key = request.getParameter("processDefinitionKey");
		ProcessInstance process = workflowManager.start( key );
		return SUCCESS;
	}
	
	/**
	 * 流程定义查询
	 */
	public String processDefinitionList( ){
		pd_page = workflowManager.listProcessDefinition(pd_page);
		return SUCCESS;
	}
	
	/**
	 * 流程定义查询
	 */
	public String processInstanceList( ){
		pi_page = workflowManager.listProcessInstance(pi_page);
		return SUCCESS;
	}
	
	/**
	 * 流程查询
	 */
	public String tasklist( ){
		task_page = workflowManager.listTask(task_page);
		return SUCCESS;
	}

}

package com.google.code.lightssh.project.workflow.web;

import javax.annotation.Resource;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.code.lightssh.common.model.page.ListPage;
import com.google.code.lightssh.project.web.action.GenericAction;
import com.google.code.lightssh.project.workflow.entity.MyProcess;
import com.google.code.lightssh.project.workflow.service.WorkflowManager;

/**
 * 
 * @author YangXiojin
 * @date 2013-8-15
 * 
 */
@SuppressWarnings("rawtypes")
@Component( "workflowProcessAction" )
@Scope("prototype")
public class WorkflowProcessAction extends GenericAction{

	private static final long serialVersionUID = 7371824285577363653L;
	
	private MyProcess process;
	
	private ListPage<ProcessDefinition> pd_page;
	
	private ListPage<ProcessInstance> pi_page;
	
	private ListPage<HistoricProcessInstance> hp_page;
	
	@Resource(name="workflowManager")
	private WorkflowManager workflowManager;
	
	public ListPage<ProcessDefinition> getPd_page() {
		return pd_page;
	}

	public void setPd_page(ListPage<ProcessDefinition> pd_page) {
		this.pd_page = pd_page;
	}

	public ListPage<ProcessInstance> getPi_page() {
		return pi_page;
	}

	public void setPi_page(ListPage<ProcessInstance> pi_page) {
		this.pi_page = pi_page;
	}

	public ListPage<HistoricProcessInstance> getHp_page() {
		return hp_page;
	}

	public void setHp_page(ListPage<HistoricProcessInstance> hp_page) {
		this.hp_page = hp_page;
	}

	public MyProcess getProcess() {
		return process;
	}

	public void setProcess(MyProcess process) {
		this.process = process;
	}

	/**
	 * 流程定义查询
	 */
	public String processDefinitionList( ){
		pd_page = workflowManager.listProcessDefinition(pd_page);
		return SUCCESS;
	}

	/**
	 * 流程实例查询
	 */
	public String processInstanceList( ){
		pi_page = workflowManager.listProcessInstance(pi_page);
		return SUCCESS;
	}
	
	/**
	 * 流程查询
	 */
	public String list( ){
		if( process == null ){
			process = new MyProcess();
			process.setFinish( false );
		}
		hp_page = workflowManager.listProcessHistory(process,hp_page);
		return SUCCESS;
	}
	
	/**
	 * 我的流程
	 */
	public String myProcess(){
		if( this.getLoginAccount() == null )
			return LOGIN;
		
		if( process == null ){
			process = new MyProcess();
			process.setFinish( false );
		}
		
		process.setAssignee(this.getLoginUser());
		hp_page = workflowManager.listMyProcess(process,hp_page);
		return SUCCESS; 
	}
}

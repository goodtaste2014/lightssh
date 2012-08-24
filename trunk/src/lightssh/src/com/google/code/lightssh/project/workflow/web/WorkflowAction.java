package com.google.code.lightssh.project.workflow.web;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.code.lightssh.common.model.page.ListPage;
import com.google.code.lightssh.common.util.StringUtil;
import com.google.code.lightssh.project.web.action.GenericAction;
import com.google.code.lightssh.project.workflow.service.WorkflowManager;

/**
 * workflow action
 * @author YangXiaojin
 *
 */
@SuppressWarnings("unchecked")
@Component( "workflowAction" )
@Scope("prototype")
public class WorkflowAction extends GenericAction{

	private static final long serialVersionUID = 1793759911301918645L;
	
	@Resource(name="workflowManager")
	private WorkflowManager workflowManager;
	
	private ListPage<ProcessDefinition> pd_page;
	
	private ListPage<ProcessInstance> pi_page;
	
	private ListPage<HistoricProcessInstance> hp_page;
	
	private ListPage<Task> task_page;
	
	private ListPage<Deployment> deployment_page;
	
	private String taskId;
	
	private File upload;
	
	private String uploadContentType;
	
	private String uploadFileName;
	
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

	public ListPage<HistoricProcessInstance> getHp_page() {
		return hp_page;
	}

	public void setHp_page(ListPage<HistoricProcessInstance> hpPage) {
		hp_page = hpPage;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
	public File getUpload() {
		return upload;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

	public String getUploadContentType() {
		return uploadContentType;
	}

	public void setUploadContentType(String uploadContentType) {
		this.uploadContentType = uploadContentType;
	}

	public String getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}
	
	public ListPage<Deployment> getDeployment_page() {
		return deployment_page;
	}

	public void setDeployment_page(ListPage<Deployment> deploymentPage) {
		deployment_page = deploymentPage;
	}

	/**
	 * 工作流部署
	 */
	public String deploymentList( ){
		deployment_page = workflowManager.listDeployment(deployment_page);
		
		return SUCCESS;
	}

	/**
	 * 部署
	 */
	public String deploy( ){
		if( this.isGet() )
			return INPUT;
		
		if( uploadFileName == null || upload == null ){
			this.addActionMessage("上传文件为空！");
			return INPUT;
		}
		
		String fileName = request.getParameter("deployment_file_name");
		if( !StringUtil.hasText(fileName) )
			fileName = uploadFileName;
			
		if( !fileName.endsWith(".bpmn20.xml") )
			fileName = fileName + ".bpmn20.xml";
		
		try{
			this.workflowManager.deploy(fileName,new FileInputStream(upload) );
		}catch(Exception e ){
			this.saveErrorMessage( e.getMessage() );
		}
		
		return SUCCESS;
	}
	
	/**
	 * 删除部署
	 */
	public String undeploy( ){
		try{
			this.workflowManager.undeploy(request.getParameter("deploymentId"));
		}catch(Exception e ){
			this.saveErrorMessage( e.getMessage() );
		}
		
		return SUCCESS;
	}

	/**
	 * 开始工作流
	 */
	public String start( ){
		String key = request.getParameter("processDefinitionKey");
		//ProcessInstance process = 
		workflowManager.start( key );
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
	 * 流程实例查询
	 */
	public String processInstanceList( ){
		pi_page = workflowManager.listProcessInstance(pi_page);
		return SUCCESS;
	}
	
	/**
	 * 历史流程实例查询
	 */
	public String historyInstanceList( ){
		hp_page = workflowManager.listProcessHistory(hp_page);
		return SUCCESS;
	}
	
	/**
	 * 流程查询
	 */
	public String tasklist( ){
		task_page = workflowManager.listTask(task_page);
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
	 * 预做任务
	 */
	public String prepare( ){
		TaskFormData data = workflowManager.getTaskFormData(taskId);
		if( data != null ){
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
			
			workflowManager.submitFormData(taskId,properties);
		}
		
		return SUCCESS;
	}
	
	/**
	 * 查看任务
	 * @return
	 */
	public String view( ){
		return SUCCESS;
	}
	
	/**
	 * 完成流程
	 */
	public String complete( ){
		try{
			workflowManager.complete( taskId );
		}catch( Exception e ){
			this.saveErrorMessage( e.getMessage() );
		}
		
		return SUCCESS;
	}
	
	

}

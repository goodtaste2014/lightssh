package com.google.code.lightssh.project.workflow.web;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.bpmn.diagram.ProcessDiagramGenerator;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.code.lightssh.common.model.page.ListPage;
import com.google.code.lightssh.common.web.action.ImageAction;
import com.google.code.lightssh.project.web.action.GenericAction;
import com.google.code.lightssh.project.workflow.model.MyProcess;
import com.google.code.lightssh.project.workflow.model.MyTask;
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
public class WorkflowProcessAction extends GenericAction implements ImageAction{

	private static final long serialVersionUID = 7371824285577363653L;
	
	private byte[] imageInBytes;
	
	private String imageContentType;
	
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

	public byte[] getImageInBytes() {
		return imageInBytes;
	}

	public void setImageInBytes(byte[] imageInBytes) {
		this.imageInBytes = imageInBytes;
	}

	public String getImageContentType() {
		return imageContentType;
	}

	public void setImageContentType(String imageContentType) {
		this.imageContentType = imageContentType;
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
			//process.setFinish( false );
		}
		
		if( hp_page == null )
			hp_page = new ListPage<HistoricProcessInstance>();
		
		if( hp_page.getOrderByList() == null )
			hp_page.addDescending("START_TIME_");
		
		hp_page = workflowManager.listProcessHistory(process,hp_page);
		return SUCCESS;
	}
	
	/**
	 * 流程查询
	 */
	public String view( ){
		if( process == null || process.getProcessInstanceId() == null )
			return INPUT;
		
		HistoricProcessInstance proc = workflowManager.getProcessHistory(process.getProcessInstanceId());
		if( proc == null ){
			return INPUT;
		}
		
		request.setAttribute("process", proc );
		
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
		
		if( hp_page == null )
			hp_page = new ListPage<HistoricProcessInstance>();
		
		if( hp_page.getOrderByList() == null )
			hp_page.addDescending("START_TIME_");
		
		process.setAssignee(this.getLoginUser());
		hp_page = workflowManager.listMyProcess(process,hp_page);
		return SUCCESS; 
	}
	
	/**
	 * 流程图
	 */
	public String procDefImage(){
		if( process == null || process.getProcessDefinitionId() ==null ){
			this.saveErrorMessage("参数错误！");
			return INPUT;
		}
		
		BpmnModel bmpnModel = workflowManager.getBpmnModel( process.getProcessDefinitionId() );
		if( bmpnModel == null ){
			this.saveErrorMessage("流程定义ID["+ process.getProcessDefinitionId()+"]对应BpmnModel数据不存在！");
			return INPUT;
		}
		
		try {
			this.imageInBytes = IOUtils.toByteArray( ProcessDiagramGenerator.generatePngDiagram(bmpnModel) );
			this.imageContentType = "image/png"; //PNG
		} catch (IOException e) {
			this.saveErrorMessage("数据转换异常："+e.getMessage() );
			return INPUT;
		}
		
		return SUCCESS;
	}
	
	/**
	 * 流程注释
	 */
	@Deprecated
	public String comment(){
		if( this.process != null && process.getProcessInstanceId() != null ){
			List<Comment> comments = workflowManager.getProcessInstanceComments( 
					process.getProcessInstanceId());
			request.setAttribute("proc_comments",comments);
		}
		
		return SUCCESS;
	}
	
	/**
	 * 流程实例的所有任务
	 */
	public String tasksOfProc(){
		if( process == null || process.getProcessInstanceId() == null )
			return INPUT;
		
		MyTask task = new MyTask();
		task.setProcessInstanceId( process.getProcessInstanceId() );
		ListPage<HistoricTaskInstance> page = new ListPage<HistoricTaskInstance>(50);
		
		page = workflowManager.listHistoricTask(task, page);
		request.setAttribute("tasksOfProc",page.getList() );
		
		return SUCCESS;
	}
}

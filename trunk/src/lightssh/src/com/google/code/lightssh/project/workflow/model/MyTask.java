package com.google.code.lightssh.project.workflow.model;

/**
 * 
 * @author YangXiojin
 * @date 2013-8-22
 * 
 */
public class MyTask {
	
	/**
	 * Task id
	 */
	private String id;
	
	/**
	 * 签收用户
	 */
	private String assignee;
	
	/**
	 * 待签用户
	 */
	private String candidateUser;
	
	/**
	 * 是否完成
	 */
	private Boolean finish;
	
	/**
	 * 流程定义ID
	 */
	private String processDefinitionId;
	
	/**
	 * 流程ID
	 */
	private String processInstanceId;
	
	/**
	 * 消息
	 */
	private String message;
	
	/**
	 * 类型
	 */
	private ExecutionType type;

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getCandidateUser() {
		return candidateUser;
	}

	public void setCandidateUser(String candidateUser) {
		this.candidateUser = candidateUser;
	}

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public Boolean getFinish() {
		return finish;
	}

	public void setFinish(Boolean finish) {
		this.finish = finish;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ExecutionType getType() {
		return type;
	}

	public void setType(ExecutionType type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}

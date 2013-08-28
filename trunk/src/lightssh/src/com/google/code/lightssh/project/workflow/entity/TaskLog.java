package com.google.code.lightssh.project.workflow.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.google.code.lightssh.common.entity.base.UUIDModel;

/**
 * 任务日志
 * @author YangXiojin
 * @date 2013-8-27
 * 
 */
@Entity
@Table( name="T_FLOW_TASK_LOG" )
public class TaskLog extends UUIDModel{

	private static final long serialVersionUID = -2347566895886457457L;
	
	/**
	 * 类型
	 */
	public enum Type{
		EDIT("编辑")	
		,REMOVE("删除")	
		,SUBMIT("提交")	
		,REVOKE("退回")	
		,NOTICE("转发")	
		,CONFIRM("确认")	
		,SIGN("会签");	
		
		private String value;
		
		Type( String value ){
			this.value = value;
		}

		public String getValue() {
			return value;
		}
		
		public String toString(){
			return this.value;
		}
	}
	
	/**
	 * 流程实例ID
	 */
	@Column(name="ACT_PROC_INST_ID",length=64)
	private String actProcInstId;
	
	/**
	 * 任务实例ID
	 */
	@Column(name="ACT_TASK_INST_ID",length=64)
	private String actTaskInstId;
	
	/**
	 * 操作人
	 */
	@Column(name="OPERATOR",length=50)
	private String operator;
	
	/**
	 * 类型
	 */
	@Column( name="TYPE",length=20 )
	@Enumerated(value=EnumType.STRING)
	private Type type;
	
	/**
	 * 描述
	 */
	@Column(name="DESCRIPTION",length=200)
	private String description;

	public String getActProcInstId() {
		return actProcInstId;
	}

	public void setActProcInstId(String actProcInstId) {
		this.actProcInstId = actProcInstId;
	}

	public String getActTaskInstId() {
		return actTaskInstId;
	}

	public void setActTaskInstId(String actTaskInstId) {
		this.actTaskInstId = actTaskInstId;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

}

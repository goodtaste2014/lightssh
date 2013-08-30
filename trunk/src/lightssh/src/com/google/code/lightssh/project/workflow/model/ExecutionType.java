package com.google.code.lightssh.project.workflow.model;

/**
 * 执行类型
 * @author YangXiojin
 * @date 2013-8-28
 * 
 */
public enum ExecutionType {
	EDIT("编辑")	
	,CLAIM("认领")	
	,REMOVE("删除")	
	,SUBMIT("提交")	
	,REVOKE("退回")	
	,NOTICE("转发")	
	,CONFIRM("确认")	
	,SIGN("会签");	
	
	private String value;
	
	ExecutionType( String value ){
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
	public String toString(){
		return this.value;
	}
	
}

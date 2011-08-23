package com.google.code.lightssh.project.security.service;

public enum SecuirtyFramework{
	SPRING_SECURITY("spring")
	,SHIRO("shiro");
	
	private String value;
	
	SecuirtyFramework( String value ){
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	 
	public String toString(){
		return this.value;
	}
}

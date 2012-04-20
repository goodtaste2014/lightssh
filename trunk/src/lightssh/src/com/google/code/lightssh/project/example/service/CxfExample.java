package com.google.code.lightssh.project.example.service;

import javax.jws.WebService;

/**
 * CXF示例
 * @author YangXiaojin
 *
 */
@WebService
public interface CxfExample {

	public String sayHello( String username );
	
}

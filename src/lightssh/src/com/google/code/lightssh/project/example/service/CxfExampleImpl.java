package com.google.code.lightssh.project.example.service;

import javax.jws.WebService;

/**
 * CXF示例
 * @author YangXiaojin
 *
 */
@WebService(endpointInterface = "com.google.code.lightssh.project.example.service.CxfExample" )
public class CxfExampleImpl implements CxfExample{
	
	public String sayHello( String username ){
		String reply =" Hello, " + username  + "!";
		System.out.print(reply) ;		
		return reply;
	}

}

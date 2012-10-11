package com.google.code.lightssh.project.example.service;

import org.springframework.stereotype.Component;

/**
 * 远程调用接口示例
 * @author YangXiaojin
 *
 */
@Component("remotingManager")
public class RemotingManagerImpl implements RemotingManager {
	
	/**
	 * for test
	 */
	public String sayHello( String name ){
		return "hello," + name + "!";
	}

}

package com.google.code.lightssh.project.message.dao;

import org.springframework.stereotype.Repository;

import com.google.code.lightssh.common.dao.jpa.JpaDao;
import com.google.code.lightssh.project.message.entity.Message;

/**
 * 
 * @author YangXiojin
 * @date 2013-9-4
 * 
 */
@Repository("messageDao")
public class MessageDaoJpa extends JpaDao<Message> implements MessageDao{

	private static final long serialVersionUID = 8245137954310955563L;

}

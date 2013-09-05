package com.google.code.lightssh.project.message.dao;

import java.util.Calendar;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.google.code.lightssh.common.dao.jpa.JpaDao;
import com.google.code.lightssh.project.message.entity.Publish;
import com.google.code.lightssh.project.security.entity.LoginAccount;

/**
 * 
 * @author Aspen
 * @date 2013-9-5
 * 
 */
@Repository("publishDao")
public class PublishDaoJpa extends JpaDao<Publish> implements PublishDao{

	private static final long serialVersionUID = -2660249120883257504L;
	
	/**
	 * 标记为已读
	 */
	public int markToRead( String id ,LoginAccount user){
		if( StringUtils.isEmpty(id) || user == null || user.getId() == null )
			return 0;
		
		String hql = " UPDATE " + this.entityClass.getName() 
				+ " SET readTime = ? WHERE id = ? AND user.id = ? ";
		
		return addQueryParams( getEntityManager().createQuery(hql)
				,new Object[]{Calendar.getInstance(),id,user.getId()}).executeUpdate();
	}

}

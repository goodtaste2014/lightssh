package com.google.code.lightssh.project.message.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.google.code.lightssh.common.dao.jpa.JpaDao;
import com.google.code.lightssh.common.model.page.ListPage;
import com.google.code.lightssh.project.message.entity.Message;
import com.google.code.lightssh.project.message.entity.Publish;

/**
 * 
 * @author Aspen
 * 
 */
@Repository("messageDao")
public class MessageDaoJpa extends JpaDao<Message> implements MessageDao{

	private static final long serialVersionUID = 8245137954310955563L;
	
	public ListPage<Message> list( ListPage<Message> page, Message t ){
		StringBuffer sql = new StringBuffer(" FROM "+this.entityClass.getName() + " AS m WHERE 1=1 ");
		List<Object> params = new ArrayList<Object>();
		
		if( t != null ){
			if( StringUtils.isNotEmpty(t.getTitle())){
				sql.append(" AND m.title like ? ");
				params.add("%"+t.getTitle().trim()+"%");
			}
			
			if( StringUtils.isNotEmpty(t.getContent())){
				sql.append(" AND m.content like ? ");
				params.add("%"+t.getContent().trim()+"%");
			}
			
			if( StringUtils.isNotEmpty(t.getCreator())){
				sql.append(" AND m.creator = ? ");
				params.add( t.getCreator().trim() );
			}
			
			if( t.getReader() != null && t.getReader().getId() != null ){
				sql.append(" AND m.id IN ( ");
				sql.append(" SELECT message.id FROM " + Publish.class.getName() );
				sql.append(" WHERE user.id = ? ) " );
				
				params.add( t.reader.getId() );
			}
			
			if( t.getCatalog() != null ){
				if( StringUtils.isNotEmpty(t.getCatalog().getId()) ){
					sql.append(" AND m.catalog.id = ? ");
					params.add( t.getCatalog().getId().trim() );
				}
			}
		}
		
		return super.query(page,sql.toString(), params.toArray() );
	}
}

package com.google.code.lightssh.project.message.web;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.json.annotations.JSON;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.code.lightssh.common.model.Result;
import com.google.code.lightssh.common.model.page.ListPage;
import com.google.code.lightssh.project.message.entity.Publish;
import com.google.code.lightssh.project.message.service.PublishManager;
import com.google.code.lightssh.project.web.action.GenericAction;

/**
 * 
 * @author Aspen
 * @date 2013-9-5
 * 
 */
@Component( "publishAction" )
@Scope("prototype")
public class PublishAction extends GenericAction<Publish>{

	private static final long serialVersionUID = -3161126116768024164L;

	private Publish publish;
	
	private Result result;
	
	@Resource(name="publishManager")
	public void setManager(PublishManager manager){
		this.manager = manager;
	}
	
	public PublishManager getManager( ){
		return (PublishManager)this.manager;
	}

	public Publish getPublish() {
		this.model = publish;
		return publish;
	}

	public void setPublish(Publish publish) {
		this.publish = publish;
		this.model = this.publish;
	}
	
	@JSON(name="result")
	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}
	
	/**
	 * 读消息
	 */
	public String read( ){
		result = new Result();
		if( publish == null ){
			result.setStatus(false);
			result.setMessage("参数错误！");
			
			return SUCCESS;
		}
		
		try{
			boolean read = getManager().markToRead(publish.getId(),getLoginAccount());
			result.setStatus( read );
		}catch( Exception e ){
			result.setMessage("阅读消息异常："+e.getMessage());
		}
		
		return SUCCESS;
	}
	
	/**
	 * 删除消息
	 */
	public String remove( ){
		result = new Result();
		if( publish == null || StringUtils.isEmpty(publish.getId()) 
				|| publish.getMessage() == null ){
			result.setStatus(false);
			result.setMessage("参数错误！");
			
			return SUCCESS;
		}
		
		try{
			boolean flag = getManager().delete(publish.getId()
					,publish.getMessage().getId(),getLoginAccount());
			result.setStatus( flag );
		}catch( Exception e ){
			result.setMessage("删除消息异常："+e.getMessage());
		}
		
		return SUCCESS;
	}

	/**
	 * 我的消息
	 */
	public String myList( ){
		if(publish == null )
			publish = new Publish();
		
		publish.setUser( this.getLoginAccount() );
		
		if( this.page == null )
			page = new ListPage<Publish>( );
		page.addDescending("readTime");
		page.addDescending("createdTime");
		//page.addAscending("message.createdTime");
		
		try{
			getManager().list(page, publish );
		}catch( Exception e ){
			this.addActionError("查询未读消息异常:"+e.getMessage());
		}
		
		return SUCCESS;
	}
}

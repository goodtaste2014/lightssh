package com.google.code.lightssh.project.param.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.code.lightssh.common.dao.Dao;
import com.google.code.lightssh.common.dao.SearchCondition;
import com.google.code.lightssh.common.model.ConnectionConfig;
import com.google.code.lightssh.common.model.page.ListPage;
import com.google.code.lightssh.common.service.BaseManagerImpl;
import com.google.code.lightssh.common.util.StringUtil;
import com.google.code.lightssh.project.param.entity.SystemParam;

/**
 * 系统参数业务实现
 * @author YangXiaojin
 *
 */
@Component( "systemParamManager" )
public class SystemParamManagerImpl extends BaseManagerImpl<SystemParam> implements SystemParamManager{
	
	private static Logger log = LoggerFactory.getLogger(SystemParamManagerImpl.class);
	
	@Resource( name="systemParamDao" )
	public void setSystemParamDao( Dao<SystemParam> dao ){
		super.dao = dao;
	}
	
	public void save( SystemParam param ){
		if( param == null )
			return;
		
		if( param.isInsert() && StringUtil.clean( param.getGroup()) == null )
			param.setGroup( SystemParam.DEFAULT_GROUP_NAME );
		
		param.setLastUpdateTime( new Date() );
		
		log.info( "修改系统参数>" + param.toString() );
		
		super.save(param);
	}

	@Override
	public SystemParam getByName(String name) {
		ListPage<SystemParam> page = new ListPage<SystemParam>(1);
		SearchCondition sc = new SearchCondition();
		sc.equal("name", name );
		page = dao.list(page, sc);
		
		return (page.getList()==null||page.getList().isEmpty())
			?null:page.getList().get(0);
	}

	@Override
	public List<SystemParam> listByGroup(String group) {
		ListPage<SystemParam> page = new ListPage<SystemParam>( );
		SearchCondition sc = new SearchCondition();
		sc.equal("group", group );
		page = dao.list(page, sc);
		
		return page.getList();
	}

	@Override
	public boolean isUniqueGroupAndName(SystemParam param) {
		if( param == null || param.getName() == null )
			return false;
		
		String group = StringUtil.clean(param.getGroup())==null
			?SystemParam.DEFAULT_GROUP_NAME:param.getGroup();
		
		ListPage<SystemParam> page = new ListPage<SystemParam>(1);
		SearchCondition sc = new SearchCondition();
		sc.equal("group", group ).equal("name",param.getName() );
		page = dao.list(page, sc);
		
		SystemParam exists = (page.getList()==null||page.getList().isEmpty())
			?null:page.getList().get(0);
	
		return exists == null || exists.getIdentity().equals(param.getIdentity() );
	}
	
	public ListPage<SystemParam> list(ListPage<SystemParam> page,SystemParam t ) {
		SearchCondition sc = new SearchCondition();
		if( t != null ){
			if( t.getName() != null )
				sc.like("name",t.getName() );
			if( t.getGroup() != null )
				sc.like("group",t.getGroup() );
		}
		return dao.list(page,sc);
	}
	
	@Override
	public ConnectionConfig getPortalWebServiceParam() {
		List<SystemParam> list = this.listByGroup( SystemParam.WS_FSPS_PORTAL_GROUP_NAME );
		if( list == null || list.isEmpty() ){
			String msg = "未设置结算门户WebService调用相关参数值";
			log.error(msg +"{}",SystemParam.WS_FSPS_PORTAL_GROUP_NAME);
			//throw new ApplicationException( msg );
			return null;
		}
		
		ConnectionConfig param = new ConnectionConfig();
		for( SystemParam item:list ){
			if( SystemParam.ADDRESS.equals(item.getName()) )
				param.setHost( item.getValue() );
			else if( SystemParam.USERNAME.equals(item.getName()) )
				param.setUsername( item.getValue() );
			else if( SystemParam.PASSWORD.equals(item.getName()) )
				param.setPassword( item.getValue() );
		}
		
		if( param.getHost() == null || param.getUsername()==null
				|| param.getPassword() == null ){
			String msg = "结算门户WebService调用参数值未正确设置";
			log.error(msg +"{}",SystemParam.WS_FSPS_PORTAL_GROUP_NAME);
			//throw new ApplicationException( msg );
			return null;
		}
		
		return param;
	} 

}

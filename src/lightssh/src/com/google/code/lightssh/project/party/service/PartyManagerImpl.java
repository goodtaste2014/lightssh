package com.google.code.lightssh.project.party.service;

import java.util.ArrayList;
import java.util.List;

import com.google.code.lightssh.common.ApplicationException;
import com.google.code.lightssh.common.model.page.ListPage;
import com.google.code.lightssh.common.service.BaseManagerImpl;
import com.google.code.lightssh.common.util.ReflectionUtil;
import com.google.code.lightssh.common.util.StringUtil;
import com.google.code.lightssh.project.log.entity.Access;
import com.google.code.lightssh.project.log.service.AccessManager;
import com.google.code.lightssh.project.party.dao.PartyDao;
import com.google.code.lightssh.project.party.entity.Organization;
import com.google.code.lightssh.project.party.entity.Party;
import com.google.code.lightssh.project.party.entity.Person;
import com.google.code.lightssh.project.sequence.service.SequenceManager;

/**
 * 
 * @author YangXiaojin
 *
 */
public class PartyManagerImpl extends BaseManagerImpl<Party> implements PartyManager{
	
	private SequenceManager sequenceManager;
	
	private AccessManager accessManager;
	
	public void setDao( PartyDao dao ){
		super.dao = dao;
	}
	
	public PartyDao getDao(){
		return (PartyDao)super.dao;
	}
	
	public void setSequenceManager(SequenceManager sequenceManager) {
		this.sequenceManager = sequenceManager;
	}
	
	public void setAccessManager(AccessManager accessManager) {
		this.accessManager = accessManager;
	}

	public void remove(Party t, Access access) {
		Class<?> clazz = Person.class;
		if( t instanceof Organization )
			clazz = Organization.class;
		
		Party party = getDao().read(clazz,t);
		if( party != null ){
			dao.delete(party);
			
			if( access != null && isDoLog(party))
				accessManager.log(access, party, null);
		}
		
	}

	@Override
	public void save(Party party, Access access) {
		if( party == null )
			throw new ApplicationException("数据为空，不能进行保存！");
		
		Party original = null;
		boolean inserted = StringUtil.clean( party.getIdentity() ) == null;
		
		if( party instanceof Organization && !isUniqneName( party ) )
			throw new ApplicationException("名称["+party.getName()+"]已经存在！");
		
		if( inserted ){
			party.setId( sequenceManager.nextSequenceNumber( party ) );
			dao.create( party );
		}else{
			Party old = dao.read( party );
			if( old != null ){
				original = old.clone();
				ReflectionUtil.assign(party, old);
				dao.update( old );
			}
		}
		
		if( access != null && isDoLog(party)){
			accessManager.log(access, original, party);
		}
	}

	/**
	 * 是否做日志记录
	 */
	protected boolean isDoLog( Party party ){
		//TODO 参数设置是否进行日志记录
		return true;
	}

	@Override
	public ListPage<Party> listOrganization(ListPage<Party> page,Party party) {
		return getDao().list( Organization.class, page, party);
	}

	@Override
	public ListPage<Party> listPerson(ListPage<Party> page, Party party) {
		return getDao().list( Person.class, page, party);
	}
	
	@Override
	public boolean isUniqneName(Party party) {
		if( party == null )
			return true;
		
		ListPage<Party> page = new ListPage<Party>(1);
		List<String> properties = new ArrayList<String>(1);
		properties.add("name");
		page = getDao().list(page,party,properties);
		
		Party exists = (page.getList()==null||page.getList().isEmpty())
			?null:page.getList().get(0);
		
		return exists == null || exists.getIdentity().equals( party.getIdentity() );
	}
	
}

package com.google.code.lightssh.project.party.dao;

import java.util.Collection;

import com.google.code.lightssh.common.dao.Dao;
import com.google.code.lightssh.project.contact.entity.ContactMechanism;
import com.google.code.lightssh.project.contact.entity.ContactMechanism.ContactMechanismType;
import com.google.code.lightssh.project.party.entity.Party;
import com.google.code.lightssh.project.party.entity.PartyContact;

/**
 * 
 * @author YangXiaojin
 *
 */
public interface PartyContactDao extends Dao<PartyContact>{
	
	/**
	 * 通过ContactMechanism删除PartyContact
	 * @param contact ContactMechanism
	 */
	public void remove( ContactMechanism contact );
	
	/**
	 * 根据Party和联系方式类型查询联系方式
	 * @param party Party
	 * @param type 联系方式类型
	 */
	public Collection<ContactMechanism> list( Party party,ContactMechanismType...type );

}

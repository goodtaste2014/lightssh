package com.google.code.lightssh.project.message.util;

import org.apache.commons.lang3.StringUtils;

import com.google.code.lightssh.project.message.entity.ReceiveType;
import com.google.code.lightssh.project.party.entity.Party;
import com.google.code.lightssh.project.party.service.PartyManager;
import com.google.code.lightssh.project.security.entity.LoginAccount;
import com.google.code.lightssh.project.security.entity.Role;
import com.google.code.lightssh.project.security.service.LoginAccountManager;
import com.google.code.lightssh.project.security.service.RoleManager;
import com.google.code.lightssh.project.util.SpringContextHelper;

/**
 * 
 * @author Aspen
 * @date 2013-9-11
 * 
 */
public class ReceiveTypeHelper {
	
	public static String getReceiveTitle( ReceiveType type,String val ){
		if( type == null || StringUtils.isEmpty(val) )
			return null;
		
		if( ReceiveType.ALL.equals(type) ){
			return "所有人";
		}else if( ReceiveType.DEPARTMENT.equals(type) 
				|| ReceiveType.PERSON.equals(type)  ){
			PartyManager partyMgr = (PartyManager)SpringContextHelper.getBean( "partyManager" );
			Party party = partyMgr.get( val );
			
			return party==null?"":party.getName();
		}else if( ReceiveType.ROLE.equals(type) ){
			RoleManager roleMgr = (RoleManager)SpringContextHelper.getBean( "roleManager" );
			Role role = roleMgr.get( val );
			
			return role==null?"":role.getName();
		}else if( ReceiveType.USER.equals(type) ){
			LoginAccountManager mgr = (LoginAccountManager)SpringContextHelper.getBean( "loginAccountManager" );
			LoginAccount acc = mgr.get( Long.parseLong(val) );
			
			return acc==null?"":acc.getLoginName();
		}
		
		return null;
	}

}

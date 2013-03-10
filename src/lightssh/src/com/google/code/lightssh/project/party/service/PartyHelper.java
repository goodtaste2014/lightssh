
package com.google.code.lightssh.project.party.service;

import com.google.code.lightssh.project.party.entity.Party;
import com.google.code.lightssh.project.util.SpringContextHelper;

/**
 * 用于页面显示
 */
public class PartyHelper {
	
	/**
	 * 查询Party
	 */
	public static Party getParty(String code ){
		PartyManager partyManager = (PartyManager)SpringContextHelper.getBean("partyManager");
		if( partyManager != null )
			return partyManager.get(code);
		
		return null;
	}

}

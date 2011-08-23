package com.google.code.lightssh.project.security.service;

import java.util.Collection;
import java.util.List;

import com.google.code.lightssh.common.service.BaseManagerImpl;
import com.google.code.lightssh.project.security.dao.NavigationDao;
import com.google.code.lightssh.project.security.entity.Navigation;
import com.google.code.lightssh.project.security.entity.Permission;

/**
 * 
 * @author YangXiaojin
 *
 */
public class NavigationManagerImpl extends BaseManagerImpl<Navigation> implements NavigationManager{

	private NavigationDao navigationDao;
	
	@Override
	public List<Navigation> listTop() {
		return navigationDao.listTop();
	}
	
	public void setNavigationDao(NavigationDao navigationDao) {
		this.navigationDao = navigationDao;
	}

	@Override
	public Collection<Permission> listPermission(Collection<Navigation> colls) {
		if( colls == null || colls.isEmpty() )
			return null;
		
		return this.navigationDao.listPermission(colls);
	}

}

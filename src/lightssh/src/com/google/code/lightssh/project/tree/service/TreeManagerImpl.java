package com.google.code.lightssh.project.tree.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.google.code.lightssh.common.dao.Dao;
import com.google.code.lightssh.common.service.BaseManagerImpl;
import com.google.code.lightssh.project.tree.entity.Tree;

/**
 * tree manager 
 * @author YangXiaojin
 *
 */
@Component("treeManager")
public class TreeManagerImpl extends BaseManagerImpl<Tree> implements TreeManager{
	
	@Resource(name="treeDao")
	public void setDao(Dao<Tree> dao) {
		this.dao = dao;
	}
	
	public Dao<Tree> getDao( ){
		return super.dao;
	}

}

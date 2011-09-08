package com.google.code.lightssh.project.tree.action;

import javax.annotation.Resource;

import org.apache.struts2.json.annotations.JSON;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.code.lightssh.common.web.action.CrudAction;
import com.google.code.lightssh.project.tree.entity.Tree;
import com.google.code.lightssh.project.tree.service.TreeManager;

/**
 * tree action
 * @author YangXiaojin
 *
 */
@Component("treeAction")
@Scope("prototype")
public class TreeAction extends CrudAction<Tree>{

	private static final long serialVersionUID = 1L;
	
	private Tree tree;
	
	@JSON(name="unique")
	public boolean isUnique() {
		return unique;
	}
	
	@Resource(name="treeManager")
	public void setTreeManager( TreeManager treeManager ){
		super.manager = treeManager;
	}

	public Tree getTree() {
		this.tree = super.model;
		return tree;
	}

	public void setTree(Tree tree) {
		this.tree = tree;
		super.model = tree;
	}

}

package com.google.code.lightssh.project.party.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * 组织
 * @author Aspen
 *
 */
@Entity
@Table( name="T_PARTY_ORGANIZATION" )
@PrimaryKeyJoinColumn(name="id") 
public class Organization extends Party {

	private static final long serialVersionUID = 1L;
		
	public Organization() {
		super();
	}
	
	/**
	 * 序号
	 */
	@Column( name="SEQUENCE")
	private Integer sequence;
	
	/**
	 * 上级
	 */
	@ManyToOne
	@JoinColumn( name="PARENT_ID")
	private Organization parent;
	
	/**
	 * 下级
	 */
	@OneToMany( mappedBy="parent" )
	private Set<Organization> children;
	
	@Override
	public String getSequenceKey() {
		return "ORG";
	}
	
	/**
	 * 全名
	 */
	public String getFullName( ){
		return this.getName();
	}
	
	/**
	 * 排序
	 */
	public static List<Organization> orderBySequence(List<Organization> list) {
		Collections.sort(list, new Comparator<Organization>() {
			public int compare(Organization o1, Organization o2) {
				int s1 = (o1==null||o1.getSequence()==null?0:o1.getSequence());
				int s2 = (o2==null||o2.getSequence()==null?0:o2.getSequence());
				return s1==s2?0:(s1>s2?1:-1);
			}
		});

		return list;
	}
	
	public List<Organization> getSortedChildren( ){
		if( this.children == null || this.children.isEmpty() )
			return null;
		
		List<Organization> list = new ArrayList<Organization>( this.children );
		orderBySequence( list );
		
		return list;
	}

	public Organization getParent() {
		return parent;
	}

	public void setParent(Organization parent) {
		this.parent = parent;
	}

	public Set<Organization> getChildren() {
		return children;
	}

	public void setChildren(Set<Organization> children) {
		this.children = children;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

}

package com.google.code.lightssh.project.party.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@AttributeOverrides(@AttributeOverride(name="name",column=@Column(unique=true)))
/**
 * hibernate 实现@AttributeOverrides存在问题
 * https://hibernate.onjira.com/browse/HHH-2619
 */
public class Organization extends Party {

	private static final long serialVersionUID = 1L;
		
	public Organization() {
		super();
	}
	
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
	
	public Organization clone(){
		 return (Organization)super.clone();
	}

}

package com.google.code.lightssh.project.party.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import com.google.code.lightssh.common.entity.Persistence;
import com.google.code.lightssh.common.model.Sequenceable;

/**
 * 社体
 * @author Aspen
 *
 */
//@MappedSuperclass
@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class Party implements Persistence<String>,Sequenceable,Cloneable{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * CODE
	 */
	@Id
	@Column( name="ID" )
	private String id;
	
	/**
	 * 名称
	 */
	@Column( name="NAME",unique=false,length=100 )
	private String name;
	
	/**
	 * enabled
	 */
	@Column( name="ENABLED" )
	protected Boolean enabled;
	
	/**
	 * 描述
	 */
	@Column( name="DESCRIPTION",length=200 )
	protected String description;
	
	@Override
	public String getIdentity() {
		return this.id;
	}

	@Override
	public String getSequenceKey() {
		return "PARTY";
	}

	@Override
	public int getSequenceLength() {
		return 5;
	}

	@Override
	public int getSequenceStep() {
		return 1;
	}
	
	public Party clone(){
		 try{
			 return (Party)super.clone();
		 }catch( CloneNotSupportedException e ){
			 return null;
		 }
	}
	
	/**
	 * 是否可用
	 * @return boolean
	 */
	public boolean isEnabled( ){
		return Boolean.TRUE.equals( this.enabled );
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}

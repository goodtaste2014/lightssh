package com.google.code.lightssh.project.security.entity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;

import com.google.code.lightssh.common.entity.base.UUIDModel;
import com.google.code.lightssh.project.util.constant.AuditStatus;

/**
 * 系统角色
 * @author YangXiaojin
 *
 */
@Entity
@Table( name="T_SECURITY_ROLE")
public class Role extends UUIDModel implements Cloneable{

	private static final long serialVersionUID = -2118356457067244665L;
	

	/**
	 * 只读，用于系统初始化角色
	 */
	@Column( name="READONLY" )
	private Boolean readonly;
	
	/**
	 * 角色名称
	 */
	
	@Column( name="NAME",length=100 )
	private String name;
	
	/**
	 * 状态
	 */
	@Column( name="STATUS",length=50 )
	@Enumerated(EnumType.STRING)
	private AuditStatus status;
	
	/**
	 * 描述
	 */
	@Column( name="DESCRIPTION",length=200  )
	private String description;
	
	/**
	 * 角色对应权限
	 */
	@ManyToMany( fetch=FetchType.EAGER )
	@JoinTable( name="T_REF_ROLE_PERMISSION", 
			joinColumns=@JoinColumn( name="ROLE_ID"),
			inverseJoinColumns=@JoinColumn( name="PERMISSION_ID"))
	private Set<Permission> permissions;
	
	/**
	 * 状态是否有效
	 */
	public boolean isEffective(){
		return AuditStatus.EFFECTIVE.equals(this.status);
	}
	
	/**
	 * 是否标记删除
	 */
	public boolean isRemoved(){
		return AuditStatus.DELETE.equals(this.status);
	}
	
	public void addPermission( Permission p ){
		if( p == null )
			return;
		
		List<Permission> list = new ArrayList<Permission>( );
		list.add(p);
		addPermission(list);
	}
	
	/**
	 * 添加权限
	 */
	public void addPermission( Collection<Permission> colls ){
		if( this.permissions == null )
			this.permissions = new HashSet<Permission>();
		
		if( colls == null ) 
			return;
		
		for( Permission p:colls )
			this.permissions.add( p );
	}
	
	public void setPermissions( Collection<Permission> colls ){
		this.permissions = new HashSet<Permission>();
		
		if( colls == null ) 
			return;
		
		for( Permission p:colls )
			this.permissions.add( p );
	}
	
	/**
	 * get permission as string array
	 */
	public Collection<String> getPermissionsAsString(){
		if( this.permissions == null || this.permissions.isEmpty() )
			return null;
		
		List<String> result = new ArrayList<String>();
		for( Permission each: this.getPermissions() ){
			result.add( each.getToken() );
		}
		
		return result;
	}
	
	public void preInsert( ){
		if( StringUtils.isEmpty(id) )
			this.id = UUID.randomUUID().toString();
		this.createdTime = Calendar.getInstance();
	}
	
	public Role clone(){
		 try{
			 return (Role)super.clone();
		 }catch( CloneNotSupportedException e ){
			 return null;
		 }
	}
	
	//-- getters and setters --------------------------------------------------

	public Boolean getReadonly() {
		return readonly;
	}

	public void setReadonly(Boolean readonly) {
		this.readonly = readonly;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}

	public AuditStatus getStatus() {
		return status;
	}

	public void setStatus(AuditStatus status) {
		this.status = status;
	}

}

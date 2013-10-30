package com.google.code.lightssh.project.column.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.google.code.lightssh.common.entity.base.UUIDModel;

/**
 * 动态列组
 * @author Aspen
 * @date 2013-10-24
 * 
 */
@Entity
@Table(name="T_COLUMN_CUSTOMIZATION")
public class ColumnCustomization extends UUIDModel{
	
	private static final long serialVersionUID = 5650569139163210417L;
	
	/**
	 * 名称
	 */
	@Column(name="NAME")
	private String name;
	
	/**
	 * 备注
	 */
	@Column(name="DESCRIPTION")
	private String description;
	
	/**
	 * 组对应列条目
	 */
	@ManyToMany( fetch=FetchType.EAGER )
	@JoinTable( name="T_REF_COLUMN_GROUP_ITEM", 
			joinColumns=@JoinColumn( name="COLUMN_GROUP_ID"),
			inverseJoinColumns=@JoinColumn( name="COLUMN_ITEM_ID"))
	private Set<ColumnItem> items;

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

	public Set<ColumnItem> getItems() {
		return items;
	}

	public void setItems(Set<ColumnItem> items) {
		this.items = items;
	}

}

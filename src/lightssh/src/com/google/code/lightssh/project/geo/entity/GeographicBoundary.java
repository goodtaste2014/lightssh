package com.google.code.lightssh.project.geo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.google.code.lightssh.common.entity.Persistence;
import com.google.code.lightssh.common.util.StringUtil;

/**
 * 地理边界
 * @author Aspen
 */
@Entity
@Table( name="T_GEO" )
public class GeographicBoundary implements Persistence<String>{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 系统编码
	 */
	@Id
	@Column( name="CODE",length=50 )
	protected String code;
	
	/**
	 * 名称
	 */
	@Column( name="NAME",length=100 )
	protected String name;
	
	/**
	 * 本地名
	 */
	@Column( name="LOCAL_NAME",length=100 )
	protected String localname;
	
	/**
	 * 缩写
	 */
	@Column( name="ABBREVIATION",length=50 )
	protected String abbreviation;
	
	/**
	 * 数字代码
	 */
	@Column( name="NUMERIC_CODE",length=50 )
	protected String numericCode;
	
	/**
	 * 类型
	 */
	@Column( name="TYPE",length=50 )
	@Enumerated( EnumType.STRING )
	protected GeoType type;
	
	/**
	 * 描述
	 */
	@Column( name="DESCRIPTION",length=200 )
	protected String description;
	
	/**
	 * 是否可见
	 */
	@Column( name="ACTIVE" )
	protected Boolean active;
	
	/**
	 * 是否活动的
	 */
	public boolean isActive(){
		return Boolean.TRUE.equals( this.active );
	}
	
	@Override
	public String getIdentity() {
		return this.code;
	}

	@Override
	public boolean isInsert() {
		return StringUtil.clean( this.code ) == null;
	}

	@Override
	public void postInsertFailure() {
		//do nothing
	}

	@Override
	public void preInsert() {
		//do nothing
	}	


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocalname() {
		return localname;
	}

	public void setLocalname(String localname) {
		this.localname = localname;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public GeoType getType() {
		return type;
	}

	public void setType(GeoType type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNumericCode() {
		return numericCode;
	}

	public void setNumericCode(String numericCode) {
		this.numericCode = numericCode;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "GeographicBoundary [abbreviation=" + abbreviation + ", active="
				+ active + ", code=" + code + ", description=" + description
				+ ", localname=" + localname + ", name=" + name
				+ ", numericCode=" + numericCode + ", type=" + type + "]";
	}

}

package com.google.code.lightssh.project.message.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.google.code.lightssh.common.entity.base.UUIDModel;
import com.google.code.lightssh.common.model.Period;

/**
 * 消息订阅
 * @author YangXiojin
 * @date 2013-9-3
 * 
 */
@Entity
@Table(name="T_MSG_SUBSCRIPTION")
public class Subscription extends UUIDModel{

	private static final long serialVersionUID = 3826073871651109841L;
	
	/**
	 * 类型
	 */
	public enum SubType{
		ROLE("角色")			
		,DEPARTMENT("部门")			
		,PERSON("人员")	
		,POSITION("岗位")	
		,USER("用户");		

		private String value;
		
		SubType( String value ){
			this.value = value;
		}
		
		/**
		 * 支持的类型
		 */
		public static SubType[] supportedValues( ){
			return new SubType[]{DEPARTMENT,PERSON,USER};
		}

		public String getValue() {
			return value;
		}
		
		public String toString(){
			return this.value;
		}
	}

	
	/**
	 * 消息类型
	 */
	@ManyToOne
	@JoinColumn(name="CATALOG_ID")
	private Catalog catalog;
	
	/**
	 * 订阅类型
	 */
	@Column(name="SUB_TYPE",length=20)
	@Enumerated(EnumType.STRING)
	private SubType subType;
	
	/**
	 * 订阅类型值
	 */
	@Column(name="SUB_VALUE",length=255)
	private String subValue;
	
	/**
	 * 创建人
	 */
	@Column(name="CREATOR",length=50)
	private String creator;
	
	/**
	 * 有效期
	 */
	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name="start", column=@Column(name="PERIOD_START")),
		@AttributeOverride(name="end", column=@Column(name="PERIOD_END"))})
	private Period period;
	
	/**
	 * 备注
	 */
	@Column(name="DESCRIPTION",length=200)
	private String description;

	public Catalog getCatalog() {
		return catalog;
	}

	public void setCatalog(Catalog catalog) {
		this.catalog = catalog;
	}

	public SubType getSubType() {
		return subType;
	}

	public void setSubType(SubType subType) {
		this.subType = subType;
	}

	public String getSubValue() {
		return subValue;
	}

	public void setSubValue(String subValue) {
		this.subValue = subValue;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Period getPeriod() {
		return period;
	}

	public void setPeriod(Period period) {
		this.period = period;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}

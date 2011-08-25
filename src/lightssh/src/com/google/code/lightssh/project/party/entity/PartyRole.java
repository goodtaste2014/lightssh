package com.google.code.lightssh.project.party.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.google.code.lightssh.common.entity.base.UUIDModel;
import com.google.code.lightssh.common.model.Period;

/**
 * Party Role
 * @author YangXiaojin
 *
 */
@Entity
@Table( name="T_PARTY_PERSON" )
public class PartyRole extends UUIDModel{
	
	private static final long serialVersionUID = 2352810614613495760L;

	/**
	 * 业务角色
	 */
	public enum RoleType{
		INTERNAL_ORG("内部组织")
		,PARENT_ORG("最上级组织")
		,CORPORATION_GROUP("集团")
		,CORPORATION("公司")
		,SUBSIDIARY("分公司")
		,DIVISION("分支机构")
		,DEPARTMENT("部门")
		,TEAM("小组")
		,OTHER_ORG_UNIT("其它组织单元")
		;
		
		private String value;
		
		RoleType( String value ){
			this.value = value;
		}

		public String getValue() {
			return value;
		}
		
		public String toString(){
			return this.value;
		}
	}
	
	/**
	 * Party
	 */
	@ManyToOne( fetch=FetchType.LAZY )
	@JoinColumn( name="PARTY_ID")
	private Party party;
	
	/**
	 * 有效期
	 */
	@Embedded
	private Period period;
	
	/**
	 * 业务角色
	 */
	@Column( name="TYPE",length=50 )
	@Enumerated(value=EnumType.STRING)
	private RoleType type;

	public PartyRole( ) {
		super();
	}
	
	public PartyRole(Party party, RoleType type) {
		super();
		this.party = party;
		this.type = type;
		this.period = new Period(new Date(),null); 
	}

	public Party getParty() {
		return party;
	}

	public void setParty(Party party) {
		this.party = party;
	}

	public Period getPeriod() {
		return period;
	}

	public void setPeriod(Period period) {
		this.period = period;
	}

	public RoleType getType() {
		return type;
	}

	public void setType(RoleType type) {
		this.type = type;
	}

}

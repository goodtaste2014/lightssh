package com.google.code.lightssh.project.security.entity;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.google.code.lightssh.common.entity.base.BaseModel;
import com.google.code.lightssh.common.model.Period;
import com.google.code.lightssh.project.party.entity.Party;

/**
 * 登录账号
 * @author YangXiaojin
 *
 */
@Entity
@Table( name="T_SECURITY_LOGINACCOUNT" )
public class LoginAccount extends BaseModel {

	private static final long serialVersionUID = 8280727772996743600L;
	
	/**
	 * 登录帐户类型
	 */
	public enum LoginAccountType{
		ADMIN("交易所")
		,MEMBER("会员");
		
		private String value;
		
		LoginAccountType( String value ){
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
	 * 登录名
	 */
	@Column(name="LOGIN_NAME",unique=true,nullable=false)
	private String loginName;
	
	/**
	 * 密码
	 */
	@Column(name="PASSWORD",unique=false,nullable=false)
	private String password;
	
	/**
	 * 邮箱
	 */
	@Column(name="EMAIL",unique=true,length=255)
	private String email;
	
	/**
	 * 是否有效
	 */
	@Column( name="ENABLED" )
	private Boolean enabled;
	
	/**
	 * 创建时间
	 */
	@Column( name="CREATE_DATE",nullable=false)
	private Date createDate;
	
	/**
	 * 使用期限
	 */
	@Embedded
	private Period period;
	
	/**
	 * 权限角色
	 */
	@ManyToMany( fetch=FetchType.EAGER )
	@JoinTable( name="T_REF_LOGINACCOUNT_ROLE", 
			joinColumns=@JoinColumn( name="LOGINACCOUNT_ID"),
			inverseJoinColumns=@JoinColumn( name="ROLE_ID"))	
	private Set<Role> roles;
	
	/**
	 * 描述
	 */
	@Column( name="DESCRIPTION",length=200 )
	private String description;
	
	/**
	 * 所属组织或人员
	 */
	@ManyToOne( fetch=FetchType.LAZY )
	@JoinColumn( name="PARTY_ID")
	private Party party;
	
	/**
	 * 登录账号类型
	 */
	@Column( name="TYPE",length=20 )
	@Enumerated(value=EnumType.STRING)
	private LoginAccountType type;
	
	/**
	 * 是否使用CA登录
	 */
	@Column( name="USE_CA" )
	private Boolean useCa;
	
	/**
	 * 最后一次更新密码时间
	 */
	@Column( name="LAST_UPDATE_PASSWORD_TIME" )
	private Calendar lastUpdatePasswordTime;
	
	public LoginAccount() {
		super();
	}
	
	public LoginAccount(Long id,String name) {
		this.loginName = name;
		this.id = id;
	}

	/**
	 * 是否过期
	 * @return 过期返回false
	 */
	public boolean isExpired( ){
		return period==null || period.isExpired(new Date() );
	}
	
	/**
	 * 是否有效
	 */
	public boolean isEnabled( ){
		return Boolean.TRUE.equals( this.enabled );
	}
	
	/**
	 * 设置时间区间
	 * @param start 起始时间
	 * @param end 结束时间
	 */
	public void setPeriod(Date start,Date end ){
		this.period = new Period(start,end);
	}
	
	/**
	 * 是否会员
	 */
	public boolean isMember(){
		return LoginAccountType.MEMBER.equals( this.type );
	}
	
	/**
	 * 添加角色
	 */
	public void addRole( Role role ){
		if( role == null )
			return;
		
		if( this.roles == null )
			roles = new HashSet<Role>();
		
		roles.add(role);
	}
	
	public String getSubjectMessage(){
		StringBuilder sb = new StringBuilder("");
		if( this.party != null){
			sb.append( party.getName() );
			if( !LoginAccountType.ADMIN.equals(this.type) )
				sb.append( "(" + party.getIdentity() + ")" );
		}
		
		return sb.toString();
	}
	/**
	 * 是否拥有某个角色
	 */
	public boolean hasRole( String roleName ){
		if( roleName == null || this.getRoles() == null 
				|| this.getRoles().isEmpty() )
			return false;
		
		for( Role role:this.roles ){
			if( role.getName().equals( roleName ) )
				return true;
		}
		
		return false;
	}
	
	public String getUsername() {
		return this.getLoginName();
	}

	public boolean isAccountNonExpired() {
		return isExpired( );
	}

	public boolean isAccountNonLocked() {
		return true;
	}

	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	/**
	 * 是否需要CA登录
	 */
	public boolean isUseCa( ){
		return Boolean.TRUE.equals(this.useCa);
	}

	//-- getters and setters --------------------------------------------------
	
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Period getPeriod() {
		return period;
	}

	public void setPeriod(Period period) {
		this.period = period;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Party getParty() {
		return party;
	}

	public void setParty(Party party) {
		this.party = party;
	}

	public LoginAccountType getType() {
		return type;
	}

	public void setType(LoginAccountType type) {
		this.type = type;
	}

	public Boolean getUseCa() {
		return useCa;
	}

	public void setUseCa(Boolean useCa) {
		this.useCa = useCa;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Calendar getLastUpdatePasswordTime() {
		return lastUpdatePasswordTime;
	}

	public void setLastUpdatePasswordTime(Calendar lastUpdatePasswordTime) {
		this.lastUpdatePasswordTime = lastUpdatePasswordTime;
	}

}

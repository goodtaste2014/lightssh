package com.google.code.lightssh.project.party.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * person
 * @author Aspen
 *
 */
@Entity
@Table( name="T_PARTY_PERSON" )
@PrimaryKeyJoinColumn(name="id") 
public class Person extends Party implements java.lang.Cloneable{
	
	private static final long serialVersionUID = 1L;

	/**
	 * gender enum
	 */
	public enum Gender{
		MALE("男")			//male
		,FEMALE("女");		//female
		
		private String value;
		
		Gender( String value ){
			this.value = value;
		}

		public String getValue() {
			return value;
		}
		
		public String toString(){
			return this.value;
		}
	}
	
	@Override
	public String getSequenceKey() {
		return "PERSON";
	}
	
	/**
	 * 婚姻状况
	 */
	public enum MaritalStatus{
		UNKNOWN("未知")			//未知
		,SINGLE("单身")			//单身
		,MARRIED("已婚")			//已婚
		,SEPARATED("离异")		//分居、离婚
		,WIDOWED("丧偶");		//丧偶
		
		private String value;
		
		MaritalStatus( String value ){
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
	 * 教育水平
	 */
	public enum EducationLevel{
		MANDATORY_SCHOOLING("义务教育")		//义务教育
		,HIGH_SCHOOL("高中")					//高中
		,VOCATIONAL_SCHOOL("职业学校")		//职业学校
		,JUNIOR_COLLEGE("专科")				//专科
		,UNDERGRADUATE("本科")				//本科
		,MASTER("研究生")					//研究生
		,PHD("博士");						//博士
		
		private String value;
		
		EducationLevel( String value ){
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
	 * first name
	 */
	@Column( name="FIRST_NAME" )
	private String firstName;
	
	/**
	 * last name
	 */
	@Column( name="LAST_NAME" )
	private String lastName;
	
	/**
	 * 证件类型
	 */
	private CredentialsType credentialsType;
	
	/**
	 * 证件号
	 */
	@Column( name="IDENTITY_CARD_NUMBER" )
	private String identityCardNumber;
	
	/**
	 * 称呼
	 */
	@Column( name="TITLE",length=20 )
	private String title;
	
	/**
	 * 别名
	 */
	@Column( name="NICKNAME",length=100 )
	private String nickname;
	
	/**
	 * 性别
	 */
	@Column( name="GENDER",length=20 )
	@Enumerated(value=EnumType.STRING)
	private Gender gender;
	
	/**
	 * 生日
	 */
	@Column( name="BIRTHDAY" )
	@Temporal(TemporalType.DATE)
	private Date birthday;
	
	/**
	 * 全名
	 */
	public String getFullName( ){
		return lastName + ' ' + firstName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getIdentityCardNumber() {
		return identityCardNumber;
	}

	public void setIdentityCardNumber(String identityCardNumber) {
		this.identityCardNumber = identityCardNumber;
	}

	public CredentialsType getCredentialsType() {
		return credentialsType;
	}

	public void setCredentialsType(CredentialsType credentialsType) {
		this.credentialsType = credentialsType;
	}

	@Override
	public String toString() {
		return "Person [id=" + getIdentity() +  ", birthday=" 
				+ birthday + ", firstName=" + firstName
				+ ", gender=" + gender + ", identityCardNumber="
				+ identityCardNumber + ", lastName=" + lastName + ", nickname="
				+ nickname + ", title=" + title + "]";
	}	
	
	 public Person clone(){
		 try{
			 return (Person)super.clone();
		 }catch( CloneNotSupportedException e ){
			 return null;
		 }
	}
}

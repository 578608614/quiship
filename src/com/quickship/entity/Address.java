package com.quickship.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "qs_address")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "qs_address_sequence")
public class Address extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = -377029285393371766L;
	/** ��ϵ��*/
	private String person;
	/** ��˾*/
	private String company;
	/** ��ϵ�绰*/
	private String phone;
	/** ��ϵ��ַ1*/
	private String address1;
	/** ��ϵ��ַ2*/
	private String address2;
	/** ����*/
	private String city;
	/** ����*/
	private Area area;
	/** �ʱ�*/
	private String zipCode;
	/** �Ƿ�Ϊסլ*/
	private Boolean isResidential;
	/** ��ַ���� */
	private String byName;
	/** ��Ա */
	private Set<Member> members = new HashSet<Member>();
	
	@JsonProperty
	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		this.person = person;
	}
	@JsonProperty
	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}
	@JsonProperty
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	@JsonProperty
	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	@JsonProperty
	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	@JsonProperty
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	@JsonProperty
	@ManyToOne(fetch=FetchType.EAGER)
	public Area getArea() {
		return area;
	}
	
	public void setArea(Area area) {
		this.area = area;
	}
	@JsonProperty
	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	
	public Boolean getIsResidential() {
		return isResidential;
	}

	public void setIsResidential(Boolean isResidential) {
		this.isResidential = isResidential;
	}

	public String getByName() {
		return byName;
	}

	public void setByName(String byName) {
		this.byName = byName;
	}
	@ManyToMany(fetch=FetchType.LAZY)
	public Set<Member> getMembers() {
		return members;
	}

	public void setMembers(Set<Member> members) {
		this.members = members;
	}
	
	
	
}

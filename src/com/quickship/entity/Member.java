package com.quickship.entity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name = "qs_member")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "qs_member_sequence")
public class Member extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1992367327763005553L;
	/** 名字 */
	private String firstName;
	/** 姓氏 */
	private String lastName;
	/** email */
	private String email;
	/** 密码 */
	private String password;
	/** 地址1 */
	private String address1;
	/** 地址2 */
	private String address2;
	/** 邮编 */
	private String zip;
	/** 电话 */
	private String phone;
	/** 消费金额 */
	private BigDecimal amount;
	/** 余额 */
	private BigDecimal balance;
	/** 保险 */
	private Boolean isNurse;
	/** 城市*/
	private String city;
	/** 是否锁定 */
	private Boolean isLocked;
	/** 是否验证 */
	private Boolean isValidate;
	/** ip地址 */
	private String ip;
	/** 地区 */
	private Area area;
	/** 运单 */
	private Set<Shipping> shippings = new HashSet<Shipping>();
	
	/** 预存、支付记录 */
	private Set<Deposit> deposits = new HashSet<Deposit>();
	
	/** 发货地址 */
	private Set<Address> addresses = new HashSet<Address>();
	
	/** 收款单 */
	private Set<Payment> payments = new HashSet<Payment>();
	
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Boolean getIsNurse() {
		return isNurse;
	}

	public void setIsNurse(Boolean isNurse) {
		this.isNurse = isNurse;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Boolean getIsLocked() {
		return isLocked;
	}

	public void setIsLocked(Boolean isLocked) {
		this.isLocked = isLocked;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public Boolean getIsValidate() {
		return isValidate;
	}

	public void setIsValidate(Boolean isValidate) {
		this.isValidate = isValidate;
	}
	@ManyToOne(fetch=FetchType.EAGER)
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}
	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	public Set<Shipping> getShippings() {
		return shippings;
	}

	public void setShippings(Set<Shipping> shippings) {
		this.shippings = shippings;
	}

	@OneToMany(mappedBy = "member",fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	public Set<Deposit> getDeposits() {
		return deposits;
	}

	public void setDeposits(Set<Deposit> deposits) {
		this.deposits = deposits;
	}
	@ManyToMany(mappedBy="members", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	public Set<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(Set<Address> addresses) {
		this.addresses = addresses;
	}

	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	public Set<Payment> getPayments() {
		return payments;
	}

	public void setPayments(Set<Payment> payments) {
		this.payments = payments;
	}

}

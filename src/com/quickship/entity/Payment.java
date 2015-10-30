package com.quickship.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 支付单
 * @author it-1
 *
 */
@Entity
@Table(name = "qs_payment")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "qs_payment_sequence")
public class Payment extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1421373547633453651L;

	/**
	 * 支付类型
	 */
	public enum Type {
		/** 订单支付 */
		payment,
		
		/** 预存款充值 */
		recharge
	}
	/**
	 * 支付方式
	 */
	public enum Method {
		/** 在线支付 */
		online,
		
		/** 预存款支付 */
		deposit
	}

	/**
	 * 支付状态
	 */
	public enum Status {

		/** 等待支付 */
		wait,

		/** 支付成功 */
		success,

		/** 支付失败 */
		failure
	}
	/** 编号 */
	private String sn;
	/** 类型 */
	private Type type;
	/** 方式 */
	private Method method;
	/** 状态 */
	private Status status;
	/** 支付方式 */
	private String paymentMethod;
	/** 支付手续费 */
	private BigDecimal fee;
	/** 付款金额 */
	private BigDecimal amount;
	/** 付款日期 */
	private Date paymentDate;
	/** 会员 */
	private Member member;
	/** 运单 */
//	private Shipping shipping;
	/** 预存款 */
	private Deposit deposit;
	/** 支付插件ID */
	private String pluginId;
	

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(updatable = false)
	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

//	public Shipping getShipping() {
//		return shipping;
//	}
//
//	public void setShipping(ShipOrder shipping) {
//		this.shipping = shipping;
//	}
	
	@OneToOne(mappedBy = "payment", fetch = FetchType.LAZY)
	public Deposit getDeposit() {
		return deposit;
	}

	public void setDeposit(Deposit deposit) {
		this.deposit = deposit;
	}

	public String getPluginId() {
		return pluginId;
	}

	public void setPluginId(String pluginId) {
		this.pluginId = pluginId;
	}
	/**
	 * 获取有效金额
	 * 
	 * @return 有效金额
	 */
	@Transient
	public BigDecimal getEffectiveAmount() {
		BigDecimal effectiveAmount = getAmount().subtract(getFee());
		return effectiveAmount.compareTo(new BigDecimal(0)) > 0 ? effectiveAmount : new BigDecimal(0);
	}
	

}

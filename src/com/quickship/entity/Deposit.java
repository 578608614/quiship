package com.quickship.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 
 * 预存、支付记录
 * @author it-1
 *
 */
@Entity
@Table(name = "qs_deposit")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "qs_deposit_sequence")
public class Deposit extends BaseEntity{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1922020145307073998L;
	
	/** 操作类型 */
	public enum Type{
		/** 会员充值 */
		memberRecharge,

		/** 会员支付 */
		memberPayment,
		
		/** 后台充值 */
		adminRecharge,

		/** 后台扣费 */
		adminChargeback
		
	}
	
	/** 操作类型 */
	private Type type;
	
	/** 收入金额 */
	private BigDecimal credit;
	
	/** 支出金额 */
	private BigDecimal debit;
	
	/** 当前余额 */
	private BigDecimal balance;
	
	/** 发货单 */
	private Shipping shipping;
	
	/** 收款单 */
	private Payment payment;
	
	
	/** 会员 */
	private Member member;

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public BigDecimal getCredit() {
		return credit;
	}

	public void setCredit(BigDecimal credit) {
		this.credit = credit;
	}

	public BigDecimal getDebit() {
		return debit;
	}

	public void setDebit(BigDecimal debit) {
		this.debit = debit;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public Shipping getShipOrder() {
		return shipping;
	}

	public void setShipOrder(Shipping shipOrder) {
		this.shipping = shipOrder;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}
	
	
}

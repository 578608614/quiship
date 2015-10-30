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
 * Ԥ�桢֧����¼
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
	
	/** �������� */
	public enum Type{
		/** ��Ա��ֵ */
		memberRecharge,

		/** ��Ա֧�� */
		memberPayment,
		
		/** ��̨��ֵ */
		adminRecharge,

		/** ��̨�۷� */
		adminChargeback
		
	}
	
	/** �������� */
	private Type type;
	
	/** ������ */
	private BigDecimal credit;
	
	/** ֧����� */
	private BigDecimal debit;
	
	/** ��ǰ��� */
	private BigDecimal balance;
	
	/** ������ */
	private Shipping shipping;
	
	/** �տ */
	private Payment payment;
	
	
	/** ��Ա */
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

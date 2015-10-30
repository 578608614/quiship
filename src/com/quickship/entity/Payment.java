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
 * ֧����
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
	 * ֧������
	 */
	public enum Type {
		/** ����֧�� */
		payment,
		
		/** Ԥ����ֵ */
		recharge
	}
	/**
	 * ֧����ʽ
	 */
	public enum Method {
		/** ����֧�� */
		online,
		
		/** Ԥ���֧�� */
		deposit
	}

	/**
	 * ֧��״̬
	 */
	public enum Status {

		/** �ȴ�֧�� */
		wait,

		/** ֧���ɹ� */
		success,

		/** ֧��ʧ�� */
		failure
	}
	/** ��� */
	private String sn;
	/** ���� */
	private Type type;
	/** ��ʽ */
	private Method method;
	/** ״̬ */
	private Status status;
	/** ֧����ʽ */
	private String paymentMethod;
	/** ֧�������� */
	private BigDecimal fee;
	/** ������ */
	private BigDecimal amount;
	/** �������� */
	private Date paymentDate;
	/** ��Ա */
	private Member member;
	/** �˵� */
//	private Shipping shipping;
	/** Ԥ��� */
	private Deposit deposit;
	/** ֧�����ID */
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
	 * ��ȡ��Ч���
	 * 
	 * @return ��Ч���
	 */
	@Transient
	public BigDecimal getEffectiveAmount() {
		BigDecimal effectiveAmount = getAmount().subtract(getFee());
		return effectiveAmount.compareTo(new BigDecimal(0)) > 0 ? effectiveAmount : new BigDecimal(0);
	}
	

}

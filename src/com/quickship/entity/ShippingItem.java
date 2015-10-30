package com.quickship.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * �˵�����
 * @author it-1
 *
 */
@Entity
@Table(name = "qs_shippingItem")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "qs_shipping_item_sequence")
public class ShippingItem extends BaseEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5654246669541812848L;
	
	/** �������� */
	private BigDecimal weight;
	
	/** �������� */
	private BigDecimal insured;
	
	/** �������� */
	private Integer length;
	
	/** ������� */
	private Integer width;
	
	/** �����߶� */
	private Integer height;
	
	/** ���� */
	private BigDecimal netCharge;
	
	/** �������� */
	private BigDecimal baseCharge;
	
	/** ���ӷ� */
	private BigDecimal surCharge;
	
	/** �ɱ� */
	private BigDecimal costCharge;
	
	/** �ۿ۷� */
	private BigDecimal discount;
	
	/** �����˵�*/
	private Shipping shipping;

	@JsonProperty
	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}
	@JsonProperty
	public BigDecimal getInsured() {
		return insured;
	}

	public void setInsured(BigDecimal insured) {
		this.insured = insured;
	}
	
	@JsonProperty
	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}
	@JsonProperty
	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}
	@JsonProperty
	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}
	
	public BigDecimal getNetCharge() {
		return netCharge;
	}

	public void setNetCharge(BigDecimal netCharge) {
		this.netCharge = netCharge;
	}

	public BigDecimal getBaseCharge() {
		return baseCharge;
	}

	public void setBaseCharge(BigDecimal baseCharge) {
		this.baseCharge = baseCharge;
	}

	public BigDecimal getSurCharge() {
		return surCharge;
	}

	public void setSurCharge(BigDecimal surCharge) {
		this.surCharge = surCharge;
	}

	public BigDecimal getCostCharge() {
		return costCharge;
	}

	public void setCostCharge(BigDecimal costCharge) {
		this.costCharge = costCharge;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Shipping getShipping() {
		return shipping;
	}

	public void setShipping(Shipping shipping) {
		this.shipping = shipping;
	}
	
	
	
	
}

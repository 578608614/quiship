package com.quickship.entity;

import java.math.BigDecimal;

public class ShippingReport {
	
	private Shipping shipping;
	
	private BigDecimal weight;
	
	private BigDecimal adjustCharge;

	public Shipping getShipping() {
		return shipping;
	}

	public void setShipping(Shipping shipping) {
		this.shipping = shipping;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public BigDecimal getAdjustCharge() {
		return adjustCharge;
	}

	public void setAdjustCharge(BigDecimal adjustCharge) {
		this.adjustCharge = adjustCharge;
	}
	
	
}

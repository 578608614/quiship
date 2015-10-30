package com.quickship;

import java.math.BigDecimal;

public class ChargeItem {
	
	private BigDecimal totalBaseCharge;
	
	private BigDecimal totalNetCharge;
	
	private BigDecimal totalSurCharge;
	
	private BigDecimal totalEffeDiscount;
	
	private BigDecimal totalRebates;

	public BigDecimal getTotalBaseCharge() {
		return totalBaseCharge;
	}

	public void setTotalBaseCharge(BigDecimal totalBaseCharge) {
		this.totalBaseCharge = totalBaseCharge;
	}

	public BigDecimal getTotalNetCharge() {
		return totalNetCharge;
	}

	public void setTotalNetCharge(BigDecimal totalNetCharge) {
		this.totalNetCharge = totalNetCharge;
	}

	public BigDecimal getTotalSurCharge() {
		return totalSurCharge;
	}

	public void setTotalSurCharge(BigDecimal totalSurCharge) {
		this.totalSurCharge = totalSurCharge;
	}

	public BigDecimal getTotalEffeDiscount() {
		return totalEffeDiscount;
	}

	public void setTotalEffeDiscount(BigDecimal totalEffeDiscount) {
		this.totalEffeDiscount = totalEffeDiscount;
	}

	public BigDecimal getTotalRebates() {
		return totalRebates;
	}

	public void setTotalRebates(BigDecimal totalRebates) {
		this.totalRebates = totalRebates;
	}
	
	
	
}

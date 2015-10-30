package com.quickship;

import java.math.BigDecimal;

public class Income {
	/**Ӫҵ�� */
	private BigDecimal charge;
	/**�ɱ� */
	private BigDecimal cost;
	/**����*/
	private BigDecimal profit;
	
	public Income(){
		
	}
	public Income(BigDecimal charge,BigDecimal cost){
		this.charge = charge;
		this.cost = cost;
	}
	public BigDecimal getCharge() {
		return charge;
	}
	
	public void setCharge(BigDecimal charge) {
		this.charge = charge;
	}
	
	public BigDecimal getCost() {
		return cost;
	}
	
	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}
	
	public BigDecimal getProfit() {
		if(charge==null||charge.compareTo(new BigDecimal(0))==0){
			charge = new BigDecimal(0);
		}
		if(cost==null||cost.compareTo(new BigDecimal(0))==0){
			cost = new BigDecimal(0);
		}
		this.profit = charge.subtract(cost);
		return profit;
	}
	public void setProfit(BigDecimal profit) {
		this.profit = profit;
	}
}

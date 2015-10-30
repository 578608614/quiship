package com.quickship.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.quickship.entity.Shipping.ServerType;

/**
 * @author it-1
 * 
 */
@Entity
@Table(name = "qs_discount")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "qs_discount_sequence")
public class Discount extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5398816996172925988L;

	/** 名称 */
	private String name;

	/** 服务类型 */
	private ServerType serverType;

	/** 首重量 */
	private Integer firstWeight;
	
	/** 首重价格 */
	private BigDecimal firstDiscount;
	
//	/** 续重量 */
//	private Integer continueWeight;

	/** 续重价格 */
	private BigDecimal continueDiscount;

	/** 描述 */
	private String description;
	
	/** 应用全部*/
	private Boolean usedAll;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ServerType getServerType() {
		return serverType;
	}

	public void setServerType(ServerType serverType) {
		this.serverType = serverType;
	}

	public Integer getFirstWeight() {
		return firstWeight;
	}

	public void setFirstWeight(Integer firstWeight) {
		this.firstWeight = firstWeight;
	}

	public BigDecimal getFirstDiscount() {
		return firstDiscount;
	}

	public void setFirstDiscount(BigDecimal firstDiscount) {
		this.firstDiscount = firstDiscount;
	}

	public BigDecimal getContinueDiscount() {
		return continueDiscount;
	}

	public void setContinueDiscount(BigDecimal continueDiscount) {
		this.continueDiscount = continueDiscount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getUsedAll() {
		return usedAll;
	}

	public void setUsedAll(Boolean usedAll) {
		this.usedAll = usedAll;
	}

	@Transient
	public BigDecimal calcucate(Shipping shipping) {
		BigDecimal discount = new BigDecimal(0);
		BigDecimal firstWeight = new BigDecimal(this.firstWeight);
		BigDecimal weight = shipping.getWeight();
		if (weight.compareTo(firstWeight) <= 0) {
			discount = this.firstDiscount;
//			discount = weight.divide(firstWeight, 2).multiply(firstPrice);
		} else {
			discount = this.continueDiscount;
//			BigDecimal temp = weight.subtract(firstWeight).divide(new BigDecimal(this.continueWeight)).multiply(this.continuePrice);
//			discount = discount.add(temp);
		}
		return discount;
	}
}

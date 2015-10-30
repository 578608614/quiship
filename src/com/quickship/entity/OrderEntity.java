package com.quickship.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Min;

import org.apache.commons.lang.builder.CompareToBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;
@MappedSuperclass
public class OrderEntity extends BaseEntity implements Comparable<OrderEntity>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2552286145565471043L;

	/** "����"�������� */
	public static final String ORDER_PROPERTY_NAME = "order";

	/** ���� */
	private Integer order;

	/**
	 * ��ȡ����
	 * 
	 * @return ����
	 */
	@JsonProperty
	@Min(0)
	@Column(name = "orders")
	public Integer getOrder() {
		return order;
	}

	/**
	 * ��������
	 * 
	 * @param order
	 *            ����
	 */
	public void setOrder(Integer order) {
		this.order = order;
	}

	/**
	 * ʵ��compareTo����
	 * 
	 * @param orderEntity
	 *            �������
	 * @return �ȽϽ��
	 */
	public int compareTo(OrderEntity orderEntity) {
		return new CompareToBuilder().append(getOrder(), orderEntity.getOrder()).append(getId(), orderEntity.getId()).toComparison();
	}

}

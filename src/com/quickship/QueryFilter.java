package com.quickship;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class QueryFilter {

	public enum Operator {
		/** ���� */
		eq,
		/** ������ */
		ne,
		/** ���� */
		gt,
		/** С�� */
		lt,
		/** ���ڵ��� */
		ge,
		/** С�ڵ��� */
		le,
		/** ���� */
		like,
		/** ���� */
		in,
		/** ΪNull */
		isNull,
		/** ��ΪNull */
		isNotNull;

		public static Operator fromString(String value) {
			return Operator.valueOf(value.toLowerCase());
		}

	}

	/** ���� */
	private String property;
	/** ����� */
	private Operator operator;
	/** ֵ */
	private Object value;
	/** �Ƿ���Դ�Сд */
	private Boolean ignoreCase = false;

	/**
	 * ��ʼ��һ���´�����Filter����
	 */
	public QueryFilter() {

	}
	/**
	 * ��ʼ��һ���´�����Filter����
	 * 
	 * @param property
	 *            ����
	 * @param operator
	 *            �����
	 * @param value
	 *            ֵ
	 */
	public QueryFilter(String property, Operator operator, Object value) {
		this.property = property;
		this.operator = operator;
		this.value = value;
	}

	/**
	 * ��ʼ��һ���´�����Filter����
	 * 
	 * @param property
	 *            ����
	 * @param operator
	 *            �����
	 * @param value
	 *            ֵ
	 */
	public QueryFilter(String property, Operator operator, Object value, boolean ignoreCase) {
		this.property = property;
		this.operator = operator;
		this.value = value;
		this.ignoreCase = ignoreCase;
	}

	/**
	 * ��ʼ��һ���´�����Filter����
	 * 
	 * @param property
	 *            ����
	 * @param operator
	 *            �����
	 * @param value
	 *            ֵ
	 * @param ignoreCase
	 *            ���Դ�Сд
	 */
	public static QueryFilter eq(String property, Object value) {
		return new QueryFilter(property, Operator.eq, value);
	}
	/**
	 * ���ز�����ɸѡ
	 * 
	 * @param property
	 *            ����
	 * @param value
	 *            ֵ
	 * @param ignoreCase
	 *            ���Դ�Сд
	 * @return ������ɸѡ
	 */
	public static QueryFilter ne(String property,Object value){
		return new QueryFilter(property,Operator.ne,value);
	}
	/**
	 * ���ش���ɸѡ
	 * 
	 * @param property
	 *            ����
	 * @param value
	 *            ֵ
	 * @return ����ɸѡ
	 */
	public static QueryFilter gt(String property, Object value) {
		return new QueryFilter(property, Operator.gt, value);
	}

	/**
	 * ����С��ɸѡ
	 * 
	 * @param property
	 *            ����
	 * @param value
	 *            ֵ
	 * @return С��ɸѡ
	 */
	public static QueryFilter lt(String property, Object value) {
		return new QueryFilter(property, Operator.lt, value);
	}

	/**
	 * ���ش��ڵ���ɸѡ
	 * 
	 * @param property
	 *            ����
	 * @param value
	 *            ֵ
	 * @return ���ڵ���ɸѡ
	 */
	public static QueryFilter ge(String property, Object value) {
		return new QueryFilter(property, Operator.ge, value);
	}

	/**
	 * ����С�ڵ���ɸѡ
	 * 
	 * @param property
	 *            ����
	 * @param value
	 *            ֵ
	 * @return С�ڵ���ɸѡ
	 */
	public static QueryFilter le(String property, Object value) {
		return new QueryFilter(property, Operator.le, value);
	}
	
	/**
	 * ��������ɸѡ
	 * 
	 * @param property
	 *            ����
	 * @param value
	 *            ֵ
	 * @return ����ɸѡ
	 */
	public static QueryFilter like(String property, Object value) {
		return new QueryFilter(property, Operator.like, value);
	}

	/**
	 * ���ذ���ɸѡ
	 * 
	 * @param property
	 *            ����
	 * @param value
	 *            ֵ
	 * @return ����ɸѡ
	 */
	public static QueryFilter in(String property, Object value) {
		return new QueryFilter(property, Operator.in, value);
	}

	/**
	 * ����ΪNullɸѡ
	 * 
	 * @param property
	 *            ����
	 * @return ΪNullɸѡ
	 */
	public static QueryFilter isNull(String property) {
		return new QueryFilter(property, Operator.isNull, null);
	}

	/**
	 * ���ز�ΪNullɸѡ
	 * 
	 * @param property
	 *            ����
	 * @return ��ΪNullɸѡ
	 */
	public static QueryFilter isNotNull(String property) {
		return new QueryFilter(property, Operator.isNotNull, null);
	}
	/**
	 * ��ȡ����
	 * 
	 * @return ����
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * ��������
	 * 
	 * @param property
	 *            ����
	 */
	public void setProperty(String property) {
		this.property = property;
	}

	/**
	 * ��ȡ�����
	 * 
	 * @return �����
	 */
	public Operator getOperator() {
		return operator;
	}

	/**
	 * ���������
	 * 
	 * @param operator
	 *            �����
	 */
	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	/**
	 * ��ȡֵ
	 * 
	 * @return ֵ
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * ����ֵ
	 * 
	 * @param value
	 *            ֵ
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * ��ȡ�Ƿ���Դ�Сд
	 * 
	 * @return �Ƿ���Դ�Сд
	 */
	public Boolean getIgnoreCase() {
		return ignoreCase;
	}

	/**
	 * �����Ƿ���Դ�Сд
	 * 
	 * @param ignoreCase
	 *            �Ƿ���Դ�Сд
	 */
	public void setIgnoreCase(Boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		QueryFilter other = (QueryFilter) obj;
		return new EqualsBuilder().append(getProperty(), other.getProperty()).append(getOperator(), other.getOperator()).append(getValue(), other.getValue()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(getProperty()).append(getOperator()).append(getValue()).toHashCode();
	}

}

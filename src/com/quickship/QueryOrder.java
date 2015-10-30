package com.quickship;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


public class QueryOrder {
	public enum Direction{
		/** ���� */
		asc,
		/** ���� */
		desc;
		/**
		 * ��String�л�ȡDirection
		 * 
		 * @param value
		 *            ֵ
		 * @return String��Ӧ��Direction
		 */
		public static Direction fromString(String value) {
			return Direction.valueOf(value.toLowerCase());
		}
	}
	
	private String  property;
	
	private Direction direction = Direction.desc;
	
	public QueryOrder(){
		
	}
	
	public QueryOrder(String property,Direction direction){
		this.property = property;
		this.direction = direction;
	}
	
	public static QueryOrder asc(String property){
		return new QueryOrder(property,Direction.asc);
	}
	public static QueryOrder desc(String property){
		return new QueryOrder(property,Direction.desc);
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

	
	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
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
		QueryOrder other = (QueryOrder) obj;
		return new EqualsBuilder().append(getProperty(), other.getProperty()).append(getDirection(), other.getDirection()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(getProperty()).append(getDirection()).toHashCode();
	}
}

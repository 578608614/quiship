/*
 * Copyright 2005-2013 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package com.quickship;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.quickship.QueryOrder.Direction;

/**
 * 分页
 * 
 */
public class Page<T> implements Serializable {

	private static final long serialVersionUID = -2053800594583879853L;

	/** 内容 */
	private List<T> content = new ArrayList<T>();

	/** 总记录数 */
	private long total;

	/** 分页信息 */
	private Pageable pageable;
	
	private int beginPageIndex; // 页码列表的开始索引
	
	private int endPageIndex; // 页码列表的结束索引
	
	/**
	 * 初始化一个新创建的Page对象
	 */
	public Page() {
		this.total = 0L;
		this.pageable = new Pageable();
	}
	
	/**
	 * @param content
	 *            内容
	 * @param total
	 *            总记录数
	 * @param pageable
	 *            分页信息
	 */
	public Page(List<T> content, long total, Pageable pageable) {
		this.content.addAll(content);
		this.total = total;
		this.pageable = pageable;
		
		int pageCount=getTotalPages();
		// 一、总页码不大于10页
		if (pageCount <= 10) {
			// >> 全部显示
			beginPageIndex = 1;
			endPageIndex = pageCount;
		}
		// 二、总页码大于10页
		else {
			// >> 默认显示当前附近的共10个页码（前4个 + 当前页 + 后5个）
			beginPageIndex = getPageNumber() - 4;  // 7 - 4 = 3
			endPageIndex = getPageNumber() + 5;  // 7 + 5 = 12
			
			// >> 前面不足4个时，显示前10个页码
			if(beginPageIndex < 1){
				beginPageIndex = 1;
				endPageIndex = 10;
			}
			// >> 后面不足5个时，显示后10个页码
			else if(endPageIndex > pageCount ){
				beginPageIndex = pageCount - 9; // 共13页，12 - 10 = 2
				endPageIndex = pageCount;
			}
		}
	}

	/**
	 * 获取页码
	 * 
	 * @return 页码
	 */
	public int getPageNumber() {
		return pageable.getPageNumber();
	}

	/**
	 * 获取每页记录数
	 * 
	 * @return 每页记录数
	 */
	public int getPageSize() {
		return pageable.getPageSize();
	}

	/**
	 * 获取搜索属性
	 * 
	 * @return 搜索属性
	 */
	public String getSearchProperty() {
		return pageable.getSearchProperty();
	}

	/**
	 * 获取搜索值
	 * 
	 * @return 搜索值
	 */
	public String getSearchValue() {
		return pageable.getSearchValue();
	}

	/**
	 * 获取排序属性
	 * 
	 * @return 排序属性
	 */
	public String getOrderProperty() {
		return pageable.getOrderProperty();
	}

	/**
	 * 获取排序方向
	 * 
	 * @return 排序方向
	 */
	public Direction getOrderDirection() {
		return pageable.getOrderDirection();
	}

	/**
	 * 获取排序
	 * 
	 * @return 排序
	 */
	public List<QueryOrder> getOrders() {
		return pageable.getOrders();
	}

	/**
	 * 获取筛选
	 * 
	 * @return 筛选
	 */
	public List<QueryFilter> getFilters() {
		return pageable.getFilters();
	}

	/**
	 * 获取总页数
	 * 
	 * @return 总页数
	 */
	public int getTotalPages() {
		return (int) Math.ceil((double) getTotal() / (double) getPageSize());
	}

	/**
	 * 获取内容
	 * 
	 * @return 内容
	 */
	public List<T> getContent() {
		return content;
	}
	
	/**
	 * 获取内容
	 * 
	 * @return 内容
	 */
	public void setContent(List<T> content) {
		this.content=content;
	}

	/**
	 * 获取总记录数
	 * 
	 * @return 总记录数
	 */
	public long getTotal() {
		return total;
	}
	
	/**
	 * 设置总记录数
	 * 
	 * @return 总记录数
	 */
	public void setTotal(long total) {
		this.total=total;
	}

	/**
	 * 获取分页信息
	 * 
	 * @return 分页信息
	 */
	public Pageable getPageable() {
		return pageable;
	}
	
	/**
	 * 设置分页信息
	 * 
	 * @return 分页信息
	 */
	public void setPageable(Pageable pageable) {
		this.pageable=pageable;
	}
	
	
	public int getBeginPageIndex() {
		return beginPageIndex;
	}

	public void setBeginPageIndex(int beginPageIndex) {
		this.beginPageIndex = beginPageIndex;
	}

	
	public int getEndPageIndex() {
		return endPageIndex;
	}

	public void setEndPageIndex(int endPageIndex) {
		this.endPageIndex = endPageIndex;
	}

	

}
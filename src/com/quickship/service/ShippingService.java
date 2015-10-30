package com.quickship.service;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.fedex.rate.stub.RateReply;
import com.fedex.ship.stub.ProcessShipmentReply;
import com.fedex.ship.stub.ShipmentRating;
import com.quickship.Income;
import com.quickship.Page;
import com.quickship.Pageable;
import com.quickship.entity.Member;
import com.quickship.entity.Shipping;
import com.quickship.entity.Shipping.ReconcileType;
import com.quickship.service.base.BaseDao;
import com.quickship.service.impl.ShippingServiceImpl.MoneyType;

public interface ShippingService extends BaseDao<Shipping,Long>{
	
	/**
	 * 获取发货单列表
	 * @param member 用户
	 * @param isSeaved  是否常用
	 * @return
	 */
	List<Shipping> findShippings(Member member,Boolean isSeaved);
	
	
	/**
	 * 获取发货类表分页
	 * @param member 用户
	 * @param pageabel 分页信息
	 * @return
	 */
	Page<Shipping> findPage(Member member,Pageable pageable);
	
	/**
	 * 
	 * @param TrackNo 
	 * @return
	 */
	Shipping findByTrackNo(String TrackNo);
	
	/**
	 * 
	 * @param reconcileType
	 * @return
	 */
	List<Shipping> findList(ReconcileType reconcileType);
	
	/**
	 * 构建多个发货单
	 * @param shipping 发货清单
	 * @param member 会员
	 */
	List<ProcessShipmentReply> buildShippings(Shipping shipping);
	
	/**
	 * 构建发货单
	 * @param shipping 发货清单
	 * @param member 会员
	 */
	ProcessShipmentReply buildShipping(Shipping shipping);
	
	/**
	 * 发货服务   计算单个费用
	 * @param reply
	 * @return
	 */
	BigDecimal calcuCharge(ShipmentRating rating,MoneyType type);
	
	
	/**
	 * 汇率服务  计算价格 
	 * @param reply
	 * @return
	 */
	List<RateReply> calcuRate(Shipping shipping);
	
	/**
	 * 发货打印标签
	 * @param reply Fedex replys
	 * @param member 会员
	 * @param request reqeust
	 * @return
	 */
	File shipping(ProcessShipmentReply reply,Shipping shipping,Member member,HttpServletRequest request);
	
	/**
	 * 发货打印标签
	 * @param listReply
	 * @param shipping
	 * @param member
	 * @param request
	 * @return
	 */
	List<File> shipping(List<ProcessShipmentReply> listReply, Shipping shipping, Member member, HttpServletRequest request);
	
	/**
	 * 
	 * @param beginDate
	 * @param endDate
	 * @param email
	 * @param pageable
	 * @return
	 */
	Page<Shipping> findPageIncome(Date beginDate,Date endDate,Member member,Pageable pageable);
	
	
	/**
	 * 计算收入
	 * @param beginDate 开始时间
	 * @param endDate 结束时间
	 * @param email 会员email
	 * @return Income
	 */
	Income calcuIncome(Date beginDate,Date endDate,Member member);
	
}

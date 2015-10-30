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
	 * ��ȡ�������б�
	 * @param member �û�
	 * @param isSeaved  �Ƿ���
	 * @return
	 */
	List<Shipping> findShippings(Member member,Boolean isSeaved);
	
	
	/**
	 * ��ȡ��������ҳ
	 * @param member �û�
	 * @param pageabel ��ҳ��Ϣ
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
	 * �������������
	 * @param shipping �����嵥
	 * @param member ��Ա
	 */
	List<ProcessShipmentReply> buildShippings(Shipping shipping);
	
	/**
	 * ����������
	 * @param shipping �����嵥
	 * @param member ��Ա
	 */
	ProcessShipmentReply buildShipping(Shipping shipping);
	
	/**
	 * ��������   ���㵥������
	 * @param reply
	 * @return
	 */
	BigDecimal calcuCharge(ShipmentRating rating,MoneyType type);
	
	
	/**
	 * ���ʷ���  ����۸� 
	 * @param reply
	 * @return
	 */
	List<RateReply> calcuRate(Shipping shipping);
	
	/**
	 * ������ӡ��ǩ
	 * @param reply Fedex replys
	 * @param member ��Ա
	 * @param request reqeust
	 * @return
	 */
	File shipping(ProcessShipmentReply reply,Shipping shipping,Member member,HttpServletRequest request);
	
	/**
	 * ������ӡ��ǩ
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
	 * ��������
	 * @param beginDate ��ʼʱ��
	 * @param endDate ����ʱ��
	 * @param email ��Աemail
	 * @return Income
	 */
	Income calcuIncome(Date beginDate,Date endDate,Member member);
	
}

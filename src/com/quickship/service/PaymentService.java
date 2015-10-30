package com.quickship.service;

import com.quickship.entity.Payment;
import com.quickship.service.base.BaseDao;

public interface PaymentService extends BaseDao<Payment, Long>{
	/**
	 * ��ȡPayment
	 * @param sn
	 * @return
	 */
	public Payment findPaymentBySN(String sn);
	
	/**
	 * ֧������
	 * 
	 * @param payment
	 *            �տ
	 */
	public void handle(Payment payment);
	
}

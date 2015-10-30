package com.quickship.service;

import com.quickship.entity.Payment;
import com.quickship.service.base.BaseDao;

public interface PaymentService extends BaseDao<Payment, Long>{
	/**
	 * 获取Payment
	 * @param sn
	 * @return
	 */
	public Payment findPaymentBySN(String sn);
	
	/**
	 * 支付处理
	 * 
	 * @param payment
	 *            收款单
	 */
	public void handle(Payment payment);
	
}

package com.quickship.service;

import java.util.List;

import com.quickship.paymentPlugin.PaymentPlugin;

public interface PaymentPluginService{
	
	
	
	/**
	 * ��ȡ֧�����
	 * 
	 * @return ֧�����
	 */
	List<PaymentPlugin> getPaymentPlugins();
	
	
	/**
	 * ��ȡ֧�����
	 * @param pluginId ֧���������
	 * @return
	 */
	PaymentPlugin getPaymentPlugin(String id);
}

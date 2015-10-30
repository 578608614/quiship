package com.quickship.service;

import java.util.List;

import com.quickship.paymentPlugin.PaymentPlugin;

public interface PaymentPluginService{
	
	
	
	/**
	 * 获取支付插件
	 * 
	 * @return 支付插件
	 */
	List<PaymentPlugin> getPaymentPlugins();
	
	
	/**
	 * 获取支付插件
	 * @param pluginId 支付插件类名
	 * @return
	 */
	PaymentPlugin getPaymentPlugin(String id);
}

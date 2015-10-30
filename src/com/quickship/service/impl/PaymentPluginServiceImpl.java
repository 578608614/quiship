package com.quickship.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.quickship.paymentPlugin.PaymentPlugin;
import com.quickship.service.PaymentPluginService;

@Service("paymentPluginServiceImpl")
public class PaymentPluginServiceImpl implements PaymentPluginService {
	
	Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private List<PaymentPlugin> paymentPlugins = new ArrayList<PaymentPlugin>();
	
	@Resource
	private Map<String, PaymentPlugin> paymentPluginMap = new HashMap<String, PaymentPlugin>();
	
	
	public List<PaymentPlugin> getPaymentPlugins() {
		return paymentPlugins;
	}
	
	public PaymentPlugin getPaymentPlugin(String id) {
		return paymentPluginMap.get(id);
	}
	
	
	
}

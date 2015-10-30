package com.quickship.paymentPlugin.paypalPlugin;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.quickship.entity.Payment;
import com.quickship.paymentPlugin.PaymentPlugin;
@Component("paypalPlugin")
public class PaypalPlugin extends PaymentPlugin{
	
	@Value("${paypal.partner}")
	private String  partner;
	@Value("${paypal.currency}")
	private String currency;
	
	@Value("${paypal.paymentName}")
	private String paymentName;
	@Value("${paypal.feeType}")
	private FeeType feeType;
	@Value("${paypal.fee}")
	private BigDecimal fee;
	@Value("${paypal.logo}")
	private String logo;
	
	public enum Currency{
		/** 美元 */
		USD,

		/** 澳大利亚元 */
		AUD,

		/** 加拿大元 */
		CAD,

		/** 捷克克郎 */
		CZK,

		/** 丹麦克朗 */
		DKK,

		/** 欧元 */
		EUR,

		/** 港元 */
		HKD,

		/** 匈牙利福林 */
		HUF,

		/** 新西兰元 */
		NZD,

		/** 挪威克朗 */
		NOK,

		/** 波兰兹罗提 */
		PLN,

		/** 英镑 */
		GBP,

		/** 新加坡元 */
		SGD,

		/** 瑞典克朗 */
		SEK,

		/** 瑞士法郎 */
		CHF,

		/** 日元 */
		JPY
	}
	
	
	public String getPaymentName() {
		return paymentName;
	}

	public FeeType getFeeType() {
		return feeType;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public String getLogo() {
		return logo;
	}
	
	public String getName() {
		return "Paypal";
	}
	
	public void setPartner(String partner) {
		this.partner = partner;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public void setPaymentName(String paymentName) {
		this.paymentName = paymentName;
	}

	public void setFeeType(FeeType feeType) {
		this.feeType = feeType;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	@Override
	public String getRequestUrl(){
		return "https://www.paypal.com/cgi-bin/webscr";
	}

	@Override
	public RequestMethod getRequestMethod() {
		return RequestMethod.post;
	}

	@Override
	public Map<String, Object> getParameterMap(String sn, String description, HttpServletRequest request) {
		Payment payment = getPayment(sn);
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("cmd","_xclick");
		paramMap.put("business",partner);
		paramMap.put("item_name", StringUtils.abbreviate(description.replaceAll("[^0-9a-zA-Z\\u4e00-\\u9fa5 ]", ""), 100));
		paramMap.put("amount", payment.getAmount().setScale(2).toString());
		paramMap.put("currency_code", currency);
		paramMap.put("return", getNotifyUrl(sn, NotifyMethod.sync));
		paramMap.put("notify_url", getNotifyUrl(sn, NotifyMethod.async));
		paramMap.put("invoice", sn);
		paramMap.put("charset", "UTF-8");
		paramMap.put("no_shipping", "1");
		paramMap.put("no_note", "0");
		paramMap.put("rm", "2");
		paramMap.put("custom", "quiship");
		return paramMap;
	}
	@SuppressWarnings("unchecked")
	@Override
	public boolean verifyNotify(String sn, NotifyMethod notifyMethod, HttpServletRequest request) {
		Payment payment = getPayment(sn);
		if(partner.equals(request.getParameter("receiver_email")) && sn.equals(request.getParameter("invoice")) && currency.equals(request.getParameter("mc_currency")) && "Completed".equals(request.getParameter("payment_status")) && payment.getAmount().compareTo(new BigDecimal(request.getParameter("mc_gross"))) == 0) {
			Map<String, Object> parameterMap = new LinkedHashMap<String, Object>();
			parameterMap.put("cmd", "_notify-validate");
			parameterMap.putAll(request.getParameterMap());
			String vali = post("https://www.paypal.com/cgi-bin/webscr", parameterMap);
			System.out.println(vali);
			if ("VERIFIED".equals(vali)) {
				return true;
			}
			return true;
		}
		return true;
	}

	@Override
	public String getNotifyMessage(String sn, NotifyMethod notifyMethod, HttpServletRequest request) {
		
		return null;
	}

	@Override
	public Integer getTimeout() {
		// TODO Auto-generated method stub
		return null;
	}



}

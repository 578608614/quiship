package com.quickship.paymentPlugin;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import com.quickship.entity.Payment;
import com.quickship.service.PaymentService;
@Component("paymentPlugin")
public abstract class PaymentPlugin {

	/** ֧����ʽ������������ */
	public static final String PAYMENT_NAME = "paymentName";
	/** ������������������ */
	public static final String FEE_TYPE = "feeType";
	/** �������������� */
	public static final String FEE = "fee";

	public static final String LOGO = "logo";

	public static final String DESCRIPTION = "description";
	/**
	 * ����������
	 */
	public enum FeeType {
		/** �������շ� */
		scale,
		
		/** �̶��շ� */
		fixed
	}
	/**
	 * ���󷽷�
	 */
	public enum RequestMethod{
		/** POST */
		post,
		/** GET */
		get
	}
	/**
	 * ֪ͨ����
	 */
	public enum NotifyMethod{
		/** ͨ�� */
		general,
		/** ͬ�� */
		sync,
		/** �첽 */
		async
	}
	@Resource(name = "paymentServiceImpl")
	private PaymentService paymentService;
	
	/**
	 * ��ȡID
	 * 
	 * @return ID
	 */
	public final String getId(){
		return getClass().getAnnotation(Component.class).value();
	}
	/**
	 * ��ȡ��ַ
	 * 
	 * @return ��ַ
	 */
	public String getSiteUrl(){
//		return "http://www.quiship.com";
		return "http://localhost:8080/quickship";
	}
	/**
	 * ��ȡ����
	 * 
	 * @return ����
	 */
	public abstract String getName();	
	/**
	 * ��ȡ����URL
	 * 
	 * @return ����URL
	 */
	public abstract String getRequestUrl();

	/**
	 * ��ȡ���󷽷�
	 * 
	 * @return ���󷽷�
	 */
	public abstract RequestMethod getRequestMethod();

	/**
	 * ��ȡ�����ַ�����
	 * 
	 * @return �����ַ�����
	 */
	public String getRequestCharset(){
		return "UTF-8";
	}

	/**
	 * ��ȡ�������
	 * 
	 * @param sn
	 *            ���
	 * @param description
	 *            ����
	 * @param request
	 *            httpServletRequest
	 * @return �������
	 */
	public abstract Map<String, Object> getParameterMap(String sn, String description, HttpServletRequest request);

	/**
	 * ��֤֪ͨ�Ƿ�Ϸ�
	 * 
	 * @param sn
	 *            ���
	 * @param notifyMethod
	 *            ֪ͨ����
	 * @param request
	 *            httpServletRequest
	 * @return ֪ͨ�Ƿ�Ϸ�
	 */
	public abstract boolean verifyNotify(String sn, NotifyMethod notifyMethod, HttpServletRequest request);

	/**
	 * ��ȡ֪ͨ������Ϣ
	 * 
	 * @param sn
	 *            ���
	 * @param notifyMethod
	 *            ֪ͨ����
	 * @param request
	 *            httpServletRequest
	 * @return ֪ͨ������Ϣ
	 */
	public abstract String getNotifyMessage(String sn, NotifyMethod notifyMethod, HttpServletRequest request);

	/**
	 * ��ȡ��ʱʱ��
	 * 
	 * @return ��ʱʱ��
	 */
	public abstract Integer getTimeout();
	

	/**
	 * ��ȡ֧����ʽ����
	 * 
	 * @return ֧����ʽ����
	 */
	public abstract String getPaymentName();
	/**
	 * ��ȡ����������
	 * 
	 * @return ����������
	 */
	public abstract FeeType getFeeType();
	/**
	 * ��ȡ������
	 * 
	 * @return ������
	 */
	public abstract BigDecimal getFee();
	
	/**
	 * ��ȡLOGO
	 * 
	 * @return LOGO
	 */
	public abstract String getLogo();
	/**
	 * ����֧��������
	 * 
	 * @param amount
	 *            ���
	 * @return ֧��������
	 */
	public BigDecimal calculateFee(BigDecimal amount) {
		BigDecimal fee;
		if (getFeeType() == FeeType.scale) {
			fee = amount.multiply(getFee());
		} else {
			fee = getFee();
		}
		return fee.setScale(2,BigDecimal.ROUND_HALF_UP);
	}
	
	/**
	 * ����֧�����
	 * 
	 * @param amount
	 *            ���
	 * @return ֧�����
	 */
	public BigDecimal calculateAmount(BigDecimal amount){
		return amount.add(calculateFee(amount)).setScale(2, RoundingMode.UP);
	}
	
	/**
	 * ���ݱ�Ų���֧����
	 *
	 * @param sn
	 *            ���(���Դ�Сд)
	 * @return ֧���������������򷵻�null
	 */
	protected Payment getPayment(String sn){
		return paymentService.findPaymentBySN(sn);
	}
	
	/**
	 * ��ȡ֪ͨURL
	 * 
	 * @param sn
	 *            ���
	 * @param notifyMethod
	 *            ֪ͨ����
	 * @return ֪ͨURL
	 */
	protected String getNotifyUrl(String sn,NotifyMethod notifyMethod){
		if(notifyMethod==null){
			return getSiteUrl()+"/payment/notify/"+NotifyMethod.general+"/"+sn+".html";
		}
		return getSiteUrl()+"/payment/notify/" + notifyMethod + "/" + sn + ".html";
	}
	
	/**
	 * POST����
	 * 
	 * @param url
	 *            URL
	 * @param parameterMap
	 *            �������
	 * @return ���ؽ��
	 */
	protected String post(String url, Map<String,Object> parameterMap){
		String result = null;
		HttpClient httpClient = new DefaultHttpClient();
		try{
			HttpPost httpPost = new HttpPost(url);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			if (parameterMap != null) {
				for (Entry<String, Object> entry : parameterMap.entrySet()) {
					String name = entry.getKey();
					String value = ConvertUtils.convert(entry.getValue());
					if (StringUtils.isNotEmpty(name)) {
						nameValuePairs.add(new BasicNameValuePair(name, value));
					}
				}
			}
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			result = EntityUtils.toString(httpEntity);
			EntityUtils.consume(httpEntity);
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * GET����
	 * 
	 * @param url
	 *            URL
	 * @param parameterMap
	 *            �������
	 * @return ���ؽ��
	 */
	protected String get(String url,Map<String,Object> parameterMap){
		String result = null;
		HttpClient httpClient = new DefaultHttpClient();
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			if (parameterMap != null) {
				for (Entry<String, Object> entry : parameterMap.entrySet()) {
					String name = entry.getKey();
					String value = ConvertUtils.convert(entry.getValue());
					if (StringUtils.isNotEmpty(name)) {
						nameValuePairs.add(new BasicNameValuePair(name, value));
					}
				}
			}
			HttpGet httpGet = new HttpGet(url + (StringUtils.contains(url, "?") ? "&" : "?") + EntityUtils.toString(new UrlEncodedFormEntity(nameValuePairs, "UTF-8")));
			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			result = EntityUtils.toString(httpEntity);
			EntityUtils.consume(httpEntity);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		return result;
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
		PaymentPlugin other = (PaymentPlugin) obj;
		return new EqualsBuilder().append(getId(), other.getId()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(getId()).toHashCode();
	}
}

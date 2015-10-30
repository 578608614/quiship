package com.quickship.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

public class HttpClientUtil {
	private static Logger logger = Logger.getLogger(HttpClientUtil.class);
	private static HttpResponse currentResponse = null;
	private static DefaultHttpClient httpClient = null;
	public static String sessionId = null;
	public static boolean isCreateNewClient = false;
	private HttpClientUtil() {
	}

	/**
	 * 发送 get 请求
	 * 
	 * @param url
	 *            请求地址
	 * @param cookie
	 *            请求Cookie
	 * @param isSSL
	 *            是否是Https请求
	 * @param encode
	 *            编码格式
	 * @param message
	 *            请求描述日志
	 * @return
	 */
	private static String doGet(String url, String cookie, boolean isSSL,boolean isRequestIntercepter, String encode, String message) {
		if(httpClient==null){
			httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY); // 设置cookie的
		}
		if(isRequestIntercepter){
			httpClient.addRequestInterceptor(new HttpRequestInterceptor() {
				public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
					if (!request.containsHeader("Accept")) {
						request.addHeader("Accept", "*/*");
					}
					if (request.containsHeader("User-Agent")) {
						request.removeHeaders("User-Agent");
					}
					if (request.containsHeader("Connection")) {
						request.removeHeaders("Connection");
					}
					request.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.27 Safari/537.36");
					request.addHeader("Host", "www.fedex.com");
					request.addHeader("Connection", "keep-alive");
					request.addHeader("Cache-Control", "max-age=0");
					request.addHeader("Accept-Language", "zh-CN,zh;q=0.8");
				}
			});
		}else{
			httpClient.addRequestInterceptor(new HttpRequestInterceptor() {
				public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
					request.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.27 Safari/537.36");
					request.addHeader("Host", "www.fedex.com");
					request.addHeader("Connection", "keep-alive");
					request.addHeader("Cache-Control", "max-age=0");
					request.addHeader("Accept-Language", "zh-CN,zh;q=0.8");
				}
			});
		}
		HttpGet httpGet = new HttpGet(url);
		String content = null;
		// 设置Cookie
		if (null != cookie && !"".equals(cookie)) {
			httpGet.addHeader("Cookie", cookie);
		}
		if (encode == null) {
			encode = "UTF-8";
		}
		try {
			// 设置SSL
			if (isSSL)buildSSLClient(httpClient);
			// 发送请求
			currentResponse = httpClient.execute(httpGet);
			// 打印日志
			printLog(message);
			content = EntityUtils.toString(currentResponse.getEntity(), encode);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpGet.abort();
			if(isCreateNewClient){
				httpClient.getConnectionManager().shutdown();
				httpClient=null;
			}
		}
		return content;
	}

	/**
	 * 没有cookie 最简单的http get请求 
	 * 
	 * @param url
	 * @param message
	 * @return
	 */
	public static String doGet(String url,boolean isRequestIntercepter, String message) {
		return doGet(url, null, false,isRequestIntercepter, null, message);
	}

	/**
	 * 带有cookie 的http get请求
	 * 
	 * @param url
	 * @param message
	 * @return
	 */
	public static String doGet(String url, String cookie, boolean isRequestIntercepter,String message) {
		return doGet(url, cookie, false,isRequestIntercepter, null, message);
	}

	/**
	 * 没有cookie https get请求
	 * 
	 * @param url
	 * @param message
	 * @return
	 */
	public static String doSSLGet(String url, String message) {
		return doGet(url, null, true,true, null, message);
	}

	/**
	 * 带cookie https get请求
	 * 
	 * @param url
	 * @param message
	 * @return
	 */
	public static String doSSLGet(String url, String cookie, String message) {
		return doGet(url, cookie, true, true,null, message);
	}

	/**
	 * 发送 post 请求
	 * 
	 * @param url
	 *            请求地址
	 * @param map
	 *            请求参数
	 * @param cookie
	 *            请求Cookie
	 * @param isSSL
	 *            是否Https请求
	 * @param encode
	 *            编码格式
	 * @param message
	 *            请求描述日志
	 * @return
	 */
	private static String doPost(String url, Map<String, String> map, String cookie, boolean isSSL, final boolean isAjax, String encode, String message) {
		if(httpClient==null){
			httpClient = new DefaultHttpClient();
			httpClient.addRequestInterceptor(new HttpRequestInterceptor() {
				public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
					if (!request.containsHeader("Accept")) {
						request.addHeader("Accept", "*/*");
					}
					if (request.containsHeader("User-Agent")) {
						request.removeHeaders("User-Agent");
					}
					if (request.containsHeader("Connection")) {
						request.removeHeaders("Connection");
					}
					request.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.27 Safari/537.36");
					request.addHeader("Host", "www.fedex.com");
					request.addHeader("Connection", "keep-alive");
					request.addHeader("Cache-Control", "max-age=0");
					request.addHeader("Accept-Language", "zh-CN,zh;q=0.8");
//					request.addHeader("Referer", "https://www.fedex.com");
//					request.addHeader("Accept-Encoding","gzip,deflate,sdch");
//					request.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
				}
			});
		}
		
		HttpPost httpPost = new HttpPost(url);
		String content = null;
		if (encode == null) {
			encode = "UTF-8";
		}
		if (null != cookie && !"".equals(cookie)) {
			httpPost.addHeader("Cookie", cookie);
		}
		try {
			// 设置参数
			if (map != null && map.size() > 0) {
				List<NameValuePair> formParams = new ArrayList<NameValuePair>();
				for (Map.Entry<String, String> entry : map.entrySet()) {
					formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
				}
				httpPost.setEntity(new UrlEncodedFormEntity(formParams, encode));
			}
			// 设置SSL
			if (isSSL)
				buildSSLClient(httpClient);
			// 发送请求
			currentResponse = httpClient.execute(httpPost);
			// 打印日志
			printLog(message);
			content = EntityUtils.toString(currentResponse.getEntity(), encode);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpPost.abort();
			if(isCreateNewClient){
				httpClient.getConnectionManager().shutdown();
				httpClient=null;
			}
			
		}
		return content;
	}
	public static void closeClient(){
		if(httpClient!=null){
			httpClient.getConnectionManager().shutdown();
		}
		httpClient=null;
	}
	
	/**
	 * 没有cookie 最简单的http post请求 
	 * @param url 请求地址
	 * @param mapForm map型表单
	 * @param message 请求描述日志
	 * @return
	 */
	public static String doPost(String url,Map<String,String> mapForm,String message){
		return doPost(url,mapForm,null,false,false,null,message);
	}
	/**
	 * 带cookie 的http post请求 
	 * @param url 请求地址
	 * @param mapForm map型表单
	 * @param cookie cookie值
	 * @param message 请求描述日志
	 * @return
	 */
	public static String doPost(String url,Map<String,String> mapForm,String cookie,String message){
		return doPost(url,mapForm,cookie,false,false,null,message);
	}
	/**
	 * 没有Cookie 的https post请求
	 * @param url 请求地址
	 * @param mapForm map型表单
	 * @param message 请求描述日志
	 * @return
	 */
	public static String doSSLPost(String url,Map<String,String> mapForm,String message){
		return doPost(url,mapForm,null,true,false,null,message);
	}
	/**
	 * 带cookie 的https post请求 
	 * @param url 请求地址
	 * @param mapForm map型表单
	 * @param cookie cookie值
	 * @param message 请求描述日志
	 * @return
	 */
	public static String doSSLPost(String url,Map<String,String> mapForm,String cookie,String message){
		return doPost(url,mapForm,cookie,true,false,null,message);
	}
	
	public static String doAjax(String url,Map<String,String> mapForm,String cookie,String message){
		return doPost(url,mapForm,cookie,false,true,null,message);
	}
	
	/**
	 * 从当前的Response中 取重定向地址
	 * 
	 * @param isPrint
	 *            是否答应响应头信息
	 * @return
	 */
	public static String checkLocation(boolean isPrint) {
		String location = null;
		Header[] headers = currentResponse.getAllHeaders();
		int i = 0;
		while (i < headers.length) {
			if (isPrint) {
				logger.info(headers[i].getName() + ":" + headers[i].getValue());
			}
			if (headers[i].getName().equals("Location")) {
				location = headers[i].getValue();
			}
			++i;
		}

		return location;
	}

	/**
	 * 从当前的Response中 取Cookie
	 * 
	 * @param cookie
	 *            原有Cookie
	 * @param startIndex
	 *            第一个Cookie索引
	 * @param endIndex
	 *            最后一个Cookie索引
	 * @param isPrint
	 *            是否打印
	 * @return
	 */
	public static String checkCookie(String cookie,boolean isPrint) {
		if (cookie != null && !cookie.equals("")) {
			cookie = cookie + ";";
		}
		List<Cookie> list = httpClient.getCookieStore().getCookies();
		for(Cookie c : list){
			System.out.println(c.getName()+"=="+c.getValue());
			if("ice.sessions".equals(c.getName())){
				sessionId=c.getValue().substring(0, c.getValue().length()-2);
				System.out.println(sessionId);
			}
			if (!"\"\"".equals(c.getValue())) {
				cookie = cookie + c.getName() + "=" + c.getValue() + ";";
			}
		}
		cookie = cookie.substring(0, cookie.length() - 1);
		if (isPrint) {
			logger.info(cookie);
		}
		return cookie;

	}
	/**
	 * 构建SSL 客户端
	 * 
	 * @param httpClient
	 * @throws Exception
	 */
	private static void buildSSLClient(HttpClient httpClient) throws Exception {
		TrustManager easyTrustManager = new X509TrustManager() {
			public void checkClientTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws java.security.cert.CertificateException {
			}

			public void checkServerTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws java.security.cert.CertificateException {
			}

			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new java.security.cert.X509Certificate[0];
			}
		};
		SSLContext ctx = null;
		ctx = SSLContext.getInstance("TLS");
		ctx.init(null, new TrustManager[] { easyTrustManager }, null);
		SSLSocketFactory socketFactory = new SSLSocketFactory(ctx);
		httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 443, socketFactory));
	}

	/**
	 * 输出日志
	 * 
	 * @param response
	 * @param message
	 */
	private static void printLog(String message) {
		int statusCode = currentResponse.getStatusLine().getStatusCode();
		if (message == null) {
			message = "knowRequest";
		}
		if (statusCode == HttpStatus.SC_OK) {
			System.out.println("******" + message + ":" + statusCode + "success");
			logger.info("******" + message + ":" + statusCode + "success");
		} else if (statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
			System.out.println("******" + message + ":" + statusCode + "notcontent");
			logger.info("******" + message + ":" + statusCode + "notcontent");
		} else {
			System.out.println("******" + message + ":" + statusCode + "error");
			logger.error("******" + message + ":" + statusCode + "error");
		}
	}
	
	
	public static boolean isSuccess(){
		boolean flag =  false;
		int statusCode = currentResponse.getStatusLine().getStatusCode();
		if (statusCode == HttpStatus.SC_OK||statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
			flag = true;
		}
		return flag;
	}
	
}

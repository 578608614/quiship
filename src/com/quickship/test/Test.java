package com.quickship.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.quickship.entity.Member;

public class Test {
	
	@org.junit.Test
	public void daoTest(){
		Member member =new Member();
		member.setFirstName("name2222");
		member.setPassword("123456");
		try{
			ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml"); 
//			MemberService ser =(MemberService)ctx.getBean("memberServiceImpl");
//			ser.save(member);
//			Member m = ser.find(1l);
//			System.out.println(m.getEmail());
////			ser.update(m);
//			QueryFilter filter = new QueryFilter("firstName",Operator.ne,"name11111");
//			List<QueryFilter> l = new ArrayList<QueryFilter>();
//			l.add(filter);
//			System.out.println(ser.exists(4l));

		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private static String userName="pxx106";
	private static String password="Lovetiantian8";
	private static String loginURL="https://www.fedex.com/fcl/logon.do?appName=fclfsm&locale=us_en&amp;step3URL=https%3A%2F%2Fwww.fedex.com%2Fship%2FshipEntryAction.do?method=doRegistration%26locale=en_us%26urlparams=us%26sType=F%26action=fsmregister&amp;afterwardsURL=https%3A%2F%2Fwww.fedex.com%2Fship%2FshipEntryAction.do?method=doEntry%26locale=en_us%26urlparams=us%26sType=F&programIndicator=1";

	public static void main(String args[]) throws Exception{
//			String cookie="",content=null;	
//			Map<String,String> mapForm = new HashMap<String,String>();
//			mapForm.put("appName","fclfsm");
//			mapForm.put("locale","us_en");
//			mapForm.put("step3URL","https%3A%2F%2Fwww.fedex.com%2Fship%2FshipEntryAction.do?method=doRegistration%26locale=en_us%26urlparams=us%26sType=F%26action=fsmregister");
//			mapForm.put("afterwardsURL","https%3A%2F%2Fwww.fedex.com%2Fship%2FshipEntryAction.do?method=doEntry%26locale=en_us%26urlparams=us%26sType=F");
//			mapForm.put("returnurl","https%3A%2F%2Fwww.fedex.com%2Fship%2FshipEntryAction.do?method=doEntry%26locale=en_us%26urlparams=us%26sType=F");
//			mapForm.put("fromLoginPage","yes");
//			mapForm.put("cc_lang","us");
//			mapForm.put("registrationType","logon");
//			mapForm.put("ssoguest","n");
//			mapForm.put("steps","2");
//			mapForm.put("username",userName);
//			mapForm.put("password",password);
//			mapForm.put("startpage","FSM");
//			
////			content = HttpClientUtil.doPost(loginURL, mapForm, "login");
////			cookie = HttpClientUtil.checkCookie(cookie, true);
//			
//			
//			
//			DefaultHttpClient httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager());
//			HttpPost httpPost = new HttpPost(loginURL);
//			// 设置参数
//			if (mapForm != null && mapForm.size() > 0) {
//				List<NameValuePair> formParams = new ArrayList<NameValuePair>();
//				for (Map.Entry<String, String> entry : mapForm.entrySet()) {
//					formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
//				}
//				httpPost.setEntity(new UrlEncodedFormEntity(formParams, "UTF-8"));
//			}
//			HttpResponse response = httpClient.execute(httpPost);
//			content = EntityUtils.toString(response.getEntity(), "UTF-8");
//			httpPost.abort();
//			List<Cookie> list = httpClient.getCookieStore().getCookies();
//			for(Cookie c : list){
//				System.out.println(c.getName()+"=="+c.getValue());
//				if (!"\"\"".equals(c.getValue())) {
//					cookie = cookie + c.getName() + "=" + c.getValue() + ";";
//				}
//			}
//			cookie = cookie.substring(0, cookie.length() - 1);
//			Header[] headers = response.getAllHeaders();
//			for(Header h : headers){
//				System.out.println(h.getName()+":"+h.getValue());
//			}
//			response = httpClient.execute(new HttpGet(response.getHeaders("Location")[0].getValue()));
//			content = EntityUtils.toString(response.getEntity(), "UTF-8");
//			System.out.println("=============================");
//			headers = response.getAllHeaders();
//			for(Header h : headers){
//				System.out.println(h.getName()+":"+h.getValue());
//			}
//			
//			
//			
//			
//			
//////			String local = HttpClientUtil.checkLocation(true);
//////			content = HttpClientUtil.doGet(local, cookie,false,"application");
//////			cookie = HttpClientUtil.checkCookie(cookie, true);
////			String aa = "https://www.fedex.com/fedexbillingonline/application.jsp?";
////			content = HttpClientUtil.doGet(aa, cookie,false,"application");
////			cookie = HttpClientUtil.checkCookie(cookie, true);
////			
//			mapForm.clear();
//			String a2="https://www.fedex.com/fedexbillingonline/block/send-receive-updates";
//			mapForm.put("ice.submit.partial","true");
//			mapForm.put("ice.event.target","mainContentId:advancedSearchwldDataNewSrch");
//			mapForm.put("ice.event.captured","mainContentId:advancedSearchwldDataNewSrch");
//			mapForm.put("ice.event.type","onclick");
//			mapForm.put("ice.event.alt","false");
//			mapForm.put("ice.event.ctrl","false");
//			mapForm.put("ice.event.shift","false");
//			mapForm.put("ice.event.meta","false");
//			mapForm.put("ice.event.x","840");
//			mapForm.put("ice.event.y","833");
//			mapForm.put("ice.event.left","false");
//			mapForm.put("ice.event.right","false");
//			mapForm.put("mainContentId:quickTempl","-1");
////			mapForm.put("mainContentId:j_id204","All");
////			mapForm.put("mainContentId:j_id198","11/26/2014");
////			mapForm.put("mainContentId:j_id196","10/01/2014");
////			mapForm.put("mainContentId:j_id189","485313443");
//			
//			mapForm.put("mainContentId:j_id210","All");
//			mapForm.put("mainContentId:j_id204","11/26/2014");
//			mapForm.put("mainContentId:j_id202","10/01/2014");
//			mapForm.put("mainContentId:j_id193","485313443");
//			
////			mapForm.put("mainContentId:j_id187","on");
//			mapForm.put("mainContentId:advTempl","Invoices");
//			mapForm.put("mainContentId:j_id176","-1");
//			mapForm.put("javax.faces.ViewState","1");
////			mapForm.put("javax.faces.RenderKitId", "ICEfacesRenderKit");
////			mapForm.put("mainContentId","mainContentId");
//			mapForm.put("mainContentId:advancedSearchwldDataNewSrch","Search");
//			mapForm.put("ice.session","o1vQZhoPA94gnx9HhX1oZg");
//			mapForm.put("ice.view","1");
//			mapForm.put("ice.focus","mainContentId:advancedSearchwldDataNewSrch");
//			mapForm.put("rand","0.8442357731983066");
//			cookie="SMIDENTITY=yQ4tJ7jGGYlQHb7f7Riw20ZAGVMEzmyGcUNFXIK4TrWvJdfkYhp4HIcNQNAC/YIwR3JO3thXjxRzs1aX1RAsOkaVMs28EPTys4OZ0aYopk958K0qBygZGZm+dOQcxRH/BknITiyOw1VU06OwpzxpXhveG6BTqNJEsHlHTkXJ05VMdmDMoFshCL7OcUspZvIZDdc5xhE5IGvYgh9ozbbjCNi8JZf9AwBEwos/wzGoBOY0HYfIv5PHeexJqE+yy/7pk2LEu3yFA5+kj5jj16GzafZY/T4mCpHnxeWGDUUItKoC9lSWXt0bPeKyYVeAFwfRte2g78d3VK2w7FYguWrHM6m6h5o6FgWg4dFNgDLITI5RCJHtAfQBeVPz+8F/XNYQR5VbICac/aZfkuiBStalVPontpnySWDPSqERkvEt93NZV2FXw/SBHCjc9Y3HV16Nf+DVZUGNvpFE3TaujHJemJwtpXvf7j7QnIffLr7CbkCZUiAlKiqHcyGCfNdzQ5irqEQqxvCXoYg/WjET1hyJ0IJdxhqiX/8P; FCLSESSIONID=TTh3J2NYmjCFJJTkcjrVR134vRCCQnC4kqjqmTYn1sYh22mtffhL!2139332988; fdx_cbid=30207856121417074433035400255511; IS3_GSV=DPL-0_TES-1417074433_PCT-1417074433_GeoIP-*_GeoCo-_GeoRg-_GeoCt-_GeoNs-_GeoDm-; AMCV_1E22171B520E93BF0A490D44%40AdobeOrg=136688995%7CMCMID%7C30417938248296725762376824344528119294%7CMCAAMLH-1417679234%7C11%7CMCAAMB-1417679234%7CNRX38WO0n5BH8Th-nqAG_A%7CMCAID%7CNONE; loadNewTrack=yes; tracking_locale=en_US; fdx_login=10030.a3df.6bcd9a45; fcl_lowbandwidth=no; fcl_registrationtype=; fcl_contactname=Xin Zhao; fcl_fname=Xin; fcl_uuid=g000gl77ce; SHIPPINGSESSIONID=nSShJ2hTxQ7ZTZQ1q4vSwRJ9HQnD358hYtyVK76HvW2htPkQmBPw!459176943; fsm_locale=en_US; fdx_locale=en_US; fdx_locale_data=acl:zh-cn1.00zh0.80en-us0.60en0.40|ccc:?|pcc:?|plo:en_US|uri:/fedexbillingonline/; GFBOEBISESSIONID=GhtgJ2vbkRpLcvTFyM2112mM1ggplkSGPflhSXc1hbkvhGTXBg9x!-1085867523; ice.sessions=o1vQZhoPA94gnx9HhX1oZg#1; countryPath=fedexbillingonline; mbox=PC#1416186963571-900365.24_12#1418286878|check#true#1417077338|session#1417077271308-852930#1417079138; IS3_History=1416143383-6-38_3--6+13--6+32--6+49--6+5-2-+8-1-+9-1-__3-13-32-49_3-13-32-49; s_cc=true; aam_tnt=aam12%3D886270%2Caam168%3D825538; aam_uuid=30534466516229985052353896025253619863; siteDC=wtc; s_sess=%20Count%3D1%3B%20s_inet_ship_domestic%3D1417075376884%3B%20flg1%3Dtrue%3B%20flg2%3Dtrue%3B%20s_ppv%3Dus%252Fhome%252Ffedex.com%252Fus%252520home%252C100%252C66%252C1504%3B%20setLink%3D%3B%20SC_LINKS%3D%3B; s_pers=%20s_vnum%3D1417104000077%2526vn%253D2%7C1417104000077%3B%20sc_fcl_uuid%3Dg000gl77ce%7C1424853277862%3B%20s_visit%3D1%7C1417080085592%3B%20gpv_pageName%3Dus%252Fen%252Ffedexbillingonline%252Fnewsearchadvancedsearchlabelsrch%7C1417080085595%3B%20s_nr%3D1417078285599-Repeat%7C1448614285599%3B%20s_evar54%3D2%7C1417164685605%3B%20s_evar55%3D1%7C1417164685607%3B%20s_invisit%3Dtrue%7C1417080085609%3B%20s_prevChan%3D%257C1417078285613%7C1418287885613%3B; s_sq=%5B%5BB%5D%5D";
//			
//			httpPost = new HttpPost(a2);
//			// 设置参数
//			if (mapForm != null && mapForm.size() > 0) {
//				List<NameValuePair> formParams = new ArrayList<NameValuePair>();
//				for (Map.Entry<String, String> entry : mapForm.entrySet()) {
//					formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
//				}
//				httpPost.setEntity(new UrlEncodedFormEntity(formParams, "UTF-8"));
//			}
//			
//			httpClient.execute(httpPost);
//			response = httpClient.execute(httpPost);
//			content = EntityUtils.toString(response.getEntity(), "UTF-8");
//			System.out.println("=============================");
//			headers = response.getAllHeaders();
//			for(Header h : headers){
//				System.out.println(h.getName()+":"+h.getValue());
//			}
//			
//			//			content = HttpClientUtil.doPost(a2, mapForm,cookie,"search");
//			
//			
//			writeFile(content);
		doRefund("https://api.mch.weixin.qq.com/secapi/pay/refund","");
		
	}
	
	public static String doRefund(String url,String data) throws Exception {
    	/**
    	 * 注意PKCS12证书 是从微信商户平台-》账户设置-》 API安全 中下载的
    	 */
		data="<xml><appid>wx98c3b54044098430</appid><mch_id>1269991401</mch_id><nonce_str>tgn76widkvpw0fo7</nonce_str><op_user_id>1269991401</op_user_id><out_refund_no>201510128989</out_refund_no><out_trade_no>2015101243632</out_trade_no><refund_fee>1</refund_fee><sign>5789406B7F15F79C06B6DF3FDB17705A</sign><total_fee>1</total_fee></xml>";
    	
        KeyStore keyStore  = KeyStore.getInstance("PKCS12");
        FileInputStream instream = new FileInputStream(new File("C:\\Users\\it-1\\Desktop\\apiclient_cert.p12"));//P12文件目录
        try {
            keyStore.load(instream, "1269991401".toCharArray());//这里写密码..默认是你的MCHID
        }catch(Exception e)
        {
        	e.printStackTrace();
        }
        finally {
            instream.close();
        }

        // Trust own CA and all self-signed certs
        /**
    	 * 此处要改
    	 * */
        SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, "1269991401".toCharArray()).build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext,new String[] { "TLSv1" },null,SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        CloseableHttpClient httpclient= HttpClients.custom().setSSLSocketFactory(sslsf).build();
        try {
        	HttpPost httpost = new HttpPost(url); // 设置响应头信息
        	httpost.addHeader("Connection", "keep-alive");
        	httpost.addHeader("Accept", "*/*");
        	httpost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        	httpost.addHeader("Host", "api.mch.weixin.qq.com");
        	httpost.addHeader("X-Requested-With", "XMLHttpRequest");
        	httpost.addHeader("Cache-Control", "max-age=0");
        	httpost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
    		httpost.setEntity(new StringEntity(data, "UTF-8"));
            CloseableHttpResponse response = httpclient.execute(httpost);
            try {
                HttpEntity entity = response.getEntity();
                String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
                EntityUtils.consume(entity);
               return jsonStr;
            } finally {
                response.close();
            }
        }catch(Exception e){
        	e.printStackTrace();
        } finally {
            httpclient.close();
        }
        return null;
    }
	public static void writeFile(String content) {
		if (content == null || "".equals(content)) {
			System.out.println("内容为空");
			return;
		}
		Writer r = null;
		try {
			File file = new File("C:/Users/it-1/Desktop/test.html");
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();
			r = new FileWriter(file);
			r.write(content);
			r.flush();
			System.out.println("输出文件完成");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (r != null) {
				try {
					r.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static Map<String, String> eleFormToMap(Element form) {
		Map<String, String> map = new HashMap<String, String>();
		Elements hiddens = form.select("input[type=hidden]");
		String name = null;
		for (Element hidden : hiddens) {
			name = hidden.attr("name");
			if (name != null && !"".equals(name)) {
				System.out.println(name+":"+hidden.attr("value"));
				map.put(name, hidden.attr("value"));
			}

		}
		return map;
	}
	
}

package com.quickship.controller;

import java.math.BigDecimal;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.quickship.entity.Member;
import com.quickship.entity.Payment;
import com.quickship.entity.Payment.Method;
import com.quickship.entity.Payment.Status;
import com.quickship.entity.Payment.Type;
import com.quickship.paymentPlugin.PaymentPlugin;
import com.quickship.paymentPlugin.PaymentPlugin.NotifyMethod;
import com.quickship.service.PaymentPluginService;
import com.quickship.service.PaymentService;
import com.quickship.utils.CreateSNUtils;

@Controller
@RequestMapping("/payment")
public class PaymentController extends BaseController{
	
	@Resource(name="paymentServiceImpl")
	private PaymentService paymentService;
	
	@Resource(name="paymentPluginServiceImpl")
	private PaymentPluginService paymentPluginService;
	
	@RequestMapping(value="/submit",method=RequestMethod.POST)
	public String submit(Type type,String paymentPluginId,String sn, BigDecimal amount,HttpServletRequest request, HttpServletResponse response, ModelMap model){
		Member member =(Member) request.getSession().getAttribute("member");
		if(member ==null){
			return ERROR_VIEW;
		}
		PaymentPlugin paymentPlugin=paymentPluginService.getPaymentPlugin(paymentPluginId);
		if(paymentPlugin==null){
			return ERROR_VIEW;
		}
		Payment payment = new Payment();
		String desc =null;
		if(type == Type.recharge){
			//支付类型为预存款
			if(amount==null||amount.compareTo(new BigDecimal(0)) <= 0 || amount.precision() > 15 || amount.scale() > 2){
				return ERROR_VIEW;
			}
			payment.setSn(CreateSNUtils.createPaymentSN(10));
			payment.setType(Type.recharge);
			payment.setMethod(Method.online);
			payment.setStatus(Status.wait);
			payment.setPaymentMethod(paymentPlugin.getPaymentName());
			payment.setFee(paymentPlugin.calculateFee(amount));
			payment.setAmount(paymentPlugin.calculateAmount(amount));
			payment.setMember(member);
			payment.setPluginId(paymentPluginId);
			paymentService.save(payment);
			desc= "Recharge Quiship Account";
		}else if(type == Type.payment){
			//支付类型为订单支付
			
			
		}else{
			return ERROR_VIEW;
		}
		String url = paymentPlugin.getRequestUrl();
		String method = paymentPlugin.getRequestMethod().toString();
		String charset = paymentPlugin.getRequestCharset();
		Map<String,Object> map = paymentPlugin.getParameterMap(payment.getSn(),desc,request);
		model.addAttribute("requestUrl",url);
		model.addAttribute("requestMethod",method);
		model.addAttribute("reqeustCharest",charset);
		model.addAttribute("paramMap",map);
		if(StringUtils.isNotBlank(paymentPlugin.getRequestCharset())){
			response.setContentType("text/html; charset=" + paymentPlugin.getRequestCharset());
		}
		System.out.println(payment.getMember().getBalance());
		return "payment/submit";
	}
	@RequestMapping(value="/notify/{notifyMethod}/{sn}")
	public String notify(@PathVariable NotifyMethod notifyMethod,@PathVariable String sn,HttpServletRequest request, HttpServletResponse response,HttpSession session, ModelMap model){
		Payment payment=paymentService.findPaymentBySN(sn);;
		if(payment!=null){
			PaymentPlugin paymentPlugin=paymentPluginService.getPaymentPlugin(payment.getPluginId());
			if(paymentPlugin !=null){
				//验证通知
				if(paymentPlugin.verifyNotify(sn,notifyMethod,request)){
					paymentService.handle(payment);
					session.setAttribute("member", payment.getMember());
				}
			}
			model.addAttribute("payment", payment);
		}
		return "payment/notify";
	}
	
}

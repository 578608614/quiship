package com.quickship.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.quickship.Message;
import com.quickship.Pageable;
import com.quickship.entity.Member;
import com.quickship.service.DepositService;
import com.quickship.service.PaymentPluginService;

@Controller
@RequestMapping("/deposit")
public class DepositController extends BaseController{
	@Resource(name="depositServiceImpl")
	private DepositService depositService;
	
	@Resource(name="paymentPluginServiceImpl")
	private PaymentPluginService paymentPluginService;
	
	@RequestMapping("/recharge")
	public String recharge(HttpSession session,ModelMap model,Pageable pageable,RedirectAttributes redirectAttributes){
		Member member= (Member)session.getAttribute("member");
		if(member==null){
			addFlashMessage(redirectAttributes, Message.warn("plaese login"));
			return "redirect:/member/login.html";
		}
		model.addAttribute("page",depositService.findPage(member, pageable));
		model.addAttribute("paymentPlugins",paymentPluginService.getPaymentPlugins());
		return "/deposit/recharge";
	}
}

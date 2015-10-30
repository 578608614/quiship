package com.quickship.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.quickship.Message;
import com.quickship.entity.Discount;
import com.quickship.entity.Shipping;
import com.quickship.service.DiscountService;

@Controller
@RequestMapping("/discount")
public class DiscountController extends BaseController{
	
	@Resource(name="discountServiceImpl")
	private DiscountService discountService;

	@RequestMapping("/add")
	public String add(ModelMap model){
		model.addAttribute("serverTypes",Shipping.ServerType.values());
		return "/discount/add";
	}
	@RequestMapping(value="/save",method=RequestMethod.POST)
	public String save(Discount discount,RedirectAttributes redirectAttributes){
		if(discount.getName()==null){
			addFlashMessage(redirectAttributes, Message.error("quickship.message.check.argsNotNull", "Name"));
			return "redirect:/discount/add.html";
		}else if(discount.getFirstDiscount()==null){
			addFlashMessage(redirectAttributes, Message.error("quickship.message.check.argsNotNull", "First Discount"));
			return "redirect:/discount/add.html";
		}else if(discount.getFirstWeight()==null){
			addFlashMessage(redirectAttributes, Message.error("quickship.message.check.argsNotNull", "First Weight"));
			return "redirect:/discount/add.html";
		}else if(discount.getContinueDiscount()==null){
			addFlashMessage(redirectAttributes, Message.error("quickship.message.check.argsNotNull", "Continue Discount"));
			return "redirect:/discount/add.html";
		}
		if(discount.getUsedAll()==null&&discount.getServerType()==null){
			addFlashMessage(redirectAttributes, Message.error("quickship.message.check.argsNotNull", "ServerType And UsedAll Moreover"));
			return "redirect:/discount/add.html";
		}
		discountService.save(discount);
		
		return "redirect:/discount/list.html";
	}
	@RequestMapping("/list")
	public String list(ModelMap model){
		model.addAttribute("discounts",discountService.findAll());
		return "/discount/list";
	}
	@RequestMapping("/edit")
	public String edit(Long id,ModelMap model){
		model.addAttribute("serverTypes",Shipping.ServerType.values());
		if(id!=null){
			model.addAttribute("discount",discountService.find(id));
		}
		return "/discount/edit";
	}
	@RequestMapping(value="/update",method=RequestMethod.POST)
	public String update(Discount discount){
		Discount dis = discountService.find(discount.getId());
		if(dis!=null){
			dis.setName(discount.getName());
			dis.setServerType(dis.getServerType());
			dis.setFirstWeight(discount.getFirstWeight());
			dis.setFirstDiscount(discount.getFirstDiscount());
			dis.setContinueDiscount(discount.getContinueDiscount());
			dis.setDescription(discount.getDescription());
			dis.setUsedAll(discount.getUsedAll());
			discountService.update(dis);
		}
		return "redirect:/discount/list.html";
	}
	@RequestMapping(value="/delete")
	public String delete(Long id){
		if(id!=null){
			discountService.remove(id);
		}
		return "redirect:/discount/list.html";
	}
}

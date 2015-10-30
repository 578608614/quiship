package com.quickship.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.quickship.Message;
import com.quickship.Message.Type;
import com.quickship.Pageable;
import com.quickship.entity.Area;
import com.quickship.entity.Member;
import com.quickship.service.AreaService;
import com.quickship.service.MemberService;
import com.quickship.utils.MD532;

@Controller
@RequestMapping("/member")
public class MemberController extends BaseController {
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "areaServiceImpl")
	private AreaService areaService;
	

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(ModelMap model,HttpServletRequest request) {
		return "member/login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(String email, String password, HttpSession session, RedirectAttributes redirectAttributes) {
		if (email == null) {
			addFlashMessage(redirectAttributes, Message.error("quickship.message.check.argsNotNull", "Email"));
			return "redirect:/member/login.html";
		}
		if (password == null) {
			addFlashMessage(redirectAttributes, Message.error("quickship.message.check.argsNotNull","Password"));
			return "redirect:/member/login.html";
		}
		List<Member> members = memberService.findByEmail(email.trim().toLowerCase());
		if (members == null || members.size() == 0) {
			addFlashMessage(redirectAttributes, Message.error("quickship.message.check.argsError","Email"));
			return "redirect:/member/login.html";
		}
		if (!members.get(0).getPassword().equals(new MD532().getMD5(password))) {
			addFlashMessage(redirectAttributes,Message.error("quickship.message.check.argsError","Password"));
			return "redirect:/member/login.html";
		}
		session.setAttribute("member", members.get(0));
		return "redirect:index.html";
	}
	@RequestMapping(value = "/adminLogin", method = RequestMethod.POST)
	public String adminLogin(Long id,HttpSession session,RedirectAttributes redirectAttributes){
		Member member = memberService.find(id);
		if(member==null){
			addFlashMessage(redirectAttributes, new Message(Type.error, "quickship.message.check.argsNotExist","Member ID"));
			return null;
		}
		session.setAttribute("member", member);
		return "redirect:index.html";
	}

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String register(ModelMap model) {
		List<Area> areas = areaService.findRoot();
		model.addAttribute("areas",areas);
		model.addAttribute("areachilds", areaService.findChild(areas.get(0)));
		return "member/register";
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String register(Member member,Long areaId,HttpServletRequest request, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("member", member);
		if (member.getEmail() == null) {
			addFlashMessage(redirectAttributes, Message.warn("quickship.message.check.argsNotNull","Email"));
			return "redirect:/member/register.html";
		}
		if (member.getPassword() == null) {
			addFlashMessage(redirectAttributes, Message.warn("quickship.message.check.argsNotNull","Password"));
			return "redirect:/member/register.html";
		}
		if (memberService.emailIsExist(member.getEmail())) {
			addFlashMessage(redirectAttributes, Message.warn("quickship.message.check.argsExist","Email"));
			return "redirect:/member/register.html";
		}
		member.setArea(areaService.find(areaId));
		member.setPassword(new MD532().getMD5(member.getPassword()));
		member.setEmail(member.getEmail().trim().toLowerCase());
		member.setAmount(new BigDecimal(0));
		member.setBalance(new BigDecimal(0));
		member.setIp(request.getRemoteAddr());
		memberService.save(member);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:index.html";
	}

	@RequestMapping(value = "/logout")
	public String logout(HttpSession session) {
		if (session.getAttribute("member") != null) {
			session.removeAttribute("member");
		}
		return "redirect:index.html";
	}
	
	@RequestMapping(value = "/index")
	public String success() {
		return "index";
	}
	
	@RequestMapping(value = "/list")
	public String findPage(Pageable pageable,ModelMap model){
		model.addAttribute("page",memberService.findPage(pageable));
		return "member/list";
	}
	@RequestMapping(value = "/edit")
	public String edit(Long id,Long flag,ModelMap model,RedirectAttributes redirectAttributes){
		Member member = memberService.find(id);
		if(member==null){
			addFlashMessage(redirectAttributes, new Message(Type.error, "quickship.message.check.argsNotExist","Member ID"));
			if(flag==1){
				return "redirect:/member/list.html";
			}
			return "redirect:/member/index.html";
		}
		model.addAttribute("member",member);
		String page = "member/edit";
		if(flag!=null&&flag==1){
			page="member/adminedit";
		}
		List<Area> areas =areaService.findRoot();
		model.addAttribute("areas",areas);
		model.addAttribute("areachilds", areaService.findChild(areas.get(0)));
		return page;
	}
	@RequestMapping(value = "/adminUpdate")
	public String adminUpdate(Long id,Long adbalance,boolean isNurse,boolean isAdmin,String phone,HttpSession session,RedirectAttributes redirectAttributes){
		Member member = memberService.find(id);
		if(member==null){
			addFlashMessage(redirectAttributes, new Message(Type.error, "quickship.message.check.argsNotExist","Member ID"));
			return "redirect:/member/list.html";
		}
		member.setIsNurse(isNurse);
		member.setPhone(phone);
		member.setIsValidate(isAdmin);
		memberService.update(member,adbalance);
		session.setAttribute("member", member);
		return "redirect:/member/list.html";
	}
	@RequestMapping(value = "/update")
	public String update(Long id,Long areaId,String firstName,String lastName,String email,String address1,String address2,String city,String phone,String zip,HttpSession session,RedirectAttributes redirectAttributes){
		Member member = memberService.find(id);
		if(member==null){
			addFlashMessage(redirectAttributes, Message.error("quickship.message.check.argsNotExist","Member ID"));
			return "redirect:/member/index.html";
		}
		Area area = areaService.find(areaId);
		if(area==null){
			addFlashMessage(redirectAttributes, Message.error("quickship.message.check.argsError","Country Or State"));
			return "redirect:/member/index.html";
		}
		member.setArea(area);
		member.setFirstName(firstName);
		member.setLastName(lastName);
		member.setEmail(email);
		member.setAddress1(address1);
		member.setAddress2(address1);
		member.setCity(city);
		member.setPhone(phone);
		member.setZip(zip);
		memberService.update(member);
		return "redirect:/member/index.html";
	}
	@RequestMapping(value="/changePassword")
	public String changePassword(Long id,String oldPassword,String password1,String password2,RedirectAttributes redirectAttributes){
		Member member = memberService.find(id);
		if(member==null){
			addFlashMessage(redirectAttributes, Message.error("quickship.message.check.argsNotExist","Member ID"));
			return "redirect:/member/index.html";
		}
		if(!member.getPassword().equals(new MD532().getMD5(oldPassword))){
			addFlashMessage(redirectAttributes, Message.error("quickship.message.check.argsError","Password"));
			return "redirect:/member/edit.html?id="+id;
		}
		if(password1!=null&&password1.equals(password2)){
			member.setPassword(new MD532().getMD5(password1));
			memberService.update(member);
		}else{
			return "redirect:/member/edit.html?id="+id;
		}
		return "redirect:/member/index.html";
	}
	
}

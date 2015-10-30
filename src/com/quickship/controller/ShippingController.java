package com.quickship.controller;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fedex.rate.stub.RateReply;
import com.fedex.ship.stub.ProcessShipmentReply;
import com.quickship.Message;
import com.quickship.Pageable;
import com.quickship.entity.Address;
import com.quickship.entity.Area;
import com.quickship.entity.Member;
import com.quickship.entity.Shipping;
import com.quickship.entity.Shipping.ServerType;
import com.quickship.service.AddressService;
import com.quickship.service.AreaService;
import com.quickship.service.MemberService;
import com.quickship.service.ShippingService;
import com.quickship.service.impl.FedexRateServiceImpl;
import com.quickship.service.impl.FedexServiceImpl;

@Controller
@RequestMapping("/shipping")
public class ShippingController extends BaseController {

	@Resource(name = "shippingServiceImpl")
	private ShippingService shippingService;
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "areaServiceImpl")
	private AreaService areaService;
	@Resource(name = "addressServiceImpl")
	private AddressService addressService;
	@Resource(name = "fedexServiceImpl")
	private FedexServiceImpl fedexService;
	@Resource(name = "fedexRateServiceImpl")
	private FedexRateServiceImpl rateService;

	@RequestMapping(value = "/shipping", method = RequestMethod.GET)
	public String addShipping(HttpSession session, ModelMap model) {
		Member member = (Member) session.getAttribute("member");
		model.addAttribute("dropoffTypes", Shipping.DropoffType.values());
		model.addAttribute("packageTypes", Shipping.PackageType.values());
		model.addAttribute("serverTypes", Shipping.ServerType.values());
		model.addAttribute("shippings", shippingService.findShippings(member, true));
		List<Area> areas = areaService.findRoot();
		model.addAttribute("areas", areas);
		model.addAttribute("areaschilds", areaService.findChild(areas.get(0)));
		try {
			model.addAttribute("addresses", memberService.find(member.getId()).getAddresses());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "shipping/add";
	}

	@RequestMapping(value = "getAddress", method = RequestMethod.GET)
	public @ResponseBody
	Address addressList(Long id) {
		return addressService.find(id);
	}

	@RequestMapping(value = "getShipping", method = RequestMethod.GET)
	public @ResponseBody
	Shipping getShipping(Long id) {
		return shippingService.find(id);
	}

	@RequestMapping("/list")
	public String list(HttpSession session, ModelMap model, Pageable pageable) {
		Member member = (Member) session.getAttribute("member");
		model.addAttribute("page", shippingService.findPage(member, pageable));
		return "shipping/list";
	}
	@RequestMapping("/adminlist")
	public String list(ModelMap model, String email,Pageable pageable,RedirectAttributes redirectAttributes) {
		Member member = null;
		if(!StringUtils.isBlank(email)){
			List<Member> mems = memberService.findByEmail(email);
			if(mems!=null&&mems.size()>0){
				member =  mems.get(0);
				model.addAttribute("email",email);
			}else{
				model.addAttribute("flashMessage",Message.error("quickship.message.check.argsNotExist",email));
			}
		}
	
		model.addAttribute("page", shippingService.findPage(member, pageable));
		return "shipping/adminlist";
	}

	@RequestMapping(value = "/buildShipping", method = RequestMethod.POST)
	public String buildShipping(Shipping shipping, Long oriAreaId, Long desAreaId, HttpSession session, ModelMap model, RedirectAttributes redirectAttributes) {
		Member member = (Member) session.getAttribute("member");
		shipping.setMember(member);	
		redirectAttributes.addFlashAttribute("tmpShipping", shipping);

		Address oriAddress = shipping.getOriAddress();
		if (oriAreaId != null && oriAreaId != 0) {
			oriAddress.setArea(areaService.find(oriAreaId));
		} else {
			oriAddress.setArea(member.getArea());
		}
		
		Address desAddress = shipping.getDesAddress();
		if (desAreaId == null || desAreaId == 0) {
			addFlashMessage(redirectAttributes, Message.warn("quickship.message.check.argsNotNull", "Recipient Country"));
			return "redirect:/shipping/shipping.html";
		} else {
			desAddress.setArea(areaService.find(desAreaId));
		}
		
		if (oriAddress.getPerson() == null || "".equals(oriAddress.getPerson())) {
			addFlashMessage(redirectAttributes, Message.warn("quickship.message.check.argsNotNull", "Sender Name"));
			return "redirect:/shipping/shipping.html";
		}
		if (oriAddress.getPhone() == null || "".equals(oriAddress.getPhone())) {
			addFlashMessage(redirectAttributes, Message.warn("quickship.message.check.argsNotNull", "Sender Phone"));
			return "redirect:/shipping/shipping.html";
		}
		if (oriAddress.getByName() != null) {
			oriAddress.getMembers().add(member);
		}

		if (desAddress.getPerson() == null || "".equals(desAddress.getPerson())) {
			addFlashMessage(redirectAttributes, Message.warn("quickship.message.check.argsNotNull", "Recipient Person"));
			return "redirect:/shipping/shipping.html";
		}
		if (desAddress.getPhone() == null || "".equals(desAddress.getPhone())) {
			addFlashMessage(redirectAttributes, Message.warn("quickship.message.check.argsNotNull", "Recipient Phone"));
			return "redirect:/shipping/shipping.html";
		}
		if (desAddress.getAddress1() == null || "".equals(desAddress.getAddress1())) {
			addFlashMessage(redirectAttributes, Message.warn("quickship.message.check.argsNotNull", "Recipient Address"));
			return "redirect:/shipping/shipping.html";
		}
		
		if (desAddress.getByName() != null) {
			desAddress.getMembers().add(member);
		}
		if(desAddress.getIsResidential()==null){
			desAddress.setIsResidential(false);
		}
		if(shipping.getServerType()==ServerType.GROUND_HOME_DELIVERY && desAddress.getIsResidential()==false){
			addFlashMessage(redirectAttributes, Message.warn("quickship.message.isResidential"));
			return "redirect:/shipping/shipping.html";
		}
		try {
//			ProcessShipmentReply reply = shippingService.buildShipping(shipping);
//			1.验证发货信息
//			ShipmentReply validateReply = fedexService.validateShippments(shipping);
//			if(validateReply!=null){
//				addFlashMessage(redirectAttributes, Message.warn(validateReply.getNotifications()[0].getMessage()));
//				return "redirect:/shipping/shipping.html";
//			}
//			
//			//2.计算价格
			List<RateReply> listRate = shippingService.calcuRate(shipping);
			if(listRate.size()==0||!rateService.isResponseOk(listRate.get(listRate.size()-1).getHighestSeverity())){
				addFlashMessage(redirectAttributes, Message.warn(listRate.get(listRate.size()-1).getNotifications()[0].getMessage()));
				return "redirect:/shipping/shipping.html";
			}
//			//获取发货信息
//			List<ProcessShipmentReply> listReply = shippingService.buildShippings(shipping);
//			if (listReply.size()==0||!fedexService.isResponseOk(listReply.get(listReply.size()-1).getHighestSeverity())) {
//				addFlashMessage(redirectAttributes, Message.warn(listReply.get(listReply.size()-1).getNotifications()[0].getMessage()));
//				return "redirect:/shipping/shipping.html";
//			}
			session.setAttribute("shipping", shipping);
//			session.setAttribute("replys", listReply);
//			
//			//------测试---------
//			printShipmentOperationalDetails(listReply.get(0).getCompletedShipmentDetail().getOperationalDetail());
//			printShipmentRating(listReply.get(0).getCompletedShipmentDetail().getShipmentRating());
			//-----------------
		} catch (Exception e) {

			e.printStackTrace();
		}
		return "shipping/select";
	}

	@RequestMapping(value = "/shipping", method = RequestMethod.POST)
	public String shipping(HttpServletRequest request, HttpServletResponse response, HttpSession session,ModelMap model,RedirectAttributes redirectAttributes) {

		Shipping shipping = (Shipping) session.getAttribute("shipping");
		session.removeAttribute("shipping");
		Member member = (Member) session.getAttribute("member");
		// 1.校验余额是否充足
		try{
			if(shipping!=null){
				BigDecimal charge = shipping.getNetCharge();
				if(member.getBalance().compareTo(charge)==-1){
					 addFlashMessage(redirectAttributes,Message.warn("quickship.message.member.balance"));
					 return "redirect:/deposit/recharge.html";
				}
				// 1.构建发货单    之后要合并
				List<ProcessShipmentReply> listReply = shippingService.buildShippings(shipping);
				if (listReply.size()==0||!fedexService.isResponseOk(listReply.get(listReply.size()-1).getHighestSeverity())) {
					addFlashMessage(redirectAttributes, Message.warn(listReply.get(listReply.size()-1).getNotifications()[0].getMessage()));
					return "redirect:/shipping/shipping.html";
				}
				// 2.发货
				List<File> files = shippingService.shipping(listReply, shipping, member, request);
				
				if(files.size()==1){
					downLoadLable(files.get(0), response);
					return null;
				}
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		model.addAttribute("shipping", shipping);
		return "shipping/mpsLable";

	}
	
	@RequestMapping(value = "/downLoad", method = RequestMethod.GET)
	public String downLoad(Long id,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes) {
		Shipping shipping = shippingService.find(id);
		File file =null;
		if(shipping==null||shipping.getLabelPath()==null){
			addFlashMessage(redirectAttributes, Message.error("quickship.message.check.argsNotExist","Shipping"));
			return "redirect:/shipping/list.html";
		}
		String basePath = request.getSession().getServletContext().getRealPath("/");
		file = new File(basePath+shipping.getLabelPath().substring(1));
		downLoadLable(file, response);
		return null;
	}
	
	@RequestMapping(value = "/income")
	public String income(Date beginDate,Date endDate,String email,Pageable pageable,ModelMap model){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		if (beginDate != null) {
			Calendar calendar = DateUtils.toCalendar(beginDate);
			calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
			calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
			calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
			beginDate = calendar.getTime();
			model.addAttribute("beginDate",format.format(beginDate));
		}
		if (endDate != null) {
			Calendar calendar = DateUtils.toCalendar(endDate);
			calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
			calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
			calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
			endDate = calendar.getTime();
			model.addAttribute("endDate",format.format(endDate));
		}
		Member member = null;
		if(email!=null){
			List<Member> list = memberService.findByEmail(email);
			if(list.size()>0){
				member = list.get(0);
				model.addAttribute("email", email);
			}
		}
		
		model.addAttribute("page",shippingService.findPageIncome(beginDate, endDate, member, pageable));
		if(beginDate!=null||endDate!=null||member!=null){
			model.addAttribute("pageIncome",shippingService.calcuIncome(beginDate, endDate, member));
		}
		model.addAttribute("totalIncome",shippingService.calcuIncome(null, null, null));
		return "income/adminList";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	public void printShipmentOperationalDetails(ShipmentOperationalDetail shipmentOperationalDetail) {
//		if (shipmentOperationalDetail != null) {
//			System.out.println("Routing Details");
//			printString(shipmentOperationalDetail.getUrsaPrefixCode(), "URSA Prefix", "  ");
//			if (shipmentOperationalDetail.getCommitDay() != null)
//				printString(shipmentOperationalDetail.getCommitDay().getValue(), "Service Commitment", "  ");
//			printString(shipmentOperationalDetail.getAirportId(), "Airport Id", "  ");
//			if (shipmentOperationalDetail.getDeliveryDay() != null)
//				printString(shipmentOperationalDetail.getDeliveryDay().getValue(), "Delivery Day", "  ");
//			if (shipmentOperationalDetail.getDeliveryDate() != null)
//				System.out.print("  Delivery Date:" + shipmentOperationalDetail.getDeliveryDate());
//			System.out.println();
//		}
//	}

//	private static void printShipmentRating(ShipmentRating shipmentRating) {
//		if (shipmentRating != null) {
//			printMoney(shipmentRating.getEffectiveNetDiscount(), "EffectiveNetDiscount", "    ");
//			System.out.println("Shipment Rate Details");
//			ShipmentRateDetail[] srd = shipmentRating.getShipmentRateDetails();
//			for (int j = 0; j < srd.length; j++) {
//				System.out.println("  Rate Type: " + srd[j].getRateType().getValue());
//				printMoney(srd[j].getTotalBaseCharge(), "Shipment Base Charge", "    ");
//				printMoney(srd[j].getTotalNetCharge(), "Shipment Net Charge", "    ");
//				printMoney(srd[j].getTotalSurcharges(), "Shipment Total Surcharge", "    ");
//				printMoney(srd[j].getTotalFreightDiscounts(),"Shipment Total FreightDiscounts","    ");
//				printMoney(srd[j].getTotalRebates(),"Shipment Total Rebates","    ");
//				if (null != srd[j].getSurcharges()) {
//					System.out.println("    Surcharge Details");
//					Surcharge[] s = srd[j].getSurcharges();
//					for (int k = 0; k < s.length; k++) {
//						printMoney(s[k].getAmount(), s[k].getSurchargeType().getValue(), "      ");
//					}
//				}
//				printFreightDetail(srd[j].getFreightRateDetail());
//				System.out.println();
//			}
//		}
//	}

//	private static void printMoney(Money money, String description, String space) {
//		if (money != null) {
//			System.out.println(space + description + ": " + money.getAmount() + " " + money.getCurrency());
//		}else{
//			System.out.println(space + description + ": Null");
//		}
//	}

//	private static void printFreightNotations(FreightRateDetail frd) {
//		if (null != frd.getNotations()) {
//			System.out.println("    Notations");
//			FreightRateNotation notations[] = frd.getNotations();
//			for (int n = 0; n < notations.length; n++) {
//				printString(notations[n].getCode(), "Code", "      ");
//				printString(notations[n].getDescription(), "Notification", "      ");
//			}
//		}
//	}

//	private static void printFreightBaseCharges(FreightRateDetail frd) {
//		if (null != frd.getBaseCharges()) {
//			FreightBaseCharge baseCharges[] = frd.getBaseCharges();
//			for (int i = 0; i < baseCharges.length; i++) {
//				System.out.println("    Freight Rate Details");
//				printString(baseCharges[i].getDescription(), "Description", "      ");
//				printString(baseCharges[i].getFreightClass().getValue(), "Freight Class", "      ");
//				printString(baseCharges[i].getRatedAsClass().getValue(), "Rated Class", "      ");
//				printString(baseCharges[i].getChargeBasis().getValue(), "Charge Basis", "      ");
//				printMoney(baseCharges[i].getChargeRate(), "Charge Rate", "      ");
//				printMoney(baseCharges[i].getExtendedAmount(), "Extended Amount", "      ");
//				printString(baseCharges[i].getNmfcCode(), "NMFC Code", "      ");
//			}
//		}
//	}

//	private static void printFreightDetail(FreightRateDetail freightRateDetail) {
//		if (freightRateDetail != null) {
//			System.out.println("  Freight Details");
//			printFreightNotations(freightRateDetail);
//			printFreightBaseCharges(freightRateDetail);
//
//		}
//	}

//	private static void printString(String value, String description, String space) {
//		if (value != null) {
//			System.out.println(space + description + ": " + value);
//		}
//	}

}

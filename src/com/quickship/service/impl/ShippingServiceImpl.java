package com.quickship.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.FlushModeType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fedex.rate.stub.RateReply;
import com.fedex.rate.stub.RateReplyDetail;
import com.fedex.rate.stub.RatedShipmentDetail;
import com.fedex.ship.stub.CompletedPackageDetail;
import com.fedex.ship.stub.Money;
import com.fedex.ship.stub.ProcessShipmentReply;
import com.fedex.ship.stub.ShipmentRateDetail;
import com.fedex.ship.stub.ShipmentRating;
import com.fedex.ship.stub.ShippingDocument;
import com.fedex.ship.stub.ShippingDocumentPart;
import com.quickship.ChargeItem;
import com.quickship.Income;
import com.quickship.Page;
import com.quickship.Pageable;
import com.quickship.entity.Deposit;
import com.quickship.entity.Deposit.Type;
import com.quickship.entity.Discount;
import com.quickship.entity.Member;
import com.quickship.entity.Shipping;
import com.quickship.entity.Shipping.ReconcileType;
import com.quickship.entity.ShippingItem;
import com.quickship.service.AddressService;
import com.quickship.service.DepositService;
import com.quickship.service.DiscountService;
import com.quickship.service.MemberService;
import com.quickship.service.ShippingService;
import com.quickship.service.base.BaseDaoImpl;

@Transactional
@Service("shippingServiceImpl")
public class ShippingServiceImpl extends BaseDaoImpl<Shipping, Long> implements ShippingService {
	@Resource(name = "fedexServiceImpl")
	private FedexServiceImpl fedexService;
	@Resource(name = "addressServiceImpl")
	private AddressService addressService;
	@Resource(name="memberServiceImpl")
	private MemberService memberService;
	@Resource(name="depositServiceImpl")
	private DepositService depositService;
	@Resource(name="discountServiceImpl")
	private DiscountService discountService;
	@Resource(name="fedexRateServiceImpl")
	private FedexRateServiceImpl fedexRateService;
	
	
	public enum MoneyType{
		/**基础费用*/
		BaseCharge,
		/**附加费*/
		SurCharge,
		/**网费 */
		NetCharge,
		/**总折扣*/
		Rebates,
		/**有效折扣*/
		EffectiveNetDiscount,
		/**运费折扣*/
		FreightDiscounts
	}

	@Transactional(readOnly = true)
	public List<Shipping> findShippings(Member member, Boolean isSeaved) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Shipping> criteriaQuery = criteriaBuilder.createQuery(Shipping.class);
		Root<Shipping> root = criteriaQuery.from(Shipping.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (member != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
		}
		if (isSeaved != null) {
			restrictions = criteriaBuilder.and(criteriaBuilder.equal(root.get("isSeaved"), isSeaved));
		}
		criteriaQuery.where(restrictions);
		return super.findList(criteriaQuery, null, null, null, null);
	}
	
	@Transactional(readOnly = true)
	public Shipping findByTrackNo(String trackNo){
		if(trackNo!=null){
			try{
				String jpql = "select shipping from Shipping shipping where shipping.labelTrackNo=:trackNo";
				return entityManager.createQuery(jpql, Shipping.class).setFlushMode(FlushModeType.COMMIT).setParameter("trackNo", trackNo).getSingleResult();
			}catch(Exception e){
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}
	@Transactional(readOnly = true)
	public List<Shipping> findList(ReconcileType reconcileType){
		if(reconcileType!=null){
			String jpql = "select shipping from Shipping shipping where shipping.reconcileType=:reconcileType";
			return entityManager.createQuery(jpql,Shipping.class).setFlushMode(FlushModeType.COMMIT).setParameter("reconcileType", reconcileType).getResultList();
		}
		return null;
	}
	
	@Transactional(readOnly = true)
	public Page<Shipping> findPage(Member member, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Shipping> criteriaQuery = criteriaBuilder.createQuery(Shipping.class);
		Root<Shipping> root = criteriaQuery.from(Shipping.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (member != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
		}
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

//	@Transactional
//	public List<ProcessShipmentReply> buildShippings(Shipping shipping) {
//		List<ProcessShipmentReply> replyList = new ArrayList<ProcessShipmentReply>();
//		if(shipping.getServerType()==ServerType.EXPRESS){
//			for(ExpressType type:ExpressType.values()){
//				if(shipping.getPakageType()==PackageType.FEDEX_BOX){
//					if(type!=ExpressType.FEDEX_EXPRESS_SAVER){
//						replyList.add(fedexService.createShipment(shipping,type));
//					}
//				}else if(shipping.getPakageType()==PackageType.FEDEX_ENVELOPE){
//					if(type==ExpressType.PRIORITY_OVERNIGHT||type==ExpressType.STANDARD_OVERNIGHT){
//						replyList.add(fedexService.createShipment(shipping,type));
//					}
//				}else{
//					replyList.add(fedexService.createShipment(shipping,type));
//				}
//			}
//		}else{
//			replyList.add(fedexService.createShipment(shipping,null));
//		}
//		
//		if (replyList.size() >0) {
//			if (shipping.getIsSeaved() != null && shipping.getIsSeaved() == true) {
//				addressService.save(shipping.getDesAddress());
//				addressService.save(shipping.getOriAddress());
//				for(ShippingItem item :shipping.getShipItems()){
//					item.setShipping(shipping);
//				}
//				super.save(shipping);
//			}else{
//				if(!StringUtils.isBlank(shipping.getDesAddress().getByName())){
//					addressService.save(shipping.getDesAddress());
//				}
//				if(!StringUtils.isBlank(shipping.getOriAddress().getByName())){
//					addressService.save(shipping.getOriAddress());
//				}
//			}
//			if(!fedexService.isResponseOk(replyList.get(0).getHighestSeverity())){
//				replyList.clear();
//			}
//		}
//		
//		return replyList;
//	}
	
	
	@Deprecated
	@Transactional
	public ProcessShipmentReply buildShipping(Shipping shipping) {
		ProcessShipmentReply reply= null;
		reply = fedexService.createShipment(shipping);
		if (reply!=null) {
			if (shipping.getIsSeaved() != null && shipping.getIsSeaved() == true) {
				addressService.save(shipping.getDesAddress());
				addressService.save(shipping.getOriAddress());
				for(ShippingItem item :shipping.getShipItems()){
					item.setShipping(shipping);
				}
				super.save(shipping);
			}else{
				if(!StringUtils.isBlank(shipping.getDesAddress().getByName())){
					addressService.save(shipping.getDesAddress());
				}
				if(!StringUtils.isBlank(shipping.getOriAddress().getByName())){
					addressService.save(shipping.getOriAddress());
				}
			}
			if(fedexService.isResponseOk(reply.getHighestSeverity())){
				Discount discount =discountService.findDiscount(shipping.getServerType());
				BigDecimal dis = discount.calcucate(shipping);
				ShipmentRating rating = reply.getCompletedShipmentDetail().getShipmentRating();
				BigDecimal netCharge = calcuCharge(rating, MoneyType.NetCharge);
				shipping.setMemberDiscount(netCharge.subtract(netCharge.multiply(dis)));
				shipping.setNetCharge(netCharge.multiply(dis));
				shipping.setBaseCharge(calcuCharge(rating,MoneyType.BaseCharge));
				shipping.setSurCharge(calcuCharge(rating,MoneyType.SurCharge));
//				shipping.setDiscount(calueCharge(rating,MoneyType.Rebates));
//				shipping.setNetCharge(calueCharge(rating,MoneyType.NetCharge));
				String serType = reply.getCompletedShipmentDetail().getServiceTypeDescription();
				shipping.setFedexServerType(serType);
			}
		}
		
		return reply;
	}
	
	@Transactional
	public List<ProcessShipmentReply> buildShippings(Shipping shipping) {
		List<ProcessShipmentReply> listReply =fedexService.createShipments(shipping);
		if (listReply.size()>0&&fedexService.isResponseOk(listReply.get(listReply.size()-1).getHighestSeverity())) {
			if (shipping.getIsSeaved() != null && shipping.getIsSeaved() == true) {
				addressService.save(shipping.getDesAddress());
				addressService.save(shipping.getOriAddress());
				for(ShippingItem item :shipping.getShipItems()){
					item.setShipping(shipping);
				}
				super.save(shipping);
			}else{
				if(!StringUtils.isBlank(shipping.getDesAddress().getByName())){
					addressService.save(shipping.getDesAddress());
				}
				if(!StringUtils.isBlank(shipping.getOriAddress().getByName())){
					addressService.save(shipping.getOriAddress());
				}
			}
			Discount discount =discountService.findDiscount(shipping.getServerType());
			BigDecimal memberDiscount = discount.calcucate(shipping);
			ProcessShipmentReply reply = listReply.get(listReply.size()-1);
			ShipmentRating rating = reply.getCompletedShipmentDetail().getShipmentRating();
			if(rating!=null){
				shipping.setBaseCharge(calcuCharge(rating,MoneyType.BaseCharge));
				shipping.setSurCharge(calcuCharge(rating,MoneyType.SurCharge));
				
				BigDecimal netCharge = calcuCharge(rating, MoneyType.NetCharge);
				BigDecimal effectiveNetDiscount = calcuCharge(rating, MoneyType.EffectiveNetDiscount);
				
				if(effectiveNetDiscount!=null&&effectiveNetDiscount.compareTo(new BigDecimal(0))>0){
					shipping.setCostCharge(netCharge.subtract(effectiveNetDiscount));
					BigDecimal memberPrice = netCharge.multiply(memberDiscount).setScale(2,BigDecimal.ROUND_HALF_UP);
					shipping.setMemberDiscount(netCharge.subtract(memberPrice));
					shipping.setNetCharge(memberPrice);
				}else{
					shipping.setCostCharge(netCharge);
					shipping.setMemberDiscount(new BigDecimal(0));
					shipping.setNetCharge(netCharge);
				}
				
				//---------------test---------------
				calcuCharge(rating, MoneyType.EffectiveNetDiscount);
				calcuCharge(rating, MoneyType.Rebates);
				calcuCharge(rating, null);
			}
			String serType = reply.getCompletedShipmentDetail().getServiceTypeDescription();
			shipping.setFedexServerType(serType);
			shipping.setReconcileType(ReconcileType.NOREADY);
			
		}
		return listReply;
	}
	
	public List<RateReply> calcuRate(Shipping shipping){
		
		List<RateReply> listRate =fedexRateService.calcuRateShippments(shipping);
		if(listRate.size()>0&&fedexRateService.isResponseOk(listRate.get(listRate.size()-1).getHighestSeverity())){
			RateReplyDetail rateReplyDetail= listRate.get(listRate.size()-1).getRateReplyDetails(0);
			shipping.setFedexServerType(rateReplyDetail.getServiceType().toString());
			Calendar calendar = rateReplyDetail.getDeliveryTimestamp();
			if(calendar!=null){
				shipping.setDeliveryDate(new Date(calendar.getTimeInMillis()));
			}
			ChargeItem chargeItem = calcuChargeItem(listRate);
			shipping.setBaseCharge(chargeItem.getTotalBaseCharge());
			shipping.setSurCharge(chargeItem.getTotalSurCharge());
			
			Discount discount =discountService.findDiscount(shipping.getServerType());
			BigDecimal memberDiscount = discount.calcucate(shipping);
			
			BigDecimal effDiscount = chargeItem.getTotalEffeDiscount();
			BigDecimal netCharge = chargeItem.getTotalNetCharge();
			
			if(effDiscount!=null&&effDiscount.compareTo(new BigDecimal(0))>0){
				shipping.setCostCharge(netCharge.subtract(effDiscount));
				BigDecimal memberPrice = netCharge.multiply(memberDiscount).setScale(2,BigDecimal.ROUND_HALF_UP);
				shipping.setNetCharge(memberPrice);
				shipping.setMemberDiscount(netCharge.subtract(memberPrice));
			}else{
				shipping.setCostCharge(netCharge);
				shipping.setMemberDiscount(new BigDecimal(0));
				shipping.setNetCharge(netCharge);
			}
		}
		return listRate;
	}
	
	public File shipping(ProcessShipmentReply reply, Shipping shipping, Member member, HttpServletRequest request) {
		//1.设置最总服务类型，
//		String serType = reply.getCompletedShipmentDetail().getServiceTypeDescription();
//		shipping.setFedexServerType(serType);
//		//2.设置价格
//		ShipmentRating rating = reply.getCompletedShipmentDetail().getShipmentRating();
//		shipping.setBaseCharge(calueCharge(rating,MoneyType.BaseCharge));
//		shipping.setSurCharge(calueCharge(rating,MoneyType.SurCharge));
//		shipping.setDiscount(calueCharge(rating,MoneyType.Rebates));
//		shipping.setNetCharge(calueCharge(rating,MoneyType.NetCharge));
		
		if (shipping.getIsSeaved() != null && shipping.getIsSeaved() == true) {
			addressService.save(shipping.getDesAddress());
			addressService.save(shipping.getOriAddress());
			for(ShippingItem item :shipping.getShipItems()){
				item.setShipping(shipping);
			}
			super.save(shipping);
		}else{
			if(!StringUtils.isBlank(shipping.getDesAddress().getByName())){
				addressService.save(shipping.getDesAddress());
			}
			if(!StringUtils.isBlank(shipping.getOriAddress().getByName())){
				addressService.save(shipping.getOriAddress());
			}
		}
		
		
		//3. 生成文件 保存shipping
		String rootPath = request.getSession().getServletContext().getRealPath("/");   
		File labelDir = new File(rootPath+File.separator+"labels");
		if(!labelDir.exists()){
			labelDir.mkdir();
		}
		String labelLocation = labelDir.getAbsolutePath();
		File labelFile = null;
		CompletedPackageDetail cpd[] = reply.getCompletedShipmentDetail().getCompletedPackageDetails();
		for (int i = 0; i < cpd.length; i++) {
			String trackingNumber = cpd[i].getTrackingIds()[0].getTrackingNumber();
			ShippingDocument sd = cpd[i].getLabel();
			ShippingDocumentPart[] sdparts = sd.getParts();
			for (int j = 0; j < sdparts.length; j++) {
				ShippingDocumentPart sdpart = sdparts[j];
				String shippingDocumentType = sd.getType().getValue();
				String labelFileName = new String(labelLocation +File.separator+ shippingDocumentType + "." + trackingNumber + "_" + j + ".pdf");
				labelFile = new File(labelFileName);
				FileOutputStream fos=null;
				try {
					fos = new FileOutputStream(labelFile);
					fos.write(sdpart.getImage());
					if(shipping.getLabelPath()!=null){
						shipping.setLabelPath(shipping.getLabelPath()+";"+"/labels/"+labelFile.getName());
					}else{
						shipping.setLabelPath("/labels/"+labelFile.getName());
					}
//					Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + labelFile.getAbsolutePath());
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					try {
						if(fos!=null){
							fos.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}
			shipping.setLabelTrackNo(trackingNumber);
		}
		if(shipping.getOriAddress().getId()==null){
			addressService.save(shipping.getOriAddress());
		}
		if(shipping.getDesAddress().getId()==null){
			addressService.save(shipping.getDesAddress());
		}
		if(shipping.getId()==null){
			for(ShippingItem item :shipping.getShipItems()){
				item.setShipping(shipping);
			}
			super.save(shipping);
		}else{
			super.update(shipping);
		}
			
		// 3.扣费修改会员扣除余额，增加消费额。
		BigDecimal netCharge = shipping.getNetCharge();
		if(netCharge!=null){
			member.setBalance(member.getBalance().subtract(netCharge));
			member.setAmount(member.getAmount().add(netCharge));
			Deposit deposit = new Deposit();
			deposit.setCredit(new BigDecimal(0));
			deposit.setDebit(netCharge.multiply(new BigDecimal(-1)));
			deposit.setType(Type.memberPayment);
			deposit.setBalance(member.getBalance());
			deposit.setMember(member);
			depositService.save(deposit);
		}
		memberService.update(member);
		return labelFile;
		
	}
	
	public List<File> shipping(List<ProcessShipmentReply> listReply, Shipping shipping, Member member, HttpServletRequest request) {
		//3. 生成文件 保存shipping
		String rootPath = request.getSession().getServletContext().getRealPath("/");   
		File labelDir = new File(rootPath+File.separator+"labels");
		if(!labelDir.exists()){
			labelDir.mkdir();
		}
		Shipping parentShipping = shipping;
		String labelLocation = labelDir.getAbsolutePath();
		List<File> labelFiles = new ArrayList<File>();
		Shipping childShipping = null;
		boolean flag = false;
		for(ProcessShipmentReply reply:listReply){
			if(flag){
				childShipping = new Shipping();
				BeanUtils.copyProperties(parentShipping, childShipping);
				childShipping.setParent(parentShipping);
				childShipping.setChildShippings(null);
				childShipping.setShipItems(null);
				childShipping.setLabelPath(null);
				childShipping.setId(null);
				parentShipping.getChildShippings().add(childShipping);
				shipping=childShipping;
			}
			
			
			CompletedPackageDetail cpd[] = reply.getCompletedShipmentDetail().getCompletedPackageDetails();
			for (int i = 0; i < cpd.length; i++) {
				String trackingNumber = cpd[i].getTrackingIds()[0].getTrackingNumber();
				ShippingDocument sd = cpd[i].getLabel();
				ShippingDocumentPart[] sdparts = sd.getParts();
				for (int j = 0; j < sdparts.length; j++) {
					ShippingDocumentPart sdpart = sdparts[j];
					String shippingDocumentType = sd.getType().getValue();
					String labelFileName = new String(labelLocation +File.separator+ shippingDocumentType + "." + trackingNumber + "_" + j + ".pdf");
					File labelFile = new File(labelFileName);
					FileOutputStream fos=null;
					try {
						fos = new FileOutputStream(labelFile);
						fos.write(sdpart.getImage());
						if(shipping.getLabelPath()!=null){
							shipping.setLabelPath(shipping.getLabelPath()+";"+"/labels/"+labelFile.getName());
						}else{
							shipping.setLabelPath("/labels/"+labelFile.getName());
						}
//						Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + labelFile.getAbsolutePath());
					} catch (Exception e) {
						e.printStackTrace();
					}finally{
						try {
							if(fos!=null){
								fos.close();
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					labelFiles.add(labelFile);

				}
				shipping.setLabelTrackNo(trackingNumber);
			}
			if(shipping.getOriAddress().getId()==null){
				addressService.save(shipping.getOriAddress());
			}
			if(shipping.getDesAddress().getId()==null){
				addressService.save(shipping.getDesAddress());
			}
			if(shipping.getId()==null){
				if(shipping.getShipItems()!=null){
					for(ShippingItem item :shipping.getShipItems()){
						item.setShipping(shipping);
					}
				}
				super.save(shipping);
			}else{
				super.update(shipping);
			}
			flag = true;
		}
		super.update(parentShipping);
			
		// 3.扣费修改会员扣除余额，增加消费额。
		BigDecimal netCharge = parentShipping.getNetCharge();
		if(netCharge!=null){
			member.setBalance(member.getBalance().subtract(netCharge));
			member.setAmount(member.getAmount().add(netCharge));
			Deposit deposit = new Deposit();
			deposit.setCredit(new BigDecimal(0));
			deposit.setDebit(netCharge.multiply(new BigDecimal(-1)));
			deposit.setType(Type.memberPayment);
			deposit.setBalance(member.getBalance());
			deposit.setMember(member);
			depositService.save(deposit);
		}
		memberService.update(member);
		return labelFiles;
		
	}
	
	/**
	 * 根据发货服务计算 单项价格
	 * @param rating
	 * @param type
	 * @return
	 */
	@Transactional(readOnly = true)
	public BigDecimal calcuCharge(ShipmentRating rating,MoneyType type) {
		if(rating==null){
			return null;
		}
		BigDecimal charge = new BigDecimal(0);
		Money money= null;
		ShipmentRateDetail[] rateDetails = rating.getShipmentRateDetails();
		if(type==MoneyType.BaseCharge){
			for(ShipmentRateDetail rate :rateDetails){
				money = rate.getTotalBaseCharge();
				if(money!=null){
					charge=charge.add(money.getAmount());
				}
			}
		}else if(type==MoneyType.NetCharge){
			for(ShipmentRateDetail rate :rateDetails){
				money = rate.getTotalNetCharge();
				if(money!=null){
					charge=charge.add(money.getAmount());
				}
			}
		}else if(type==MoneyType.Rebates){
			for(ShipmentRateDetail rate :rateDetails){
				money = rate.getTotalRebates();
				if(money!=null){
					charge=charge.add(money.getAmount());
				}
			}
		}else if(type==MoneyType.SurCharge){
			for(ShipmentRateDetail rate :rateDetails){
				money = rate.getTotalSurcharges();
				if(money!=null){
					charge=charge.add(money.getAmount());
				}
			}
		}else if(type==MoneyType.EffectiveNetDiscount){
			money= rating.getEffectiveNetDiscount();
			if(money!=null){
				charge=charge.add(money.getAmount());
			}
		}else if(type==MoneyType.FreightDiscounts){
			for(ShipmentRateDetail rate :rateDetails){
				money = rate.getTotalFreightDiscounts();
				if(money!=null){
					charge=charge.add(money.getAmount());
				}
			}
		}
		System.out.println(type+":"+charge);
		return charge;
	}
	
	/**
	 * 根据汇率服务的Reply 计算单项价格
	 * @param rateDetail
	 * @param type
	 * @return
	 */
	@Transactional(readOnly = true)
	public BigDecimal calcuCharge(RateReplyDetail rateDetail,MoneyType type){
		if(rateDetail==null){
			return null;
		}
		BigDecimal charge = new BigDecimal(0);
		com.fedex.rate.stub.Money money= null;
		RatedShipmentDetail[] rateDetails = rateDetail.getRatedShipmentDetails();
		if(type==MoneyType.BaseCharge){
			for(RatedShipmentDetail rate :rateDetails){
				money = rate.getShipmentRateDetail().getTotalBaseCharge();
				if(money!=null){
					charge=charge.add(money.getAmount());
				}
			}
		}else if(type==MoneyType.NetCharge){
			for(RatedShipmentDetail rate :rateDetails){
				money = rate.getShipmentRateDetail().getTotalNetCharge();
				if(money!=null){
					charge=charge.add(money.getAmount());
				}
			}
		}else if(type==MoneyType.Rebates){
			for(RatedShipmentDetail rate :rateDetails){
				money = rate.getShipmentRateDetail().getTotalRebates();
				if(money!=null){
					charge=charge.add(money.getAmount());
				}
			}
		}else if(type==MoneyType.SurCharge){
			for(RatedShipmentDetail rate :rateDetails){
				money = rate.getShipmentRateDetail().getTotalSurcharges();
				if(money!=null){
					charge=charge.add(money.getAmount());
				}
			}
		}else if(type==MoneyType.EffectiveNetDiscount){
			for(RatedShipmentDetail rate :rateDetails){
				money = rate.getEffectiveNetDiscount();
				if(money!=null){
					charge=charge.add(money.getAmount());
				}
			}
			
		}else if(type==MoneyType.FreightDiscounts){
			for(RatedShipmentDetail rate :rateDetails){
				money = rate.getShipmentRateDetail().getTotalFreightDiscounts();
				if(money!=null){
					charge=charge.add(money.getAmount());
				}
			}
		}
		System.out.println(type+":"+charge);
		return charge;
	}
	
	/**
	 * 更具发货服务的Reply 计算价格
	 * @param rating
	 * @return
	 */
//	/*private ChargeItem calcuChargeItem(ShipmentRating rating){
//		ChargeItem chargeItem = new ChargeItem();
//		if(rating!=null){
//			BigDecimal baseCharge = new BigDecimal(0);
//			BigDecimal netCharge = new BigDecimal(0);
//			BigDecimal surCharge = new BigDecimal(0);
//			BigDecimal rebates =new BigDecimal(0);
//			Money money = null;
//			ShipmentRateDetail[] rateDetails = rating.getShipmentRateDetails();
//			
//			money = rating.getEffectiveNetDiscount();
//			if(money!=null){
//				chargeItem.setTotalEffeDiscount(money.getAmount());
//			}
//			for(ShipmentRateDetail rate :rateDetails){
//				money = rate.getTotalBaseCharge();
//				if(money!=null){
//					baseCharge=baseCharge.add(money.getAmount());
//				}
//				money = rate.getTotalNetCharge();
//				if(money!=null){
//					netCharge=netCharge.add(money.getAmount());
//				}
//				money = rate.getTotalSurcharges();
//				if(money!=null){
//					surCharge=surCharge.add(money.getAmount());
//				}
//				money = rate.getTotalRebates();
//				if(money!=null){
//					rebates=rebates.add(money.getAmount());
//				}
//			}
//			chargeItem.setTotalBaseCharge(baseCharge);
//			chargeItem.setTotalNetCharge(netCharge);
//			chargeItem.setTotalSurCharge(surCharge);
//			chargeItem.setTotalRebates(rebates);
//		}
//		return chargeItem;
//	}*/
	
	/**
	 * 根据汇率服务的Reply 计算价格
	 * @param rateReplyList
	 * @return
	 */
	private ChargeItem calcuChargeItem(List<RateReply> rateReplyList){
		
		BigDecimal baseCharge = new BigDecimal(0);
		BigDecimal netCharge = new BigDecimal(0);
		BigDecimal surCharge = new BigDecimal(0);
		BigDecimal effeDiscount =new BigDecimal(0);
		BigDecimal rebates =new BigDecimal(0);
		ChargeItem chargeItem = new ChargeItem();
		for(RateReply reply:rateReplyList){
			RateReplyDetail[] rateReplyDetails=reply.getRateReplyDetails();
			if(rateReplyDetails!=null && rateReplyDetails.length!=0){
				com.fedex.rate.stub.Money money = null;
				for(RateReplyDetail rateReplyDetail : rateReplyDetails){
					RatedShipmentDetail[] rateDetails = rateReplyDetail.getRatedShipmentDetails();
					for(RatedShipmentDetail rate :rateDetails){
						com.fedex.rate.stub.ShipmentRateDetail rateDetail = rate.getShipmentRateDetail();
						money=rateDetail.getTotalBaseCharge();
						if(money!=null){
							baseCharge=baseCharge.add(money.getAmount());
						}
						money = rateDetail.getTotalNetCharge();
						if(money!=null){
							netCharge=netCharge.add(money.getAmount());
						}
						money = rateDetail.getTotalRebates();
						if(money!=null){
							rebates=rebates.add(money.getAmount());
						}
						money = rateDetail.getTotalSurcharges();
						if(money!=null){
							surCharge=surCharge.add(money.getAmount());
						}
						money= rate.getEffectiveNetDiscount();
						if(money!=null){
							effeDiscount=effeDiscount.add(money.getAmount());
						}
					}
				}
			}
			
		}
		chargeItem.setTotalBaseCharge(baseCharge);
		chargeItem.setTotalNetCharge(netCharge);
		chargeItem.setTotalSurCharge(surCharge);
		chargeItem.setTotalRebates(rebates);
		chargeItem.setTotalEffeDiscount(effeDiscount);
		return chargeItem;
	}
	
	/**
	 * 收入分页列表
	 */
	public Page<Shipping> findPageIncome(Date beginDate,Date endDate,Member member,Pageable pageable){
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Shipping> criteriaQuery = criteriaBuilder.createQuery(Shipping.class);
		Root<Shipping> root = criteriaQuery.from(Shipping.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNull(root.get("parent")));
		if(beginDate!=null){
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("createDate"), beginDate));
		}
		if(endDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get("createDate"), endDate));
		}
		if(member!=null){
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
		}
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
		
	}
	
	/**
	 * 计算收入
	 */
	public Income calcuIncome(Date beginDate,Date endDate,Member member){
		Income income =null;
		BigDecimal netCharge = new BigDecimal(0);
		BigDecimal costCharge = new BigDecimal(0);
		try{
			String where = "where parent is null ";
			if(beginDate!=null){
				where+="and s.createDate>=:beginDate";
			}
			if(endDate!=null){
				where=where+" and s.createDate<=:endDate";
			}
			if(member!=null){
				where=where +" and s.member=:member";
			}
			String jpql ="select sum(s.netCharge) from Shipping s "+where;
			TypedQuery<BigDecimal> query=entityManager.createQuery(jpql, BigDecimal.class).setFlushMode(FlushModeType.COMMIT);
			if(beginDate!=null){
				query= query.setParameter("beginDate", beginDate);
			}
			if(endDate!=null){
				query =query.setParameter("endDate", endDate);
			}
			if(member!=null){
				query =query.setParameter("member", member);
			}
			netCharge = query.getSingleResult();
			
			jpql ="select sum(s.costCharge) from Shipping s "+where;
			query=entityManager.createQuery(jpql, BigDecimal.class).setFlushMode(FlushModeType.COMMIT);
			if(beginDate!=null){
				query= query.setParameter("beginDate", beginDate);
			}
			if(endDate!=null){
				query =query.setParameter("endDate", endDate);
			}
			if(member!=null){
				query =query.setParameter("member", member);
			}
			costCharge=query.getSingleResult();
			income = new Income(netCharge,costCharge);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return income;
	}
}

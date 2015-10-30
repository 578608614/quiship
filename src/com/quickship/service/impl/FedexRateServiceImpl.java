package com.quickship.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.axis.types.NonNegativeInteger;
import org.apache.axis.types.PositiveInteger;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fedex.rate.stub.Address;
import com.fedex.rate.stub.ClientDetail;
import com.fedex.rate.stub.Contact;
import com.fedex.rate.stub.Dimensions;
import com.fedex.rate.stub.DropoffType;
import com.fedex.rate.stub.LinearUnits;
import com.fedex.rate.stub.Money;
import com.fedex.rate.stub.Notification;
import com.fedex.rate.stub.NotificationSeverityType;
import com.fedex.rate.stub.PackageRateDetail;
import com.fedex.rate.stub.PackagingType;
import com.fedex.rate.stub.Party;
import com.fedex.rate.stub.Payment;
import com.fedex.rate.stub.PaymentType;
import com.fedex.rate.stub.RatePortType;
import com.fedex.rate.stub.RateReply;
import com.fedex.rate.stub.RateReplyDetail;
import com.fedex.rate.stub.RateRequest;
import com.fedex.rate.stub.RateServiceLocator;
import com.fedex.rate.stub.RatedPackageDetail;
import com.fedex.rate.stub.RatedShipmentDetail;
import com.fedex.rate.stub.RequestedPackageLineItem;
import com.fedex.rate.stub.RequestedShipment;
import com.fedex.rate.stub.ServiceType;
import com.fedex.rate.stub.ShipmentRateDetail;
import com.fedex.rate.stub.Surcharge;
import com.fedex.rate.stub.TransactionDetail;
import com.fedex.rate.stub.VersionId;
import com.fedex.rate.stub.WebAuthenticationCredential;
import com.fedex.rate.stub.WebAuthenticationDetail;
import com.fedex.rate.stub.Weight;
import com.fedex.rate.stub.WeightUnits;
import com.quickship.entity.Shipping;
import com.quickship.entity.Shipping.PackageType;
import com.quickship.entity.Shipping.ServerType;
import com.quickship.entity.ShippingItem;

@Service("fedexRateServiceImpl")
public class FedexRateServiceImpl {
	@Value("${fedex.accountNumber}")
	private String accountNumber;
	@Value("${fedex.meterNumber}")
	private String meterNumber;
	@Value("${fedex.key}")
	private String key;
	@Value("${fedex.password}")
	private String password;
//	@Value("${fedex.payorAccountNumber}")
//	private String payorAccountNumber;

	private static Logger logger = Logger.getLogger(FedexRateServiceImpl.class);
		

	/**
	 * 验证Shipping
	 * @param shipping
	 * @return 通过返回null 不通过返回ShipmentReply
	 */
	public List<RateReply> calcuRateShippments(Shipping shipping){
		RateRequest rateRequest =  buildRateRequest(shipping,null,shipping.getShipItems().get(0),1);
		RateServiceLocator service = new RateServiceLocator();;
		updateEndPoint(service);
		RateReply masterReply = null;
		List<RateReply> list = new ArrayList<RateReply>();
		try {
			RatePortType port = service.getRateServicePort();
			masterReply = port.getRates(rateRequest); 
			if (isResponseOk(masterReply.getHighestSeverity())) {
				list.add(masterReply);
				printShipmentRating(masterReply);
				if(shipping.getShipItems().size()>1){
					ShippingItem item = null;
					for(int i=1;i<shipping.getShipItems().size();i++){
						item=shipping.getShipItems().get(i);
						RateRequest childRequest= buildRateRequest(shipping, masterReply,item,i+1);
						service = new RateServiceLocator();
						updateEndPoint(service);
						port = service.getRateServicePort();
						RateReply childReply = port.getRates(childRequest);
						if(isResponseOk(childReply.getHighestSeverity())){
							printShipmentRating(childReply);
							list.add(childReply);
						}else{
							printNotifications(childReply.getNotifications());
							list.add(childReply);
							return list;
						}
						
					}
				}
			}else{
				printNotifications(masterReply.getNotifications());
				list.add(masterReply);
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	private RateRequest buildRateRequest(Shipping shipping, RateReply masterReply,ShippingItem item,int index){
		RateRequest rateRequest = new RateRequest();
		//客户账户信息
		rateRequest.setClientDetail(createClientDetail());
        //客户端账户验证信息
		rateRequest.setWebAuthenticationDetail(createWebAuthenticationDetail());
		rateRequest.setReturnTransitAndCommit(true);
		//设置交易ID
		TransactionDetail transactionDetail = new TransactionDetail();
	    transactionDetail.setCustomerTransactionId("quickship"+index); // The client will get the same value back in the response
	    rateRequest.setTransactionDetail(transactionDetail);
	    //设置请求版本
	    VersionId versionId = new VersionId("crs", 16, 0, 0);
	    rateRequest.setVersion(versionId);
        //创建requestedShipment
        RequestedShipment requestedShipment = createRequestedShipment(shipping.getServerType(),shipping.getPakageType(),shipping.getDropoffType());
        // 设置发货人
     	requestedShipment.setShipper(setAddress(shipping.getOriAddress()));
 		// 设置收货人
 		requestedShipment.setRecipient(setAddress(shipping.getDesAddress()));
 		// 设置支付账户信息
 		requestedShipment.setShippingChargesPayment(setShippingChargesPayment());
 		// 包裹数 
 		requestedShipment.setPackageCount(new NonNegativeInteger(String.valueOf(shipping.getShipItems().size())));
 		// 设置包裹项
 		requestedShipment.setRequestedPackageLineItems(new RequestedPackageLineItem[] { setPackageItem(item,index) });
 		
 		requestedShipment.setTotalWeight(new Weight(WeightUnits.LB,shipping.getWeight()));
 		requestedShipment.setTotalInsuredValue(new Money("USD",shipping.getInsured() ));
 		
 		
 		rateRequest.setRequestedShipment(requestedShipment);
		return rateRequest;
	}
	
	/**
	 * 客户账户信息
	 * 
	 * @return
	 */
  	private ClientDetail createClientDetail() {
		ClientDetail clientDetail = new ClientDetail();
		clientDetail.setAccountNumber(accountNumber);
		clientDetail.setMeterNumber(meterNumber);
		return clientDetail;
	}

	/**
	 * 客户端账户验证信息
	 * 
	 * @return
	 */
	private WebAuthenticationDetail createWebAuthenticationDetail() {
		WebAuthenticationCredential wac = new WebAuthenticationCredential();
		wac.setKey(key);
		wac.setPassword(password);
		return new WebAuthenticationDetail(wac);
	}

	/**
	 * 设置账户费用信息
	 * 
	 * @return
	 */
	private Payment setShippingChargesPayment() {
		Payment payment = new Payment(); // Payment information
		payment.setPaymentType(PaymentType.SENDER);
//		Payor payor = new Payor();
//		Party responsibleParty = new Party();
//		responsibleParty.setAccountNumber(payorAccountNumber);
//		Address responsiblePartyAddress = new Address();
//		responsiblePartyAddress.setCountryCode("US");
//		responsibleParty.setAddress(responsiblePartyAddress);
//		responsibleParty.setContact(new Contact());
//		payor.setResponsibleParty(responsibleParty);
//		payment.setPayor(payor);
		return payment;
	}

	/**
	 * 创建RequestedShipment
	 * @param serType
	 * @param packType
	 * @param dropType
	 * @return
	 */
	private RequestedShipment createRequestedShipment(ServerType serType,Shipping.PackageType packType,Shipping.DropoffType dropType){
		RequestedShipment requestedShipment = new RequestedShipment();
		requestedShipment.setShipTimestamp(Calendar.getInstance());
		if (serType == Shipping.ServerType.FEDEX_GROUND) {
			requestedShipment.setServiceType(ServiceType.FEDEX_GROUND);
		} else if (serType == Shipping.ServerType.GROUND_HOME_DELIVERY) {
			requestedShipment.setServiceType(ServiceType.GROUND_HOME_DELIVERY);
		} else if (serType == Shipping.ServerType.FIRST_OVERNIGHT) {
			requestedShipment.setServiceType(ServiceType.FIRST_OVERNIGHT);
		} else if (serType == Shipping.ServerType.PRIORITY_OVERNIGHT) {
			requestedShipment.setServiceType(ServiceType.PRIORITY_OVERNIGHT);
		} else if (serType == Shipping.ServerType.STANDARD_OVERNIGHT) {
			requestedShipment.setServiceType(ServiceType.STANDARD_OVERNIGHT);
		} else if (serType == Shipping.ServerType.FEDEX_2_DAY) {
			requestedShipment.setServiceType(ServiceType.FEDEX_2_DAY);
		} else if (serType == Shipping.ServerType.FEDEX_2_DAY_AM) {
			requestedShipment.setServiceType(ServiceType.FEDEX_2_DAY_AM);
		} else if (serType == Shipping.ServerType.FEDEX_EXPRESS_SAVER) {
			requestedShipment.setServiceType(ServiceType.FEDEX_EXPRESS_SAVER);
		}
//		} else if (serType == Shipping.ServerType.FEDEX_FIRST_FREIGHT) {
//			requestedShipment.setServiceType(ServiceType.FEDEX_FIRST_FREIGHT);
//		} else if (serType == Shipping.ServerType.FEDEX_1_DAY_FREIGHT) {
//			requestedShipment.setServiceType(ServiceType.FEDEX_1_DAY_FREIGHT);
//		} else if (serType == Shipping.ServerType.FEDEX_2_DAY_FREIGHT) {
//			requestedShipment.setServiceType(ServiceType.FEDEX_2_DAY_FREIGHT);
//		} else if (serType == Shipping.ServerType.FEDEX_3_DAY_FREIGHT) {
//			requestedShipment.setServiceType(ServiceType.FEDEX_3_DAY_FREIGHT);
//		}

		if (dropType == Shipping.DropoffType.DROP_BOX) {
			requestedShipment.setDropoffType(DropoffType.DROP_BOX);
		} else if (dropType == Shipping.DropoffType.REGULAR_PICKUP) {
			requestedShipment.setDropoffType(DropoffType.REGULAR_PICKUP);
		}

		if (packType == PackageType.FEDEX_BOX) {
			requestedShipment.setPackagingType(PackagingType.FEDEX_BOX);
		} else if (packType == PackageType.FEDEX_ENVELOPE) {
			requestedShipment.setPackagingType(PackagingType.FEDEX_ENVELOPE);
		} else if (packType == PackageType.FEDEX_PAK) {
			requestedShipment.setPackagingType(PackagingType.FEDEX_PAK);
		} else if (packType == PackageType.FEDEX_TUBE) {
			requestedShipment.setPackagingType(PackagingType.FEDEX_TUBE);
		} else if (packType == PackageType.YOUR_PACKAGING) {
			requestedShipment.setPackagingType(PackagingType.YOUR_PACKAGING);
		}
		return requestedShipment;
	}
	
	/**
	 * 设置收件人和发件人信息
	 * 
	 * @param address
	 * @return
	 */
	private Party setAddress(com.quickship.entity.Address address) {
		Party shipperParty = new Party();
		Contact shipperContact = new Contact();
		shipperContact.setPersonName(address.getPhone());
		shipperContact.setCompanyName(address.getCompany());
		shipperContact.setPhoneNumber(address.getPhone());
		Address shipperAddress = new Address();
		String[] streets = new String[] { address.getAddress1() };
		if (address.getAddress2() != null && !"".equals(address.getAddress2())) {
			streets[1] = address.getAddress2();
		}
		shipperAddress.setStreetLines(streets);
		shipperAddress.setCity(address.getCity());
		if (address.getArea() != null) {
			if (address.getArea().getParent() == null) {
				shipperAddress.setCountryCode(address.getArea().getValue());
			} else {
				shipperAddress.setStateOrProvinceCode(address.getArea().getValue());
				shipperAddress.setCountryCode(address.getArea().getParent().getValue());
			}
		}
		shipperAddress.setPostalCode(address.getZipCode());
		shipperParty.setContact(shipperContact);
		shipperParty.setAddress(shipperAddress);
		shipperAddress.setResidential(false);
		if (address.getIsResidential() != null) {
			shipperAddress.setResidential(address.getIsResidential());
		}
		return shipperParty;
	}


	/**
	 * 设置包裹项
	 * @param item
	 * @param index
	 * @return
	 */
	private RequestedPackageLineItem setPackageItem(ShippingItem item,int index){
		RequestedPackageLineItem packageItem = new RequestedPackageLineItem();
		packageItem.setWeight(new Weight(WeightUnits.LB, item.getWeight()));
		packageItem.setInsuredValue(new Money("USD", item.getInsured()));
		packageItem.setSequenceNumber(new PositiveInteger(String.valueOf(index)));
		packageItem.setGroupPackageCount(new NonNegativeInteger("1"));
		if (item.getLength() != null && item.getHeight() != null && item.getWidth() != null) {
			Dimensions dimensions = new Dimensions();
			dimensions.setLength(new NonNegativeInteger(item.getLength().toString()));
			dimensions.setHeight(new NonNegativeInteger(item.getHeight().toString()));
			dimensions.setWidth(new NonNegativeInteger(item.getWidth().toString()));
			dimensions.setUnits(LinearUnits.IN);
			packageItem.setDimensions(dimensions);
		}
		return packageItem;
	}
	/**
	 * 
	 * @param serviceLocator
	 */
	private void updateEndPoint(RateServiceLocator serviceLocator) {
		String endPoint = System.getProperty("endPoint");
		if (endPoint != null) {
			serviceLocator.setRateServicePortEndpointAddress(endPoint);
		}
	}
	
	public boolean isResponseOk(NotificationSeverityType notificationSeverityType) {
		if (notificationSeverityType == null) {
			return false;
		}
		if (notificationSeverityType.equals(NotificationSeverityType.WARNING) || notificationSeverityType.equals(NotificationSeverityType.NOTE) || notificationSeverityType.equals(NotificationSeverityType.SUCCESS)) {
			return true;
		}
		return false;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private void printNotifications(Notification[] notifications) {
		if (notifications == null || notifications.length == 0) {
			logger.error("  No notifications returned");
			System.out.println("  No notifications returned");
		}
		for (int i = 0; i < notifications.length; i++) {
			Notification n = notifications[i];
			System.out.print("  Notification no. " + i + ": ");
			if (n == null) {
				logger.error("Notification is null");
				System.out.println("null");
				continue;
			}
			NotificationSeverityType nst = n.getSeverity();
			logger.error("Severity: " + (nst == null ? "null" : nst.getValue()) + "    Code: " + n.getCode() + "    Message: " + n.getMessage() + "    Source: " + n.getSource());
			System.out.println("    Severity: " + (nst == null ? "null" : nst.getValue()));
			System.out.println("    Code: " + n.getCode());
			System.out.println("    Message: " + n.getMessage());
			System.out.println("    Source: " + n.getSource());
		}
	}
	
	
	
	//===================测试==========================
	
	private static void printShipmentRating(RateReply reply){
		RateReplyDetail[] rrds = reply.getRateReplyDetails();
		for (int i = 0; i < rrds.length; i++) {
			RateReplyDetail rrd = rrds[i];
			print("Service type", rrd.getServiceType());
			print("Packaging type", rrd.getPackagingType());
			//print("Delivery DOW", rrd.getDeliveryDayOfWeek());
			if(rrd.getDeliveryTimestamp()!=null){
				int month = rrd.getDeliveryTimestamp().get(Calendar.MONTH)+1;
				int date = rrd.getDeliveryTimestamp().get(Calendar.DAY_OF_MONTH);
				int year = rrd.getDeliveryTimestamp().get(Calendar.YEAR);
				String delDate = new String(month + "/" + date + "/" + year);
				print("Delivery date", delDate);
				print("Calendar DOW", rrd.getDeliveryTimestamp().get(Calendar.DAY_OF_WEEK));
			}
			
			RatedShipmentDetail[] rsds = rrd.getRatedShipmentDetails();
			for (int j = 0; j < rsds.length; j++) {
				print("RatedShipmentDetail " + j, "");
				RatedShipmentDetail rsd = rsds[j];
				ShipmentRateDetail srd = rsd.getShipmentRateDetail();
				print("  Rate type", srd.getRateType());
				printWeight("  Total Billing weight", srd.getTotalBillingWeight());
				printMoney("  Total surcharges", srd.getTotalSurcharges());
				printMoney("  Total net charge", srd.getTotalNetCharge());
				printMoney("  Total base charge", srd.getTotalBaseCharge());

				RatedPackageDetail[] rpds = rsd.getRatedPackages();
				if (rpds != null && rpds.length > 0) {
					print("RatedPackageDetails", "");
					for (int k = 0; k < rpds.length; k++) {
						print("  RatedPackageDetail " + i, "");
						RatedPackageDetail rpd = rpds[k];
						PackageRateDetail prd = rpd.getPackageRateDetail();
						if (prd != null) {
							printWeight("    Billing weight", prd.getBillingWeight());
							printMoney("    Base charge", prd.getBaseCharge());
							Surcharge[] surcharges = prd.getSurcharges();
							if (surcharges != null && surcharges.length > 0) {
								for (int m = 0; m < surcharges.length; m++) {
									Surcharge surcharge = surcharges[m];
									printMoney("    " + surcharge.getDescription() + " surcharge", surcharge.getAmount());
								}
							}
						}
					}
				}
			}
			System.out.println("");
			System.out.println("**********************");
			System.out.println("");
		}
	}
	
	private static void print(String msg, Object obj) {
		if (msg == null || obj == null) {
			return;
		}
		System.out.println(msg + ": " + obj.toString());
	}
	
	private static void printMoney(String msg, Money money) {
		if (msg == null || money == null) {
			return;
		}
		System.out.println(msg + ": " + money.getAmount() + " " + money.getCurrency());
	}
	
	private static void printWeight(String msg, Weight weight) {
		if (msg == null || weight == null) {
			return;
		}
		System.out.println(msg + ": " + weight.getValue() + " " + weight.getUnits());
	}

	
}

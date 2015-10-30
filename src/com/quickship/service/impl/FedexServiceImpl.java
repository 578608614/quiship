package com.quickship.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.axis.types.NonNegativeInteger;
import org.apache.axis.types.PositiveInteger;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fedex.ship.stub.Address;
import com.fedex.ship.stub.ClientDetail;
import com.fedex.ship.stub.CompletedPackageDetail;
import com.fedex.ship.stub.CompletedShipmentDetail;
import com.fedex.ship.stub.Contact;
import com.fedex.ship.stub.Dimensions;
import com.fedex.ship.stub.DropoffType;
import com.fedex.ship.stub.LabelFormatType;
import com.fedex.ship.stub.LabelSpecification;
import com.fedex.ship.stub.LinearUnits;
import com.fedex.ship.stub.Money;
import com.fedex.ship.stub.Notification;
import com.fedex.ship.stub.NotificationSeverityType;
import com.fedex.ship.stub.PackageRateDetail;
import com.fedex.ship.stub.PackageRating;
import com.fedex.ship.stub.PackagingType;
import com.fedex.ship.stub.Party;
import com.fedex.ship.stub.Payment;
import com.fedex.ship.stub.PaymentType;
import com.fedex.ship.stub.Payor;
import com.fedex.ship.stub.ProcessShipmentReply;
import com.fedex.ship.stub.ProcessShipmentRequest;
import com.fedex.ship.stub.RequestedPackageLineItem;
import com.fedex.ship.stub.RequestedShipment;
import com.fedex.ship.stub.ServiceType;
import com.fedex.ship.stub.ShipPortType;
import com.fedex.ship.stub.ShipServiceLocator;
import com.fedex.ship.stub.ShipmentRateDetail;
import com.fedex.ship.stub.ShipmentRating;
import com.fedex.ship.stub.ShipmentReply;
import com.fedex.ship.stub.ShippingDocumentImageType;
import com.fedex.ship.stub.Surcharge;
import com.fedex.ship.stub.TrackingId;
import com.fedex.ship.stub.TransactionDetail;
import com.fedex.ship.stub.ValidateShipmentRequest;
import com.fedex.ship.stub.VersionId;
import com.fedex.ship.stub.WebAuthenticationCredential;
import com.fedex.ship.stub.WebAuthenticationDetail;
import com.fedex.ship.stub.Weight;
import com.fedex.ship.stub.WeightUnits;
import com.quickship.entity.Shipping;
import com.quickship.entity.Shipping.PackageType;
import com.quickship.entity.Shipping.ServerType;
import com.quickship.entity.ShippingItem;

@Service("fedexServiceImpl")
public class FedexServiceImpl {
	@Value("${fedex.accountNumber}")
	private String accountNumber;
	@Value("${fedex.meterNumber}")
	private String meterNumber;
	@Value("${fedex.key}")
	private String key;
	@Value("${fedex.password}")
	private String password;
	@Value("${fedex.payorAccountNumber}")
	private String payorAccountNumber;

	private static Logger logger = Logger.getLogger(FedexServiceImpl.class);
	
	/**
	 *	创建简单发货信息
	 * @param shipping
	 * @return
	 */
	public ProcessShipmentReply createShipment(Shipping shipping) {
		ProcessShipmentRequest request = buildRequest(shipping);
		ProcessShipmentReply reply = null;
		try {
			ShipServiceLocator service = new ShipServiceLocator();
			updateEndPoint(service);
			ShipPortType port = service.getShipServicePort();
			reply = port.processShipment(request);
			if (isResponseOk(reply.getHighestSeverity())) {
				return reply;
			} else {
				printNotifications(reply.getNotifications());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reply;
	}
	
	/**
	 * 建立简单发货请求
	 * @param shipping
	 * @return
	 */
	public ProcessShipmentRequest buildRequest(Shipping shipping) {

		ProcessShipmentRequest request = new ProcessShipmentRequest();
		// 设置账户信息
		request.setClientDetail(createClientDetail());
		// 设置账户验证
		request.setWebAuthenticationDetail(createWebAuthenticationDetail());

		TransactionDetail transactionDetail = new TransactionDetail();
		transactionDetail.setCustomerTransactionId("quickship");
		request.setTransactionDetail(transactionDetail);

		request.setVersion(new VersionId("ship", 15, 0, 0));
		
		RequestedShipment requestedShipment = createRequestedShipment(shipping.getServerType(),shipping.getPakageType(),shipping.getDropoffType());
		// 设置发货人
		requestedShipment.setShipper(setAddress(shipping.getOriAddress()));
		// 设置收货人
		requestedShipment.setRecipient(setAddress(shipping.getDesAddress()));
		// 设置支付账户信息
		requestedShipment.setShippingChargesPayment(setShippingChargesPayment());
		// 设置标签格式
		requestedShipment.setLabelSpecification(setLabelSpecification());
		// 设置包裹数量
		List<ShippingItem> items = shipping.getShipItems();
		// 包裹数 
		requestedShipment.setPackageCount(new NonNegativeInteger(String.valueOf(items.size())));
		// 设置包裹项
		RequestedPackageLineItem packageItem = new RequestedPackageLineItem();
		packageItem.setWeight(new Weight(WeightUnits.LB, shipping.getWeight()));
		packageItem.setInsuredValue(new Money("USD", shipping.getInsured()));
		requestedShipment.setRequestedPackageLineItems(new RequestedPackageLineItem[] { packageItem });
		
		request.setRequestedShipment(requestedShipment);
		return request;
	}
	
	/**
	 * 创建多个MPS发货信息
	 * @param shipping
	 * @return
	 */
	public List<ProcessShipmentReply> createShipments(Shipping shipping) {
		ProcessShipmentRequest request =  buildRequest(shipping,null,shipping.getShipItems().get(0),1);
		List<ProcessShipmentReply> listReply = new ArrayList<ProcessShipmentReply>();
		try {
			ShipServiceLocator service = new ShipServiceLocator();
			updateEndPoint(service);
			ShipPortType port = service.getShipServicePort();
			ProcessShipmentReply masterReply = port.processShipment(request);
			if (isResponseOk(masterReply.getHighestSeverity())) {
				listReply.add(masterReply);
				CompletedShipmentDetail csd =masterReply.getCompletedShipmentDetail();
				shipping.setDeliveryDate(csd.getOperationalDetail().getDeliveryDate());
				/*----------*/
				printShipmentRating(csd.getShipmentRating());
				CompletedPackageDetail[]  cpd = csd.getCompletedPackageDetails();
				printPackageDetails(cpd);
				/*----------*/
				if(shipping.getShipItems().size()>1){
					ShippingItem item = null;
					for(int i=1;i<shipping.getShipItems().size();i++){
						item=shipping.getShipItems().get(i);
						ProcessShipmentRequest childRequest = buildRequest(shipping, masterReply,item,i+1);
						service = new ShipServiceLocator();
						updateEndPoint(service);
						port = service.getShipServicePort();
						ProcessShipmentReply childReply = port.processShipment(childRequest);
						if(isResponseOk(childReply.getHighestSeverity())){
							/*----------*/
							csd =childReply.getCompletedShipmentDetail();
							printShipmentRating(csd.getShipmentRating());
							cpd = csd.getCompletedPackageDetails();
							printPackageDetails(cpd);
							/*----------*/
							listReply.add(childReply);
						}else{
							printNotifications(childReply.getNotifications());
							listReply.add(childReply);
							return listReply;
						}
					}
				}
			} else {
				printNotifications(masterReply.getNotifications());
				listReply.add(masterReply);
				return listReply;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listReply;
	}

	/**
	 * 建立多个MPS发货请求
	 * @param shipping
	 * @param masterReply
	 * @param item
	 * @param index
	 * @return
	 */
	public ProcessShipmentRequest buildRequest(Shipping shipping,ProcessShipmentReply masterReply,ShippingItem item,int index){
		ProcessShipmentRequest request = new ProcessShipmentRequest();
		// 设置账户信息
		request.setClientDetail(createClientDetail());
		// 设置账户验证
		request.setWebAuthenticationDetail(createWebAuthenticationDetail());
		// 设置交易ID
		TransactionDetail transactionDetail = new TransactionDetail();
		transactionDetail.setCustomerTransactionId("quickship"+index);
		request.setTransactionDetail(transactionDetail);
		// 设置版本
		request.setVersion(new VersionId("ship", 15, 0, 0));
		//创建requestedShipment
        RequestedShipment requestedShipment = createRequestedShipment(shipping.getServerType(),shipping.getPakageType(),shipping.getDropoffType());
		// 设置发货人
		requestedShipment.setShipper(setAddress(shipping.getOriAddress()));
		// 设置收货人
		requestedShipment.setRecipient(setAddress(shipping.getDesAddress()));
		// 设置支付账户信息
		requestedShipment.setShippingChargesPayment(setShippingChargesPayment());
		// 设置标签格式
		requestedShipment.setLabelSpecification(setLabelSpecification());
		// 包裹数 
		requestedShipment.setPackageCount(new NonNegativeInteger(String.valueOf(shipping.getShipItems().size())));
		// 设置包裹项
		requestedShipment.setRequestedPackageLineItems(new RequestedPackageLineItem[] { setPackageItem(item,index) });
		
		if(masterReply!=null){
			requestedShipment.setMasterTrackingId(new TrackingId());
			String trkNum = masterReply.getCompletedShipmentDetail().getCompletedPackageDetails()[0].getTrackingIds()[0].getTrackingNumber();
			requestedShipment.getMasterTrackingId().setTrackingNumber(trkNum);
		}
		requestedShipment.setTotalWeight(new Weight(WeightUnits.LB, shipping.getWeight()));
		requestedShipment.setTotalInsuredValue(new Money("USD", shipping.getInsured()));
		request.setRequestedShipment(requestedShipment);
		return request;
	}
	
	/**
	 * 验证Shipping
	 * @param shipping
	 * @return 通过返回null 不通过返回ShipmentReply
	 */
	public ShipmentReply validateShippments(Shipping shipping){
		ValidateShipmentRequest validateRequest =  buildValidateRequest(shipping,null,shipping.getShipItems().get(0),1);
		ShipServiceLocator service = new ShipServiceLocator();
		updateEndPoint(service);
		try {
			ShipPortType port = service.getShipServicePort();
			ShipmentReply masterReply = port.validateShipment(validateRequest);
			if (isResponseOk(masterReply.getHighestSeverity())) {
				if(shipping.getShipItems().size()>1){
					ShippingItem item = null;
					for(int i=1;i<shipping.getShipItems().size();i++){
						item=shipping.getShipItems().get(i);
						ValidateShipmentRequest childRequest= buildValidateRequest(shipping, masterReply,item,i+1);
						service = new ShipServiceLocator();
						updateEndPoint(service);
						port = service.getShipServicePort();
						ShipmentReply childReply = port.validateShipment(childRequest);
						if(!isResponseOk(childReply.getHighestSeverity())){
							printNotifications(childReply.getNotifications());
							return childReply;
						}
					}
				}
			}else{
				printNotifications(masterReply.getNotifications());
				return masterReply;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 建立多个MPS验证请求
	 * @param shipping
	 * @param masterReply
	 * @param item
	 * @param index
	 * @return
	 */
	private ValidateShipmentRequest buildValidateRequest(Shipping shipping,ShipmentReply masterReply,ShippingItem item,int index){
		ValidateShipmentRequest request = new ValidateShipmentRequest(); 
		//客户账户信息
		request.setClientDetail(createClientDetail());
        //客户端账户验证信息
		request.setWebAuthenticationDetail(createWebAuthenticationDetail());
        //设置交易ID
        TransactionDetail transactionDetail = new TransactionDetail();
	    transactionDetail.setCustomerTransactionId("quickship"+index); // The client will get the same value back in the response
	    request.setTransactionDetail(transactionDetail);
	    //设置请求版本
	    VersionId versionId = new VersionId("ship", 15, 0, 0);
        request.setVersion(versionId);
        //创建requestedShipment
        RequestedShipment requestedShipment = createRequestedShipment(shipping.getServerType(),shipping.getPakageType(),shipping.getDropoffType());
		// Total weight information
		requestedShipment.setTotalWeight(new Weight(WeightUnits.LB,shipping.getWeight()));
		// 设置发货人
		requestedShipment.setShipper(setAddress(shipping.getOriAddress()));
		// 设置收货人
		requestedShipment.setRecipient(setAddress(shipping.getDesAddress()));
		// 设置支付账户信息
		requestedShipment.setShippingChargesPayment(setShippingChargesPayment());
		// 设置标签格式
		requestedShipment.setLabelSpecification(setLabelSpecification());
		// 包裹数 
		requestedShipment.setPackageCount(new NonNegativeInteger(String.valueOf(shipping.getShipItems().size())));
		// 设置包裹项
		requestedShipment.setRequestedPackageLineItems(new RequestedPackageLineItem[] { setPackageItem(item,index) });
		
		request.setRequestedShipment(requestedShipment);
		return request;
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
		Payor payor = new Payor();
		Party responsibleParty = new Party();
		responsibleParty.setAccountNumber(payorAccountNumber);
		Address responsiblePartyAddress = new Address();
		responsiblePartyAddress.setCountryCode("US");
		responsibleParty.setAddress(responsiblePartyAddress);
		responsibleParty.setContact(new Contact());
		payor.setResponsibleParty(responsibleParty);
		payment.setPayor(payor);
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
	 * 设置标签格式
	 * 
	 * @return
	 */
	private LabelSpecification setLabelSpecification() {
		LabelSpecification labelSpecification = new LabelSpecification(); // Label specification
		labelSpecification.setImageType(ShippingDocumentImageType.PDF);// Image types PDF, PNG, DPL,
		labelSpecification.setLabelFormatType(LabelFormatType.COMMON2D); // LABEL_DATA_ONLY,COMMON2D
		// labelSpecification.setLabelStockType(LabelStockType.value2); //
		// STOCK_4X6.75_LEADING_DOC_TAB
		// labelSpecification.setLabelPrintingOrientation(LabelPrintingOrientationType.TOP_EDGE_OF_TEXT_FIRST);
		return labelSpecification;
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
		packageItem.setGroupPackageCount(new NonNegativeInteger ("1"));
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
	private void updateEndPoint(ShipServiceLocator serviceLocator) {
		String endPoint = System.getProperty("endPoint");
		if (endPoint != null) {
			serviceLocator.setShipServicePortEndpointAddress(endPoint);
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
	
	private static void printShipmentRating(ShipmentRating shipmentRating){
		if(shipmentRating!=null){
			System.out.println("Shipment Rate Details");
			ShipmentRateDetail[] srd = shipmentRating.getShipmentRateDetails();
			for(int j=0; j < srd.length; j++)
			{
				System.out.println("  Rate Type: " + srd[j].getRateType().getValue());
				printWeight(srd[j].getTotalBillingWeight(), "Shipment Billing Weight", "    ");
				printMoney(srd[j].getTotalBaseCharge(), "Shipment Total BasCharge", "    ");
				printMoney(srd[j].getTotalNetCharge(), 	"Shipment Total NetCharge", "    ");
				printMoney(srd[j].getTotalSurcharges(), "Shipment Total SurCharge", "    ");
				printMoney(srd[j].getTotalFreightDiscounts(), "Shipment Total FreightDiscount", "    ");
				printMoney(srd[j].getTotalRebates(), "Shipment Total Rebates", "    ");
				if (null != srd[j].getSurcharges())
				{
					System.out.println("    Surcharge Details");
					Surcharge[] s = srd[j].getSurcharges();
					for(int k=0; k < s.length; k++)
					{
						printMoney(s[k].getAmount(),s[k].getSurchargeType().getValue(), "      ");
					}
				}
				System.out.println();
			}
		}
	}
	private static void printPackageRating(PackageRating packageRating){
		if(packageRating!=null){
			System.out.println("Package Rate Details");
			PackageRateDetail[] prd = packageRating.getPackageRateDetails();
			for(int j=0; j < prd.length; j++)
			{
				System.out.println("  Rate Type: " + prd[j].getRateType().getValue());
				printWeight(prd[j].getBillingWeight(), "Billing Weight", "    ");
				printMoney(prd[j].getBaseCharge(), "Base Charge", "    ");
				printMoney(prd[j].getNetCharge(), "Net Charge", "    ");
				printMoney(prd[j].getTotalSurcharges(), "Total Surcharge", "    ");
				if (null != prd[j].getSurcharges())
				{
					System.out.println("    Surcharge Details");
					Surcharge[] s = prd[j].getSurcharges();
					for(int k=0; k < s.length; k++)
					{
						printMoney(s[k].getAmount(),s[k].getSurchargeType().getValue(), "      ");
					}
				}
				System.out.println();
			}
		}
	}
	private static void printPackageDetails(CompletedPackageDetail[] cpd) throws Exception{
		if(cpd!=null){
			System.out.println("Package Details");
			for (int i=0; i < cpd.length; i++) { // Package details / Rating information for each package
				String trackingNumber = cpd[i].getTrackingIds()[0].getTrackingNumber();
				System.out.println(trackingNumber);
				//
				printPackageRating(cpd[i].getPackageRating());
				System.out.println();
			}
		}
	}
	private static void printMoney(Money money, String description, String space){
		if(money!=null){
			System.out.println(space + description + ": " + money.getAmount() + " " + money.getCurrency());
		}
	}
	private static void printWeight(Weight weight, String description, String space){
		if(weight!=null){
			System.out.println(space + description + ": " + weight.getValue() + " " + weight.getUnits());
		}
	}

}

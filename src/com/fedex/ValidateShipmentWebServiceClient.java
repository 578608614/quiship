package com.fedex;
import java.math.BigDecimal;
import java.util.Calendar;

import org.apache.axis.types.NonNegativeInteger;
import org.apache.axis.types.PositiveInteger;

import com.fedex.ship.stub.Address;
import com.fedex.ship.stub.ClientDetail;
import com.fedex.ship.stub.Contact;
import com.fedex.ship.stub.CustomerReference;
import com.fedex.ship.stub.CustomerReferenceType;
import com.fedex.ship.stub.DropoffType;
import com.fedex.ship.stub.LabelFormatType;
import com.fedex.ship.stub.LabelSpecification;
import com.fedex.ship.stub.Money;
import com.fedex.ship.stub.Notification;
import com.fedex.ship.stub.NotificationSeverityType;
import com.fedex.ship.stub.PackagingType;
import com.fedex.ship.stub.Party;
import com.fedex.ship.stub.Payment;
import com.fedex.ship.stub.PaymentType;
import com.fedex.ship.stub.Payor;
import com.fedex.ship.stub.RequestedPackageLineItem;
import com.fedex.ship.stub.RequestedShipment;
import com.fedex.ship.stub.ServiceType;
import com.fedex.ship.stub.ShipPortType;
import com.fedex.ship.stub.ShipServiceLocator;
import com.fedex.ship.stub.ShipmentReply;
import com.fedex.ship.stub.ShippingDocumentImageType;
import com.fedex.ship.stub.TransactionDetail;
import com.fedex.ship.stub.ValidateShipmentRequest;
import com.fedex.ship.stub.VersionId;
import com.fedex.ship.stub.WebAuthenticationCredential;
import com.fedex.ship.stub.WebAuthenticationDetail;
import com.fedex.ship.stub.Weight;
import com.fedex.ship.stub.WeightUnits;




/** 
 * Sample code to call the FedEx Ship Service
 * <p>
 * com.fedex.ship.stub is generated via WSDL2Java, like this:<br>
 * <pre>
 * java org.apache.axis.wsdl.WSDL2Java -w -p com.fedex.ship.stub http://www.fedex.com/...../ShipService?wsdl
 * </pre>
 * 
 * This sample code has been tested with JDK 5 and Apache Axis 1.4
 */

public class ValidateShipmentWebServiceClient 
{
	//
	public static void main(String[] args) 
	{
		ValidateShipmentRequest request = buildRequest();
		//
		try {
			// Initialize the service
			ShipServiceLocator service;
			ShipPortType port;
			//
			service = new ShipServiceLocator();
			updateEndPoint(service);
			port = service.getShipServicePort();
		    //
			ShipmentReply reply = port.validateShipment(request); // This is the call to the ship web service passing in a request object and returning a reply object
			//
			printNotifications(reply.getNotifications());

		} catch (Exception e) {
		    e.printStackTrace();
		} 
	}
	
	private static ValidateShipmentRequest buildRequest()
	{
		ValidateShipmentRequest request = new ValidateShipmentRequest(); // Build a request object

        request.setClientDetail(createClientDetail());
        request.setWebAuthenticationDetail(createWebAuthenticationDetail());
        // 
	    TransactionDetail transactionDetail = new TransactionDetail();
	    transactionDetail.setCustomerTransactionId("java sample - ValidateShipment Request"); // The client will get the same value back in the response
	    request.setTransactionDetail(transactionDetail);

        //
        VersionId versionId = new VersionId("ship", 15, 0, 0);
        request.setVersion(versionId);
        //
	    RequestedShipment requestedShipment = new RequestedShipment();
	    Calendar shipTimeStamp = Calendar.getInstance();
	    shipTimeStamp.add(Calendar.DATE,1);
	    requestedShipment.setShipTimestamp(shipTimeStamp); // Ship date and time
	    requestedShipment.setDropoffType(DropoffType.REGULAR_PICKUP); // Dropoff Types are BUSINESS_SERVICE_CENTER, DROP_BOX, REGULAR_PICKUP, REQUEST_COURIER, STATION
	    requestedShipment.setServiceType(ServiceType.PRIORITY_OVERNIGHT); // Service types are STANDARD_OVERNIGHT, PRIORITY_OVERNIGHT, FEDEX_GROUND ...
//	    requestedShipment.setServiceType(ServiceType.FEDEX_GROUND);
	    requestedShipment.setPackagingType(PackagingType.YOUR_PACKAGING); // Packaging type FEDEX_BOX, FEDEX_PAK, FEDEX_TUBE, YOUR_PACKAGING, ...
	    //

	    Weight weight = new Weight(); // Total weight information
	    weight.setValue(new BigDecimal(50.0));
	    weight.setUnits(WeightUnits.LB);
	    requestedShipment.setTotalWeight(weight);
	    

	    //
	    Party shipperParty = new Party(); // Sender information
	    Contact shipperContact = new Contact();
	    shipperContact.setPersonName("Sender Name");
	    shipperContact.setCompanyName("Sender Company Name");
	    shipperContact.setPhoneNumber("0805522713");
	    Address shipperAddress = new Address();
	    shipperAddress.setStreetLines(new String[] {"Address Line 1"});
	    shipperAddress.setCity("Austin");
	    shipperAddress.setStateOrProvinceCode("TX");
	    shipperAddress.setPostalCode("73301");
	    shipperAddress.setCountryCode("US");	    
	    shipperParty.setContact(shipperContact);
	    shipperParty.setAddress(shipperAddress);
	    requestedShipment.setShipper(shipperParty);
        //
	    Party recipientParty = new Party(); // Recipient information
	    Contact recipientContact = new Contact();
	    recipientContact.setPersonName("Recipient Name");
	    recipientContact.setCompanyName("Recipient Company Name");
	    recipientContact.setPhoneNumber("9012637906");
	    Address recipientAddress = new Address();
	    recipientAddress.setStreetLines(new String[] {"Address Line 1"});
	    recipientAddress.setCity("Windsor");
	    recipientAddress.setStateOrProvinceCode("CT");
	    recipientAddress.setPostalCode("06006");
	    recipientAddress.setCountryCode("US");
	    recipientAddress.setResidential(Boolean.valueOf(true));	    
	    recipientParty.setContact(recipientContact);
	    recipientParty.setAddress(recipientAddress);
	    requestedShipment.setRecipient(recipientParty);
        //
	    requestedShipment.setShippingChargesPayment(addShippingChargesPayment());
	    //	    
	    LabelSpecification labelSpecification = new LabelSpecification(); // Label specification	    
	    labelSpecification.setImageType(ShippingDocumentImageType.PDF);// Image types PDF, PNG, DPL, ...	
	    labelSpecification.setLabelFormatType(LabelFormatType.COMMON2D); //LABEL_DATA_ONLY, COMMON2D
	    requestedShipment.setLabelSpecification(labelSpecification);
	    
        //
	    requestedShipment.setPackageCount(new NonNegativeInteger("1"));

	    //

	    RequestedPackageLineItem rp = new RequestedPackageLineItem();
	    rp.setSequenceNumber(new PositiveInteger("1"));
	    rp.setInsuredValue(new Money("USD", new BigDecimal(10.00)));
	    rp.setWeight(new Weight(WeightUnits.LB, new BigDecimal(50.0)));
	    rp.setItemDescription("item desc - 1");
	    CustomerReference customerReference = new CustomerReference(CustomerReferenceType.CUSTOMER_REFERENCE, "D-ANY-F42953XPRS12");
	    rp.setCustomerReferences(new CustomerReference[] {customerReference});
	    
	    requestedShipment.setRequestedPackageLineItems(new RequestedPackageLineItem[] {rp});
	    
	    request.setRequestedShipment(requestedShipment);
	    //
		return request;
	}
		
	private static String getPayorAccountNumber() {
		// See if payor account number is set as system property,
		// if not default it to "XXX"
		String payorAccountNumber = System.getProperty("Payor.AccountNumber");
		if (payorAccountNumber == null) {
			payorAccountNumber = "510087089"; // 510087089510087089Replace "XXX" with the payor account number
		}
		return payorAccountNumber;
	}
	
	private static ClientDetail createClientDetail() {
        ClientDetail clientDetail = new ClientDetail();
        String accountNumber = System.getProperty("accountNumber");
        String meterNumber = System.getProperty("meterNumber");
        
        //
        // See if the accountNumber and meterNumber properties are set,
        // if set use those values, otherwise default them to "XXX"
        //
        if (accountNumber == null) {
        	accountNumber = "510087089"; // Replace "XXX" with clients account number
        }
        if (meterNumber == null) {
        	meterNumber = "118546777"; // Replace "XXX" with clients meter number
        }
        clientDetail.setAccountNumber(accountNumber);
        clientDetail.setMeterNumber(meterNumber);
        return clientDetail;
	}
	
	private static WebAuthenticationDetail createWebAuthenticationDetail() {
        WebAuthenticationCredential wac = new WebAuthenticationCredential();
        String key = System.getProperty("key");
        String password = System.getProperty("password");
        
        //
        // See if the key and password properties are set,
        // if set use those values, otherwise default them to "XXX"
        //
        if (key == null) {
        	key = "GeqHm1Kuwpf3PIfm"; // Replace "XXX" with clients key
        }
        if (password == null) {
        	password = "vRI1GbRIEeUdX5d523HatrY5w"; // Replace "XXX" with clients password
        }
        wac.setKey(key);
        wac.setPassword(password);
		return new WebAuthenticationDetail(wac);
	}
	
	private static void printNotifications(Notification[] notifications) {
		System.out.println("Notifications:");
		if (notifications == null || notifications.length == 0) {
			System.out.println("  No notifications returned");
		}
		for (int i=0; i < notifications.length; i++){
			Notification n = notifications[i];
			System.out.print("  Notification no. " + i + ": ");
			if (n == null) {
				System.out.println("null");
				continue;
			} else {
				System.out.println("");
			}
			NotificationSeverityType nst = n.getSeverity();

			System.out.println("    Severity: " + (nst == null ? "null" : nst.getValue()));
			System.out.println("    Code: " + n.getCode());
			System.out.println("    Message: " + n.getMessage());
			System.out.println("    Source: " + n.getSource());
		}
	}
	
	private static Payment addShippingChargesPayment(){
	    Payment payment = new Payment(); // Payment information
	    payment.setPaymentType(PaymentType.SENDER);
	    Payor payor = new Payor();
	    Party responsibleParty = new Party();
	    responsibleParty.setAccountNumber(getPayorAccountNumber());
	    Address responsiblePartyAddress = new Address();
	    responsiblePartyAddress.setCountryCode("US");
	    responsibleParty.setAddress(responsiblePartyAddress);
	    responsibleParty.setContact(new Contact());
		payor.setResponsibleParty(responsibleParty);
	    payment.setPayor(payor);
	    return payment;
	}
	
	private static void updateEndPoint(ShipServiceLocator serviceLocator) {
		String endPoint = System.getProperty("endPoint");
		if (endPoint != null) {
			serviceLocator.setShipServicePortEndpointAddress(endPoint);
		}
	}

}


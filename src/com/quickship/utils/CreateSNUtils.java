package com.quickship.utils;

import java.util.Calendar;

import org.apache.commons.lang.math.RandomUtils;

public class CreateSNUtils {
	private static String  prefix = null;
	private static Integer paymentBase= 1;
	private static Integer shipBase= 1;

	private enum SNType{
		shipOrder,
		payment
	}
	
	private static String createSN(SNType type,int size){
		Calendar now=Calendar.getInstance();
		String year = String.valueOf(now.get(Calendar.YEAR)).substring(2, 4);
		String month =  String.valueOf(now.get(Calendar.MONTH)+1);
		if(month.length()==1){
			month="0"+month;
		}
		String day =  String.valueOf(now.get(Calendar.DATE));
		if(day.length()==1){
			day = "0"+day;
		}
		String str = year+month+day;
		StringBuffer suffix = null;
		if(type==SNType.payment){
			if(!str.equals(prefix)){
				paymentBase=1;
			}else{
				paymentBase++;
			}
			suffix=new StringBuffer(paymentBase.toString());
		}else if(type==SNType.shipOrder){
			if(!str.equals(prefix)){
				shipBase=1;
			}else{
				shipBase++;
			}
			suffix=new StringBuffer(shipBase.toString());
		}
		prefix = str;
		if(size<=6)size=10;
		while(suffix.length()<size-6){
			suffix.insert(0, RandomUtils.nextInt(10));
		}
		str=str+suffix.toString();
		return str;
		
	}
	public static String createPaymentSN(int size){
		return createSN(SNType.payment,size);
	}
	public static String createShipOrderSN(int size){
		return createSN(SNType.shipOrder,size);
	}
}

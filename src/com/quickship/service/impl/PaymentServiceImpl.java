package com.quickship.service.impl;

import java.util.Date;

import javax.annotation.Resource;
import javax.persistence.FlushModeType;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.quickship.entity.Member;
import com.quickship.entity.Payment;
import com.quickship.entity.Payment.Status;
import com.quickship.entity.Payment.Type;
import com.quickship.service.MemberService;
import com.quickship.service.PaymentService;
import com.quickship.service.base.BaseDaoImpl;
@Service("paymentServiceImpl")
public class PaymentServiceImpl extends BaseDaoImpl<Payment,Long>implements PaymentService {
	
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	
	@Transactional(readOnly = true)
	public Payment findPaymentBySN(String sn) {
		if(sn!=null){
			String jpdl = "select payment from Payment payment where payment.sn=:sn";
			try{
				Payment payment =  entityManager.createQuery(jpdl, Payment.class).setFlushMode(FlushModeType.COMMIT).setParameter("sn", sn).getSingleResult();
				return payment;
			}catch (Exception e) {
				return null;
			}
			
		}
		return null;
	}
	
	@Transactional
	public void handle(Payment payment) {
//		try{
//			entityManager.refresh(payment, LockModeType.PESSIMISTIC_WRITE);
//		}catch (Exception e) {
//			e.printStackTrace();
//		}
		try{
			if(payment!=null&&payment.getStatus() == Status.wait){
				if(payment.getType()==Type.recharge){
					//预存款充值
					Member member = payment.getMember();
					if(member!=null){
						memberService.update(member,payment);
					}
				}else if(payment.getType()==Type.payment){
					//订单支付
				}
				payment.setStatus(Status.success);
				payment.setPaymentDate(new Date());
				super.update(payment);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
}

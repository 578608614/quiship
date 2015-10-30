package com.quickship.service.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.FlushModeType;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.quickship.entity.Deposit;
import com.quickship.entity.Deposit.Type;
import com.quickship.entity.Member;
import com.quickship.entity.Payment;
import com.quickship.service.DepositService;
import com.quickship.service.MemberService;
import com.quickship.service.base.BaseDaoImpl;

@Transactional
@Service("memberServiceImpl")
public class MemberServiceImpl extends BaseDaoImpl<Member, Long> implements MemberService {

	@Resource(name = "depositServiceImpl")
	private DepositService depositService;

	@Transactional(readOnly = true)
	public List<Member> findByEmail(String email) {
		if (email == null) {
			return null;
		}
		String jpql = "select members from Member members where lower(members.email) = lower(:email)";
		return entityManager.createQuery(jpql, Member.class).setFlushMode(FlushModeType.COMMIT).setParameter("email", email).getResultList();
	}

	@Transactional(readOnly = true)
	public Boolean emailIsExist(String email) {
		if (email == null) {
			return false;
		}
		String jpql = "select count(*) from Member members where lower(members.email) = lower(:email)";
		Long count = entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("email", email).getSingleResult();
		return count > 0;
	}

	
	public void update(Member member, Payment payment) {
//		super.lock(member, LockModeType.PESSIMISTIC_WRITE);
		
		BigDecimal modifyBalance = payment.getEffectiveAmount();
		if (modifyBalance != null && modifyBalance.compareTo(new BigDecimal(0)) != 0 && member.getBalance().add(modifyBalance).compareTo(new BigDecimal(0)) >= 0) {
			member.setBalance(member.getBalance().add(modifyBalance));
			Deposit deposit = new Deposit();
			if (modifyBalance.compareTo(new BigDecimal(0)) > 0) {
				deposit.setType(Type.memberRecharge);
				deposit.setCredit(modifyBalance);
				deposit.setDebit(new BigDecimal(0));
			} else {
				deposit.setType(Type.memberPayment);
				deposit.setCredit(new BigDecimal(0));
				deposit.setDebit(modifyBalance);
			}
			deposit.setPayment(payment);
			deposit.setBalance(member.getBalance());
			deposit.setMember(member);
			depositService.save(deposit);
		}
		super.update(member);
	}
	
	public void update(Member member,Long adbalance){
		if(adbalance!=null){
			Deposit deposit = new Deposit();
			member.setBalance(member.getBalance().add(new BigDecimal(adbalance)));
			if(adbalance>0){
				deposit.setCredit(new BigDecimal(adbalance));
				deposit.setDebit(new BigDecimal(0));
				deposit.setType(Type.adminRecharge);
			}else if(adbalance<0){
				deposit.setCredit(new BigDecimal(0));
				deposit.setDebit(new BigDecimal(adbalance));
				deposit.setType(Type.adminChargeback);
			}
			deposit.setBalance(member.getBalance());
			deposit.setMember(member);
			depositService.save(deposit);
		}
		super.update(member);
	}

}

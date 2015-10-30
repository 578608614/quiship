package com.quickship.service.impl;

import java.util.List;

import javax.persistence.FlushModeType;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.quickship.entity.Address;
import com.quickship.entity.Member;
import com.quickship.service.AddressService;
import com.quickship.service.base.BaseDaoImpl;
@Transactional
@Service("addressServiceImpl")
public class AddressServiceImpl extends BaseDaoImpl<Address,Long> implements AddressService{
	@Transactional(readOnly = true)
	public List<Address> findAddress(Member member){
		String jpql= "select a from Address a join a.Member m where m.id=:member";
		return entityManager.createQuery(jpql,Address.class).setFlushMode(FlushModeType.COMMIT).setParameter("member", member.getId()).getResultList();
	}
}

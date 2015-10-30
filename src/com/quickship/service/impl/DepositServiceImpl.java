package com.quickship.service.impl;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.quickship.Page;
import com.quickship.Pageable;
import com.quickship.QueryOrder.Direction;
import com.quickship.entity.Deposit;
import com.quickship.entity.Member;
import com.quickship.service.DepositService;
import com.quickship.service.base.BaseDaoImpl;

@Transactional
@Service("depositServiceImpl")
public class DepositServiceImpl extends BaseDaoImpl<Deposit,Long> implements DepositService{
	
	
	public Page<Deposit> findPage(Member member,Pageable pageable){
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Deposit> criteriaQuery = criteriaBuilder.createQuery(Deposit.class);
		Root<Deposit> root = criteriaQuery.from(Deposit.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if(member!=null){
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
		}
		criteriaQuery.where(restrictions);
		pageable.setOrderProperty("createDate");
		pageable.setOrderDirection(Direction.desc);
		return super.findPage(criteriaQuery, pageable);
	}
}

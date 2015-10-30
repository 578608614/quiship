package com.quickship.service.impl;

import java.util.List;

import javax.persistence.FlushModeType;

import org.springframework.stereotype.Service;

import com.quickship.entity.Discount;
import com.quickship.entity.Shipping.ServerType;
import com.quickship.service.DiscountService;
import com.quickship.service.base.BaseDaoImpl;
@Service("discountServiceImpl")
public class DiscountServiceImpl extends BaseDaoImpl<Discount, Long> implements DiscountService {


	public Discount findDiscount(ServerType serverType) {
		if(serverType!=null){
			String jpql = "select discount from Discount discount where discount.serverType=:serverType";
			List<Discount> list = entityManager.createQuery(jpql,Discount.class).setFlushMode(FlushModeType.COMMIT).setParameter("serverType", serverType).getResultList();
			if(list!=null&&list.size()>0){
				return list.get(0);
			}else{
				jpql = "select discount from Discount discount where discount.usedAll=true";
				return entityManager.createQuery(jpql,Discount.class).setFlushMode(FlushModeType.COMMIT).getResultList().get(0);
			}
		}else{
			String jpql = "select discount from Discount discount where discount.usedAll=true";
			return entityManager.createQuery(jpql,Discount.class).setFlushMode(FlushModeType.COMMIT).getResultList().get(0);
		}
	}

}

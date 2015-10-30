package com.quickship.service;

import com.quickship.entity.Discount;
import com.quickship.entity.Shipping.ServerType;
import com.quickship.service.base.BaseDao;

public interface DiscountService extends BaseDao<Discount, Long> {
	
	Discount findDiscount(ServerType serverType);
}

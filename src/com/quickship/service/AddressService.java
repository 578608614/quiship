package com.quickship.service;

import java.util.List;

import com.quickship.entity.Address;
import com.quickship.entity.Member;
import com.quickship.service.base.BaseDao;

public interface AddressService extends BaseDao<Address,Long> {
	
	List<Address> findAddress(Member member);
}

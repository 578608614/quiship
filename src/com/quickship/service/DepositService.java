package com.quickship.service;

import com.quickship.Page;
import com.quickship.Pageable;
import com.quickship.entity.Deposit;
import com.quickship.entity.Member;
import com.quickship.service.base.BaseDao;

public interface DepositService extends BaseDao<Deposit,Long>{
	
	/**
	 *  获取充值记录分页信息
	 * @param member 会员
	 * @param pageable 分页信息
	 * @return
	 */
	Page<Deposit> findPage(Member member,Pageable pageable);
}

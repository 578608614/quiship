package com.quickship.service;

import com.quickship.Page;
import com.quickship.Pageable;
import com.quickship.entity.Deposit;
import com.quickship.entity.Member;
import com.quickship.service.base.BaseDao;

public interface DepositService extends BaseDao<Deposit,Long>{
	
	/**
	 *  ��ȡ��ֵ��¼��ҳ��Ϣ
	 * @param member ��Ա
	 * @param pageable ��ҳ��Ϣ
	 * @return
	 */
	Page<Deposit> findPage(Member member,Pageable pageable);
}

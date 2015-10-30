package com.quickship.service;

import java.util.List;

import com.quickship.entity.Member;
import com.quickship.entity.Payment;
import com.quickship.service.base.BaseDao;

public interface MemberService extends BaseDao<Member, Long>{
	/**
	 * 获取会员列表
	 * @param email email
	 * @return
	 */
	List<Member> findByEmail(String email);
	
	/**
	 * 查看email是否存在
	 * @param email email
	 * @return
	 */
	Boolean emailIsExist(String email);
	
	/**
	 * 更新会员预存款
	 * @param member 会员
	 * @param payment 支付单
	 */
	void update(Member member, Payment payment);
	
	/**
	 * 更新会员预存款
	 * @param member 会员
	 * @param adbalance 调整金额
	 */
	public void update(Member member,Long adbalance);
	
	
}

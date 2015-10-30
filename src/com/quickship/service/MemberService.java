package com.quickship.service;

import java.util.List;

import com.quickship.entity.Member;
import com.quickship.entity.Payment;
import com.quickship.service.base.BaseDao;

public interface MemberService extends BaseDao<Member, Long>{
	/**
	 * ��ȡ��Ա�б�
	 * @param email email
	 * @return
	 */
	List<Member> findByEmail(String email);
	
	/**
	 * �鿴email�Ƿ����
	 * @param email email
	 * @return
	 */
	Boolean emailIsExist(String email);
	
	/**
	 * ���»�ԱԤ���
	 * @param member ��Ա
	 * @param payment ֧����
	 */
	void update(Member member, Payment payment);
	
	/**
	 * ���»�ԱԤ���
	 * @param member ��Ա
	 * @param adbalance �������
	 */
	public void update(Member member,Long adbalance);
	
	
}

package com.quickship.service.base;

import java.io.Serializable;
import java.util.List;

import javax.persistence.LockModeType;

import com.quickship.Page;
import com.quickship.Pageable;
import com.quickship.QueryFilter;
import com.quickship.QueryOrder;


public interface BaseDao<T,ID extends Serializable> {
	
	/**
	 * ����ʵ�����
	 * 
	 * @param id
	 *            ID
	 * @return ʵ��������������򷵻�null
	 */
	T find(ID id);

	/**
	 * ����ʵ�����
	 * 
	 * @param id
	 *            ID
	 * @param lockModeType
	 *            ������ʽ
	 * @return ʵ��������������򷵻�null
	 */
	T find(ID id, LockModeType lockModeType);

	/**
	 * ����ʵ����󼯺�
	 * 
	 * @param first
	 *            ��ʼ��¼
	 * @param count
	 *            ����
	 * @param filters
	 *            ɸѡ
	 * @param orders
	 *            ����
	 * @return ʵ����󼯺�
	 */
	List<T> findList(Integer first, Integer count, List<QueryFilter> filters, List<QueryOrder> orders);
	
	/**
	 * ����ʵ����󼯺�
	 * 
	 * 
	 */
	List<T> findAll();
	
	/**
	 * ����ʵ������ҳ
	 * 
	 * @param pageable
	 *            ��ҳ��Ϣ
	 * @return ʵ������ҳ
	 */
	Page<T> findPage(Pageable pageable);

	/**
	 * ��ѯʵ���������
	 * 
	 * @param filters
	 *            ɸѡ
	 * @return ʵ���������
	 */
	long count(List<QueryFilter> filters);

	/**
	 * �־û�ʵ�����
	 * 
	 * @param entity
	 *            ʵ�����
	 */
	void save(T entity);

	/**
	 * �ϲ�ʵ�����
	 * 
	 * @param entity
	 *            ʵ�����
	 * @return ʵ�����
	 */
	T update(T entity);
	
	/**
	 * �ϲ�ʵ�����
	 * 
	 * @param entity
	 *            ʵ�����
	 * @param ignoreProperties
	 *            ��������
	 * @return ʵ�����
	 */
	T update(T entity,String... ignoreProperties);

	/**
	 * �Ƴ�ʵ�����
	 * 
	 * @param entity
	 *            ʵ�����
	 */
	void remove(T entity);
	
	/**
	 * �Ƴ�ʵ�����
	 * 
	 * @param ID
	 *            id
	 */
	void remove(ID id);
	
	/**
	 * �Ƴ�ʵ�����
	 * 
	 * @param ID
	 *            ids
	 */
	void remove(ID... ids);
	/**
	 * �ж�ʵ������Ƿ����
	 * 
	 * @param id
	 *            ID
	 * @return ʵ������Ƿ����
	 */
	boolean exists(ID id);

	/**
	 * ˢ��ʵ�����
	 * 
	 * @param entity
	 *            ʵ�����
	 */
	void refresh(T entity);

	/**
	 * ˢ��ʵ�����
	 * 
	 * @param entity
	 *            ʵ�����
	 * @param lockModeType
	 *            ������ʽ
	 */
	void refresh(T entity, LockModeType lockModeType);

	/**
	 * ��ȡʵ�����ID
	 * 
	 * @param entity
	 *            ʵ�����
	 * @return ʵ�����ID
	 */
	ID getIdentifier(T entity);

	/**
	 * �ж��Ƿ�Ϊ�й�״̬
	 * 
	 * @param entity
	 *            ʵ�����
	 * @return �Ƿ�Ϊ�й�״̬
	 */
	boolean isManaged(T entity);

	/**
	 * ����Ϊ����״̬
	 * 
	 * @param entity
	 *            ʵ�����
	 */
	void detach(T entity);

	/**
	 * ����ʵ�����
	 * 
	 * @param entity
	 *            ʵ�����
	 * @param lockModeType
	 *            ������ʽ
	 */
	void lock(T entity, LockModeType lockModeType);

	/**
	 * �������
	 */
	void clear();

	/**
	 * ͬ������
	 */
	void flush();
	
}

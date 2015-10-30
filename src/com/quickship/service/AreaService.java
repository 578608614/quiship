package com.quickship.service;

import java.util.List;

import com.quickship.entity.Area;
import com.quickship.service.base.BaseDao;

public interface AreaService extends BaseDao<Area,Long>{
	/**
	 * ���Ҷ�������
	 * @return
	 */
	List<Area> findRoot();
	
	
	/**
	 * �����Ӽ�����
	 * @param parent ��������
	 * @return
	 */
	List<Area> findChild(Area parent);
}

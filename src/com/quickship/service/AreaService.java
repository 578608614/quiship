package com.quickship.service;

import java.util.List;

import com.quickship.entity.Area;
import com.quickship.service.base.BaseDao;

public interface AreaService extends BaseDao<Area,Long>{
	/**
	 * 查找顶级地区
	 * @return
	 */
	List<Area> findRoot();
	
	
	/**
	 * 查找子集地区
	 * @param parent 父级地区
	 * @return
	 */
	List<Area> findChild(Area parent);
}

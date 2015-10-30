package com.quickship.service.impl;

import java.util.List;

import javax.persistence.FlushModeType;

import org.springframework.stereotype.Service;

import com.quickship.entity.Area;
import com.quickship.service.AreaService;
import com.quickship.service.base.BaseDaoImpl;

@Service("areaServiceImpl")
public class AreaServiceImpl extends BaseDaoImpl<Area,Long> implements AreaService{

	public List<Area> findRoot() {
		String jpql="select area from Area area where area.parent=null";
		return entityManager.createQuery(jpql, Area.class).setFlushMode(FlushModeType.COMMIT).getResultList();
	}

	public List<Area> findChild(Area parent) {
		String jpql="select area from Area area where area.parent=:parent";
		return entityManager.createQuery(jpql, Area.class).setFlushMode(FlushModeType.COMMIT).setParameter("parent", parent).getResultList();
	}

}

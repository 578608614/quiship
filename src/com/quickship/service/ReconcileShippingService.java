package com.quickship.service;

import java.io.File;

import com.quickship.entity.Shipping;
import com.quickship.service.base.BaseDao;

public interface ReconcileShippingService extends BaseDao<Shipping, Long> {
	
	/**登录fedex*/
	void loginFedex();
	
	/**根据xls批量修改shipping*/
	File updateShipping(File file);
	
	/**更新Member*/
	void updateMember();
}

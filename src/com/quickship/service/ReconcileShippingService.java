package com.quickship.service;

import java.io.File;

import com.quickship.entity.Shipping;
import com.quickship.service.base.BaseDao;

public interface ReconcileShippingService extends BaseDao<Shipping, Long> {
	
	/**��¼fedex*/
	void loginFedex();
	
	/**����xls�����޸�shipping*/
	File updateShipping(File file);
	
	/**����Member*/
	void updateMember();
}

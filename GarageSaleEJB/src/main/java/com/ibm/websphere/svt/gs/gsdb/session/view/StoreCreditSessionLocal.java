/**
 * 
 */
package com.ibm.websphere.svt.gs.gsdb.session.view;

import com.ibm.websphere.svt.gs.gsdb.entities.StoreCredit;
import java.util.Date;
import java.util.List;

/**
 * @author JAGRAJ
 *
 */
public interface StoreCreditSessionLocal {

	String createStoreCredit(StoreCredit storeCredit) throws Exception;

	String deleteStoreCredit(StoreCredit storeCredit) throws Exception;

	String updateStoreCredit(StoreCredit storeCredit) throws Exception;

	StoreCredit findStoreCreditByPrimaryKey(String custId, Date time1);

	StoreCredit getNewStoreCredit();

	List<StoreCredit> getAllStoreCredit();

}

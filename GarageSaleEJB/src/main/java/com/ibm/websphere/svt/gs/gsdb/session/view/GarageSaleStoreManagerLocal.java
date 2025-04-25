/**
 * 
 */
package com.ibm.websphere.svt.gs.gsdb.session.view;

import java.util.Vector;

import javax.jws.WebService;

import com.ibm.websphere.svt.gs.cart.CartItem;
import com.ibm.websphere.svt.gs.ccdb.jaxws.client.CCInfoWrapper;
import com.ibm.websphere.svt.gs.wrapper.CategoryWrapper;
import com.ibm.websphere.svt.gs.wrapper.CustomerInfoWrapper;
import com.ibm.websphere.svt.gs.wrapper.CustomerWrapper;
import com.ibm.websphere.svt.gs.wrapper.InventoryWrapper;
import com.ibm.websphere.svt.gs.wrapper.MfgCategoryWrapper;
import com.ibm.websphere.svt.gs.wrapper.PaymentInfoWrapper;
import com.ibm.websphere.svt.gs.wrapper.SettingsWrapper;

/**
 * @author JAGRAJ
 *
 */
//@WebService(wsdlLocation="META-INF/wsdl/GarageSaleStoreManagerService.wsdl")
@WebService
public interface GarageSaleStoreManagerLocal {

	boolean checkOut(String custID, Vector<CartItem> cart);

	void orderShipped(PaymentInfoWrapper paymentInfo, Vector<CartItem> cart,
			CustomerInfoWrapper customerInfo);

	Vector<CategoryWrapper> listCategories();

	Vector<MfgCategoryWrapper> listMfgCategories(String categoryName);

	Vector<MfgCategoryWrapper> listAllMfgCategories();

	Vector<InventoryWrapper> listInventory(String categoryName, String mfgName);

	Vector<InventoryWrapper> listInventoryByCategoryOrMfg(String arg1,
			int findBy);

	Vector<InventoryWrapper> listAllInventory();

	Vector listAllCustomers();

	String getTotalStoreCredit();

	InventoryWrapper getInventory(String itemID);

	CategoryWrapper getCategory(String categoryName);

	CustomerInfoWrapper getCustomerInfo(String custID);

	CustomerWrapper getCustomer(String custID, String custPwd);

	boolean populateInventory(SettingsWrapper settingsWrapper);

	boolean populateCustomers(SettingsWrapper settingsWrapper,
			CCInfoWrapper[] ccinfoArray);

	void populateBankDB(SettingsWrapper settings);

	CCInfoWrapper[] populateCCDB(int numCustomers);

	boolean clearGSDB();

	boolean resetGSDB(boolean isZ);

	boolean clearBankDB();

	boolean resetBankDB();

	boolean clearCCDB();

	boolean resetCCDB();

	String getCCHistoryData();
	SettingsWrapper getSettings();

}

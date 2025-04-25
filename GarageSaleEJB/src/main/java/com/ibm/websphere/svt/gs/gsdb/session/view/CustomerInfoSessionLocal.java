/**
 * 
 */
package com.ibm.websphere.svt.gs.gsdb.session.view;

import com.ibm.websphere.svt.gs.gsdb.entities.CustomerInfo;
import com.ibm.websphere.svt.gs.wrapper.CustomerInfoWrapper;

/**
 * @author JAGRAJ
 *
 */
public interface CustomerInfoSessionLocal {

	String createCustomerInfo(CustomerInfo customerInfo) throws Exception;

	String deleteCustomerInfo(CustomerInfo customerInfo) throws Exception;

	String updateCustomerInfo(CustomerInfo customerInfo) throws Exception;

	CustomerInfo findCustomerInfoByCustId(String custId);

	CustomerInfo getNewCustomerInfo();

	CustomerInfoWrapper getWrapper(CustomerInfo customerInfo);

}

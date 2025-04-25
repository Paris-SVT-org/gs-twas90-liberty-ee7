/**
 * 
 */
package com.ibm.websphere.svt.gs.wsbankdb.session.view;

import javax.jws.WebService;

import com.ibm.websphere.svt.gs.wrapper.TransactionHistoryWrapper;

/**
 * @author JAGRAJ
 *
 */
//@WebService(wsdlLocation="META-INF/wsdl/GarageSaleBankManagerService.wsdl")
@WebService
public interface GarageSaleBankManagerLocal {

	void createAccountsForGS(int id, int numSubAccounts);

	boolean doTransaction(int id, int suffix, float amount, String partner,
			boolean wsnEnabled, String wsnVariables);

	float[][] getAccountBalances(int id);

	TransactionHistoryWrapper getTransactionHistory(int id, int suffix);

}

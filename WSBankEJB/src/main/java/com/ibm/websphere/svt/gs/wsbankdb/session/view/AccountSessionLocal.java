/**
 * 
 */
package com.ibm.websphere.svt.gs.wsbankdb.session.view;

import com.ibm.websphere.svt.gs.wsbankdb.entities.Account;

/**
 * @author JAGRAJ
 *
 */
public interface AccountSessionLocal {

	String createAccount(Account account) throws Exception;

	String deleteAccount(Account account) throws Exception;

	String updateAccount(Account account) throws Exception;

	Account findAccountById(int id);

	Account getNewAccount();

}

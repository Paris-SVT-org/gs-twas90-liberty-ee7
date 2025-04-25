/**
 * 
 */
package com.ibm.websphere.svt.gs.wsbankdb.session.view;

import com.ibm.websphere.svt.gs.wsbankdb.entities.SubAccount;
import java.util.List;

/**
 * @author JAGRAJ
 *
 */
public interface SubAccountSessionLocal {

	String createSubAccount(SubAccount subAccount) throws Exception;

	String deleteSubAccount(SubAccount subAccount) throws Exception;

	String updateSubAccount(SubAccount subAccount) throws Exception;

	SubAccount findSubAccountByPrimaryKey(int suffix, int id);

	SubAccount getNewSubAccount();

	List<SubAccount> getSubAccountsById(int id_id);

}

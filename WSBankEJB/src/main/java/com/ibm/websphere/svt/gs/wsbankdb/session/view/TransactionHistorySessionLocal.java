/**
 * 
 */
package com.ibm.websphere.svt.gs.wsbankdb.session.view;

import com.ibm.websphere.svt.gs.wsbankdb.entities.TransactionHistory;
import java.util.Date;
import java.util.List;

/**
 * @author JAGRAJ
 *
 */
public interface TransactionHistorySessionLocal {

	String createTransactionHistory(TransactionHistory transactionHistory)
			throws Exception;

	String deleteTransactionHistory(TransactionHistory transactionHistory)
			throws Exception;

	String updateTransactionHistory(TransactionHistory transactionHistory)
			throws Exception;

	TransactionHistory findTransactionHistoryByPrimaryKey(int id, int suffix,
			Date time1);

	TransactionHistory getNewTransactionHistory();

	List<TransactionHistory> getTransactionHistoryBySubAccount(int id_id,
			int id_suffix);

}

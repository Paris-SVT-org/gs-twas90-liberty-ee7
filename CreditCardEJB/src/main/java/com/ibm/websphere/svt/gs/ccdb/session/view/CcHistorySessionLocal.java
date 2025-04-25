/**
 * 
 */
package com.ibm.websphere.svt.gs.ccdb.session.view;

import com.ibm.websphere.svt.gs.ccdb.entities.CcHistory;
import java.util.Date;
import java.util.List;

/**
 * @author JAGRAJ
 *
 */
public interface CcHistorySessionLocal {

	String createCcHistory(CcHistory ccHistory) throws Exception;

	String deleteCcHistory(CcHistory ccHistory) throws Exception;

	String updateCcHistory(CcHistory ccHistory) throws Exception;

	CcHistory findCcHistoryByPrimaryKey(String ccNum, Date timestamp1);

	CcHistory getNewCcHistory();

	List<CcHistory> getCcHistoryByCCNum(String id_ccNum);

}

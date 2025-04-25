/**
 * 
 */
package com.ibm.websphere.svt.gs.ccdb.session.view;

import com.ibm.websphere.svt.gs.ccdb.entities.CcUniqueId;

/**
 * @author JAGRAJ
 *
 */
public interface CcUniqueIdSessionLocal {

	String createCcUniqueId(CcUniqueId ccUniqueId) throws Exception;

	String deleteCcUniqueId(CcUniqueId ccUniqueId) throws Exception;

	String updateCcUniqueId(CcUniqueId ccUniqueId) throws Exception;

	CcUniqueId findCcUniqueIdByIndex1(String index1);

	CcUniqueId getNewCcUniqueId();

}

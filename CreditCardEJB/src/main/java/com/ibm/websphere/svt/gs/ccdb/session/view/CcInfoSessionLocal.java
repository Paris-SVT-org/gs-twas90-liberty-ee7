/**
 * 
 */
package com.ibm.websphere.svt.gs.ccdb.session.view;

import com.ibm.websphere.svt.gs.ccdb.entities.CcInfo;

/**
 * @author JAGRAJ
 *
 */
public interface CcInfoSessionLocal {

	String createCcInfo(CcInfo ccInfo) throws Exception;

	String deleteCcInfo(CcInfo ccInfo) throws Exception;

	String updateCcInfo(CcInfo ccInfo) throws Exception;

	CcInfo findCcInfoByCcNum(String ccNum);

	CcInfo getNewCcInfo();

}

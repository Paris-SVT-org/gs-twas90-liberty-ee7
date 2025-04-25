/**
 * 
 */
package com.ibm.websphere.svt.gs.ccdb.session.view;

import java.util.List;

import javax.jws.WebService;

import com.ibm.websphere.svt.cc.wrapper.CCInfoWrapper;
import com.ibm.websphere.svt.gs.ccdb.entities.CcAuthInfo;
import com.ibm.websphere.svt.gs.ccdb.entities.CcHistory;
import com.ibm.websphere.svt.gs.ccdb.entities.CcInfo;
import com.ibm.websphere.svt.gs.ccdb.entities.CcOwnerInfo;

/**
 * @author JAGRAJ
 *
 */
//@WebService(wsdlLocation="META-INF/wsdl/GarageSaleCCManagerService.wsdl")
@WebService
public interface GarageSaleCCManagerLocal {

	boolean createCCOwnerInfo(CcOwnerInfo cCOwnerInfo);

	boolean createCCHistory(CcHistory cCHistory);

	boolean createCCAuthInfo(CcAuthInfo cCAuthInfo);

	boolean createCCInfo(CcInfo cCInfo);

	boolean updateCCOwnerInfo(CcOwnerInfo cCOwnerInfo);

	boolean updateCCInfo(CcInfo cCInfo);

	boolean updateCCAuthInfo(CcAuthInfo cCAuthInfo);

	boolean removeCCOwner(String cCNum);

	boolean removeCCInfo(String cCNum);

	boolean removeCCAuthInfo(String cCNum);

	String genCCNum();

	String genTransID();

	boolean createNewCustomer(CcAuthInfo cCAuthInfo, CcInfo cCInfo,
			CcOwnerInfo cCOwnerInfo);

	boolean doCredit(String cCNum, int amt, String cmpyID, boolean wsnEnabled,
			String wsnVariables);

	boolean doDebit(String cCNum, float amt, String cmpyID, boolean wsnEnabled,
			String wsnVariables);

	boolean validateCard(String cCNum, String fName, String lName,
			String address1, String address2);

	List<CcHistory> showHistoryInfo(String cCNum);

	CCInfoWrapper getCCInfoWrapper(CcInfo cCInfo);

	CCInfoWrapper getCCInfo(String cCNum);

	CCInfoWrapper[] createAccountForGS(int numAcCounts);

	boolean resetCCDB();

	boolean clearCCDB();

}

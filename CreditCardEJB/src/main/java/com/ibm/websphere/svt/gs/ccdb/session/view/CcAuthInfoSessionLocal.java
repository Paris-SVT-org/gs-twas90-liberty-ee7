package com.ibm.websphere.svt.gs.ccdb.session.view;

import com.ibm.websphere.svt.gs.ccdb.entities.CcAuthInfo;

public interface CcAuthInfoSessionLocal {

	String createCcAuthInfo(CcAuthInfo ccAuthInfo) throws Exception;

	String deleteCcAuthInfo(CcAuthInfo ccAuthInfo) throws Exception;

	String updateCcAuthInfo(CcAuthInfo ccAuthInfo) throws Exception;

	CcAuthInfo findCcAuthInfoByCcNum(String ccNum);

	CcAuthInfo getNewCcAuthInfo();

}

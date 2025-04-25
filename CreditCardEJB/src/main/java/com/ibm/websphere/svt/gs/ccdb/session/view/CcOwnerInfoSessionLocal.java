package com.ibm.websphere.svt.gs.ccdb.session.view;

import com.ibm.websphere.svt.gs.ccdb.entities.CcOwnerInfo;

public interface CcOwnerInfoSessionLocal {

	String createCcOwnerInfo(CcOwnerInfo ccOwnerInfo) throws Exception;

	String deleteCcOwnerInfo(CcOwnerInfo ccOwnerInfo) throws Exception;

	String updateCcOwnerInfo(CcOwnerInfo ccOwnerInfo) throws Exception;

	CcOwnerInfo findCcOwnerInfoByCcNum(String ccNum);

	CcOwnerInfo getNewCcOwnerInfo();

}

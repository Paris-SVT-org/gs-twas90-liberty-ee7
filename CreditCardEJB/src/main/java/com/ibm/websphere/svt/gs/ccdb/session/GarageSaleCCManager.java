/**
 * 
 */
package com.ibm.websphere.svt.gs.ccdb.session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.jws.WebService;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.ibm.websphere.svt.cc.wrapper.CCInfoWrapper;
import com.ibm.websphere.svt.gs.ccdb.entities.CcAuthInfo;
import com.ibm.websphere.svt.gs.ccdb.entities.CcHistory;
import com.ibm.websphere.svt.gs.ccdb.entities.CcHistoryPK;
import com.ibm.websphere.svt.gs.ccdb.entities.CcInfo;
import com.ibm.websphere.svt.gs.ccdb.entities.CcOwnerInfo;
import com.ibm.websphere.svt.gs.ccdb.entities.CcUniqueId;
import com.ibm.websphere.svt.gs.ccdb.session.view.GarageSaleCCManagerLocal;

/**
 * @author JAGRAJ
 * 
 */
@Stateless
@WebService(endpointInterface = "com.ibm.websphere.svt.gs.ccdb.session.view.GarageSaleCCManagerLocal")
@Local(GarageSaleCCManagerLocal.class)
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
@Resource(name = "jdbc/ccdb", type = DataSource.class, authenticationType = Resource.AuthenticationType.CONTAINER, shareable = true)
public class GarageSaleCCManager {
	private static String componentName = "com.ibm.websphere.svt.gs.ccdb.session";
	private static Logger logger = Logger.getLogger(componentName);
	private static String className = GarageSaleCCManager.class.getName();

	private static final String DELETEFROMCCOWNERINFO = "delete from ccownerinfo";
	private static final String DELETEFROMCCINFO = "delete from ccinfo";
	private static final String DELETEFROMCCUNIQUEID = "delete from CcUniqueId";
	private static final String DELETEFROMCCHISTORY = "delete from cchistory";
	private static final String DELETEFROMCCAUTHINFO = "delete from ccauthinfo";
	private static final String SETCCINFOBALANCEZERO = "update ccinfo set balance=0";

	@EJB
	private CcInfoSessionBean cCInfoSessionBean;
	@EJB
	private CcAuthInfoSessionBean cCAuthInfoSessionBean;
	@EJB
	private CcHistorySessionBean cCHistorySessionBean;
	@EJB
	private CcOwnerInfoSessionBean cCOwnerInfoSessionBean;
	@EJB
	private CcUniqueIdSessionBean cCUniqueIdSessionBean;

	/*
	 * Create CCOwnerInfo
	 */

	/**
	 * @return the cCInfoSessionBean
	 */
	public CcInfoSessionBean getcCInfoSessionBean() {
		return cCInfoSessionBean;
	}

	/**
	 * @param cCInfoSessionBean
	 *            the cCInfoSessionBean to set
	 */
	public void setcCInfoSessionBean(CcInfoSessionBean cCInfoSessionBean) {
		this.cCInfoSessionBean = cCInfoSessionBean;
	}

	/**
	 * @return the cCAuthInfoSessionBean
	 */
	public CcAuthInfoSessionBean getcCAuthInfoSessionBean() {
		return cCAuthInfoSessionBean;
	}

	/**
	 * @param cCAuthInfoSessionBean
	 *            the cCAuthInfoSessionBean to set
	 */
	public void setcCAuthInfoSessionBean(
			CcAuthInfoSessionBean cCAuthInfoSessionBean) {
		this.cCAuthInfoSessionBean = cCAuthInfoSessionBean;
	}

	/**
	 * @return the cCHistorySessionBean
	 */
	public CcHistorySessionBean getcCHistorySessionBean() {
		return cCHistorySessionBean;
	}

	/**
	 * @param cCHistorySessionBean
	 *            the cCHistorySessionBean to set
	 */
	public void setcCHistorySessionBean(
			CcHistorySessionBean cCHistorySessionBean) {
		this.cCHistorySessionBean = cCHistorySessionBean;
	}

	/**
	 * @return the cCOwnerInfoSessionBean
	 */
	public CcOwnerInfoSessionBean getcCOwnerInfoSessionBean() {
		return cCOwnerInfoSessionBean;
	}

	/**
	 * @param cCOwnerInfoSessionBean
	 *            the cCOwnerInfoSessionBean to set
	 */
	public void setcCOwnerInfoSessionBean(
			CcOwnerInfoSessionBean cCOwnerInfoSessionBean) {
		this.cCOwnerInfoSessionBean = cCOwnerInfoSessionBean;
	}

	/**
	 * @return the cCUniqueIdSessionBean
	 */
	public CcUniqueIdSessionBean getcCUniqueIdSessionBean() {
		return cCUniqueIdSessionBean;
	}

	/**
	 * @param cCUniqueIdSessionBean
	 *            the cCUniqueIdSessionBean to set
	 */
	public void setcCUniqueIdSessionBean(
			CcUniqueIdSessionBean cCUniqueIdSessionBean) {
		this.cCUniqueIdSessionBean = cCUniqueIdSessionBean;
	}

	static final long serialVersionUID = 3206093459760846163L;

	public boolean createCCOwnerInfo(CcOwnerInfo cCOwnerInfo) {
		String methodName = "createCCOwnerInfo";

		try {
			cCOwnerInfoSessionBean.createCcOwnerInfo(cCOwnerInfo);
		} catch (Exception e) {
			logger.logp(Level.SEVERE, className, methodName, e.getMessage());
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/*
	 * Create CC History
	 */

	public boolean createCCHistory(CcHistory cCHistory) {
		String methodName = "createCCHistory";

		try {
			cCHistorySessionBean.createCcHistory(cCHistory);
		} catch (Exception e) {
			logger.logp(Level.SEVERE, className, methodName, e.getMessage());
			e.printStackTrace();
			return false;
		}

		return true;

	}

	/*
	 * Create CCAuthInfo
	 */

	public boolean createCCAuthInfo(CcAuthInfo cCAuthInfo) {
		String methodName = "createCCAuthInfo";

		try {
			cCAuthInfoSessionBean.createCcAuthInfo(cCAuthInfo);

		} catch (Exception e) {
			logger.logp(Level.SEVERE, className, methodName, e.getMessage());
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/*
	 * Create CCInfo
	 */

	public boolean createCCInfo(CcInfo cCInfo) {
		String methodName = "createCCInfo";

		try {
			cCInfoSessionBean.createCcInfo(cCInfo);

		} catch (Exception e) {
			logger.logp(Level.SEVERE, className, methodName, e.getMessage());
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/*
	 * Update CCOwnerInfo
	 */

	public boolean updateCCOwnerInfo(CcOwnerInfo cCOwnerInfo) {
		String methodName = "updateCCOwnerInfo";

		try {
			cCOwnerInfoSessionBean.createCcOwnerInfo(cCOwnerInfo);

		} catch (Exception e) {
			logger.logp(Level.SEVERE, className, methodName, e.getMessage());
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/*
	 * Update CCInfo
	 */

	public boolean updateCCInfo(CcInfo cCInfo) {
		String methodName = "updateCCInfo";

		try {
			cCInfoSessionBean.createCcInfo(cCInfo);

		} catch (Exception e) {
			logger.logp(Level.SEVERE, className, methodName, e.getMessage());
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/*
	 * Update CCAuthInfo
	 */

	public boolean updateCCAuthInfo(CcAuthInfo cCAuthInfo) {
		String methodName = "updateCCAuthInfo";

		try {
			cCAuthInfoSessionBean.createCcAuthInfo(cCAuthInfo);

		} catch (Exception e) {
			logger.logp(Level.SEVERE, className, methodName, e.getMessage());
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/*
	 * Remove CCOwnerInfo
	 */

	public boolean removeCCOwner(String cCNum) {
		String methodName = "removeCCOwner";

		try {
			cCOwnerInfoSessionBean.deleteCcOwnerInfo(cCOwnerInfoSessionBean
					.findCcOwnerInfoByCcNum(cCNum));

		} catch (Exception e) {
			logger.logp(Level.SEVERE, className, methodName, e.getMessage());
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/*
	 * Remove CCInfo
	 */

	public boolean removeCCInfo(String cCNum) {
		String methodName = "removeCCInfo";

		try {
			cCInfoSessionBean.deleteCcInfo(cCInfoSessionBean
					.findCcInfoByCcNum(cCNum));
		} catch (Exception e) {
			logger.logp(Level.SEVERE, className, methodName, e.getMessage());
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/*
	 * Remove CCAuthInfo
	 */

	public boolean removeCCAuthInfo(String cCNum) {
		String methodName = "removeCCAuthInto";

		try {
			cCAuthInfoSessionBean.deleteCcAuthInfo(cCAuthInfoSessionBean
					.findCcAuthInfoByCcNum(cCNum));
		} catch (Exception e) {
			logger.logp(Level.SEVERE, className, methodName, e.getMessage());
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/*
	 * Generate unique CreditCard Num
	 */

	public String genCCNum() {
		String methodName = "genCCNum";
		String cCNum = null;
		String preFix = "12345678";
		int tempID = 0;
		CcUniqueId CcUniqueId = null;

		try {
			CcUniqueId = cCUniqueIdSessionBean.findCcUniqueIdByIndex1("1");
			logger.logp(Level.FINE, className, methodName,
					"CcUniqueId initialized " + CcUniqueId);
		} catch (Exception e) {
			logger.logp(Level.SEVERE, className, methodName,
					"CcUniqueId not initialized");
			logger.logp(Level.SEVERE, className, methodName, e.getMessage());
			e.printStackTrace();
			return "0";
		}
		if (CcUniqueId == null) {
			CcUniqueId = new CcUniqueId();
			CcUniqueId.setIndex1("1");
			CcUniqueId.setCcNum(10000000);
			CcUniqueId.setTransId(1000);

			try {
				cCUniqueIdSessionBean.createCcUniqueId(CcUniqueId);
				CcUniqueId = cCUniqueIdSessionBean.findCcUniqueIdByIndex1("1");
			} catch (Exception e) {
				logger.logp(Level.SEVERE, className, methodName, e.getMessage());
				e.printStackTrace();
				return "0";
			}
		}
		try {
			tempID = CcUniqueId.getCcNum();
			tempID++;
			CcUniqueId.setCcNum(tempID);
			cCNum = preFix + tempID;
			logger.logp(Level.FINE, className, methodName, "got new cCNum: "
					+ cCNum);

			cCUniqueIdSessionBean.updateCcUniqueId(CcUniqueId);
		} catch (Exception e) {
			logger.logp(Level.SEVERE, className, methodName, e.getMessage());
			e.printStackTrace();
			return "0";

		}

		return cCNum;

		/*
		 * String methodName = "genCCNum"; String cCNum=null; String
		 * preFix="12345678"; int tempID=0; CcUniqueId CcUniqueId = null;
		 * 
		 * try{ CcUniqueId =
		 * CcUniqueIdSessionBean.findCcUniqueIdByGetIndex("1"); }catch(Exception
		 * e){ logger.logp(Level.FINER, className, methodName,
		 * "CcUniqueId not initialized"); logger.logp(Level.FINER, className,
		 * methodName, e.getMessage()); } if (CcUniqueId == null){ CcUniqueId =
		 * new CcUniqueId(); CcUniqueId.setIndex1("1");
		 * CcUniqueId.setCcNum(10000000); CcUniqueId.setTransID(1000);
		 * 
		 * try{ CcUniqueIdSessionBean.createCcUniqueId(CcUniqueId);
		 * }catch(Exception e){ logger.logp(Level.SEVERE, className, methodName,
		 * e.getMessage()); e.printStackTrace(); return "0"; } } tempID =
		 * CcUniqueId.getCcNum(); tempID++; CcUniqueId.setCcNum(tempID);
		 * cCNum=preFix + tempID; try{
		 * CcUniqueIdSessionBean.updateCcUniqueId(CcUniqueId); }catch(Exception
		 * e){ logger.logp(Level.SEVERE, className, methodName, e.getMessage());
		 * e.printStackTrace(); return "0";
		 * 
		 * }
		 * 
		 * return cCNum;
		 */
	}

	/*
	 * Generate Unique Transaction ID
	 */

	public String genTransID() {
		String methodName = "genTransID";

		String transID = null;
		String preFix = "AQC";
		int tempID = 0;

		CcUniqueId CcUniqueId = cCUniqueIdSessionBean
				.findCcUniqueIdByIndex1("1");

		if (logger.isLoggable(Level.FINER)) {
			logger.logp(Level.FINER, className, methodName, "outside unique id");
		}

		if (CcUniqueId == null) {
			CcUniqueId = new CcUniqueId();
			CcUniqueId.setCcNum(10000000);
			CcUniqueId.setTransId(1000);
			CcUniqueId.setIndex1("1");

			try {
				cCUniqueIdSessionBean.createCcUniqueId(CcUniqueId);
			} catch (Exception e) {
				logger.logp(Level.SEVERE, className, methodName, e.getMessage());
				e.printStackTrace();
				return "0";
			}

			if (logger.isLoggable(Level.FINER)) {
				logger.logp(Level.FINER, className, methodName,
						"inside unique id");
			}
		}

		tempID = CcUniqueId.getTransId();
		tempID++;
		CcUniqueId.setTransId(tempID);
		transID = preFix + tempID;
		try {
			cCUniqueIdSessionBean.updateCcUniqueId(CcUniqueId);
		} catch (Exception e) {
			logger.logp(Level.SEVERE, className, methodName, e.getMessage());
			e.printStackTrace();
			return "0";

		}

		return transID;
	}

	/*
	 * Create new Customer This method is used by JSP/Servlets (the front end of
	 * this application)
	 */

	public boolean createNewCustomer(CcAuthInfo cCAuthInfo, CcInfo cCInfo,
			CcOwnerInfo cCOwnerInfo) {
		String methodName = "createNewCustomer";
		String cCNum = null;
		cCNum = genCCNum();

		cCAuthInfo.setCcNum(cCNum);
		cCInfo.setCcNum(cCNum);
		cCOwnerInfo.setCcNum(cCNum);

		try {
			cCAuthInfoSessionBean.createCcAuthInfo(cCAuthInfo);
			cCInfoSessionBean.createCcInfo(cCInfo);
			cCOwnerInfoSessionBean.createCcOwnerInfo(cCOwnerInfo);
		} catch (Exception e) {
			logger.logp(Level.SEVERE, className, methodName, e.getMessage());
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/*
	 * doCredit Method.
	 */

	public boolean doCredit(String cCNum, int amt, String cmpyID,
			boolean wsnEnabled, String wsnVariables) {
		String methodName = "doCredit";

		CcInfo cCInfo = cCInfoSessionBean.findCcInfoByCcNum(cCNum);

		CcHistory cCHistory = new CcHistory();
		cCHistory.setAmount(amt);
		CcHistoryPK cCHistoryPK = new CcHistoryPK();
		cCHistoryPK.setCcNum(cCNum);
		cCHistoryPK.setTimestamp1(new java.sql.Timestamp(System
				.currentTimeMillis()));
		cCHistory.setId(cCHistoryPK);
		cCHistory.setCompanyId(cmpyID);
		// cCHistory.setTransactionID(getTransID());

		cCInfo.setBalance((cCInfo.getBalance() + amt));

		try {
			if (logger.isLoggable(Level.FINER)) {
				logger.logp(Level.FINER, className, methodName,
						"updating cCInfo for " + cCNum);
			}
			cCInfoSessionBean.updateCcInfo(cCInfo);
			if (logger.isLoggable(Level.FINER)) {
				logger.logp(Level.FINER, className, methodName,
						"creating cCHistory for " + cCNum);
			}
			cCHistorySessionBean.createCcHistory(cCHistory);
		} catch (Exception e) {
			logger.logp(Level.SEVERE, className, methodName, e.getMessage());
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/*
	 * doDebit Method
	 */

	public boolean doDebit(String cCNum, float amt, String cmpyID,
			boolean wsnEnabled, java.lang.String wsnVariables) {
		String methodName = "doDebit";
		CcHistory cCHistory = cCHistorySessionBean.getNewCcHistory();
		// CcHistory cCHistory =
		// cCHistorySessionBean.findCcHistoryByGetCCNum(cCNum);
		CcInfo cCInfo = cCInfoSessionBean.findCcInfoByCcNum(cCNum);

		if (cCInfo != null) {
			cCInfo.setBalance((cCInfo.getBalance() + amt));
		} else {
			logger.logp(Level.INFO, className, methodName,
					"CCNum was not found or null returned");
			return false;
		}

		CcHistoryPK cCHistoryPK = cCHistory.getId();
		cCHistoryPK.setCcNum(cCNum);
		cCHistoryPK.setTimestamp1(new java.sql.Timestamp(System
				.currentTimeMillis()));

		// cCHistory.setCcNum(cCNum);
		cCHistory.setAmount(amt);
		cCHistory.setCompanyId(cmpyID);
		// cCHistory.setTimeStamp1(new
		// java.sql.Timestamp(System.currentTimeMillis()));
		cCHistory.setId(cCHistoryPK);

		try {
			if (logger.isLoggable(Level.FINER)) {
				logger.logp(Level.FINER, className, methodName,
						"updating cCInfo for " + cCNum);
			}
			// cCInfoSessionBean.updateCcInfo(cCInfo);
			if (logger.isLoggable(Level.FINER)) {
				logger.logp(Level.FINER, className, methodName,
						"creating cCHistory for " + cCNum);
			}
			cCHistorySessionBean.createCcHistory(cCHistory);
		} catch (ClassNotFoundException | SQLException e) {
			logger.logp(Level.SEVERE, className, methodName, e.getMessage());
			e.printStackTrace();
			return false;
		}

		catch (Exception e) {
			logger.logp(Level.SEVERE, className, methodName, e.getMessage());
			e.printStackTrace();
			return false;
		}

		return true;

		/*
		 * - WSN method section
		 * 
		 * // WSN enabled. Log to GSLogger using WSN if(wsnEnabled){ try{
		 * java.sql.Timestamp time = new
		 * java.sql.Timestamp(System.currentTimeMillis()); String msg = null;
		 * WSNProxy wsnProxyRemote = null; WSNUtils wsnUtils = new WSNUtils();
		 * 
		 * String resultMsg [] = wsnVariables.split("\\|"); String
		 * nameSpaceMapping = resultMsg[0]; String nameSpaceURI = resultMsg[1];
		 * String topic = resultMsg[2]; String endPointURL = resultMsg[3];
		 * String port = resultMsg[4];
		 * 
		 * 
		 * // wsnProxyRemote = WSNUtils.createWSNProxyRemote(); wsnProxyRemote =
		 * wsnUtils.createWSNProxyRemote(); msg =
		 * wsnProxyRemote.createMsg2Send("CCApp", time.toString(),
		 * this.getClass().toString(), "Credit Card SucCessfully Charged");
		 * wsnProxyRemote.sendMessage(msg, nameSpaceMapping, nameSpaceURI,
		 * topic, endPointURL, port);
		 * 
		 * 
		 * } catch(RemoteException e) { logger.logp(Level.SEVERE, className,
		 * methodName, e.getMessage()); e.printStackTrace();
		 * //handleException("doDebit()"
		 * ,e,"An exception was caught in CCSessionBean in method doDebit()"); }
		 * }
		 */// end WSN section
	}

	public boolean validateCard(String cCNum, String fName, String lName,
			String address1, String address2) {
		String methodName = "validateCard";
		CcOwnerInfo cCOwnerInfo = cCOwnerInfoSessionBean
				.findCcOwnerInfoByCcNum(cCNum);

		if (cCOwnerInfo != null) {
			if (logger.isLoggable(Level.FINER)) {
				logger.logp(Level.FINER, className, methodName,
						"card exists for " + cCNum);
			}
			return true;
		} else {
			logger.logp(Level.SEVERE, className, methodName,
					"card entry not found for " + cCNum);
			return false;
		}

	}

	/*
	 * Show all the transaction history for a given creditcard number
	 */

	public List<CcHistory> showHistoryInfo(String cCNum) {
		List<CcHistory> historyInfo = null;
		try {

			historyInfo = cCHistorySessionBean.getCcHistoryByCCNum(cCNum);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return historyInfo;
	}

	/**
	 * 
	 * @param cCInfo
	 * @return
	 */
	public CCInfoWrapper getCCInfoWrapper(CcInfo cCInfo) {
		CCInfoWrapper cCInfoWrapper = new CCInfoWrapper();
		cCInfoWrapper.setBalance(cCInfo.getBalance());
		cCInfoWrapper.setCCNum(cCInfo.getCcNum());
		cCInfoWrapper.setClimit(cCInfo.getCLimit());
		cCInfoWrapper.setExpDate(cCInfo.getExpDate());

		return cCInfoWrapper;
	}

	public CCInfoWrapper getCCInfo(String cCNum) {
		String methodName = "getCCInfo";
		CcInfo cCInfo = cCInfoSessionBean.findCcInfoByCcNum(cCNum);
		if (cCInfo == null) {
			logger.logp(Level.SEVERE, className, methodName,
					"CCInfo not found for " + cCNum + ", returning NULL value");
		} else {
			if (logger.isLoggable(Level.FINER)) {
				logger.logp(Level.FINER, className, methodName,
						"CCInfo found for " + cCNum);
			}
		}
		return getCCInfoWrapper(cCInfo);
	}

	/*
	 * Population method for the GarageSale Application to be all to populate n
	 * number of credit card acCounts depending upon the number of garagesale
	 * users.
	 */
	public CCInfoWrapper[] createAccountForGS(int numAcCounts) {
		String methodName = "createAccountForGS";
		String cCNum = null;
		boolean isCCOwnerInfo = false;
		boolean isCCAuthInfo = false;
		boolean isCCInfo = false;

		CcOwnerInfo cCOwnerInfo = new CcOwnerInfo();
		CcAuthInfo cCAuthInfo = new CcAuthInfo();
		CcInfo cCInfo = new CcInfo();

		CCInfoWrapper[] totalAcCounts = new CCInfoWrapper[numAcCounts];
		// try{
		for (int i = 0; i < numAcCounts; i++) {
			cCNum = genCCNum();
			cCOwnerInfo.setCcNum(cCNum);
			cCAuthInfo.setCcNum(cCNum);
			cCInfo.setCcNum(cCNum);

			isCCOwnerInfo = createCCOwnerInfo(cCOwnerInfo);
			isCCAuthInfo = createCCAuthInfo(cCAuthInfo);
			isCCInfo = createCCInfo(cCInfo);

			if (isCCOwnerInfo & isCCAuthInfo & isCCInfo) {
				totalAcCounts[i] = getCCInfoWrapper(cCInfoSessionBean
						.findCcInfoByCcNum(cCNum));

				if (logger.isLoggable(Level.FINER)) {
					logger.logp(Level.FINER, className, methodName,
							"created a new acCount with creditcard #" + cCNum);
				}
			} else {
				logger.logp(Level.SEVERE, className, methodName,
						"failed to create a new acCount with creditcard #"
								+ cCNum);
			}
		}
		if (logger.isLoggable(Level.FINE)) {
			logger.logp(Level.FINE, className, methodName, "CCApp:  create "
					+ numAcCounts + " new acCounts");
		}
		return totalAcCounts;

	}

	public boolean resetCCDB() {
		String methodName = "resetCCDB";

		try {
			Context ctx = new InitialContext();
			DataSource cCDS = (DataSource) ctx
					.lookup("java:comp/env/jdbc/cCdb");
			Connection cCConn = cCDS.getConnection();
			cCConn.setAutoCommit(false);
			PreparedStatement stmt = null;
			// ResultSet rs = null;
			// String sql = null;

			// sql = "delete from cChistory";
			stmt = cCConn.prepareStatement(DELETEFROMCCHISTORY);
			stmt.executeUpdate();
			stmt.close();
			cCConn.commit();

			// sql = "update cCinfo set balance=0";
			stmt = cCConn.prepareStatement(SETCCINFOBALANCEZERO);
			stmt.executeUpdate();
			stmt.close();
			cCConn.commit();

		} catch (SQLException e) {
			logger.logp(
					Level.SEVERE,
					className,
					methodName,
					"An exception ocCurred resetting the CCDB, please drop/recreate the tables to ensure consistency");
			logger.logp(Level.SEVERE, className, methodName, e.getMessage());
			e.printStackTrace();
			// handleException("resetCCDB()", e,
			// "An Exception was caught while resetting the CC database.");
			return false;
		} catch (NamingException e) {
			logger.logp(Level.SEVERE, className, methodName,
					"Unable to lookup CCDB datasource, naming exception thrown");
			logger.logp(Level.SEVERE, className, methodName, e.getMessage());
			return false;
		}

		return true;
	}

	public boolean clearCCDB() {
		String methodName = "clearCCDB";
		try {
			Context ctx = new InitialContext();
			DataSource cCDS = (DataSource) ctx
					.lookup("java:comp/env/jdbc/ccdb");
			Connection cCConn = cCDS.getConnection();
			cCConn.setAutoCommit(false);
			PreparedStatement stmt = null;
			// ResultSet rs = null;
			// String sql = null;

			// sql = DELETEFROMCCOWNERINFO;
			stmt = cCConn.prepareStatement(DELETEFROMCCOWNERINFO);
			stmt.executeUpdate();
			stmt.close();
			cCConn.commit();

			// sql = "delete from cCinfo";
			stmt = cCConn.prepareStatement(DELETEFROMCCINFO);
			stmt.executeUpdate();
			stmt.close();
			cCConn.commit();

			// sql = "delete from cCauthinfo";
			stmt = cCConn.prepareStatement(DELETEFROMCCAUTHINFO);
			stmt.executeUpdate();
			stmt.close();
			cCConn.commit();

			// sql = "delete from cChistory";
			stmt = cCConn.prepareStatement(DELETEFROMCCHISTORY);
			stmt.executeUpdate();
			stmt.close();
			cCConn.commit();

			// sql = "delete from CcUniqueId";
			stmt = cCConn.prepareStatement(DELETEFROMCCUNIQUEID);
			stmt.executeUpdate();
			stmt.close();
			cCConn.commit();

		} catch (SQLException e) {
			logger.logp(
					Level.SEVERE,
					className,
					methodName,
					"An exception ocCurred clearing the CCDB, please drop/recreate the tables to ensure consistency");
			logger.logp(Level.SEVERE, className, methodName, e.getMessage());
			e.printStackTrace();
			return false;
		} catch (NamingException e) {
			logger.logp(Level.SEVERE, className, methodName,
					"Unable to lookup CCDB datasource, naming exception thrown");
			logger.logp(Level.SEVERE, className, methodName, e.getMessage());
			return false;
		}

		return true;
	}

}

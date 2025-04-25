/**
 * 
 */
package com.ibm.websphere.svt.gs.wsbankdb.session;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
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
import javax.sql.DataSource;

import com.ibm.websphere.svt.gs.wrapper.TransactionHistoryWrapper;
import com.ibm.websphere.svt.gs.wsbankdb.entities.SubAccount;
import com.ibm.websphere.svt.gs.wsbankdb.entities.SubAccountPK;
import com.ibm.websphere.svt.gs.wsbankdb.entities.TransactionHistory;
import com.ibm.websphere.svt.gs.wsbankdb.entities.TransactionHistoryPK;
import com.ibm.websphere.svt.gs.wsbankdb.session.view.AccountSessionLocal;
import com.ibm.websphere.svt.gs.wsbankdb.session.view.GarageSaleBankManagerLocal;
import com.ibm.websphere.svt.gs.wsbankdb.session.view.SubAccountSessionLocal;
import com.ibm.websphere.svt.gs.wsbankdb.session.view.TransactionHistorySessionLocal;

/**
 * @author JAGRAJ
 *
 */
@Stateless
@WebService(endpointInterface="com.ibm.websphere.svt.gs.wsbankdb.session.view.GarageSaleBankManagerLocal",wsdlLocation = "META-INF/wsdl/GarageSaleBankManagerService.wsdl")
@Local(GarageSaleBankManagerLocal.class)
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
@Resource(name = "jdbc/wsbankdb", type = DataSource.class, authenticationType = Resource.AuthenticationType.CONTAINER, shareable = true)
public class GarageSaleBankManager {
	private static final long serialVersionUID = 8L;
	
	private static String componentName = "com.ibm.websphere.svt.wsbank.ejb";
	private static Logger logger = Logger.getLogger(componentName);
	private static String className = GarageSaleBankManager.class.getName();
	
	
	private static final String RESETSUBACCOUNTZERO = "update subaccount set balance=0";
	private static final String DELETEFROMTRANSACTIONHISTORY = "delete from transactionhistory";
	private static final String DELETEFROMSUBACCOUNT = "delete from subaccount";
	private static final String DELETEFROMACCOUNT = "delete from account";
	
	@EJB private AccountSessionLocal accountSessionBean ;
	@EJB private SubAccountSessionLocal subAccountSessionBean;
	@EJB private TransactionHistorySessionLocal  transactionHistorySessionBean;
	


	
	/**
	 * @return the accountSessionBean
	 */
	public AccountSessionLocal getAccountSessionBean() {
		return accountSessionBean;
	}

	/**
	 * @param accountSessionBean the accountSessionBean to set
	 */
	public void setAccountSessionBean(AccountSessionLocal accountSessionBean) {
		this.accountSessionBean = accountSessionBean;
	}

	/**
	 * @return the subAccountSessionBean
	 */
	public SubAccountSessionLocal getSubAccountSessionBean() {
		return subAccountSessionBean;
	}

	/**
	 * @param subAccountSessionBean the subAccountSessionBean to set
	 */
	public void setSubAccountSessionBean(SubAccountSessionLocal subAccountSessionBean) {
		this.subAccountSessionBean = subAccountSessionBean;
	}

	/**
	 * @return the transactionHistorySessionBean
	 */
	public TransactionHistorySessionLocal getTransactionHistorySessionBean() {
		return transactionHistorySessionBean;
	}

	/**
	 * @param transactionHistorySessionBean the transactionHistorySessionBean to set
	 */
	public void setTransactionHistorySessionBean(
			TransactionHistorySessionLocal transactionHistorySessionBean) {
		this.transactionHistorySessionBean = transactionHistorySessionBean;
	}

	public void createAccountsForGS(int id, int numSubAccounts){
		String methodName = "createAccountsForGS";
		//if (logger.isLoggable(Level.FINE)) {
		logger.logp(Level.FINE, className, methodName, "\tjust entered the " + methodName);
		//}
		try{
		//AccountLocal account = EJBUtils.createAccount(id);
		//if (logger.isLoggable(Level.FINE)) {
		logger.logp(Level.FINE, className, methodName, "created the account ..." + accountSessionBean.toString());
		//}
		//System.out.println("WSBANK_TRACE(BankSessionBeanBean.java): Account created:  " + account.getPrimaryKey());
			
		if (accountSessionBean!=null){ //create subaccounts				
			//SubAccountLocal subAccount = null;
			for(int i = 1 ; i <= numSubAccounts ; i++ ){
				SubAccount subAccount = new SubAccount();
				SubAccountPK subAccountPK = new SubAccountPK();
				subAccountPK.setId(id);
				subAccountPK.setSuffix(i);
				subAccount.setId(subAccountPK);
				subAccountSessionBean.createSubAccount(subAccount);
				//if (logger.isLoggable(Level.FINER)) {
				logger.logp(Level.FINER, className, methodName, "created subaccounts created for master account " + subAccount.toString());
				//}
			}
			//if (logger.isLoggable(Level.FINE)) {
			logger.logp(Level.FINE, className, methodName, "For : " + numSubAccounts + " subaccounts created for master account " + id);
			//}
			//System.out.println("WSBANK_TRACE(BankSessionBeanBean.java): " + numSubAccounts + " subaccounts created for master account " + id);
			
		}
		}catch(Exception e){
			logger.logp(Level.SEVERE, className, methodName, e.getMessage());
			e.printStackTrace();
			//handleException("createAccountsForGS()", e, "An Exception occurred in BankSessionBean method createAccountsForGS()");
		} 
	}
	
	public boolean doTransaction(int id, 
			int suffix, 
			float amount, 
			String partner, 
			boolean wsnEnabled, 
			String wsnVariables){
		String methodName = "doTransaction";
		try{
			//System.out.println("Find subaccount for suffix " + suffix);
			
			//SubAccountLocal subAccount = EJBUtils.findSubAccountByPrimaryKey(id,suffix);
			SubAccount subAccount = subAccountSessionBean.findSubAccountByPrimaryKey(suffix, id);
			if(subAccount==null){
				//if (logger.isLoggable(Level.FINE)) {
				logger.logp(Level.FINE, className, methodName, "subaccount not found or null returned");
				//}
				//System.out.println("subaccount not found or null returned");
				return false;
			}
			//System.out.println("Update subaccount balance");
			subAccount.setBalance(subAccount.getBalance() + amount);
			logger.logp(Level.FINE, className, methodName, "set subAccount balance " + amount);
			//System.out.println("Update subaccount lastTransaction");
			subAccount.setLastTransactionTime(new Timestamp(System.currentTimeMillis()));
			logger.logp(Level.FINE, className, methodName, "set subAccount TransactionTime");
			//System.out.println("create transaction history for this transaction");
			
			//TransactionHistoryLocal history = EJBUtils.createTransactionHistory(id,suffix,subAccount.getLastTransactionTime());
			TransactionHistory transactionHistory= new TransactionHistory();
			TransactionHistoryPK transactionHistoryPK = new TransactionHistoryPK();
			transactionHistoryPK.setId(id);
			transactionHistoryPK.setSuffix(suffix);
			transactionHistoryPK.setTime1(subAccount.getLastTransactionTime());
			transactionHistory.setId(transactionHistoryPK);
			String isTranSuccess = transactionHistorySessionBean.createTransactionHistory(transactionHistory);
			if(isTranSuccess != null){
				//System.out.println("Transaction history was created, set values");
				logger.logp(Level.FINE, className, methodName, "Transaction history was created, set values...");
				transactionHistory.setNewBalance(subAccount.getBalance());
				transactionHistory.setAmount(amount);
				transactionHistory.setPartner(partner);
				logger.logp(Level.FINE, className, methodName, "Transaction history was created, set value done");
			}
			
			
			//			WSN enabled. Log to GSLogger using WSN
			if(wsnEnabled){
			  	/**Timestamp time = new java.sql.Timestamp(System.currentTimeMillis());
			  	String msg = null;
			 
			  	WSNProxy wsnProxyRemote = null;
			  	WSNUtils wsnUtils = new WSNUtils();
			
			//  	wsnProxyRemote = WSNUtils.createWSNProxyRemote();
			  	wsnProxyRemote = wsnUtils.createWSNProxyRemote();
				msg = wsnProxyRemote.createMsg2Send("WSBank", time.toString(), this.getClass().toString(), "Bank Account Successfully Credited");
				String resultMsg [] = wsnVariables.split("\\|");
                String nameSpaceMapping = resultMsg[0];
                String nameSpaceURI = resultMsg[1];
                String topic = resultMsg[2];
                String endPointURL = resultMsg[3];
                String port = resultMsg[4];
				
                wsnProxyRemote.sendMessage(msg, nameSpaceMapping, nameSpaceURI, topic, endPointURL, port);
                logger.logp(Level.FINE, className, methodName, "wsn Enabled, wsnMsg sent, content: " + msg);*/
			}
			
		} catch(ClassNotFoundException |SQLException | RemoteException e) {
			logger.logp(Level.SEVERE, className, methodName, e.getMessage());
			e.printStackTrace();
			//handleException("doTransaction()", e, "An Exception occurred in BankSessionBean method doTransaction()");
			return false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.logp(Level.SEVERE, className, methodName, e.getMessage());
			e.printStackTrace();
			return false;
		} 
		return true;		
	}
	
	/**public boolean resetWSBankDB(){
		String methodName = "resetWSBankDB";
		boolean cleared = false;
		try{
			DataSource wsbankDS = EJBUtils.getWsbankDs();
			Connection wsbankConn = wsbankDS.getConnection();
			wsbankConn.setAutoCommit(false);
			PreparedStatement stmt=null;
			//ResultSet rs = null;
			//String sql = null;
			
			//sql = "delete from transactionhistory";
			stmt = wsbankConn.prepareStatement(DELETEFROMTRANSACTIONHISTORY);
			stmt.executeUpdate();
			stmt.close();	
			wsbankConn.commit();
			
			//sql = "update subaccount set balance=0";
			stmt = wsbankConn.prepareStatement(RESETSUBACCOUNTZERO);
			stmt.executeUpdate();
			stmt.close();	
			wsbankConn.commit();
						
			cleared = true;		
		}catch (SQLException e) {
			logger.logp(Level.SEVERE, className, methodName, e.getMessage());
			e.printStackTrace();
			//handleException("resetWSBankDB()", e, "An Exception was caught while resetting the WSBank database.");
			cleared = false;
		}	
		return cleared;		
	}*/
	
	/**public boolean clearWSBankDB(){
		String methodName = "clearWSBankDB";
		boolean cleared = false;
		try{
			DataSource wsbankDS = EJBUtils.getWsbankDs();
			Connection wsbankConn = wsbankDS.getConnection();
			wsbankConn.setAutoCommit(false);
			PreparedStatement stmt=null;
			//ResultSet rs = null;
			//String sql = null;
			
			//sql = "delete from transactionhistory";
			stmt = wsbankConn.prepareStatement(DELETEFROMTRANSACTIONHISTORY);
			stmt.executeUpdate();
			stmt.close();	
			wsbankConn.commit();
			
			//sql = "delete from subaccount";
			stmt = wsbankConn.prepareStatement(DELETEFROMSUBACCOUNT);
			stmt.executeUpdate();
			stmt.close();	
			wsbankConn.commit();
			
			//sql = "delete from account";
			stmt = wsbankConn.prepareStatement(DELETEFROMACCOUNT);
			stmt.executeUpdate();
			stmt.close();	
			wsbankConn.commit();
			
			cleared = true;		
		}catch (SQLException e) {
			logger.logp(Level.SEVERE, className, methodName, e.getMessage());
			e.printStackTrace();
			//handleException("clearWSBankDB()", e, "An Exception was caught while clearing the WSBank database.");
			cleared = false;
		}
		return cleared;
		
	}*/
	
	public float[][] getAccountBalances(int id){
		
		//Collection subAccounts = EJBUtils.findSubAccountsByID(id);
		Collection subAccounts = subAccountSessionBean.getSubAccountsById(id);
		//Vector vbalances = new Vector();
		//SubAccountLocal[] accountArray = null;
		//accountArray = (SubAccountLocal[])subAccounts.toArray();
		
		SubAccount[] accountArray = null;
		accountArray = (SubAccount[])subAccounts.toArray();

		float[][] balances = new float[subAccounts.size()][2];
		for (int i = 0 ; i < subAccounts.size(); i++ ){
			balances[i][0]=(float)(i+1);
			balances[i][1]= accountArray[i].getBalance();
		}
		return balances;
	}
	
	public TransactionHistoryWrapper getTransactionHistory(int id, int suffix){
		TransactionHistoryWrapper wrapper = null;
		//Collection history = EJBUtils.findTransactionHistoriesForSubAccount(id,suffix);
		Collection history = transactionHistorySessionBean.getTransactionHistoryBySubAccount(id,suffix);
		//TransactionHistoryLocal[] transactionHistory = (TransactionHistoryLocal[])history.toArray();
			
		TransactionHistory[] transactionHistory = (TransactionHistory[])history.toArray();
		int numTransactions = history.size();
		
		float[] amounts = new float[numTransactions];
		Date[] times = new Timestamp[numTransactions];
		String[] partners = new String[numTransactions];
		
		for (int i = 0 ; i < numTransactions ; i++){
			amounts[i] = transactionHistory[i].getAmount();
			times[i] = new Date( transactionHistory[i].getId().getTime1().getTime() );
			partners[i] = transactionHistory[i].getPartner();
		}
		
		wrapper = new TransactionHistoryWrapper(id,suffix);
		wrapper.setAmounts(amounts);
		wrapper.setTimes(times);
		wrapper.setPartners(partners);		
		
		return wrapper;
	}
	
    //*******************************************************************************************
	//Logger and exception handling methods
	//********************************************************************************************
/*
	private void sendLoggerNotification(String info){
		
		//String appName, String time, String className, String info
		//TODO insert code to call WS-N logger
	} */
	/*
	private void handleException(String method, Exception e, String message){
		System.err.println(message);
		System.out.println(message);
		e.printStackTrace(System.err);
		sendLoggerNotification("Exception caught by BankSessionBeanBean in method " + method + ".  Exception details:  " + e.toString());
	} */
	
	
}

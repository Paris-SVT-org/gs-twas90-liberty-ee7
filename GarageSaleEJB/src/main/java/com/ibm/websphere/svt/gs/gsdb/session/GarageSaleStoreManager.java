/**
 * 
 */
package com.ibm.websphere.svt.gs.gsdb.session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.LocalBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.jws.WebService;
import javax.sql.DataSource;
import javax.transaction.Status;
import javax.transaction.UserTransaction;

import com.ibm.websphere.svt.gs.cart.CartItem;
import com.ibm.websphere.svt.gs.ccdb.jaxws.client.CCInfoWrapper;
import com.ibm.websphere.svt.gs.ccdb.jaxws.client.GarageSaleCCManagerLocal;
import com.ibm.websphere.svt.gs.gsdb.entities.Category;
import com.ibm.websphere.svt.gs.gsdb.entities.Customer;
import com.ibm.websphere.svt.gs.gsdb.entities.CustomerInfo;
import com.ibm.websphere.svt.gs.gsdb.entities.Inventory;
import com.ibm.websphere.svt.gs.gsdb.entities.MfgCategory;
import com.ibm.websphere.svt.gs.gsdb.entities.Setting;
import com.ibm.websphere.svt.gs.gsdb.entities.StoreCredit;
import com.ibm.websphere.svt.gs.gsdb.entities.StoreCreditPK;
import com.ibm.websphere.svt.gs.gsdb.session.util.GarageSaleEJBWSClientUtil;
import com.ibm.websphere.svt.gs.gsdb.session.view.CategorySessionLocal;
import com.ibm.websphere.svt.gs.gsdb.session.view.CustomerInfoSessionLocal;
import com.ibm.websphere.svt.gs.gsdb.session.view.CustomerSessionLocal;
import com.ibm.websphere.svt.gs.gsdb.session.view.GarageSaleStoreManagerLocal;
import com.ibm.websphere.svt.gs.gsdb.session.view.InventorySessionLocal;
import com.ibm.websphere.svt.gs.gsdb.session.view.MfgCategorySessionLocal;
import com.ibm.websphere.svt.gs.gsdb.session.view.SettingSessionLocal;
import com.ibm.websphere.svt.gs.gsdb.session.view.StoreCreditSessionLocal;
import com.ibm.websphere.svt.gs.wrapper.CategoryWrapper;
import com.ibm.websphere.svt.gs.wrapper.CustomerInfoWrapper;
import com.ibm.websphere.svt.gs.wrapper.CustomerWrapper;
import com.ibm.websphere.svt.gs.wrapper.InventoryWrapper;
import com.ibm.websphere.svt.gs.wrapper.MfgCategoryWrapper;
import com.ibm.websphere.svt.gs.wrapper.PaymentInfoWrapper;
import com.ibm.websphere.svt.gs.wrapper.SettingsWrapper;
import com.ibm.websphere.svt.gs.wsbankdb.jaxws.client.GarageSaleBankManagerLocal;


/**
 * @author JAGRAJ
 *
 */
@Stateless
@WebService(endpointInterface="com.ibm.websphere.svt.gs.gsdb.session.view.GarageSaleStoreManagerLocal")
@Local(GarageSaleStoreManagerLocal.class)
@LocalBean
@TransactionManagement(TransactionManagementType.BEAN)
@Resource(name = "jdbc/gsdb", type = DataSource.class, authenticationType = Resource.AuthenticationType.CONTAINER, shareable = true)
public class GarageSaleStoreManager {
	
	private static String componentName = "com.ibm.websphere.svt.gs.gsdb.session";
	private static Logger logger = Logger.getLogger(componentName);
	private static String className = GarageSaleStoreManager.class.getName();		
		
	private static int settingsID = 1;	//used for settings table and master account number in wsbank
	private static java.util.Random random = new java.util.Random();
	private static SettingsWrapper settings = null;
	
	@Resource
	private SessionContext mySessionCtx;
	
	@javax.annotation.Resource(lookup="java:module/GarageSaleEJBWSClientUtil")
	private GarageSaleEJBWSClientUtil garageSaleEJBWSClientUtil;
	
	private UserTransaction ut;
	
	public GarageSaleCCManagerLocal getCcManager() {
		return ccManager;
	}

	public void setCcManager(GarageSaleCCManagerLocal ccManager) {
		this.ccManager = ccManager;
	}

	public GarageSaleBankManagerLocal getBankManager() {
		return bankManager;
	}

	public void setBankManager(GarageSaleBankManagerLocal bankManager) {
		this.bankManager = bankManager;
	}
	
	
	@EJB private CustomerSessionLocal customerSessionBean;
	@EJB private CustomerInfoSessionLocal  customerInfoSessionBean;
	//@EJB private GarageSaleCCManagerLocal ccManager;
	//@EJB private GarageSaleBankManagerLocal bankManager;
	
	
	//private GarageSaleCCManagerLocal ccManager=GarageSaleWSClientUtils.getCCManager();
	//private GarageSaleBankManagerLocal bankManager=GarageSaleWSClientUtils.getBankManager();
	
	private GarageSaleCCManagerLocal ccManager;
	private GarageSaleBankManagerLocal bankManager;


	@EJB private StoreCreditSessionLocal storeCreditSessionBean;
	@EJB private InventorySessionLocal inventorySessionBean;
	@EJB private CategorySessionLocal categorySessionBean;
	@EJB private MfgCategorySessionLocal mfgCategorySessionBean;
	@EJB private SettingSessionLocal settingsSessionBean;

	  @javax.annotation.PostConstruct
	    public void init() {
		  ccManager=garageSaleEJBWSClientUtil.getCcManager(); 
		  bankManager=garageSaleEJBWSClientUtil.getBankManager();
	    }


	
	//********************************************************************************************
	//methods related to checkout
	//********************************************************************************************

	/**
	 * @return the customerSessionBean
	 */
	public CustomerSessionLocal getCustomerSessionBean() {
		return customerSessionBean;
	}

	/**
	 * @param customerSessionBean the customerSessionBean to set
	 */
	public void setCustomerSessionBean(CustomerSessionLocal customerSessionBean) {
		this.customerSessionBean = customerSessionBean;
	}

	/**
	 * @return the customerInfoSessionBean
	 */
	public CustomerInfoSessionLocal getCustomerInfoSessionBean() {
		return customerInfoSessionBean;
	}

	/**
	 * @param customerInfoSessionBean the customerInfoSessionBean to set
	 */
	public void setCustomerInfoSessionBean(CustomerInfoSessionLocal customerInfoSessionBean) {
		this.customerInfoSessionBean = customerInfoSessionBean;
	}

	/**
	 * @return the storeCreditSessionBean
	 */
	public StoreCreditSessionLocal getStoreCreditSessionBean() {
		return storeCreditSessionBean;
	}

	/**
	 * @param storeCreditSessionBean the storeCreditSessionBean to set
	 */
	public void setStoreCreditSessionBean(StoreCreditSessionLocal storeCreditSessionBean) {
		this.storeCreditSessionBean = storeCreditSessionBean;
	}

	/**
	 * @return the inventorySessionBean
	 */
	public InventorySessionLocal getInventorySessionBean() {
		return inventorySessionBean;
	}

	/**
	 * @param inventorySessionBean the inventorySessionBean to set
	 */
	public void setInventorySessionBean(InventorySessionLocal inventorySessionBean) {
		this.inventorySessionBean = inventorySessionBean;
	}

	/**
	 * @return the categorySessionBean
	 */
	public CategorySessionLocal getCategorySessionBean() {
		return categorySessionBean;
	}

	/**
	 * @param categorySessionBean the categorySessionBean to set
	 */
	public void setCategorySessionBean(CategorySessionLocal categorySessionBean) {
		this.categorySessionBean = categorySessionBean;
	}

	/**
	 * @return the mfgCategorySessionBean
	 */
	public MfgCategorySessionLocal getMfgCategorySessionBean() {
		return mfgCategorySessionBean;
	}

	/**
	 * @param mfgCategorySessionBean the mfgCategorySessionBean to set
	 */
	public void setMfgCategorySessionBean(MfgCategorySessionLocal mfgCategorySessionBean) {
		this.mfgCategorySessionBean = mfgCategorySessionBean;
	}

	/**
	 * @return the settingsSessionBean
	 */
	public SettingSessionLocal getSettingsSessionBean() {
		return settingsSessionBean;
	}

	/**
	 * @param settingsSessionBean the settingsSessionBean to set
	 */
	public void setSettingsSessionBean(SettingSessionLocal settingsSessionBean) {
		this.settingsSessionBean = settingsSessionBean;
	}

	public boolean checkOut(String custID, Vector<CartItem> cart){
		String methodName = "checkOut";
		boolean checkOutCompleted = false;
		
		if (custID == "") {
			logger.logp(Level.SEVERE, className, methodName, "in checkOut(custID, cart), but custID is empty" );
			return false;
		} else {
			logger.logp(Level.FINE, className, methodName, "running checkOut for custID: " + custID);
		}
		
		if (cart.isEmpty() ) {
			logger.logp(Level.SEVERE, className, methodName, "in checkOut(custID, cart), but cart is empty" );
			return false;
		} 
		
		try{
			
			if(settings==null) initializeSettings();
			
			boolean wsatEnabled = settings.isWsatEnabled();
			boolean wsnEnabled = settings.isWsnEnabled();
			boolean sdoEnabled = settings.isSdoEnabled();
			
			//obtain local object refs
			
			
			//CustomerLocal customer = EJBUtils.findCustomerByPrimaryKeyForUpdate( custID );
			Customer customer = customerSessionBean.findCustomerByCustId(custID);
			if(customer==null){	
				logger.logp(Level.INFO, className, methodName, "null customer? how come?");
				return false; 
			} //invalid custID or customer does not exist
			//CustomerInfoLocal customerInfo = EJBUtils.findCustomerInfoByPrimaryKey(custID);
			CustomerInfo customerInfo = customerInfoSessionBean.findCustomerInfoByCustId(custID);
			
			if(customerInfo==null){ 
				logger.logp(Level.INFO, className, methodName, "null customerInfo? How come?");
				return false; 
			}	//no customer info
			
			//	calculate invoice amount from cart data
			if(cart==null || cart.isEmpty()){ 
				logger.logp(Level.INFO, className, methodName, "empty cart? why bother checkOut?");
				return false;	
			} //no cart data received
			float totalBill = this.calculateCart(cart);
			logger.logp(Level.FINER, className, methodName, "totalBill: " + totalBill);
			
			//increment invoices created
			int tempInvoiceCount= customer.getNumInvoicesCreated();
			String username = customer.getCustName();
			String userpwd = customer.getCustPwd();
			customer.setNumInvoicesCreated( tempInvoiceCount+1 );
			customerSessionBean.updateCustomer(customer);
			if (logger.isLoggable(Level.FINER)) {
				logger.logp(Level.FINER, className, methodName, "Customer: " + username 
						+ " with password: " + userpwd 
						+ " has " + customer.getNumInvoicesCreated() + " invoices");
			}
			
			//initialize payment information(CCNum,invoice amount,etc)
			PaymentInfoWrapper paymentInfo = new PaymentInfoWrapper();
			if (logger.isLoggable(Level.FINER)) {
				logger.logp(Level.FINER, className, methodName, "take a look at the paymentinfo, account: "
						+ paymentInfo.getAccountNumber()
						+ " amount: " + paymentInfo.getAmount()
						+ " trackingNumber: " + paymentInfo.getTrackingNumber());
			}
			if( wsatEnabled ){          // ---------- WSAT
				logger.logp(Level.FINEST, className, methodName, "WSAT enabled");
				paymentInfo.setAccountNumber(customerInfo.getCcNum());
			}else{
				logger.logp(Level.FINEST, className, methodName, "WSAT not enabled");
				paymentInfo.setAccountNumber(username);
				logger.logp(Level.FINE, className, methodName, "set username: " + username + " to paymentInfo");
			}
			paymentInfo.setAmount(totalBill);
			CustomerWrapper customerWrapper = getCustomer(username,userpwd);
			if (customerWrapper == null) { 
				return false;
			}
			CustomerInfoWrapper customerInfoWrapper = getCustomerInfo(username);
			
			if( validateCreditCard(paymentInfo, customerInfoWrapper) ){
				
				if( sdoEnabled ){
					
					/**logger.logp(Level.FINER, className, methodName, "SDO Enabled");
					ShoppingCartWrapper shoppingCart = new ShoppingCartWrapper();
					shoppingCart.setCartItems(cart);
					shoppingCart.setCustomerInfoWrapper(customerInfoWrapper);
					shoppingCart.setCustomerWrapper(customerWrapper);
					shoppingCart.setPaymentInfoWrapper(paymentInfo);
					boolean addInvoiceCompleted = addInvoiceToShippingQueue(shoppingCart);
					if (addInvoiceCompleted == false)
						logger.logp(Level.INFO, className, methodName, "Add invoice to Shipping Queue failed !!");
						//System.out.println("GS : Add invoice to Shipping Queue failed !!");*/
					
					/* ------------------------------ */
					/**checkOutCompleted = this.processTransaction(paymentInfo);*/
					/* ------------------------------ */
					
					//TODO insert call to sendLoggerNotification - checkout method 
					//logging info  would be something like:  "<Servername>-<username>-credit card validated, invoice placed in shipping queue
				}else{
					logger.logp(Level.FINER, className, methodName, "SDO not Enabled");
					checkOutCompleted = this.processTransaction(paymentInfo);
					if(checkOutCompleted){
						 customer = customerSessionBean.findCustomerByCustId(custID);
						tempInvoiceCount = customer.getNumInvoicesCompleted();
						tempInvoiceCount++;
						customer.setNumInvoicesCompleted( tempInvoiceCount );
						customerSessionBean.updateCustomer(customer);
						logger.logp(Level.FINE, className, methodName, "customer " + username + " has "
							 + tempInvoiceCount + " invoices completed");
						/*
						if (logger.isLoggable(Level.FINER)) {
							logger.logp(Level.FINER, className, methodName, "Customer: " + 
									customer.getCustName() + " now has " +
									tempInvoiceCount + " invoices completed");
						} */
					}					
				}
			}else{
				logger.logp(Level.FINER, className, methodName, "check credit not good, return false");
				return false;
			}
		}catch(Exception e){
			logger.logp(Level.SEVERE, className, methodName, e.getMessage());
			e.printStackTrace();
			return false;
		}		
		return checkOutCompleted;
	}
	
	private boolean validateCreditCard(PaymentInfoWrapper paymentInfo, CustomerInfoWrapper customerInfo){
		String methodName = "validateCreditCard";
		boolean cardValidated = false;
		if(settings==null) initializeSettings();
		
		if(settings.isWsatEnabled()){
			logger.logp(Level.FINER, className, methodName, "WSAT enabled, will have to go to ccManager");
			try{
				//ccManagerRemote ccManager = EJBUtils.getccManager();
				
				cardValidated = ccManager.validateCard(paymentInfo.getAccountNumber(), customerInfo.getCustID(), customerInfo.getCustID(), customerInfo.getAddress1(), customerInfo.getAddress2());
			}catch(Exception e){
				logger.logp(Level.SEVERE, className, methodName, e.getMessage());
				e.printStackTrace();
			}
		} else {
			logger.logp(Level.FINER, className, methodName, "WSAT not enabled, just return a true");
			cardValidated = true;
		}		
		return cardValidated;
	}
	
	@SuppressWarnings("unused")
	private boolean processTransaction(PaymentInfoWrapper paymentInfo){
		String methodName = "processTransaction";
		boolean wsatForcedRollback = false;
		if(settings==null)	initializeSettings();
		
		//boolean transactionCompleted=false;   <== not used
		//StoreCreditLocal storeCredit = null;
		//BankSessionBean bankSessionBean = null;
		//ccManagerRemote ccManager = null;
		StoreCredit storeCredit = null;
		
		//TODO insert call to sendLoggerNotification - processTransaction started
		//logging info  would be something like:  "<Servername>-<username>-invoice shipped, initiating transaction"
		
		//determine whether to use WS-AT
		boolean wsatEnabled=settings.isWsatEnabled();
		boolean wsnEnabled=settings.isWsnEnabled();
		//boolean sdoEnabled = settings.isSdoEnabled();  <== again, not used
		logger.logp(Level.FINER, className, methodName, "process transaction with WSAT: " + 
				wsatEnabled + " wsnEnabled: " + wsnEnabled);
		
		//initialize bank account to use if ws-at enabled
		int id=settingsID;
		int suffix=getSubAccountNumber();
		
		try{
			//ut = ut.getUserTransaction();
			ut=mySessionCtx.getUserTransaction();
			ut.begin();
		    	
			if(wsatEnabled){		
				//Call CreditCard Service 
				logger.logp(Level.FINE, className, methodName, "Before CreditCard doDebit : AccountNumber " + paymentInfo.getAccountNumber());
				logger.logp(Level.FINE, className, methodName, "Before CreditCard doDebit : TmeStamp " + System.currentTimeMillis());
				logger.logp(Level.FINE, className, methodName, "Before CreditCard doDebit : AccountNumber " + paymentInfo.getAccountNumber());
				logger.logp(Level.FINE, className, methodName, "Before CreditCard doDebit : TmeStamp " + System.currentTimeMillis());
		//		System.out.println("GS : Before CreditCard doDebit : AccountNumber " + paymentInfo.getAccountNumber());
		//		System.out.println("GS : Before CreditCard doDebit : TmeStamp " + System.currentTimeMillis());
				if(!ccManager.doDebit(paymentInfo.getAccountNumber(),paymentInfo.getAmount(),"GS", settings.isWsnEnabled(),settings.getWsnVariables())){
					//System.out.println("credit cart failed");
					logger.logp(Level.INFO, className, methodName, "credit card failed");
					ut.rollback();
					return false;
				}
				logger.logp(Level.FINE, className, methodName, "After CreditCard doDebit : AccountNumber " + paymentInfo.getAccountNumber());
				logger.logp(Level.FINE, className, methodName, "After CreditCard doDebit : TmeStamp " + System.currentTimeMillis());
				
				if (System.getProperty("WSAT_ROLLBACK_CC")!= null){
					if ((System.getProperty("WSAT_ROLLBACK_CC")).equalsIgnoreCase("true")) {
						ut.rollback();
						logger.logp(Level.INFO, className, methodName, "WSAT_ROLLBACK_CC is true, rolled-back ");
						return false;
					}
				}
				
				if(!bankManager.doTransaction(id,suffix,paymentInfo.getAmount(),"GS", settings.isWsnEnabled(),settings.getWsnVariables())){
					//System.out.println("bank failed");
					logger.logp(Level.INFO, className, methodName, "bank failed");
					ut.rollback();
					return false;
				}			
			}	//end if(wsatEnabled) block
				
			
			if (System.getProperty("WSAT_ROLLBACK_WSBANK")!= null){
				if ((System.getProperty("WSAT_ROLLBACK_WSBANK")).equalsIgnoreCase("true")){
					ut.rollback();
					logger.logp(Level.INFO, className, methodName, "WSAT_ROLLBACK_WSBANK is true, rolled-back ");
					return false;
				}
				}
			// seems that storecredit is done all the time
			//storeCredit = EJBUtils.createStoreCredit(paymentInfo.getAccountNumber(),new java.sql.Timestamp( System.currentTimeMillis() ) );
			storeCredit = storeCreditSessionBean.getNewStoreCredit();
			StoreCreditPK storeCreditPK = storeCredit.getId();
			storeCreditPK.setCustId(paymentInfo.getAccountNumber());
			storeCreditPK.setTime1(new java.sql.Timestamp( System.currentTimeMillis() ));
			//storeCredit = EJBUtils.createStoreCredit(paymentInfo.getAccountNumber(),new java.sql.Timestamp( System.currentTimeMillis() ) );
			if(storeCredit != null){
				storeCredit.setAmount(paymentInfo.getAmount());
				storeCreditSessionBean.createStoreCredit(storeCredit);
				logger.logp(Level.FINE, className, methodName, "StoreCredit updated: " + storeCredit.getAmount());
			}
			else{
				logger.logp(Level.INFO, className, methodName, "ATTENTION! null store credit");
				ut.rollback();
				return false;
			}
			
			if (System.getProperty("WSAT_ROLLBACK_ALL")!= null){
				if ((System.getProperty("WSAT_ROLLBACK_ALL")).equalsIgnoreCase("true")) {
					logger.logp(Level.INFO, className, methodName, "WSAT_ROLLBACK_ALL is " + System.getProperty("WSAT_ROLLBACK_ALL"));
					ut.rollback();
					logger.logp(Level.INFO, className, methodName, "done rollback...");
					return false;
				}
			}
			
			//attempt to commit transaction
			if(ut.getStatus()==Status.STATUS_ACTIVE){ 
				ut.commit();
				//transactionCompleted=true;				
				
			}
		}catch(Exception e){
			logger.logp(Level.SEVERE, className, methodName, e.getMessage());
			e.printStackTrace();
			try{
				if(ut.getStatus()==Status.STATUS_ACTIVE){
					ut.rollback();
					logger.logp(Level.INFO, className, methodName, "Attempting rollback of WS-TX");
					//System.out.println("Attempting rollback of WS-TX");
				}
			}catch(Exception e2){
				logger.logp(Level.SEVERE, className, methodName, e2.getMessage());
				e2.printStackTrace();
			}
			return false;
		}
		return true;
	}

/**	private boolean addInvoiceToShippingQueue(ShoppingCartWrapper shoppingCart){
		String methodName = "addInvoiceToShippingQueue";
		javax.jms.Connection conn = null;
		MessageProducer msgProducer = null;
		Session sess = null;
		try{
			conn = EJBUtils.getGSShippingQCFConn();
		//	Session	sess = EJBUtils.getGSShippingQCF();
			sess = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue queue = EJBUtils.getGSShippingQueue();
			msgProducer = sess.createProducer(queue);
			ObjectMessage message = sess.createObjectMessage(shoppingCart);
			msgProducer.send(message);
			
		}catch(Exception e){
			logger.logp(Level.SEVERE, className, methodName, e.getMessage());
			e.printStackTrace();
			return false;
		}finally {
			try{
				msgProducer.close();
				sess.close();
				conn.close();
				
				//System.out.println("GSEJB : QCF connection closed");
			}catch(Exception e){
				logger.logp(Level.SEVERE, className, methodName, e.getMessage());
				e.printStackTrace();
			}
		}
		return true;
	}*/
	
	public void orderShipped(PaymentInfoWrapper paymentInfo, Vector<CartItem> cart, CustomerInfoWrapper customerInfo){
		String methodName = "orderShipped";
		//if (logger.isLoggable(Level.FINE)) {
        logger.logp(Level.FINE, className, methodName, "<====EJBEJB: entered " + methodName);
        //}
		try{
			//String trackingNumber = paymentInfo.getTrackingNumber();
			//TODO: insert call to WS-N logger, order for userX has shipped with tracking number ###
			
			/* code block in case we ever want to do something if any items haven't shipped
			boolean allShipped = true;
			for(int i = 0 ; i < cart.size() ; i++){
				CartItem cartItem = (CartItem)cart.elementAt(i);
				if(!cartItem.isShipped()){
					allShipped = false;
				}
			}
			*/
			//if (logger.isLoggable(Level.FINE)) {
	        logger.logp(Level.FINE, className, methodName, "<====EJBEJB: to find the customer ");
	        //}
			//CustomerLocal customer = EJBUtils.findCustomerByPrimaryKeyForUpdate(customerInfo.getCustID());
	        Customer customer = customerSessionBean.findCustomerByCustId(customerInfo.getCustID());
			//if (logger.isLoggable(Level.FINE)) {
	        logger.logp(Level.FINE, className, methodName, "<====EJBEJB: found the customer " + customer.getCustName());
	        //}
			int tempInvoiceCount = customer.getNumInvoicesCompleted();
			//if (logger.isLoggable(Level.FINE)) {
	        logger.logp(Level.FINE, className, methodName, "<====EJBEJB: found the customer invoice " + tempInvoiceCount);
	        //}
		//	if( processTransaction(paymentInfo) ){
			tempInvoiceCount++;
			customer.setNumInvoicesCompleted( tempInvoiceCount );
			//if (logger.isLoggable(Level.FINE)) {
	        logger.logp(Level.FINE, className, methodName, "<====EJBEJB: set the invoice completed");
	        //}
		//	}
		}catch(Exception e){
			logger.logp(Level.SEVERE, className, methodName, e.getMessage());
			e.printStackTrace();
			//handleException("processTransaction()",e,"An exception was caught in GSEJB in method orderShipped()");
		}
	}
	
	private float calculateCart(Vector<CartItem> cart){
		String methodName = "calculateCart";
		float totalBill = 0;
		try{
			int verify=0;
			CartItem currentRow = null;
			for(int i=0; i < cart.size(); i++){
				currentRow = cart.elementAt(i);
				verify=verifyCartItem(currentRow);
				if(verify!=0){
					logger.logp(Level.INFO, className, methodName, "Invalid Cart Item, disregarding current CartItem with ID= " + currentRow.getItemID() + ". Return Code is " + verify);
					//System.out.println("Invalid Cart Item, disregarding current CartItem.  Return Code is " + verify);
					continue;
				}
				float totalCostForItem= (float)( currentRow.getItemCount() ) * currentRow.getUnitPrice();
				totalBill+=totalCostForItem;
			}
		}catch(Exception e){
			logger.logp(Level.SEVERE, className, methodName, e.getMessage());
			e.printStackTrace();
		}
		logger.logp(Level.FINE, className, methodName, "total bill is: " + totalBill);
		return totalBill;
			
	}
	
	// TODO:  this logic is very good, some business Exceptions should be created for these situations
	private int verifyCartItem(CartItem item){
		//InventoryLocal inventory = null;
		Inventory inventory=null;
		//inventory=EJBUtils.findInventoryByPrimaryKey(item.getItemID());
		inventory = inventorySessionBean.findInventoryByItemId(item.getItemID());
	
		if( inventory==null)	//INVALID ITEMID, inventory entity does not exist
			return 8;
		if(item.getItemCount()<=0 )	//INVALID ITEM COUNT
			return 4; 
		if( item.getItemID().equals("") ||	//EMPTY OR NULL ITEMID
			item.getItemID()==null	)
			return 7;
		if(item.getUnitPrice()<0)	//INVALID UNITPRICE, unitPrice less than zero
			return 11;
		return 0;	//all checks passed ok
	}
		
	private int getSubAccountNumber(){
		String methodName = "getSubAccountNumber";
		if(settings==null)	initializeSettings();	
		
		int subAccount=0;
		try{			
			subAccount =  random.nextInt(settings.getNumSubAccounts());
			subAccount = subAccount + 1;
		}catch(Exception e){
			logger.logp(Level.SEVERE, className, methodName, e.getMessage());
			e.printStackTrace();
			
		}		
		return subAccount;
	}
	
	
	
	//********************************************************************************************
	//Logger and exception handling methods
	//********************************************************************************************
/*
	private void sendLoggerNotification(String info){
		if(settings==null) initializeSettings();
		
		//String appName, String time, String className, String info
		//TODO insert code to call WS-N logger
	}	*/
	/*
	private void handleException(String method, Exception e, String message){
		System.err.println(message);
		System.out.println(message);
		e.printStackTrace(System.err);
		sendLoggerNotification("Exception caught by GSEJB in method " + method + ".  Exception details:  " + e.toString());
	} */
	

	//********************************************************************************************
	//List and Get methods(finders)
	//********************************************************************************************
	
	public Vector<CategoryWrapper> listCategories(){
		String methodName = "listCategories";
		if(settings==null) initializeSettings();
		
		Vector<CategoryWrapper> categories = new Vector<CategoryWrapper>();
		
		//Iterator i = EJBUtils.findAllCategories();
		List categoriesList = categorySessionBean.getCategories();
		Iterator i=categoriesList.iterator();
		//CategoryLocal categoryLocal = null;
		Category categoryLocal = null;

		CategoryWrapper categoryWrapper = null;
		
		while( i != null && i.hasNext() ) {
			try {				
				categoryLocal = (Category)i.next();
				categoryWrapper = categorySessionBean.getWrapper(categoryLocal);
				if(categoryWrapper!=null) categories.add(categoryWrapper);
				
			} catch (Exception e) {
				logger.logp(Level.SEVERE, className, methodName, e.getMessage());
				e.printStackTrace();
			}
		}
		return categories;
	}
		
	public Vector<MfgCategoryWrapper> listMfgCategories(String categoryName){
		String methodName = "listMfgCategories";
		if(settings==null) initializeSettings();		
		
		Vector<MfgCategoryWrapper> manufacturers = new Vector<MfgCategoryWrapper>();
		//CategoryLocal categoryLocal = null;
		Category categoryLocal = null;
		//MfgCategoryLocal mfgCategoryLocal = null;
		MfgCategory mfgCategoryLocal = null;
		MfgCategoryWrapper mfgCategoryWrapper = null;
		
		try {
			ut = mySessionCtx.getUserTransaction();
			ut.begin();
			categoryLocal=categorySessionBean.findCategoryByCategoryName(categoryName);
			Iterator<MfgCategory> i = mfgCategorySessionBean.getMfgCategory(categoryName).iterator();
			while( i != null && i.hasNext() ) {
					mfgCategoryLocal = (MfgCategory)i.next();
					mfgCategoryWrapper = mfgCategorySessionBean.getWrapper(mfgCategoryLocal);
					if(mfgCategoryWrapper!=null) manufacturers.add(mfgCategoryWrapper);
			}		
			if(i == null)  ut.rollback();
			else ut.commit();
		} catch(Exception ex) {
			logger.logp(Level.SEVERE, className, methodName, ex.getMessage());
			ex.printStackTrace();
		}		
		return manufacturers;
	}
	
	public Vector<MfgCategoryWrapper> listAllMfgCategories(){
		String methodName = "listAllMfgCategories";
		if(settings==null) initializeSettings();
		
		MfgCategory mfgCategoryLocal = null;
		MfgCategoryWrapper mfgCategoryWrapper = null;
		Vector<MfgCategoryWrapper> mfgCategories = new Vector<MfgCategoryWrapper>();
		
		try{
			
			//Iterator i = EJBUtils.findAllMfgCategories();
			Iterator i = mfgCategorySessionBean.getAllMfgCategories().iterator();
			
			while( i != null && i.hasNext() ) {
				mfgCategoryLocal = (MfgCategory)i.next();
				mfgCategoryWrapper = mfgCategorySessionBean.getWrapper(mfgCategoryLocal);
				if(mfgCategoryWrapper != null) mfgCategories.add(mfgCategoryWrapper);
			}			
		} catch(Exception e) {
			logger.logp(Level.SEVERE, className, methodName, e.getMessage());
			e.printStackTrace();			
		}
		return mfgCategories;
	}
	
	public Vector<InventoryWrapper> listInventory(String categoryName, String mfgName){
		String methodName = "listInventory";
		if(settings==null) initializeSettings();
		
		Vector<InventoryWrapper> inventory = new Vector<InventoryWrapper>();
		Inventory inventoryLocal = null;
		InventoryWrapper inventoryWrapper = null;
		
		//Iterator i = EJBUtils.findInventoryByCategoryMfgName(categoryName, mfgName);
		Iterator i = inventorySessionBean.getInventoryByCatAndMfg(categoryName, mfgName).iterator();
		
		while( i != null && i.hasNext() ) {
			try {		
				inventoryLocal = (Inventory)i.next();
				inventoryWrapper = inventorySessionBean.getWrapper(inventoryLocal);
				if(inventoryWrapper != null) inventory.add(inventoryWrapper);				
			} catch (Exception e) {
				logger.logp(Level.SEVERE, className, methodName, e.getMessage());
				e.printStackTrace();				
			}
		}		
		return inventory;	
	}
		
	public Vector<InventoryWrapper> listInventoryByCategoryOrMfg(String arg1,int findBy ){
		String methodName = "listInventoryByCategoryOrMfg";
		if(settings==null)	initializeSettings();	
		
		Iterator i = null;
		Vector<InventoryWrapper> inventory = new Vector<InventoryWrapper>();
		Inventory inventoryLocal = null;
		InventoryWrapper inventoryWrapper = null;
		if(findBy==54){			//find by mfgName
			//i = EJBUtils.findInventoryByMfgName(arg1);
			i=inventorySessionBean.getInventoryListByMfgName(arg1).iterator();
		}else if(findBy==53){	//find by category
			//i = EJBUtils.findInventoryByCategory(arg1);
			i= inventorySessionBean.getInventoryListByCatName(arg1).iterator();
		}
		
		while( i != null && i.hasNext() ) {
			try {		
				inventoryLocal = (Inventory)i.next();
				inventoryWrapper = inventorySessionBean.getWrapper(inventoryLocal);
				if(inventoryWrapper != null) inventory.add(inventoryWrapper);				
			} catch (Exception e) {
				logger.logp(Level.SEVERE, className, methodName, e.getMessage());
				e.printStackTrace();
			}
		}		
		return inventory;	
	}
		
	public Vector<InventoryWrapper> listAllInventory(){
		String methodName = "listAllInventory";
		if(settings==null) initializeSettings();
		
		Vector<InventoryWrapper> inventory = new Vector<InventoryWrapper>();
		//InventoryLocal inventoryLocal = null;
		Inventory inventoryLocal=null;
		InventoryWrapper inventoryWrapper = null;
		//Iterator i = EJBUtils.findAllInventory();
		Iterator i = inventorySessionBean.getAllInventory().iterator();
		
		while( i != null && i.hasNext() ) {
			try {		
				inventoryLocal = (Inventory)i.next();
				inventoryWrapper = inventorySessionBean.getWrapper(inventoryLocal);
				if(inventoryWrapper != null) inventory.add(inventoryWrapper);				
			} catch (Exception e) {
				logger.logp(Level.SEVERE, className, methodName, e.getMessage());
				e.printStackTrace();
			}
		}		
		return inventory;	
	}
	
	public Vector listAllCustomers(){
		String methodName = "listAllCustomers";
		if(settings==null) initializeSettings();		
		
		Vector customers = new Vector();
		//CustomerLocal customer = null;
		Customer customer = null;
		CustomerWrapper customerWrapper = null;
		
		//Iterator i = EJBUtils.findAllCustomers();
		Iterator i = customerSessionBean.getAllCustomers().iterator();
		int totalInvoicesCreated=0;
		int totalInvoicesCompleted=0;
		int numCustomers;
		
		while( i!=null && i.hasNext()) {   // why not simply i.hasNext() ?
			try{
				customer = (Customer)i.next();
				customerWrapper = customerSessionBean.getWrapper(customer);
				if(customerWrapper !=null) customers.add(customerWrapper);
				totalInvoicesCreated += customerWrapper.getNumInvoicesCreated();
				totalInvoicesCompleted += customerWrapper.getNumInvoicesCompleted();
			}catch(Exception e){
				logger.logp(Level.SEVERE, className, methodName, e.getMessage());
				e.printStackTrace();
				//handleException("processTransaction()",e,"An exception was caught in GSEJB in method listAllCustomers()");
			}
		}
		numCustomers = customers.size();
		customers.add(new String(totalInvoicesCreated + " invoices created and " + totalInvoicesCompleted + " invoices completed by " + numCustomers + " customers"));
		return customers;
	}
	
	public String getTotalStoreCredit(){
		String methodName = "getTotalStoreCredit";
		if(settings==null)	initializeSettings();
		
		StoreCredit storeCredit = null;
		String totalCredit=null;
		float runningTotal=0;
		int numCredits=0;
		
		try{
			//Iterator i = EJBUtils.findAllStoreCredit();
			Iterator<StoreCredit> i = storeCreditSessionBean.getAllStoreCredit().iterator();
			while(i!=null && i.hasNext()){
				storeCredit=(StoreCredit)i.next();
				numCredits++;
				runningTotal += storeCredit.getAmount();
			}
			totalCredit = new String("$" + runningTotal + " in " + numCredits + " storeCredit entries");
		}catch(Exception e){
			logger.logp(Level.SEVERE, className, methodName, e.getMessage());
			e.printStackTrace();
		}
		return totalCredit;
	}
	
	public InventoryWrapper getInventory(String itemID){
		String methodName = "getInventory";
		if(settings==null) initializeSettings();
		
		InventoryWrapper inventoryWrapper = new InventoryWrapper();
		Inventory inventoryLocal = null;
		
		try{
			//inventoryLocal = EJBUtils.findInventoryByPrimaryKey(itemID);
			inventoryLocal = inventorySessionBean.findInventoryByItemId(itemID);
			inventoryWrapper = inventorySessionBean.getWrapper(inventoryLocal);
		} catch (Exception e){
			logger.logp(Level.SEVERE, className, methodName, e.getMessage());
			e.printStackTrace();
		}
		return inventoryWrapper;
	}
	
	public CategoryWrapper getCategory(String categoryName){
		String methodName = "getCategory";
		if(settings==null)	initializeSettings();
		
		CategoryWrapper categoryWrapper = new CategoryWrapper();
		Category categoryLocal = null;
		
		try{
			//categoryLocal = EJBUtils.findCategoryByPrimaryKey(categoryName);
			categoryLocal = categorySessionBean.findCategoryByCategoryName(categoryName);
			categoryWrapper = categorySessionBean.getWrapper(categoryLocal);
		} catch(Exception e){
			logger.logp(Level.SEVERE, className, methodName, e.getMessage());
			e.printStackTrace();
		}
		return categoryWrapper;
	}
	
	public CustomerInfoWrapper getCustomerInfo(String custID){
		String methodName = "getCustomerInfo";
		if(settings==null)	initializeSettings();
		
		CustomerInfoWrapper customerInfoWrapper = new CustomerInfoWrapper();
		CustomerInfo customerInfoLocal = null;
		
		try{
			//customerInfoLocal = EJBUtils.findCustomerInfoByPrimaryKey(custID);
			customerInfoLocal= customerInfoSessionBean.findCustomerInfoByCustId(custID);
			customerInfoWrapper = customerInfoSessionBean.getWrapper(customerInfoLocal);
		} catch(Exception e){
			logger.logp(Level.SEVERE, className, methodName, e.getMessage());
			e.printStackTrace();
		}
		return customerInfoWrapper;
	}
		
	public CustomerWrapper getCustomer(String custID,String custPwd){
		String methodName = "getCustomer";
		if(settings==null)	initializeSettings();
		
		if ( custID.equals(custPwd)) {
			// in GSEJB, we are seeing matched userid and password
			logger.logp(Level.FINE, className, methodName, "customer: " + custID
					+ " = password: " + custPwd);
		} else {
			logger.logp(Level.INFO, className, methodName, "ATTENTION! customer: " + custID
					+ " doesn't match password: " + custPwd);
		}
		CustomerWrapper customerWrapper = new CustomerWrapper();
		Customer customerLocal = null;
		
		try{
			//customerLocal = EJBUtils.findCustomerByPrimaryKey(custID);
			customerLocal= customerSessionBean.findCustomerByCustId(custID);
			customerWrapper = customerSessionBean.getWrapper(customerLocal);
		} catch(Exception e){
			logger.logp(Level.SEVERE, className, methodName, e.getMessage());
			e.printStackTrace();
		}
		if(customerWrapper.getCustPwd().equals(custPwd)) 
			return customerWrapper;
		else {
			
			logger.logp(Level.INFO, className, methodName, "Null customer ???? " + custID
					+ " while password: " + custPwd);
			return null;
		}
	}
	
	//********************************************************************************************
	//Populate, Clear and Reset Methods
	//********************************************************************************************
	
	public boolean populateInventory(SettingsWrapper settingsWrapper){
		String methodName = "populateInventory";
		InventoryWrapper inventory;
		//CategoryLocal category=null;
		Category category=null;
		boolean success = false;
		
		try{
			/* has been moved to get called from PopulateDBServlet
			//if WS-AT is enabled, kick off population for wsbank and cc webservices
			CCInfoWrapper[] ccinfo = new CCInfoWrapper[settingsWrapper.getNumCustomers()];
			if(settingsWrapper.isWsatEnabled()){	
				System.out.println("WSAT is enabled, popoulate band and credit cards");
				populateBankDB(settings);
				ccinfo = populateCCDB(settingsWrapper.getNumCustomers());
			}
			*/
								
			for(int i=1;i<=settingsWrapper.getNumCategories();i++) {
				createNewCategory("CATEGORY" + i,"Category " + i);
				//category=EJBUtils.findCategoryByPrimaryKey("CATEGORY" + i);
				category=categorySessionBean.findCategoryByCategoryName("CATEGORY" + i);
				
				//if (logger.isLoggable(Level.FINER)) {
				logger.logp(Level.FINER, className, methodName, "found category: " + category.getCategoryDescription());
				//}
				for(int j=1;j<=settingsWrapper.getNumMfgCategories();j++){
					createNewMfgCategory("CATEGORY" + i ,"CAT" + i + "MFG" + j,false);
					for(int k=1;k<=settingsWrapper.getNumItemsPerMfg();k++) {
						Double itemValue=new Double(Math.random()*500);
						inventory=new InventoryWrapper();
						inventory.setCategoryName("CATEGORY" + i);
						inventory.setDescription("CAT" + i + " MFG" + j + " item " + k);
						inventory.setMfgName("CAT" + i + "MFG" + j);
						inventory.setInventorySold(0);
						inventory.setUnitPrice( itemValue.intValue());
						this.createNewInventory(inventory);
					}
				}
			}
			success=true;
		}catch(Exception e){
			logger.logp(Level.SEVERE, className, methodName, e.getMessage());
			e.printStackTrace();
		}
		return success;
	}
	
/*	public boolean populateCustomers(SettingsWrapper settingsWrapper,Vector ccinfoVector){
		CustomerWrapper customer;
		CustomerInfoWrapper customerInfo;
		boolean success = false;

		try{//create customer and customerInfo data
			
			for(int i=0;i<settingsWrapper.getNumCustomers();i++){
				int custID = i + 1;
				customer=new CustomerWrapper();
				customerInfo=new CustomerInfoWrapper();
				customer.setCustID("user" + custID);
				customer.setCustName("user" + custID);	
				customer.setCustPwd("user" + custID);
				customer.setNumInvoicesCompleted(0);
				customer.setNumInvoicesCreated(0);
				
				customerInfo=new CustomerInfoWrapper();
				customerInfo.setCustID("user" + custID);
				customerInfo.setAddress1(custID + " Miami Blvd.");
				customerInfo.setAddress2("Apartment " + custID);
				customerInfo.setEmail("user" + custID + "@SVTRTPM.ibm.com");
				customerInfo.setPhone("555-555-5555");	
				
				if(settingsWrapper.isWsatEnabled()){ 
					customerInfo.setCcNum((String)ccinfoVector.elementAt(i));
				}					
				createNewCustomer(customer,customerInfo);
			}
			success = true;
		}catch(Exception e){
			handleException("populateCustomers()",e,"An exception was caught in GSEJB in method populateCustomers()");
		}
		return success;
	}
		*/
	
	public boolean populateCustomers(SettingsWrapper settingsWrapper,
			CCInfoWrapper[] ccinfoArray) {
		
		String methodName = "populateCustomers";
		
		CustomerWrapper customer;
		CustomerInfoWrapper customerInfo;
		boolean success = false;
	
		//if (logger.isLoggable(Level.FINE)) {
		logger.logp(Level.FINE, className, methodName, "\tjust entered the populateCustomers()...");
		//}
		
		try {  //create customer and customerInfo data
			int numLoops = settingsWrapper.getNumCustomers();
			//if (logger.isLoggable(Level.FINE)) {
			logger.logp(Level.FINE, className, methodName, "from SettingsWrapper, the loop is: " + numLoops);
			logger.logp(Level.FINE, className, methodName, "from ccinfoArray, the element # is: " + ccinfoArray.length);
			//}
			
			for(int i=0; i < numLoops; i++) {
				int custID = i + 1;
				customer=new CustomerWrapper();
				customerInfo=new CustomerInfoWrapper();
				customer.setCustID("user" + custID);
				customer.setCustName("user" + custID);	
				customer.setCustPwd("user" + custID);
				customer.setNumInvoicesCompleted(0);
				customer.setNumInvoicesCreated(0);
			
				customerInfo=new CustomerInfoWrapper();
				customerInfo.setCustID("user" + custID);
				customerInfo.setAddress1(custID + " Miami Blvd.");
				customerInfo.setAddress2("Apartment " + custID);
				customerInfo.setEmail("user" + custID + "@SVTRTPM.ibm.com");
				customerInfo.setPhone("555-555-5555");	
			
				if(settingsWrapper.isWsatEnabled()){ 
					customerInfo.setCcNum((String)ccinfoArray[i].getCCNum());
				}					
				createNewCustomer(customer,customerInfo);
			}
			success = true;
		}catch(Exception e){
			logger.logp(Level.SEVERE, className, methodName, e.getMessage());
			e.printStackTrace();
		}
		return success;
	}
	
	
	public void populateBankDB(SettingsWrapper settings){
		String methodName = "populateBandDB";
		//BankSessionBean bankSessionBean = EJBUtils.getBankSessionBean();
				
		try{
			bankManager.createAccountsForGS(settingsID,settings.getNumSubAccounts());
		}catch(Exception e){
			logger.logp(Level.SEVERE, className, methodName, e.getMessage());
			e.printStackTrace();
		}
	}
	
 /*  public Vector populateCCDB(int numCustomers){
		Vector ccinfo = null;
		Vector ccinfoString = new Vector();
		ccManager ccManager = EJBUtils.getccManager();
	    	    
		try{
			ccinfo = ccManager.createAccountForGS(numCustomers);
			for(int i = 0 ; i < ccinfo.size() ; i++){
				ccinfoString.add( ((CCInfoWrapper)ccinfo.elementAt(i)).getCCNum() );
			}
		}catch(java.rmi.RemoteException e){
			handleException("populateCCDB()",e,"An exception was caught in GSEJB in method populateCCDB()");
		} 
		return ccinfoString;
		
	} */
	
	@SuppressWarnings("unused")
	public CCInfoWrapper[] populateCCDB(int numCustomers){
		String methodName = "populateCCDB";
		
		CCInfoWrapper[] ccinfo = new CCInfoWrapper[numCustomers];		
		//if (logger.isLoggable(Level.FINE)) 
		logger.logp(Level.FINE, className, methodName, "Populate CC Tables with numCustomers " + numCustomers);
	    //ccManagerRemote ccManager = EJBUtils.getccManager();
	    //if (logger.isLoggable(Level.FINE))
	    if(ccManager==null){
			logger.logp(Level.FINE, className, methodName, "The ccManagerService reference is null");
	    }
	    logger.logp(Level.FINE, className, methodName, "ccManager reference is: " + ccManager.toString());
	    
		try{
			//if (logger.isLoggable(Level.FINE))
			logger.logp(Level.FINE, className, methodName, "Attempt to make call to ccManager");
			List<CCInfoWrapper> tempList = ccManager.createAccountForGS(numCustomers);
			//if (logger.isLoggable(Level.FINE)) {
			if (tempList == null) {
				logger.logp(Level.FINE, className, methodName, "tempList null");
			} else {
				logger.logp(Level.FINE, className, methodName, "List returned from ccManager has element #: " + tempList.size());
			}
				//CCInfoWrapper[] tempArray = ccManager.createAccountForGS(numCustomers);
			CCInfoWrapper[] tempArray = new CCInfoWrapper[tempList.size()];
			tempArray = tempList.toArray(tempArray);
			//if (logger.isLoggable(Level.FINE)) {
			logger.logp(Level.FINE, className, methodName, "tempArray returned from ccManager, "
					+ tempArray.length + " elements, attempting to copy");
			//}
			//System.out.println("tempArray returned from ccManager, attempting to copy");
			System.arraycopy(tempArray,0,ccinfo,0,numCustomers);
			//if (logger.isLoggable(Level.FINE))
			logger.logp(Level.FINE, className, methodName, "tempArray copied into ccinfo array");
			//System.out.println("tempArray copied into ccinfo array");						
		}catch(Exception e){
			logger.logp(Level.SEVERE, className, methodName, e.getMessage());
			e.printStackTrace();
		} 
		if(ccinfo==null){
			logger.logp(Level.FINE, className, methodName, "The CCInfoList is  null in StoreSessionBeanBean");
		}
		else{
			logger.logp(Level.FINE, className, methodName, "The CCInfoList is  not null in StoreSessionBeanBean");
		}
		return ccinfo;
		
	} 
	
	
	public boolean clearGSDB(){
		String methodName = "clearGSDB";
		boolean cleared = false;
		try{
			//DataSource gsDs = EJBUtils.getGsDs();
			DataSource gsDs =GarageSaleUtils.getGsDs();
			Connection gsConn = gsDs.getConnection();
			gsConn.setAutoCommit(false);
			PreparedStatement stmt=null;
			//ResultSet rs = null;
			String sql = null;
			
			sql = "delete from category";
			stmt = gsConn.prepareStatement(sql);
			stmt.executeUpdate();
			stmt.close();	
			gsConn.commit();
			
			sql = "delete from customer";
			stmt = gsConn.prepareStatement(sql);
			stmt.executeUpdate();
			stmt.close();	
			gsConn.commit();
			
			sql = "delete from customerinfo";
			stmt = gsConn.prepareStatement(sql);
			stmt.executeUpdate();
			stmt.close();	
			gsConn.commit();
			
			sql = "delete from inventory";
			stmt = gsConn.prepareStatement(sql);
			stmt.executeUpdate();
			stmt.close();	
			gsConn.commit();
			
			sql = "delete from mfgcategory";
			stmt = gsConn.prepareStatement(sql);
			stmt.executeUpdate();
			stmt.close();	
			gsConn.commit();
			
			sql = "delete from settings";
			stmt = gsConn.prepareStatement(sql);
			stmt.executeUpdate();
			stmt.close();	
			gsConn.commit();
			
			sql = "delete from storecredit";
			stmt = gsConn.prepareStatement(sql);
			stmt.executeUpdate();
			stmt.close();	
			gsConn.commit();
			
			cleared = true;			
		}catch(Exception e){
			logger.logp(Level.SEVERE, className, methodName, e.getMessage());
			e.printStackTrace();
			cleared = false;
		}
		return cleared;
	}
	
	public boolean resetGSDB(boolean isZ){
		String methodName = "resetGSDB";
		boolean resetComplete = false;
		try{
			//DataSource gsDs = EJBUtils.getGsDs();
			DataSource gsDs =GarageSaleUtils.getGsDs();
			Connection gsConn = gsDs.getConnection();
			gsConn.setAutoCommit(false);
			PreparedStatement stmt=null;
			//ResultSet rs = null;
			String sql = null;
			
			sql = "delete from storeCredit";
			stmt = gsConn.prepareStatement(sql);
			stmt.executeUpdate();
			stmt.close();	
			gsConn.commit();
			
			sql = "update customer set numinvoicescreated=0";
			stmt = gsConn.prepareStatement(sql);
			stmt.executeUpdate();
			stmt.close();	
			gsConn.commit();
			
			if(isZ){
				sql = "update customer set numinvoicescomple1=0";
				stmt = gsConn.prepareStatement(sql);
				stmt.executeUpdate();
				stmt.close();	
				gsConn.commit();
			}else{
				sql = "update customer set numinvoicescompleted=0";
				stmt = gsConn.prepareStatement(sql);
				stmt.executeUpdate();
				stmt.close();	
				gsConn.commit();
			}
			
			
			resetComplete = true;
			
		}catch(Exception e){
			logger.logp(Level.SEVERE, className, methodName, e.getMessage());
			e.printStackTrace();
			resetComplete = false;
		}
		return resetComplete;
	}
	
	public boolean clearBankDB(){
		String methodName = "clearBankDB";
		boolean cleared = false;
		try{
			//DataSource wsbankDS = EJBUtils.getWsbankDs();
			DataSource wsbankDS =GarageSaleUtils.getWsbankDs();

			Connection wsbankConn = wsbankDS.getConnection();
			wsbankConn.setAutoCommit(false);
			PreparedStatement stmt=null;
			//ResultSet rs = null;
			String sql = null;
			
			sql = "delete from transactionhistory";
			stmt = wsbankConn.prepareStatement(sql);
			stmt.executeUpdate();
			stmt.close();	
			wsbankConn.commit();
			
			sql = "delete from subaccount";
			stmt = wsbankConn.prepareStatement(sql);
			stmt.executeUpdate();
			stmt.close();	
			wsbankConn.commit();
			
			sql = "delete from account";
			stmt = wsbankConn.prepareStatement(sql);
			stmt.executeUpdate();
			stmt.close();	
			wsbankConn.commit();
			
			cleared = true;		
		}catch(Exception e){
			logger.logp(Level.SEVERE, className, methodName, e.getMessage());
			e.printStackTrace();
			cleared = false;
		}		
		return cleared;
	}
	
	public boolean resetBankDB(){
		String methodName = "resetBankDB";
		boolean cleared = false;
		try{
			//DataSource wsbankDS = EJBUtils.getWsbankDs();
			DataSource wsbankDS =GarageSaleUtils.getWsbankDs();
			Connection wsbankConn = wsbankDS.getConnection();
			wsbankConn.setAutoCommit(false);
			PreparedStatement stmt=null;
			//ResultSet rs = null;
			String sql = null;
			
			sql = "delete from transactionhistory";
			stmt = wsbankConn.prepareStatement(sql);
			stmt.executeUpdate();
			stmt.close();	
			wsbankConn.commit();
			
			sql = "update subaccount set balance=0";
			stmt = wsbankConn.prepareStatement(sql);
			stmt.executeUpdate();
			stmt.close();	
			wsbankConn.commit();
						
			cleared = true;		
		}catch(Exception e){
			logger.logp(Level.SEVERE, className, methodName, e.getMessage());
			e.printStackTrace();
			cleared = false;
		}		
		return cleared;		
	}
		
	public boolean clearCCDB(){
		String methodName = "clearCCDB";
		boolean cleared = false;
		try{
			//DataSource ccDS = EJBUtils.getCcDs();
			DataSource ccDS =GarageSaleUtils.getCcDs();

			Connection ccConn = ccDS.getConnection();
			ccConn.setAutoCommit(false);
			PreparedStatement stmt=null;
			//ResultSet rs = null;
			String sql = null;
			
			sql = "delete from ccownerinfo";
			stmt = ccConn.prepareStatement(sql);
			stmt.executeUpdate();
			stmt.close();	
			ccConn.commit();
			
			sql = "delete from ccinfo";
			stmt = ccConn.prepareStatement(sql);
			stmt.executeUpdate();
			stmt.close();	
			ccConn.commit();
			
			sql = "delete from ccauthinfo";
			stmt = ccConn.prepareStatement(sql);
			stmt.executeUpdate();
			stmt.close();	
			ccConn.commit();
			
			sql = "delete from cchistory";
			stmt = ccConn.prepareStatement(sql);
			stmt.executeUpdate();
			stmt.close();	
			ccConn.commit();
			
			sql = "delete from ccuniqueid";
			stmt = ccConn.prepareStatement(sql);
			stmt.executeUpdate();
			stmt.close();	
			ccConn.commit();
			
			cleared = true;			
		}catch(Exception e){
			logger.logp(Level.SEVERE, className, methodName, e.getMessage());
			e.printStackTrace();
			cleared = false;
		}
		return cleared;
	}

	public boolean resetCCDB(){
		String methodName = "resetCCDB";
		boolean cleared = false;
	  	try{
			//DataSource ccDS = EJBUtils.getCcDs();
			DataSource ccDS =GarageSaleUtils.getCcDs();
	  		Connection ccConn = ccDS.getConnection();
			ccConn.setAutoCommit(false);
			PreparedStatement stmt=null;
			//ResultSet rs = null;
			String sql = null;
			
			sql = "delete from cchistory";
			stmt = ccConn.prepareStatement(sql);
			stmt.executeUpdate();
			stmt.close();	
			ccConn.commit();
			
			sql = "update ccinfo set balance=0";
			stmt = ccConn.prepareStatement(sql);
			stmt.executeUpdate();
			stmt.close();	
			ccConn.commit();
			
			cleared = true;		
		}catch(Exception e){
			logger.logp(Level.SEVERE, className, methodName, e.getMessage());
			e.printStackTrace();
			cleared = false;
		}
		
	  	return cleared;
	}

	//********************************************************************************************
	//DB Verification methods
	//********************************************************************************************
	
	public String getCCHistoryData(){
		String methodName = "getCCHistoryData";
		StringBuffer ccHistoryData = new StringBuffer();
		try{
			//DataSource ccDS = EJBUtils.getCcDs();
			DataSource ccDS =GarageSaleUtils.getCcDs();
			Connection ccConn = ccDS.getConnection();
			ccConn.setAutoCommit(false);
			PreparedStatement stmt=null;
			ResultSet rs = null;
			String sql = null;
			
			sql = "select count(*) from cchistory";
			stmt = ccConn.prepareStatement(sql);
			rs = stmt.executeQuery();
			ccHistoryData.append(rs.getFloat(1));
			ccHistoryData.append("|");
			stmt.close();	
			ccConn.commit();
			
			sql = "select sum(amount) from cchistory";
			stmt = ccConn.prepareStatement(sql);
			rs = stmt.executeQuery();
			ccHistoryData.append(rs.getFloat(1));
			ccHistoryData.append("|");
			stmt.close();	
			ccConn.commit();
			
			sql = "select sum(balance) from ccinfo";
			stmt = ccConn.prepareStatement(sql);
			rs = stmt.executeQuery();
			ccHistoryData.append(rs.getFloat(1));
			ccHistoryData.append("|");
			stmt.close();	
			ccConn.commit();
			ccConn.close();
			
		}catch(Exception e){
			logger.logp(Level.SEVERE, className, methodName, e.getMessage());
			e.printStackTrace();
		}
		
		return ccHistoryData.toString();
	}
	
	public String getBankHistoryData(){
		String methodName = "getBankHistoryData";
		StringBuffer bankHistoryData = new StringBuffer();
		try{
			//DataSource bankDS = EJBUtils.getWsbankDs();
			DataSource bankDS =GarageSaleUtils.getWsbankDs();

			Connection bankConn = bankDS.getConnection();
			bankConn.setAutoCommit(false);
			PreparedStatement stmt=null;
			ResultSet rs = null;
			String sql = null;
			
			sql = "select count(*) from transactionhistory";
			stmt = bankConn.prepareStatement(sql);
			rs = stmt.executeQuery();
			bankHistoryData.append(rs.getFloat(1));
			bankHistoryData.append("|");
			stmt.close();	
			bankConn.commit();
			
			sql = "select sum(amount) from transactionhistory";
			stmt = bankConn.prepareStatement(sql);
			rs = stmt.executeQuery();
			bankHistoryData.append(rs.getFloat(1));
			bankHistoryData.append("|");
			stmt.close();	
			bankConn.commit();
			
			sql = "select sum(balance) from subaccount";
			stmt = bankConn.prepareStatement(sql);
			rs = stmt.executeQuery();
			bankHistoryData.append(rs.getFloat(1));
			bankHistoryData.append("|");
			stmt.close();	
			bankConn.commit();
			bankConn.close();
			
		}catch(Exception e){
			logger.logp(Level.SEVERE, className, methodName, e.getMessage());
			e.printStackTrace();
		}
		
		return bankHistoryData.toString();
	}
	
	
	//********************************************************************************************
	//local settings methods
	//********************************************************************************************
	
	public SettingsWrapper getSettings(){
		if(settings == null){
			initializeSettings();
		}
		return settings;
	}
		
	private void initializeSettings(){
		settings = null;
		settings = new SettingsWrapper();
		//SettingsLocal settingsCMP = EJBUtils.findSettingsByPrimaryKey(settingsID);
		Setting setting = settingsSessionBean.findSettingById(settingsID);
		settings.setNumCategories(setting.getNumCategories());
		settings.setNumCustomers(setting.getNumCustomers());
		settings.setNumItemsPerMfg(setting.getNumItemsPerMfg());
		settings.setNumMfgCategories(setting.getNumMfgCategories());
		settings.setNumSubAccounts(setting.getNumSubAccounts());
		settings.setWsatEnabled(isEnabled(setting.getWsatEnabled()));
		settings.setWsnEnabled(isEnabled(setting.getWsnEnabled())); 
		settings.setSdoEnabled(isEnabled(setting.getSdoEnabled()));
		settings.setWsnVariables(setting.getWsnVariables());
	}
	
	
	
	
	
	//********************************************************************************************
	//Create, Delete and Update Methods
	//********************************************************************************************
	

	public void updateSettings(SettingsWrapper settingsWrapper){
		//SettingsLocal settingsLocal = EJBUtils.findSettingsByPrimaryKey(settingsID);
		Setting settingsLocal = settingsSessionBean.findSettingById(settingsID);
		settingsLocal.setNumCategories(settingsWrapper.getNumCategories());
		settingsLocal.setNumMfgCategories(settingsWrapper.getNumMfgCategories());
		settingsLocal.setNumItemsPerMfg(settingsWrapper.getNumItemsPerMfg());
		settingsLocal.setNumCustomers(settingsWrapper.getNumCustomers());
		settingsLocal.setNumSubAccounts(settingsWrapper.getNumCustomers()/10);
		settingsLocal.setWsatEnabled(getDBShort(settingsWrapper.isWsatEnabled()));
		settingsLocal.setWsnEnabled(getDBShort(settingsWrapper.isWsnEnabled()));
		settingsLocal.setSdoEnabled(getDBShort(settingsWrapper.isSdoEnabled()));
		settingsLocal.setWsnVariables(settingsWrapper.getWsnVariables());
		
		settings = settingsWrapper;
	}
	
	public boolean createNewCustomer(CustomerWrapper customer, CustomerInfoWrapper customerInfo){
		String methodName = "createNewCustomer";
		if(settings==null) 	initializeSettings();
		
		boolean isCreated = false;
		//CustomerLocal customerLocal = null;
		//CustomerInfoLocal customerInfoLocal = null;
		Customer customerLocal = null;
		CustomerInfo customerInfoLocal = null;
		
		try{
			//customerLocal = EJBUtils.createCustomer(customer.getCustID());
			//customerInfoLocal = EJBUtils.createCustomerInfo(customerInfo.getCustID());

			customerLocal = customerSessionBean.getNewCustomer();
			customerLocal.setCustId(customer.getCustID());
			customerInfoLocal = customerInfoSessionBean.getNewCustomerInfo();
			customerInfoLocal.setCustId(customerInfo.getCustID());
			
			customerLocal.setCustName(customer.getCustName());
			customerLocal.setCustPwd(customer.getCustPwd());
			
			customerInfoLocal.setAddress1(customerInfo.getAddress1());
			customerInfoLocal.setAddress2(customerInfo.getAddress2());
			customerInfoLocal.setEMail(customerInfo.getEmail());
			customerInfoLocal.setPhone(customerInfo.getPhone());
			customerInfoLocal.setCcNum(customerInfo.getCcNum());
			isCreated = true;
			
		} catch(Exception e){
			logger.logp(Level.SEVERE, className, methodName, e.getMessage());
			e.printStackTrace();
		}
		
		return isCreated;
	}
	
	public boolean createNewCategory(String categoryName,String categoryDescription) {
		String methodName = "createNewCategory";
		if(settings==null) initializeSettings();
		
		boolean isCreated = false;
		//CategoryLocal categoryLocal = null;
		Category categoryLocal = null;

		try {
			//categoryLocal = EJBUtils.createCategory(categoryName);
			categoryLocal = categorySessionBean.getNewCategory();
			categoryLocal.setCategoryName(categoryName);
			categorySessionBean.createCategory(categoryLocal);
			
			categoryLocal.setNumItemsInCategory(0);
			categoryLocal.setCategoryDescription(categoryDescription);

			isCreated = true;
			//if (logger.isLoggable(Level.FINE)) {
				
			logger.logp(Level.FINE, className, methodName, "created category: " + categoryName);
			//}
		} catch (Exception e) {
			logger.logp(Level.SEVERE, className, methodName, e.getMessage());
			e.printStackTrace();
		}

		return isCreated;
	}

	public boolean createNewMfgCategory(String categoryName,
			String mfgName,	
			boolean createCategory) {
		String methodName = "createNewMfgCategory";
		if(settings==null)	initializeSettings();
		
		boolean isCreated = false;
		//CategoryLocal categoryLocal = null;
		//MfgCategoryLocal mfgCategoryLocal = null;
		
		Category categoryLocal = null;
		MfgCategory mfgCategoryLocal = null;


		try {
		categoryLocal = categorySessionBean.findCategoryByCategoryName(categoryName);
		if (categoryLocal == null && createCategory) {
			logger.logp(Level.FINE, className, methodName, "category " + categoryName
					+ " doesn't exist, to create? " + createCategory);
			
			// if category does not exist, attempt to create a new category
			boolean categoryCreated = createNewCategory(categoryName, "");
			// if category still not created, return false
			if (!categoryCreated) {
				logger.logp(Level.FINE, className, methodName, "category " + categoryName 
						+ " Created? " + categoryCreated);
				return false;
			// if category was created, assign categoryLocal to newly created category
			} else {
				logger.logp(Level.FINE, className, methodName, "category " + categoryName + " created");
				categoryLocal =
						categorySessionBean.findCategoryByCategoryName(categoryName);
				if (logger.isLoggable(Level.FINE)) {
					String mfgCategoryName = categorySessionBean.getWrapper(categoryLocal).getCategoryName();
					logger.logp(Level.FINE, className, methodName, "found categorylocal: " + 
							mfgCategoryName);
							//categoryLocal.getCategoryDescription());
				}
			}

		} else if (categoryLocal == null && !createCategory) {
			// if category does not exist, do not create a new category, return false
			logger.logp(Level.FINE, className, methodName, "category " + categoryName
					+ " doesn't exist, to create? " + createCategory);
			return false;
		}

			//category exists or has been created, attempt to create the MfgCategory
		//mfgCategoryLocal = mfgCategorySessionBean.createMfgCategory(mfgName, categoryLocal);
		mfgCategoryLocal=mfgCategorySessionBean.getNewMfgCategory();
		mfgCategoryLocal.getId().setMfgName(mfgName);
		mfgCategoryLocal.getId().setCategoryCategoryName(categoryLocal.getCategoryName());
		mfgCategorySessionBean.createMfgCategory(mfgCategoryLocal);
		
			//if (logger.isLoggable(Level.FINE)) {
		logger.logp(Level.FINE, className, methodName, "found mfgCategory: " + mfgCategoryLocal.toString());
		isCreated = true;
		}
		catch (Exception e) {
			logger.logp(Level.SEVERE, className, methodName, e.getMessage());
			e.printStackTrace();
		} 
		return isCreated;
	}
	
	public boolean createNewInventory(InventoryWrapper inventoryWrapper) {
		String methodName = "createNewInventory";
		if(settings==null)	initializeSettings();
		
		
		//InventoryLocal inventoryLocal = null;
		//CategoryLocal categoryLocal = null;
		Inventory inventoryLocal = null;
		Category categoryLocal = null;
		boolean isCreated = false;
		String itemID;
					
		try {
			//categoryLocal =	EJBUtils.findCategoryByPrimaryKey(inventoryWrapper.getCategoryName());
			categoryLocal =	categorySessionBean.findCategoryByCategoryName(inventoryWrapper.getCategoryName());
			itemID = categorySessionBean.getNextItemID(categoryLocal);

			//inventoryLocal = EJBUtils.createInventory(itemID);
			inventoryLocal=inventorySessionBean.getNewInventory();
			inventoryLocal.setItemId(itemID);
			inventoryLocal.setCategoryName(inventoryWrapper.getCategoryName());
			inventoryLocal.setDescription(inventoryWrapper.getDescription());
			inventoryLocal.setInventorySold(0);
			inventoryLocal.setMfgName(inventoryWrapper.getMfgName());
			inventoryLocal.setUnitPrice(inventoryWrapper.getUnitPrice());
			inventorySessionBean.createInventory(inventoryLocal);

			isCreated = true;
		} catch (Exception e) {
			logger.logp(Level.SEVERE, className, methodName, e.getMessage());
			e.printStackTrace();
		}
		return isCreated;
	}
	
	public void createSettings(SettingsWrapper settingsWrapper){
		String methodName = "createSettings";
		//if (logger.isLoggable(Level.FINE)) {
		logger.logp(Level.FINE, className, methodName, "<++++SSSSS: entered " + methodName);
		//}
		//int numCategories, int numMfgPerCategory, int numInventoryPerMfg, int numCustomers,boolean wsatEnabled, boolean wsnEnabled){
		try {
		//SettingsLocal settingsLocal  = null;
		Setting settingsLocal  = null;
		//create and initialize settings ejb
		//settingsLocal  = EJBUtils.createSettings(settingsID);
		//settingsLocal  = settingSessionBean.createSettings(settingsID);
		settingsLocal  = settingsSessionBean.getNewSetting();
		settingsLocal.setId(settingsID);
		settingsLocal.setNumCategories(settingsWrapper.getNumCategories());
		settingsLocal.setNumMfgCategories(settingsWrapper.getNumMfgCategories());
		settingsLocal.setNumItemsPerMfg(settingsWrapper.getNumItemsPerMfg());
		settingsLocal.setNumCustomers(settingsWrapper.getNumCustomers());
		settingsLocal.setNumSubAccounts(settingsWrapper.getNumSubAccounts());
		settingsLocal.setWsatEnabled(getDBShort((settingsWrapper.isWsatEnabled())));
		settingsLocal.setWsnEnabled(getDBShort(settingsWrapper.isWsnEnabled()));
		settingsLocal.setSdoEnabled(getDBShort(settingsWrapper.isSdoEnabled()));
		settingsLocal.setWsnVariables(settingsWrapper.getWsnVariables());
			settingsSessionBean.createSetting(settingsLocal);
			settings = settingsWrapper; 	
		} catch (Exception e) {
			logger.logp(Level.SEVERE, className, methodName, e.getMessage());
			e.printStackTrace();
		}
		
		//if (logger.isLoggable(Level.FINE)) {
		logger.logp(Level.FINE, className, methodName, "<++++SSSSS: done " + methodName);
		//}
	}
	
	/**
	 * 
	 * @param custID
	 * @return
	 */
	public boolean deleteCustomer(String custID){
		
		//boolean wasRemoved = EJBUtils.removeCustomer(custID);
		String isSuccess = null;
		try {
		Customer customer = customerSessionBean.findCustomerByCustId(custID);
		
		
			isSuccess = customerSessionBean.deleteCustomer(customer);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return getBooleanValue(isSuccess);
	}
		
	public boolean deleteCustomerInfo(String custID){
		String isSuccess = null;
		try {
			
		CustomerInfo customerInfo=new CustomerInfo();
		customerInfo = customerInfoSessionBean.findCustomerInfoByCustId(custID);
		
		//boolean wasRemoved = EJBUtils.removeCustomerInfo(custID);
			isSuccess = customerInfoSessionBean.deleteCustomerInfo(customerInfo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return getBooleanValue(isSuccess);
	}
	
	/**
	 * 
	 * @param categoryName
	 * @return
	 */
	public boolean deleteCategory(String categoryName){
		String isSuccess=null;
		try {

		Category category = categorySessionBean.findCategoryByCategoryName(categoryName);
		isSuccess=	categorySessionBean.deleteCategory(category);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//boolean wasRemoved = EJBUtils.removeCategory(categoryName);
		return getBooleanValue(isSuccess);
	}
	
	/**
	 * 
	 * @param categoryName
	 * @param mfgName
	 * @return
	 */
	public boolean deleteMfgCategory(String categoryName,String mfgName){
		String isSuccess=null;
		MfgCategorySessionBean mfgCategorySessionBean = new MfgCategorySessionBean();
		MfgCategory mfgCategory=mfgCategorySessionBean.findMfgCategoryByPrimaryKey(mfgName, categoryName);
		try {
			isSuccess=mfgCategorySessionBean.deleteMfgCategory(mfgCategory);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return getBooleanValue(isSuccess);
	}
	
	public boolean deleteInventory(String itemID){
		String isSuccess = null;
		try {
		Inventory inventory= inventorySessionBean.findInventoryByItemId(itemID);
			isSuccess = inventorySessionBean.deleteInventory(inventory);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return getBooleanValue(isSuccess);
	}
		
	//dummy methods to make sure all webservice stubs get generated
/**	public void generateWSStubs(CartItem cart,
								CategoryWrapper category, 
								MfgCategoryWrapper mfg, 
								InventoryWrapper inventory,
								CustomerWrapper customer,
								CustomerInfoWrapper customerInfo,
								PaymentInfoWrapper paymentInfo){
		//nothing happens, this method is here only to make sure the webservice stubs get generated
	}*/
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	public boolean getBooleanValue(String value){
		boolean isSuccess=false;
		
		if((value!=null) && (value.trim().equals(""))){
			return isSuccess = true;
		}
		return isSuccess;
	}
	
	/**
	 * 
	 * @param enabled
	 * @return
	 */
	public boolean isEnabled(short enabled){
		if(enabled==0){
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param isEnabled
	 * @return
	 */
	public short getDBShort(boolean isEnabled){
		if(isEnabled){
			return 1;
		}
		return 0;
	}

}

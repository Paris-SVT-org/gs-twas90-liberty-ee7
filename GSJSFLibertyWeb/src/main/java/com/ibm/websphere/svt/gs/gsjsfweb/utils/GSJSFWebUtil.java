package com.ibm.websphere.svt.gs.gsjsfweb.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;


public class GSJSFWebUtil {
	private static String componentName = "com.ibm.websphere.svt.gs.gsjsfweb.utils";
	private static Logger logger = Logger.getLogger(componentName);
	private static String className = GSJSFWebUtil.class.getName();
	private static final String CLIENTSIDE = "<====CCCCC: ";
	
	public static final String STOREMGRADDRESS = "StoreManager.WSDL.Address";
	public static final String STOREMGRNAME = "StoreManagerWrapperService";
	public static final String STOREMGRNS = "http://ejbwrapper.gs.svt.websphere.ibm.com";
	//public static final String TAXSHIPRATEADDRESS="TaxShipRate.EndPoint.Address";
	public static  String TAXSHIPRATEADDRESS="";
    //private static String readTimeout=AccessPropFile.getProperty("rest.client.readTimeout");
    //private static String connectTimeout=AccessPropFile.getProperty("rest.client.connectTimeout");
	//private static StoreManagerWrapperInterface _storeManager = null;
	
	public static String getUserID() {
		String methodName = "getUserID";
		
		FacesContext fc = FacesContext.getCurrentInstance();
	    Application app = fc.getApplication();
	    ExpressionFactory elFactory = app.getExpressionFactory();
	    ELContext elContext = fc.getELContext();
	    logger.logp(Level.FINEST, className, methodName, "about to find customer id...");
	    ValueExpression userIDExp = elFactory.createValueExpression(elContext, "#{sessionScope.custID}", String.class);
	    String custID = (String) userIDExp.getValue(elContext);
	    
	    return custID;
		
	}
	
/**	public static StoreManagerWrapperInterface getStoreManagerPort(){
		String methodName = "getStoremanagerPort";
	
		//StoreManagerWrapperInterface localMgr = null;
		
		if (_storeManager != null) {
			logger.logp(Level.FINEST, className, methodName, "called before, return without hassle..." );
			return _storeManager;
		} 
		logger.logp(Level.FINE, className, methodName, "null static _storeManager, will have to obtain ...");
		try {	
			
			URL wsdlLocation = new URL(AccessPropFile.getProperty( STOREMGRADDRESS )) ;
			QName serviceName = new QName(STOREMGRNS, STOREMGRNAME);
	 		
			//logger.logp(Level.FINE, className, methodName, CLIENTSIDE + "about to construct StoreManagerWrapperService...");
			StoreManagerWrapperService service = new StoreManagerWrapperService(wsdlLocation, serviceName);
			logger.logp(Level.FINE, className, methodName, CLIENTSIDE + "Invoking static API StoreManagerWrapperService..." + service);
			
			_storeManager = service.getStoreManagerWrapperPort();
			
			logger.logp(Level.FINE, className, methodName, CLIENTSIDE + "got the StoreManagerWrapperInterface port.." + _storeManager);
			
		} catch(Exception e){
			logger.logp(Level.SEVERE, className, methodName, e.getMessage());
			e.printStackTrace();
		}
		return _storeManager;
		//return localMgr;
	}*/
	
	public static void addErrorMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        FacesContext fc = FacesContext.getCurrentInstance();
        fc.addMessage(null, facesMsg);
    }
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String getTaxShipRateServiceEndPoint() throws Exception {
		//return AccessPropFile.getProperty( TAXSHIPRATEADDRESS );
		FacesContext fc = FacesContext.getCurrentInstance();
	    Application app = fc.getApplication();
	    ExternalContext eCtx= fc.getExternalContext();
	    String serverName=eCtx.getRequestServerName();
	    int portNumber=eCtx.getRequestServerPort();
	    String contextPath = eCtx.getRequestContextPath();
	    String scheme = ((HttpServletRequest)eCtx.getRequest()).getScheme();
	    if(portNumber==0){
	    	
		    TAXSHIPRATEADDRESS= scheme+"://"+serverName+contextPath;
		    return TAXSHIPRATEADDRESS;
	    }
	    else{
		    TAXSHIPRATEADDRESS= scheme+"://"+serverName+":"+portNumber+contextPath;
			return TAXSHIPRATEADDRESS ;
	    }
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String getReadTimeout() throws Exception {
		//return readTimeout="3000";
		return "300000";
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String getConnectTimeout() throws Exception {
		//return connectTimeout;
		return "400000";
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String printCurrentTimestamp(String cName,String mName,String currentStatus)  {
		String methodName="printCurrentTimestamp";
		String strDate ="";
		logger.logp(Level.FINEST, className, methodName, "PrintCurrentTimestamp");
		try{
			SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		    Date now = new Date();
		    strDate = sdfDate.format(now);
			logger.logp(Level.FINEST, cName, mName, mName+":"+currentStatus+":currentTimestamp<<<<<<:"+strDate+":>>>>>>");
		}
		catch(Exception e){
			logger.logp(Level.SEVERE, className, methodName, "Error:" +e);
		}
	    return strDate;
		
	}
	
}

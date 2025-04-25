package com.ibm.websphere.svt.gs.ws.client.utils;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import com.ibm.websphere.svt.gs.ccdb.jaxws.client.GarageSaleCCManagerLocal;
import com.ibm.websphere.svt.gs.ccdb.jaxws.client.GarageSaleCCManagerService;
import com.ibm.websphere.svt.gs.gsdb.jaxws.client.GarageSaleStoreManagerLocal;
import com.ibm.websphere.svt.gs.gsdb.jaxws.client.GarageSaleStoreManagerService;
import com.ibm.websphere.svt.gs.wsbankdb.jaxws.client.GarageSaleBankManagerLocal;
import com.ibm.websphere.svt.gs.wsbankdb.jaxws.client.GarageSaleBankManagerService;

/**
 * 
 * @author JAGRAJ
 *
 */
public class GarageSaleWSClientUtils {
	
	private static String componentName = "com.ibm.websphere.svt.gs.ws.client.utils";
	private static Logger logger = Logger.getLogger(componentName);
	private static String className = GarageSaleWSClientUtils.class.getName();
	private static final String CLIENTSIDE = "<====CCCCC ";
	private static 	GarageSaleCCManagerLocal ccManager;
	private static GarageSaleStoreManagerLocal storeManager;
	private static GarageSaleBankManagerLocal bankManager;
	/**
	 * 
	 * @return
	 */
	public static GarageSaleCCManagerLocal getCCManager() {
		String methodName = "getCCManager";
		if (ccManager != null) {
			logger.logp(Level.FINER, className, methodName, "called before, return without hassle..." + ccManager);
			return ccManager;
		}
		
		logger.logp(Level.FINE, className, methodName, "null static ccManager, will have to obtain ...");
		try{
			String ccManagerEndPointAddress=System.getProperty("GarageSaleCCManagerService.endPoint.Address")+"?wsdl";
			URL wsdlLocation = new URL(ccManagerEndPointAddress) ;
	 		QName serviceName = null; 
	 		serviceName = new QName("http://session.ccdb.gs.svt.websphere.ibm.com/", "GarageSaleCCManagerService");
	 		GarageSaleCCManagerService service = new GarageSaleCCManagerService(wsdlLocation, serviceName);
	 		logger.logp(Level.FINE, className, methodName, CLIENTSIDE + "Invoking static API CCManagerService...");
	 		
            ccManager = service.getGarageSaleCCManagerPort();
            logger.logp(Level.INFO, className, methodName, CLIENTSIDE + "got the GarageSaleCCManagerService port.." + ccManager);
	 		
		} catch (Exception e) {
			logger.logp(Level.SEVERE, className, methodName, CLIENTSIDE + e.getMessage());
			e.printStackTrace();
		}
		return ccManager;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public static GarageSaleStoreManagerLocal getStoreManager() {
		String methodName = "getStoreManager";
		if (storeManager != null) {
			logger.logp(Level.FINER, className, methodName, "called before, return without hassle..." + storeManager);
			return storeManager;
		}
		
		logger.logp(Level.FINE, className, methodName, "null static getStoreManager, will have to obtain ...");
		try{
			String storeManagerEndPointAddress=System.getProperty("GarageSaleStoreManagerService.endPoint.Address")+"?wsdl";
			URL wsdlLocation = new URL(storeManagerEndPointAddress) ;
	 		QName serviceName = null; 
	 		serviceName = new QName("http://session.gsdb.gs.svt.websphere.ibm.com/", "GarageSaleStoreManagerService");
	 		GarageSaleStoreManagerService service = new GarageSaleStoreManagerService(wsdlLocation, serviceName);
	 		logger.logp(Level.FINE, className, methodName, CLIENTSIDE + "Invoking static API GarageSaleStoreManagerService...");
	 		
	 		storeManager = service.getGarageSaleStoreManagerPort();
            logger.logp(Level.INFO, className, methodName, CLIENTSIDE + "got the GarageSaleStoreManagerService port.." + storeManager);
	 		
		} catch (Exception e) {
			logger.logp(Level.SEVERE, className, methodName, CLIENTSIDE + e.getMessage());
			e.printStackTrace();
		}
		return storeManager;
	}
	
	/**
	 * 
	 * @return
	 */
	public static GarageSaleBankManagerLocal getBankManager() {
		String methodName = "getBankManager";
		if (bankManager != null) {
			logger.logp(Level.FINER, className, methodName, "called before, return without hassle..." + bankManager);
			return bankManager;
		}
		
		logger.logp(Level.FINE, className, methodName, "null static getBankManager, will have to obtain ...");
		try{
			String bankManagerEndPointAddress=System.getProperty("GarageSaleBankManagerService.endPoint.Address")+"?wsdl";
			URL wsdlLocation = new URL(bankManagerEndPointAddress) ;
	 		QName serviceName = null; 
	 		serviceName = new QName("http://session.wsbankdb.gs.svt.websphere.ibm.com/", "GarageSaleBankManagerService");
	 		GarageSaleBankManagerService service = new GarageSaleBankManagerService(wsdlLocation, serviceName);
	 		logger.logp(Level.FINE, className, methodName, CLIENTSIDE + "Invoking static API GarageSaleStoreManagerService...");
	 		
	 		bankManager = service.getGarageSaleBankManagerPort();
            logger.logp(Level.INFO, className, methodName, CLIENTSIDE + "got the GarageSaleBankManagerService port.." + bankManager);
	 		
		} catch (Exception e) {
			logger.logp(Level.SEVERE, className, methodName, CLIENTSIDE + e.getMessage());
			e.printStackTrace();
		}
		return bankManager;
	}
	

}

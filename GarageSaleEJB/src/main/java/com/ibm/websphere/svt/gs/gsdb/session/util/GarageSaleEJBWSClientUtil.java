/**
 * 
 */
package com.ibm.websphere.svt.gs.gsdb.session.util;

import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.ManagedBean;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceRef;

import com.ibm.websphere.svt.gs.ccdb.jaxws.client.GarageSaleCCManagerLocal;
import com.ibm.websphere.svt.gs.ccdb.jaxws.client.GarageSaleCCManagerService;
import com.ibm.websphere.svt.gs.gsdb.jaxws.client.GarageSaleStoreManagerLocal;
import com.ibm.websphere.svt.gs.gsdb.jaxws.client.GarageSaleStoreManagerService;
import com.ibm.websphere.svt.gs.wsbankdb.jaxws.client.GarageSaleBankManagerLocal;
import com.ibm.websphere.svt.gs.wsbankdb.jaxws.client.GarageSaleBankManagerService;



/**
 * @author JAGRAJ
 *
 */
@ManagedBean("GarageSaleEJBWSClientUtil")
public class GarageSaleEJBWSClientUtil {
	
	private static String componentName = "com.ibm.websphere.svt.gs.gsdb.session.util";
	private static Logger logger = Logger.getLogger(componentName);
	private static String className = GarageSaleEJBWSClientUtil.class.getName();
	private static final String CLIENTSIDE = "<====CCCCC ";
	
	@WebServiceRef(name="GarageSaleCCManagerService",value=GarageSaleCCManagerService.class)
	private GarageSaleCCManagerLocal ccManager;
	@WebServiceRef(name="GarageSaleStoreManagerService",value=GarageSaleStoreManagerService.class)
	private GarageSaleStoreManagerLocal storeManager;
	@WebServiceRef(name="GarageSaleBankManagerService",value=GarageSaleBankManagerService.class)
	private GarageSaleBankManagerLocal bankManager;
	
	
	/**
	 * 
	 * @return
	 */
	public GarageSaleCCManagerLocal getCcManager() {
		Map<String,Object> requestContext =((BindingProvider)ccManager).getRequestContext();
		String ccManagerEndPointAddress=System.getProperty("GarageSaleCCManagerService.endPoint.Address");
		requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, ccManagerEndPointAddress);
		return ccManager;
	}
	
	/**
	 * 
	 * @param ccManager
	 */
	public void setCcManager(GarageSaleCCManagerLocal ccManager) {
		this.ccManager = ccManager;
	}
	/**
	 * 
	 * @return
	 */
	public GarageSaleStoreManagerLocal getStoreManager() {
		Map<String,Object> requestContext =((BindingProvider)storeManager).getRequestContext();
		String storeManagerEndPointAddress=System.getProperty("GarageSaleStoreManagerService.endPoint.Address");
		requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, storeManagerEndPointAddress);
		return storeManager;
	}
	/**
	 * 
	 * @param storeManager
	 */
	public void setStoreManager(GarageSaleStoreManagerLocal storeManager) {
		this.storeManager = storeManager;
	}
	
	/**
	 * 
	 * @return
	 */
	public GarageSaleBankManagerLocal getBankManager() {
		Map<String,Object> requestContext =((BindingProvider)bankManager).getRequestContext();
		String bankManagerEndPointAddress=System.getProperty("GarageSaleBankManagerService.endPoint.Address");
		requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, bankManagerEndPointAddress);
		return bankManager;
	}
	
	/**
	 * 
	 * @param bankManager
	 */
	public void setBankManager(GarageSaleBankManagerLocal bankManager) {
		this.bankManager = bankManager;
	}



}

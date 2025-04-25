package com.ibm.websphere.svt.gs.gsjsfweb.utils;

import java.io.Serializable;
import java.util.logging.Logger;

//@ApplicationScoped
public class Generator implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6694223474251558397L;
	private static String componentName = "com.ibm.websphere.svt.gs.gsjsfweb.utils";
	private static Logger logger = Logger.getLogger(componentName);
	private static String className = Generator.class.getName();
	
	
	//private StoreManagerWrapperInterface storeMgrWrapper;
	
/**	@Produces @GSStoreService StoreManagerWrapperInterface getGSStoreService() {
		String methodName = "getGSStoreService";
		
		logger.logp(Level.FINE, className, methodName, "will get service and return");
		return GSJSFWebUtil.getStoreManagerPort();
	}*/

}

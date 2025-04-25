/**
 * 
 */
package com.ibm.websphere.svt.gs.gsjsfweb.utils;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.interceptor.AroundConstruct;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.xml.ws.BindingProvider;

import com.ibm.websphere.svt.gs.ccdb.jaxws.client.GarageSaleCCManagerLocal;
import com.ibm.websphere.svt.gs.gsdb.jaxws.client.GarageSaleStoreManagerLocal;
import com.ibm.websphere.svt.gs.wsbankdb.jaxws.client.GarageSaleBankManagerLocal;

/**
 * @author JAGRAJ
 *
 */
public class GarageSaleJSFWSClientInterceptor {
	
	private static String componentName = "com.ibm.websphere.svt.gs.gsjsfweb.utils";
	private static Logger logger = Logger.getLogger(componentName);
	private static String className = GarageSaleJSFWSClientInterceptor.class.getName();
	private static final String CLIENTSIDE = "<====CCCCC ";
	
	/*private static String ccManagerEndPointAddress=System.getProperty("GarageSaleCCManagerService.endPoint.Address");
	private static String storeManagerEndPointAddress=System.getProperty("GarageSaleStoreManagerService.endPoint.Address");
	private static String bankManagerEndPointAddress=System.getProperty("GarageSaleBankManagerService.endPoint.Address");*/

	private static String ccManagerEndPointAddress;
	private static String storeManagerEndPointAddress;
	private static String bankManagerEndPointAddress;

	/**
	 * 
	 * @param ctx
	 * @return
	 * @throws Exception
	 */
	
	public GarageSaleJSFWSClientInterceptor(){
	}
	
	@AroundConstruct
	public Object initializeEndpoints(InvocationContext ctx){
		try {
			ccManagerEndPointAddress=System.getProperty("GarageSaleCCManagerService.endPoint.Address");
			storeManagerEndPointAddress=System.getProperty("GarageSaleStoreManagerService.endPoint.Address");
			bankManagerEndPointAddress=System.getProperty("GarageSaleBankManagerService.endPoint.Address");
			return ctx.proceed();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	@AroundInvoke
	public Object setEndPointAddess(InvocationContext ctx){
		String methodName="setEndPointAddess";
        logger.logp(Level.FINER, className, methodName, "Calling Interceptor method to Override WebService Endpoint address.");
        
		Object [] parameters= ctx.getParameters();
		if((parameters !=null && parameters.length > 0)&& (parameters[0] instanceof GarageSaleCCManagerLocal)){
	        logger.logp(Level.FINER, className, methodName, "Calling Interceptor method to Override WebService Endpoint address.");
			GarageSaleCCManagerLocal ccManager = (GarageSaleCCManagerLocal) parameters[0];
			Map<String,Object> requestContext =((BindingProvider)ccManager).getRequestContext();
			//String ccManagerEndPointAddress=System.getProperty("GarageSaleCCManagerService.endPoint.Address");
			requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, ccManagerEndPointAddress);
			parameters[0]=ccManager;
			ctx.setParameters(parameters);
		}
		else if((parameters !=null && parameters.length > 0) && (parameters[0] instanceof GarageSaleStoreManagerLocal)){
	        logger.logp(Level.FINER, className, methodName, "Calling Interceptor method to Override WebService Endpoint address.");
			GarageSaleStoreManagerLocal storeManager=(GarageSaleStoreManagerLocal)parameters[0];
			Map<String,Object> requestContext =((BindingProvider)storeManager).getRequestContext();
			//String storeManagerEndPointAddress=System.getProperty("GarageSaleStoreManagerService.endPoint.Address");
			requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, storeManagerEndPointAddress);
			parameters[0]=storeManager;
			ctx.setParameters(parameters);
		}
		else if((parameters !=null && parameters.length > 0) && (parameters[0] instanceof GarageSaleBankManagerLocal)){
	        logger.logp(Level.FINER, className, methodName, "Calling Interceptor method to Override WebService Endpoint address.");
			GarageSaleBankManagerLocal bankManager=(GarageSaleBankManagerLocal)parameters[0];
			Map<String,Object> requestContext =((BindingProvider)bankManager).getRequestContext();
			//String bankManagerEndPointAddress=System.getProperty("GarageSaleBankManagerService.endPoint.Address");
			requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, bankManagerEndPointAddress);
			parameters[0]=bankManager;
			ctx.setParameters(parameters);
		}
		try {
	        return ctx.proceed();
	    } catch (Exception e) {
	        logger.warning("Error calling ctx.proceed in calling setEndPointAddess"+e);
	        return null;
	    }
		
	}	
}

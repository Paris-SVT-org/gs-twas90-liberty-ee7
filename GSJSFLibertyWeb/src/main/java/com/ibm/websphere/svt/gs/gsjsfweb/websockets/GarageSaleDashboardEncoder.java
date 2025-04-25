/**
 * 
 */
package com.ibm.websphere.svt.gs.gsjsfweb.websockets;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import com.ibm.json.java.JSONObject;

/**
 * @author JAGRAJ
 *
 */
public class GarageSaleDashboardEncoder implements Encoder.Text<GarageSaleDashboardBean>{
	
	private static String componentName = "com.ibm.websphere.svt.gs.gsjsfweb.websockets";
	private static Logger logger = Logger.getLogger(componentName);
	private static String className = GarageSaleDashboardEncoder.class.getName();

	/** 
     * @see javax.websocket.Encoder#init(javax.websocket.EndpointConfig)
     */
    public void init(EndpointConfig config) {
    }
 
    /** 
     * @see javax.websocket.Encoder#destroy()
     */
    public void destroy() {
    }
 
    /**
     * @see javax.websocket.Encoder.Text#encode(java.lang.Object)
     */
    public String encode(GarageSaleDashboardBean garageSaleDashboardBean) throws EncodeException {
    	String methodName="encode";
    	logger.logp(Level.FINE, className, methodName, "Inside encode");
    	JSONObject jsonObject= new JSONObject();
    	jsonObject.put("noOfInvoicesCreated", garageSaleDashboardBean.getNoOfInvoicesCreated());
    	jsonObject.put("noOfInvoicesCompleted", garageSaleDashboardBean.getNoOfInvoicesCompleted());
    	jsonObject.put("noOfWebSocketsOpenSessions", garageSaleDashboardBean.getNoOfWebSocketsOpenSessions());
    	return jsonObject.toString();
    }

}

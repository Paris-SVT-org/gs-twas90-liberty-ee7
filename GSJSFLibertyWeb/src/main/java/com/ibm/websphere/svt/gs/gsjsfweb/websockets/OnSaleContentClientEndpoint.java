package com.ibm.websphere.svt.gs.gsjsfweb.websockets;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;



/**
 * Websocket Endpoint implementation class GarageSaleUploadVideoClientEndpoint */

@ClientEndpoint
public class OnSaleContentClientEndpoint {
    
	private static String componentName = "com.ibm.websphere.svt.gs.gsjsfweb.websockets";
	private static Logger logger = Logger.getLogger(componentName);
	private static String className = OnSaleContentClientEndpoint.class.getName();
	private static final String DASHBOARD_MAP_KEY= OnSaleContentClientEndpoint.class.getSimpleName();
	private GarageSaleDashboardApplicationScoppedBean garageSaleDashboardApplicationScoppedBean=GarageSaleWebSocketsUtil.getGarageSaleDashboardApplicationScoppedBean();
	
	private Session currentSession;
    
	@OnOpen
	public void onOpen(Session session) {
		String methodName="onOpen";
    	logger.logp(Level.FINE, className, methodName, "Inside onOpen");
    	logger.logp(Level.FINE, className, methodName, "Connected to endpoint: " + session.getBasicRemote());
		try {
			currentSession=session;
			//session.getBasicRemote().sendText("Hello From :"+className);
        	logger.logp(Level.FINE, className, methodName, "Inside :  "+className);
        	GarageSaleWebSocketsUtil.updateCounters(garageSaleDashboardApplicationScoppedBean, DASHBOARD_MAP_KEY, "onOpen",session.getOpenSessions().size());
        	
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@OnMessage
	public void onMessage(Session session, String message) {
		String methodName="onMessage(Session session, String message)";
    	logger.logp(Level.FINE, className, methodName, "Inside :  "+className);	
    	logger.logp(Level.FINE, className, methodName, "Received Success message :"+message);	
    	if(message !=null){
        	try {
        		WebSocketsDataBean.setInventoryOnSaleJsonData(message);
        		if(session!=null){
        			logger.logp(Level.FINE, className, methodName, "The session is closed in ClientEndpoint:");
        			session.close();
                	GarageSaleWebSocketsUtil.updateCounters(garageSaleDashboardApplicationScoppedBean, DASHBOARD_MAP_KEY, "onMessage",session.getOpenSessions().size());
        			
        		}
        		currentSession=null;
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    		
    	}
    }
	
	@OnClose
	public void onClose(Session session,CloseReason reson) {
		currentSession=null;
    	try {
    		if(session!=null){
    			session.close();
            	GarageSaleWebSocketsUtil.updateCounters(garageSaleDashboardApplicationScoppedBean, DASHBOARD_MAP_KEY, "onClose",session.getOpenSessions().size());
    			
    		}
    		currentSession=null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	@OnError
	public void onError(Session session,Throwable t) {
    	try {
    		if(session!=null){
    			session.close();
            	GarageSaleWebSocketsUtil.updateCounters(garageSaleDashboardApplicationScoppedBean, DASHBOARD_MAP_KEY, "onError",session.getOpenSessions().size());
    		}
    		currentSession=null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		t.printStackTrace();
	}
}

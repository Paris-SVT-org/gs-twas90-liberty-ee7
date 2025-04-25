package com.ibm.websphere.persona.pmi.websockets;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import com.ibm.websphere.persona.pmi.jsfweb.PersonaPMIStaticData;



/**
 * Websocket Endpoint implementation class GarageSaleUploadVideoClientEndpoint */

//@ClientEndpoint
public class PersonaWebServicesPMIWebSocketClientEndpoint {
    
/*	private static String componentName = "com.ibm.websphere.svt.gs.gsjsfweb.websockets";
	private static Logger logger = Logger.getLogger(componentName);
	private static String className = PersonaWebServicesPMIWebSocketClientEndpoint.class.getName();
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
        		PersonaPMIStaticData.setPersonaPMIJsonData(message);
        		if(session!=null){
        			logger.logp(Level.FINE, className, methodName, "The session is closed in ClientEndpoint:");
        			session.close();
        		}
        		currentSession=null;
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    		
    	}
    }
	
	@OnClose
	public void onClose(Session session,CloseReason reson) {
    	try {
    		if(session!=null){
    			session.close();
    		}
    		currentSession=null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	

	@OnError
	public void onError(Session session,Throwable t) {
    	try {
    		if(session!=null){
    			session.close();
    		}
    		currentSession=null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		t.printStackTrace();
	}*/
}

/**
 * 
 */
package com.ibm.websphere.svt.gs.gsjsfweb.websockets;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.inject.Inject;
import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

/**
 * @author JAGRAJ
 *
 */
public class InventoryOnSaleProgEndpoint extends Endpoint{
	
    private static Queue<Session> sessionsQueue = new ConcurrentLinkedQueue<>();
    
    
    private static final String INVENTORY_MAP_KEY = InventoryOnSaleProgEndpoint.class.getSimpleName();
    
    int count = 0;
	public static Queue<Session> getSessionsQueue() {
		return sessionsQueue;
	}

	public static void setSessionsQueue(Queue<Session> sessionsQueue) {
		InventoryOnSaleProgEndpoint.sessionsQueue = sessionsQueue;
	}



	private static String componentName = "com.ibm.websphere.svt.gs.gsjsfweb.websockets";
	private static Logger logger = Logger.getLogger(componentName);
	private static String className = InventoryOnSaleProgEndpoint.class.getName();
	
	
	@Resource(lookup="java:comp/DefaultManagedScheduledExecutorService")
	private ManagedScheduledExecutorService managedScheduledExecutorService;
	
	private Session currentSession;
	
	private static int brokenCounter=1;
	private static int writeNotCounter=1;

	private static AtomicLong brokenPipeCounter  = new AtomicLong();
	private static AtomicLong illegalStateCounter = new AtomicLong();
	
	
	//@Inject
    //private  GarageSaleWebSocketSessionBean garageSaleWebSocketSessionBean=GarageSaleWebSocketsUtil.getGarageSaleWebSocketSessionBean();
	
	//@Inject
	//private GarageSaleDashboardApplicationScoppedBean garageSaleDashboardApplicationScoppedBean=GarageSaleWebSocketsUtil.getGarageSaleDashboardApplicationScoppedBean();
	
	@Inject
    private  GarageSaleWebSocketSessionBean garageSaleWebSocketSessionBean;
	
	@Inject
	private GarageSaleDashboardApplicationScoppedBean garageSaleDashboardApplicationScoppedBean;
	

    /**
	 * @return the garageSaleDashboardApplicationScoppedBean
	 */
	public GarageSaleDashboardApplicationScoppedBean getGarageSaleDashboardApplicationScoppedBean() {
		return garageSaleDashboardApplicationScoppedBean;
	}

	/**
	 * @param garageSaleDashboardApplicationScoppedBean the garageSaleDashboardApplicationScoppedBean to set
	 */
	public void setGarageSaleDashboardApplicationScoppedBean(
			GarageSaleDashboardApplicationScoppedBean garageSaleDashboardApplicationScoppedBean) {
		this.garageSaleDashboardApplicationScoppedBean = garageSaleDashboardApplicationScoppedBean;
	}

	public  GarageSaleWebSocketSessionBean getGarageSaleWebSocketSessionBean() {
		return garageSaleWebSocketSessionBean;
	}

	public  void setGarageSaleWebSocketSessionBean(GarageSaleWebSocketSessionBean garageSaleWebSocketSessionBean) {
		this.garageSaleWebSocketSessionBean = garageSaleWebSocketSessionBean;
	}

	// OnOpen will get called by WebSockets when the connection has been established successfully using WebSocket handshaking with
    // the HTTP Request - Response processing.
    @Override
    public void onOpen(Session session, EndpointConfig ec) {
    	// Store the WebSocket session for later use.
    	String methodName="onOpen";
    	boolean isConnected=true;
    	logger.logp(Level.FINE, className, methodName, "Inside onOpen");
    	
        try {
        	//This is workaround to put the queue bottom of the priority queue.
        	//Thread.yield();

        	sessionsQueue.add(session);
            MessageHandler.Whole<String> handler = new InventoryOnSaleMessageHandler(session,ec);
            session.addMessageHandler(handler);
        	
        	GarageSaleWebSocketsUtil.updateCounters(garageSaleDashboardApplicationScoppedBean, INVENTORY_MAP_KEY, "onOpen",session.getOpenSessions().size());
        	handler.onMessage("refreshMe");
			//refreshInventoryOnSale();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }


    // Using the OnClose annotation will cause this method to be called when the WebSocket Session is being closed.
    @Override
    public void onClose(Session session, CloseReason reason){
    	String methodName="onClose";
    	logger.logp(Level.FINE, className, methodName, "Inside onClose");
			try {
				GarageSaleWebSocketsUtil.updateCounters(garageSaleDashboardApplicationScoppedBean, INVENTORY_MAP_KEY, "onClose",session.getOpenSessions().size());
	    		if(session!=null){
	    			session.close();
	    	       	sessionsQueue.remove(session);
	    		}
	    		currentSession=null;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
     }

    // Using the OnError annotation will cause this method to be called when the WebSocket Session has an error to report. For the Alpha version
    // of the WebSocket implentation on Liberty, this will not be called on error conditions.
    @Override
    public void onError(Session session,Throwable t){
    	String methodName="onError";
    	logger.logp(Level.FINE, className, methodName, "Inside onError");
    	if(t !=null && t instanceof IOException){
    		if(t.getMessage() !=null && t.getMessage().indexOf("Broken")!=-1){
    			brokenCounter=brokenCounter+1;
            	logger.logp(Level.FINE, className, methodName, "Inside onError: "+t.getMessage());
    			long currentCount=brokenPipeCounter.incrementAndGet();
    			if(brokenCounter==1 || brokenCounter==50){
        		    logger.logp(Level.WARNING,	className,	methodName,className+" :The current IOErrors Broken Pipe counter is : "+currentCount);
        		    brokenCounter=1;
    			}
    		}
    		
    	}else if(t !=null && t instanceof IllegalStateException){
    		if(t.getMessage() !=null && t.getMessage().indexOf("write not allowed")!=-1){
    			writeNotCounter=writeNotCounter+1;
            	logger.logp(Level.FINE, className, methodName, "Inside onError :"+t.getMessage());
    			long currentCount=illegalStateCounter.incrementAndGet();
    			if(writeNotCounter==1 || writeNotCounter==50){
    				logger.logp(Level.WARNING,	className,	methodName,className+" :The current IllegalStateException write not allowed counter : "+currentCount);
    				writeNotCounter=1;
    			}
    		}
    	}
    	else {
        	logger.logp(Level.SEVERE, className, methodName, "Inside onError");
        	t.printStackTrace();
    	}
    	try {
			GarageSaleWebSocketsUtil.updateCounters(garageSaleDashboardApplicationScoppedBean, INVENTORY_MAP_KEY, "onError",session.getOpenSessions().size());
	    	/* Remove this connection from the queue */
    		if(session!=null){
    			session.close();
    	    	sessionsQueue.remove(session);
    		}
    		currentSession=null;
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
 }

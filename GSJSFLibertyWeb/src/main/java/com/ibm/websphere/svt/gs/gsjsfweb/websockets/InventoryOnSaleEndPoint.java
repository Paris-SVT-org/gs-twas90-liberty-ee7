/**
 * 
 */
package com.ibm.websphere.svt.gs.gsjsfweb.websockets;

import java.io.IOException;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.inject.Inject;
import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.ibm.json.java.JSONArray;
import com.ibm.websphere.svt.gs.inventory.InventoryOnSaleWrapper;

/**
 * @author JAGRAJ
 * This WebSocket Endpoint class pushes current inventory on sale to all the clients 
 * connected to this endpoint. The data is refreshed every 10 seconds to all the clients.
 */
@ServerEndpoint(value="/inventoryOnSaleEndPoint")
public class InventoryOnSaleEndPoint{
    private static Queue<Session> sessionsQueue = new ConcurrentLinkedQueue<>();
    
    
    private static final String INVENTORY_MAP_KEY = InventoryOnSaleEndPoint.class.getSimpleName();
    
    int count = 0;
	public static Queue<Session> getSessionsQueue() {
		return sessionsQueue;
	}

	public static void setSessionsQueue(Queue<Session> sessionsQueue) {
		InventoryOnSaleEndPoint.sessionsQueue = sessionsQueue;
	}



	private static String componentName = "com.ibm.websphere.svt.gs.gsjsfweb.websockets";
	private static Logger logger = Logger.getLogger(componentName);
	private static String className = InventoryOnSaleEndPoint.class.getName();
	
	private Session currentSession;
	
	@Resource(lookup="java:comp/DefaultManagedScheduledExecutorService")
	private ManagedScheduledExecutorService managedScheduledExecutorService;
	
	@Inject
    private  GarageSaleWebSocketSessionBean garageSaleWebSocketSessionBean;
	
	@Inject
	private GarageSaleDashboardApplicationScoppedBean garageSaleDashboardApplicationScoppedBean;
	
	
	private static int brokenCounter=1;
	private static int writeNotCounter=1;

	private static AtomicLong brokenPipeCounter  = new AtomicLong();
	private static AtomicLong illegalStateCounter = new AtomicLong();


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
    @OnOpen
    public void onOpen(Session session, EndpointConfig ec) {
    	// Store the WebSocket session for later use.
    	String methodName="onOpen";
    	boolean isConnected=true;
    	logger.logp(Level.FINE, className, methodName, "Inside onOpen");
    	currentSession=session;
        try {
        	sessionsQueue.add(session);
        	GarageSaleWebSocketsUtil.updateCounters(garageSaleDashboardApplicationScoppedBean, INVENTORY_MAP_KEY, "onOpen",session.getOpenSessions().size());
        	sendAll();
			//refreshInventoryOnSale();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    // using the OnMessage annotation for this method will cause this method to get called by WebSockets when this connection has received 
    // a WebSocket message from the other side of the connection.  
    // The message is derived from the WebSocket frame payloads of one, and only one, WebSocket message.
    
    @OnMessage
    public void receiveMessage(Session session,String message) {
    	String methodName="receiveMessage";
    	logger.logp(Level.FINE, className, methodName, "Inside receiveMessage and Message is: "+message);
        try {
    		//refreshInventoryOnSale();
        	GarageSaleWebSocketsUtil.updateCounters(garageSaleDashboardApplicationScoppedBean, INVENTORY_MAP_KEY, "onMessage",session.getOpenSessions().size());        	
        	sendAll();
        	//GarageSaleWebSocketsUtil.updateCounters(garageSaleDashboardApplicationScoppedBean, INVENTORY_MAP_KEY, "openSessions",session.getOpenSessions().size());        	
        	
        } catch (Exception e) {
			e.printStackTrace();
		}
    }

    // Using the OnClose annotation will cause this method to be called when the WebSocket Session is being closed.
    @OnClose
    public void onClose(Session session, CloseReason reason) throws Exception{
    	String methodName="onClose";
    	logger.logp(Level.FINE, className, methodName, "Inside onClose");
			GarageSaleWebSocketsUtil.updateCounters(garageSaleDashboardApplicationScoppedBean, INVENTORY_MAP_KEY, "onClose",session.getOpenSessions().size());
	       	sessionsQueue.remove(session);
	    	try {
	    		if(session!=null){
	    			session.close();
	    		}
	    		currentSession=null;
			} catch (IOException e) {
				e.printStackTrace();
			}
			

     }

    // Using the OnError annotation will cause this method to be called when the WebSocket Session has an error to report. For the Alpha version
    // of the WebSocket implentation on Liberty, this will not be called on error conditions.
    @OnError
    public void onError(Session session,Throwable t) throws Exception {
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
    	sessionsQueue.remove(session);
		GarageSaleWebSocketsUtil.updateCounters(garageSaleDashboardApplicationScoppedBean, INVENTORY_MAP_KEY, "onError",session.getOpenSessions().size());
    	try {
    		if(session!=null){
    			session.close();
    		}
    		currentSession=null;
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    

    
    /**
     * 
     * @throws Exception
     */
    public  void refreshInventoryOnSale() throws Exception{
    	String methodName="refreshInventoryOnSale";
    	logger.logp(Level.FINE, className, methodName, "Inside refreshInventoryOnSale");
		try {
			ScheduledFuture future =
			managedScheduledExecutorService.scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {
					try {
							sendAll();
							GarageSaleWebSocketsUtil.updateCounters(garageSaleDashboardApplicationScoppedBean, INVENTORY_MAP_KEY, "onMessage",currentSession.getOpenSessions().size());
					} catch (Exception e) {
						e.printStackTrace();
				    	logger.log(Level.SEVERE, "Executor Service Task Failed"+e.getMessage());        
					}
				}
			},0,30,TimeUnit.SECONDS);
			
			if(sessionsQueue!=null && sessionsQueue.size()==0){
				if(future.isDone()){
					future.cancel(true);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

    	
    }
    
    /**
     * 
     * @throws Exception
     */
	public void sendAll() throws Exception {
        String methodName="sendAll";
        logger.logp(Level.FINE, className, methodName, "Number of Open sessions are:  "+sessionsQueue.size());
        //GarageSaleWebSocketSessionBean myWebSocketSessionBean= GarageSaleWebSocketsUtil.getGarageSaleWebSocketSessionBean();        
		List<InventoryOnSaleWrapper> inventoryOnSalePartialList =GarageSaleWebSocketsUtil.getPartialList(garageSaleWebSocketSessionBean.getInventoryOnSaleList());
		try{
			//RPT does not handle this properly and we want to disable sending updates to everyone.
	        /*for(Session currentOpensession:sessionsQueue){
	        	if(currentOpensession.isOpen()){
	        		JSONArray jsonInventoryArray =GarageSaleWebSocketsUtil.getInventoryOnSaleJSONArray(inventoryOnSalePartialList);
	        		currentOpensession.getBasicRemote().sendText(jsonInventoryArray.toString());
	        	}
	        }*/
        	if(currentSession !=null && currentSession.isOpen()){
        		JSONArray jsonInventoryArray =GarageSaleWebSocketsUtil.getInventoryOnSaleJSONArray(inventoryOnSalePartialList);
        		currentSession.getBasicRemote().sendText(jsonInventoryArray.toString());
        	}
			GarageSaleWebSocketsUtil.updateCounters(garageSaleDashboardApplicationScoppedBean, INVENTORY_MAP_KEY, "onMessage",currentSession.getOpenSessions().size());
		}catch (Exception t){
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
	        	logger.logp(Level.SEVERE, className, methodName, className+" :sendAll ");
	        	t.printStackTrace();
	    	}
			
		}
	}
	
}

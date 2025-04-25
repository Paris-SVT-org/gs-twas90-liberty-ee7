package com.ibm.websphere.svt.gs.gsjsfweb.websockets;

import java.io.IOException;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

import com.ibm.json.java.JSONArray;
import com.ibm.websphere.svt.gs.inventory.InventoryOnSaleWrapper;

public class InventoryOnSaleMessageHandler  implements MessageHandler.Whole<String>{
	
    Session currentSession = null;
	private static String componentName = "com.ibm.websphere.svt.gs.gsjsfweb.websockets";
	private static Logger logger = Logger.getLogger(componentName);
	private static String className = InventoryOnSaleEndPoint.class.getName();
	
    private static final String INVENTORY_MAP_KEY = InventoryOnSaleProgEndpoint.class.getSimpleName();
    
    private static Queue<Session> sessionsQueue = InventoryOnSaleProgEndpoint.getSessionsQueue();

	private static int brokenCounter=1;
	private static int writeNotCounter=1;

	private static AtomicLong brokenPipeCounter  = new AtomicLong();
	private static AtomicLong illegalStateCounter = new AtomicLong();

	/*@Resource(lookup="java:comp/DefaultManagedScheduledExecutorService")
	private ManagedScheduledExecutorService managedScheduledExecutorService;*/
	
	//@Inject
    private  GarageSaleWebSocketSessionBean garageSaleWebSocketSessionBean=GarageSaleWebSocketsUtil.getGarageSaleWebSocketSessionBean();
	
	//@Inject
	private GarageSaleDashboardApplicationScoppedBean garageSaleDashboardApplicationScoppedBean=GarageSaleWebSocketsUtil.getGarageSaleDashboardApplicationScoppedBean();

	/*@Inject
    private  GarageSaleWebSocketSessionBean garageSaleWebSocketSessionBean;
	
	@Inject
	private GarageSaleDashboardApplicationScoppedBean garageSaleDashboardApplicationScoppedBean;*/
	
    EndpointConfig ec=null;
    public InventoryOnSaleMessageHandler(Session session,EndpointConfig ec) {
    	// store the session so our onMessage method can use it later
    	this.ec=ec;
        currentSession = session;
    }
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

    

    // using the OnMessage annotation for this method will cause this method to get called by WebSockets when this connection has received 
    // a WebSocket message from the other side of the connection.  
    // The message is derived from the WebSocket frame payloads of one, and only one, WebSocket message.
    
    @Override
    public void onMessage(String message) {
    	String methodName="receiveMessage";
    	logger.logp(Level.FINE, className, methodName, "Inside receiveMessage and Message is: "+message);
        try {
    		//refreshInventoryOnSale();
        	GarageSaleWebSocketsUtil.updateCounters(garageSaleDashboardApplicationScoppedBean, INVENTORY_MAP_KEY, "onMessage",currentSession.getOpenSessions().size());        	
        	sendAll();
        } catch (Exception e) {
			e.printStackTrace();
		}
    }

    /**
     * 
     * @throws Exception
     */
   /* public  void refreshInventoryOnSale() throws Exception{
    	String methodName="refreshInventoryOnSale";
    	logger.logp(Level.FINE, className, methodName, "Inside refreshInventoryOnSale");
		try {
			ScheduledFuture future =
			managedScheduledExecutorService.scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {
					try {
							sendAll();
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

    	
    }*/
    
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
				//Does not want to overkill RPT and it does not support web sockets which is causing lot of IOExceptions by closing the conenctions.
		       /*for(Session currentOpensession:sessionsQueue){
		        	if(currentOpensession.isOpen()){
		        		JSONArray jsonInventoryArray =GarageSaleWebSocketsUtil.getInventoryOnSaleJSONArray(inventoryOnSalePartialList);
		        		currentOpensession.getBasicRemote().sendText(jsonInventoryArray.toString());
		        	}
		        }*/
        	if(currentSession!=null && currentSession.isOpen()){
        		JSONArray jsonInventoryArray =GarageSaleWebSocketsUtil.getInventoryOnSaleJSONArray(inventoryOnSalePartialList);
        		currentSession.getBasicRemote().sendText(jsonInventoryArray.toString());
        	}
			
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

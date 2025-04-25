/**
 * 
 */
package com.ibm.websphere.svt.gs.gsjsfweb.websockets;

import java.io.IOException;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.inject.Inject;
import javax.websocket.CloseReason;
import javax.websocket.EncodeException;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.ibm.json.java.JSONArray;
import com.ibm.websphere.svt.gs.tax.session.CustomerSession;

/**
 * @author JAGRAJ
 * This WebSocket Endpoint class pushes GarageSale Dashboard information  to all the clients 
 * connected to this endpoint. The data is refreshed every 10 seconds to all the clients.
 */
@ServerEndpoint(value="/garageSaleDashboardEndPoint/{dashboardType}" ,encoders={GarageSaleDashboardEncoder.class})
public class GarageSaleDashboardEndPoint{
    private static Queue<Session> databaseQueue = new ConcurrentLinkedQueue<>();
    private static Queue<Session> webSocketsQueue = new ConcurrentLinkedQueue<>();
    private static long currentRefreshInterval=60000;
    
    int count = 0;
	boolean isOpen=false;
	private static String componentName = "com.ibm.websphere.svt.gs.gsjsfweb.websockets";
	private static Logger logger = Logger.getLogger(componentName);
	private static String className = GarageSaleDashboardEndPoint.class.getName();
	
	private static final String DASHBOARD_MAP_KEY= GarageSaleDashboardEndPoint.class.getSimpleName();
	private boolean enableConcurrency= GarageSaleWebSocketsUtil.isEnableConcurrency();
	
	private Session currentSession;
	
	@EJB
	private  CustomerSession customerSession;
	
	@Resource(lookup="java:comp/DefaultManagedScheduledExecutorService")
	private ManagedScheduledExecutorService managedScheduledExecutorService;
	
	public GarageSaleDashboardEndPoint(){
		
	}
	
	
	//@Inject
	
	//Commenting this resource lookup and this was workaround
	//private GarageSaleDashboardApplicationScoppedBean garageSaleDashboardApplicationScoppedBean=GarageSaleWebSocketsUtil.getGarageSaleDashboardApplicationScoppedBean();
	//Changing for CDI 1.2 features - Constructor dependency injection.
	
	private GarageSaleDashboardApplicationScoppedBean garageSaleDashboardApplicationScoppedBean;
	
	@Inject
	public GarageSaleDashboardEndPoint(GarageSaleDashboardApplicationScoppedBean garageSaleDashboardApplicationScoppedBean){
		this.garageSaleDashboardApplicationScoppedBean=garageSaleDashboardApplicationScoppedBean;
	}
	
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

	// OnOpen will get called by WebSockets when the connection has been established successfully using WebSocket handshaking with
    // the HTTP Request - Response processing.
    @OnOpen
    public void onOpen(Session session, EndpointConfig ec,@PathParam("dashboardType") String dashboardType) {
    	// Store the WebSocket session for later use.
    	String methodName="onOpen";
    	logger.logp(Level.FINE, className, methodName, "Inside onOpen");
    	long currentTime=0;
    	long endTime=0;
    	currentSession=session;
        try {
        	//This is workaround to put the queue bottom of the priority queue.
        	//Thread.yield();
        	if(dashboardType!=null && dashboardType.equals("databaseMetrics")){
        		
        		databaseQueue.add(session);
        		if(enableConcurrency){
        			currentTime=System.currentTimeMillis();
        			sendAll(session,dashboardType);
        			endTime=System.currentTimeMillis();
                    refreshDashboard(session,dashboardType,endTime-currentTime);
        		}else {
        			sendAll(session,dashboardType);
        		}
        		
        	}
        	else if(dashboardType!=null && dashboardType.equals("webSocketsMetrics")){
        		webSocketsQueue.add(session);
        		if(enableConcurrency){
        			currentTime=System.currentTimeMillis();
        			sendAll(session,dashboardType);
        			endTime=System.currentTimeMillis();
                    refreshDashboard(session,dashboardType,endTime-currentTime);
        		}else {
        			sendAll(session,dashboardType);
        		}
        	}
        	GarageSaleWebSocketsUtil.updateCounters(garageSaleDashboardApplicationScoppedBean, DASHBOARD_MAP_KEY+"-"+dashboardType, "onOpen",session.getOpenSessions().size());
	    	isOpen=true;
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    // using the OnMessage annotation for this method will cause this method to get called by WebSockets when this connection has received 
    // a WebSocket message from the other side of the connection.  
    // The message is derived from the WebSocket frame payloads of one, and only one, WebSocket message.
    
    @OnMessage
    public void sendMessage(Session session,String message,@PathParam("dashboardType") String dashboardType) {
		String methodName = "receiveMessage";
		logger.logp(Level.FINE, className, methodName, "Inside sendMessage");
    	long currentTime=0;
    	long endTime=0;
		
		try {
    		if(enableConcurrency){
    			currentTime=System.currentTimeMillis();
    			sendAll(session,dashboardType);
    			endTime=System.currentTimeMillis();
                refreshDashboard(session,dashboardType,endTime-currentTime);
    		}else {
    			sendAll(session,dashboardType);
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    // Using the OnClose annotation will cause this method to be called when the WebSocket Session is being closed.
    @OnClose
    public void onClose(Session session, CloseReason reason,@PathParam("dashboardType") String dashboardType) throws Exception{
    	String methodName="onClose";
    	logger.logp(Level.FINE, className, methodName, "Inside onClose");
    	/* Remove this connection from the queue */
    	GarageSaleWebSocketsUtil.updateCounters(garageSaleDashboardApplicationScoppedBean, DASHBOARD_MAP_KEY+"-"+dashboardType, "onClose",session.getOpenSessions().size());
    	if(dashboardType!=null && dashboardType.equals("databaseMetrics")){
    		databaseQueue.remove(session);
    		
    	}
    	else if(dashboardType!=null && dashboardType.equals("webSocketsMetrics")){
    		webSocketsQueue.remove(session);
    	}
       	try {
    		if(session!=null){
    			session.close();
    		}
    		currentSession=null;
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	logger.log(Level.FINE, "Connection closed.");    	
    }

    // Using the OnError annotation will cause this method to be called when the WebSocket Session has an error to report. For the Alpha version
    // of the WebSocket implentation on Liberty, this will not be called on error conditions.
    @OnError
    public void onError(Session session,Throwable t,@PathParam("dashboardType") String dashboardType) throws Exception{
    	String methodName="onError";
    	logger.logp(Level.FINE, className, methodName, "Inside onError");
    	/* Remove this connection from the queue */
    	if(dashboardType!=null && dashboardType.equals("databaseMetrics")){
    		databaseQueue.remove(session);
    	}
    	else if(dashboardType!=null && dashboardType.equals("webSocketsMetrics")){
    		webSocketsQueue.remove(session);
    	}
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
    	
    	GarageSaleWebSocketsUtil.updateCounters(garageSaleDashboardApplicationScoppedBean, DASHBOARD_MAP_KEY+"-"+dashboardType, "onError",session.getOpenSessions().size());
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
     * @throws IOException
     * @throws EncodeException
     * @throws Exception
     */
	public void refreshDashboard(final Session session,final String dashboardType,long refreshInterval) throws IOException, EncodeException,
			Exception {
		String methodName = "refreshDashboard()";
		logger.logp(Level.FINE, className, methodName, "refreshDashboard()");
		//int refreshInterval=GarageSaleWebSocketsUtil.getRefreshInterval();
		try {
			if(refreshInterval <60000){
				refreshInterval=60000;
			}
	    	if(dashboardType!=null && dashboardType.equals("databaseMetrics")){
				/*ScheduledFuture future =
						managedScheduledExecutorService.scheduleAtFixedRate(new Runnable() {
							@Override
							public void run() {
								try {
						        		GarageSaleWebSocketsUtil.updateCounters(garageSaleDashboardApplicationScoppedBean, DASHBOARD_MAP_KEY+"-"+dashboardType, "onMessage",session.getOpenSessions().size());
					        			sendAll(session,dashboardType);
								} catch (Exception e) {
									e.printStackTrace();
							    	logger.log(Level.SEVERE, "Executor Service Task Failed"+e.getMessage());        
								}
							}
						},0,refreshInterval,TimeUnit.MILLISECONDS);
						
						if(databaseQueue!=null && databaseQueue.size()==0){
							if(future.isDone()){
								future.cancel(true);
							}
						}*/
	    		Future future =
						managedScheduledExecutorService.submit(new Runnable() {
							@Override
							public void run() {
								try {
						        		GarageSaleWebSocketsUtil.updateCounters(garageSaleDashboardApplicationScoppedBean, DASHBOARD_MAP_KEY+"-"+dashboardType, "onMessage",session.getOpenSessions().size());
					        			sendAll(session,dashboardType);
								} catch (Exception e) {
									e.printStackTrace();
							    	logger.log(Level.SEVERE, "Executor Service Task Failed"+e.getMessage());        
								}
							}
						});
	    		
	    		if(future.get()==null){
			    	logger.log(Level.FINE, "Executor Service Task Completed Successfully for Database Dashboard Metrics");        
	    			
	    		}
						
	    	}
	    	else if(dashboardType!=null && dashboardType.equals("webSocketsMetrics")){
				/*ScheduledFuture future =
						managedScheduledExecutorService.scheduleAtFixedRate(new Runnable() {
							@Override
							public void run() {
								try {
						        	GarageSaleWebSocketsUtil.updateCounters(garageSaleDashboardApplicationScoppedBean, DASHBOARD_MAP_KEY+"-"+dashboardType, "onMessage",session.getOpenSessions().size());
				        			sendAll(session,dashboardType);
								} catch (Exception e) {
									e.printStackTrace();
							    	logger.log(Level.SEVERE, "Executor Service Task Failed"+e.getMessage());        
								}
							}
						},0,refreshInterval,TimeUnit.MILLISECONDS);
						
						if(webSocketsQueue!=null && webSocketsQueue.size()==0){
							if(future.isDone()){
								future.cancel(true);
							}
						}*/
	    		
	    		Future future =
						managedScheduledExecutorService.submit(new Runnable() {
							@Override
							public void run() {
								try {
						        	GarageSaleWebSocketsUtil.updateCounters(garageSaleDashboardApplicationScoppedBean, DASHBOARD_MAP_KEY+"-"+dashboardType, "onMessage",session.getOpenSessions().size());
				        			sendAll(session,dashboardType);
								} catch (Exception e) {
									e.printStackTrace();
							    	logger.log(Level.SEVERE, "Executor Service Task Failed"+e.getMessage());        
								}
							}
						});
						
	    		if(future.get()==null){
			    	logger.log(Level.FINE, "Executor Service Task Completed Successfully For WebSocketsMetrics");        
	    			
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
	public synchronized void sendAll(Session session,String dashboardType) throws Exception {
		String methodName="sendAll()";
		
		
		if(dashboardType !=null && dashboardType.equals("databaseMetrics")){
			GarageSaleDashboardBean garageSaleDashboardBean = new GarageSaleDashboardBean();
			if (databaseQueue != null && databaseQueue.size()>0) {
				Object[] result = customerSession.getTotalNoOfInvoices();
				logger.logp(
						Level.FINE,
						className,
						methodName,
						"Number of Open sessions are:  "
								+ databaseQueue.size());
				if (result != null && result.length > 0) {
					garageSaleDashboardBean.setNoOfInvoicesCompleted(new Long(
							result[0].toString()));
					garageSaleDashboardBean.setNoOfInvoicesCreated(new Long(
							result[1].toString()));
				}
				garageSaleDashboardBean.setNoOfWebSocketsOpenSessions(new Long(
						databaseQueue.size()));
				
					try{
						/*for (Session currentOpensession : databaseQueue) {
							if (currentOpensession.isOpen()) {
								currentOpensession.getBasicRemote().sendObject(
										garageSaleDashboardBean);
							}
						}*/
						if (session!=null && session.isOpen()) {
							session.getBasicRemote().sendObject(
									garageSaleDashboardBean);
						}

						
					}catch (Exception t){
				    	GarageSaleWebSocketsUtil.updateCounters(garageSaleDashboardApplicationScoppedBean, DASHBOARD_MAP_KEY+"-"+dashboardType, "currentIoErrorsCounter",session.getOpenSessions().size());

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
		else if(dashboardType !=null && dashboardType.equals("webSocketsMetrics")){
			JSONArray jsonArray= new JSONArray();
			ConcurrentHashMap<String,GarageSaleWebSocketsDashboardBean> concurrentHashMap = garageSaleDashboardApplicationScoppedBean.getGarageSaleWebSocketsDashboardMap();
			Set<String> keys = concurrentHashMap.keySet();
			Iterator<String> iterator= keys.iterator();
			while(iterator.hasNext()){
				String key = iterator.next();
				GarageSaleWebSocketsDashboardBean garageSaleWebSocketsDashboardBean = concurrentHashMap.get(key);
		    	logger.logp(Level.FINE, className, methodName, "Inside Send All WebSockets Metrics "+garageSaleWebSocketsDashboardBean.toString());
				jsonArray.add(garageSaleWebSocketsDashboardBean.getJSONObject());
			}
				try{
				    /*for (Session currentOpensession : webSocketsQueue) {
				    	if (currentOpensession.isOpen()) {
				    		currentOpensession.getBasicRemote().sendObject(jsonArray.toString());
				    	}
				    }*/
			    	if (session!=null&& session.isOpen()) {
			    		session.getBasicRemote().sendObject(jsonArray.toString());
			    	}
					
					
				}catch (Exception t){
			    	GarageSaleWebSocketsUtil.updateCounters(garageSaleDashboardApplicationScoppedBean, DASHBOARD_MAP_KEY+"-"+dashboardType, "currentIoErrorsCounter",session.getOpenSessions().size());

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

	/**
	 * @return the currentRefreshInterval
	 */
	public static long getCurrentRefreshInterval() {
			if(currentRefreshInterval <60000){
				currentRefreshInterval=60000;
			}
			
		return currentRefreshInterval;
	}

	/**
	 * @param currentRefreshInterval the currentRefreshInterval to set
	 */
	public static void setCurrentRefreshInterval(long currentRefreshInterval) {
		GarageSaleDashboardEndPoint.currentRefreshInterval = currentRefreshInterval;
	}

}

/**
 * 
 */
package com.ibm.websphere.persona.pmi.websockets;

import javax.websocket.server.ServerEndpoint;


/**
 * @author JAGRAJ
 *
 */
//@ServerEndpoint(value="/personaWebServicesPMIWebSocketEndpoint")
public class PersonaWebServicesPMIWebSocketEndpoint {
/*    private static Queue<Session> sessionsQueue = new ConcurrentLinkedQueue<>();
    
    int count = 0;
	boolean isOpen=false;
	private static String componentName = "com.ibm.websphere.persona.pmi.websockets";
	private static Logger logger = Logger.getLogger(componentName);
	private static String className = PersonaWebServicesPMIWebSocketEndpoint.class.getName();
	
	@Resource(lookup="java:comp/DefaultManagedScheduledExecutorService")
	private ManagedScheduledExecutorService managedScheduledExecutorService;

	@Resource(name="personaJAXWSWebServicesPMIService", lookup="personaWebServicesPMIService_Broken")
	private WASPersonaWebServicesPMIService personaWebServicesPMIService;
	
	@Resource(lookup="personaRestfulServicesPMIService")
	private WASPersonaRestfulServicesPMIService personaRestfulervicesPMIService;
	private boolean enableConcurrency=PersonaWebServicesPMIWebSocketsUtil.isEnableConcurrency();
	private Session currentSession; 
	private static int brokenCounter=0;
	private static int writeNotCounter=0;

    @OnOpen
    public void onOpen(Session session, EndpointConfig ec) {
    	// Store the WebSocket session for later use.
    	String methodName="onOpen";
    	long currentTime=0;
    	long endTime=0;
    	currentSession=session;
    	logger.logp(Level.FINE, className, methodName, "Inside onOpen");
        try {
        	//This is workaround to put the queue bottom of the priority queue.
        	Thread.yield();

        	sessionsQueue.add(session);
        	if(enableConcurrency){
    			currentTime=System.currentTimeMillis();
            	sendAll();
            	endTime=System.currentTimeMillis();            	
            	refreshPMIData(endTime-currentTime);
        	}else {
            	sendAll();
        	}
	    	isOpen=true;
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    // using the OnMessage annotation for this method will cause this method to get called by WebSockets when this connection has received 
    // a WebSocket message from the other side of the connection.  
    // The message is derived from the WebSocket frame payloads of one, and only one, WebSocket message.
    
    @OnMessage
    public void sendMessage(String message) {
		String methodName = "receiveMessage";
		logger.logp(Level.FINE, className, methodName, "Inside sendMessage");
    	long currentTime=0;
    	long endTime=0;
		try {
        	if(enableConcurrency){
    			currentTime=System.currentTimeMillis();
            	sendAll();
            	endTime=System.currentTimeMillis();            	
            	refreshPMIData(endTime-currentTime);
        	}else {
            	sendAll();
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    // Using the OnClose annotation will cause this method to be called when the WebSocket Session is being closed.
    @OnClose
    public void onClose(Session session, CloseReason reason) {
    	String methodName="onClose";
    	logger.logp(Level.FINE, className, methodName, "Inside onClose");
    	 Remove this connection from the queue 
    	try {
    		if(session!=null){
    			session.close();
    	    	sessionsQueue.remove(session);
    			
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
    public void onError(Session session,Throwable t) {
    	String methodName="onError";
    	if(t !=null && t instanceof IOException){
    		if(t.getMessage() !=null && t.getMessage().indexOf("Broken")!=-1){
    			brokenCounter=brokenCounter+1;
            	logger.logp(Level.FINE, className, methodName, "Inside onError");
    			long currentCount=PersonaWebServicesPMIWebSocketsUtil.getIoErrorsCounter().incrementAndGet();
    			PersonaWebServicesPMIWebSocketsUtil.setCurrentIoErrorsCounter(currentCount);
    			if(brokenCounter==1 || brokenCounter==50){
        		    logger.logp(Level.WARNING,	className,	methodName, className+" :The current IOErrors Broken Pipe counter is : "+currentCount);
        		    brokenCounter=1;
    			}
    		}
    		
    	}else if(t !=null && t instanceof IllegalStateException){
    		if(t.getMessage() !=null && t.getMessage().indexOf("write not allowed")!=-1){
    			writeNotCounter=writeNotCounter+1;
            	logger.logp(Level.FINE, className, methodName, "Inside onError :"+t.getMessage());
    			long currentCount=PersonaWebServicesPMIWebSocketsUtil.getIllegalStateCounter().incrementAndGet();
    			PersonaWebServicesPMIWebSocketsUtil.setCurrentIllegalStateCount(currentCount);
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
    	 Remove this connection from the queue 
    	sessionsQueue.remove(session);
    	try {
    		if(session!=null){
    			session.close();
    	    	sessionsQueue.remove(session);
    			
    		}
    		currentSession=null;
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    
    *//**
     * 
     * @throws IOException
     * @throws EncodeException
     * @throws Exception
     *//*
	public void refreshPMIData(long refreshInterval) throws IOException, EncodeException,
			Exception {
		String methodName = "refreshDashboard()";
		logger.logp(Level.FINE, className, methodName, "refreshDashboard()");
		//int refreshInterval=PersonaWebServicesPMIWebSocketsUtil.getRefreshInterval();

		try {
			if(refreshInterval <60000){
				refreshInterval=60000;
			}
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
			},0,refreshInterval,TimeUnit.MINUTES);
			
			
			
			if(sessionsQueue!=null && sessionsQueue.size()==0){
				if(future.isDone()){
					future.cancel(true);
				}
			}

			Future future =
					managedScheduledExecutorService.submit(new Runnable() {
						@Override
						public void run() {
							try {
									sendAll();
							} catch (Exception e) {
								e.printStackTrace();
						    	logger.log(Level.SEVERE, "Executor Service Task Failed"+e.getMessage());        
							}
						}
					});
			
			if(future.get() == null){
		    	logger.logp(Level.FINE, className, methodName, "Scheduled task completed successfully");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	*//**
	 * 
	 * @throws Exception
	 *//*
	public synchronized void sendAll() throws Exception {
		String methodName="sendAll()";
		
		if (sessionsQueue != null && sessionsQueue.size()>0) {
			//Object[] result = customerSession.getTotalNoOfInvoices();
			Map<String,WASPersonaWebServicesPMIDataBean> jaxWSMetricsMap =personaWebServicesPMIService.getJAXWSMetricsFromHandler();
			Map<String,WASPersonaRestfulServicesPMIDataBean> jaxRSMetricsMap =personaRestfulervicesPMIService.getJAXRSMetricsFromHandler();
			JSONArray jsonArray = new JSONArray();
			
			if (((jaxWSMetricsMap != null && jaxWSMetricsMap.size() > 0)) || ((jaxRSMetricsMap != null && jaxRSMetricsMap.size() > 0))){
				logger.logp(Level.FINE,className,methodName,"jaxWSMetricsMap Size is:  "+ jaxWSMetricsMap.size());
				logger.logp(Level.FINE,className,methodName,"jaxRSMetricsMap Size is:  "+ jaxRSMetricsMap.size());
				
				Set<String> jaxWSKeys = jaxWSMetricsMap.keySet();
				Iterator<String> i = jaxWSKeys.iterator();
				while(i.hasNext()){
					String key =  i.next();
				   WASPersonaWebServicesPMIDataBean webServicesPMIDataBean =  jaxWSMetricsMap.get(key);
			       logger.logp(Level.FINE,	className,	methodName,"Key is : "+key+" The Data is: "+webServicesPMIDataBean.toString());
			       jsonArray.add(webServicesPMIDataBean.getJSONObject());
				}
				
				Set<String> jaxRSKeys = jaxRSMetricsMap.keySet();
				Iterator<String> j = jaxRSKeys.iterator();
				while(j.hasNext()){
					String key =  j.next();
				   WASPersonaRestfulServicesPMIDataBean restfulServicesPMIDataBean =  jaxRSMetricsMap.get(key);
			       logger.logp(Level.FINE,	className,	methodName,"Key is : "+key+" The Data is: "+restfulServicesPMIDataBean.toString());
			       jsonArray.add(restfulServicesPMIDataBean.getJSONObject());
				}
				
			    logger.logp(Level.FINE,	className,	methodName,"jsonArray is  :   "+jsonArray.toString());
				logger.logp(Level.FINE,className,methodName,"Number of Open sessions are:  "+ sessionsQueue.size());
					try{
					    for (Session currentOpensession : sessionsQueue) {
					    	if (currentOpensession.isOpen()) {
					    		currentOpensession.getBasicRemote().sendObject(jsonArray.toString());
					    	}
					    }
				    	if (currentSession!=null && currentSession.isOpen()) {
				    		currentSession.getBasicRemote().sendObject(jsonArray.toString());
				    	}
					}catch (Exception t){
				    	if(t !=null && t instanceof IOException){
				    		if(t.getMessage() !=null && t.getMessage().indexOf("Broken")!=-1){
				    			brokenCounter=brokenCounter+1;
				            	logger.logp(Level.FINE, className, methodName, "Inside onError");
				    			long currentCount=PersonaWebServicesPMIWebSocketsUtil.getIoErrorsCounter().incrementAndGet();
				    			PersonaWebServicesPMIWebSocketsUtil.setCurrentIoErrorsCounter(currentCount);
				    			if(brokenCounter==1 || brokenCounter==50){
				        		    logger.logp(Level.WARNING,	className,	methodName, className+" :The current IOErrors Broken Pipe counter is : "+currentCount);
				        		    brokenCounter=1;
				    			}
				    		}
				    		
				    	}else if(t !=null && t instanceof IllegalStateException){
				    		if(t.getMessage() !=null && t.getMessage().indexOf("write not allowed")!=-1){
				    			writeNotCounter=writeNotCounter+1;
				            	logger.logp(Level.FINE, className, methodName, "Inside onError :"+t.getMessage());
				    			long currentCount=PersonaWebServicesPMIWebSocketsUtil.getIllegalStateCounter().incrementAndGet();
				    			PersonaWebServicesPMIWebSocketsUtil.setCurrentIllegalStateCount(currentCount);
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
					    
					}
					
			}
		}

	}
*/
}

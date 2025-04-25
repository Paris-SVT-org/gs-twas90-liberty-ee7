/**
 * 
 */
package com.ibm.websphere.svt.gs.gsjsfweb.websockets;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

/**
 * @author JAGRAJ
 *
 *
 */
@ServerEndpoint(value="/garageSaleProductDemosWebSocketEndpoint/{requestType}", configurator=GarageSaleEndpointConfigurator.class)
public class GarageSaleProductDemosWebSocketEndpoint {

    private Session currentSession = null;
    int count = 0;
	private static String componentName = "com.ibm.websphere.svt.gs.gsjsfweb.websockets";
	private static Logger logger = Logger.getLogger(componentName);
	private static String className = GarageSaleProductDemosWebSocketEndpoint.class.getName();
	private EndpointConfig endpointConfig=null;
	
	@Inject
	private GarageSaleProdctVideosCacheBean garageSaleProdctVideosCacheBean;
	
	@Inject
	private GarageSaleDashboardApplicationScoppedBean garageSaleDashboardApplicationScoppedBean;
	
	private static final String PRODUCT_DEMO_VIDEOS_MAP_KEY= GarageSaleProductDemosWebSocketEndpoint.class.getSimpleName();
	
	private int brokenCounter=1;
	private int writeNotCounter=1;

	private static AtomicLong brokenPipeCounter  = new AtomicLong();
	private static AtomicLong illegalStateCounter = new AtomicLong();
	

	
	/**
	 * @return the garageSaleProdctVideosCacheBean
	 */
	public GarageSaleProdctVideosCacheBean getGarageSaleProdctVideosCacheBean() {
		return garageSaleProdctVideosCacheBean;
	}

	/**
	 * @param garageSaleProdctVideosCacheBean the garageSaleProdctVideosCacheBean to set
	 */
	public void setGarageSaleProdctVideosCacheBean(
			GarageSaleProdctVideosCacheBean garageSaleProdctVideosCacheBean) {
		this.garageSaleProdctVideosCacheBean = garageSaleProdctVideosCacheBean;
	}

	/**
	 * 
	 * @param session
	 * @param ec
	 */
    @OnOpen
    public void onOpen(Session session, EndpointConfig ec,@PathParam("requestType") String requestType) {
    	// Store the WebSocket session for later use.
    	String methodName="onOpen";
    	
    	logger.logp(Level.FINE, className, methodName, "Inside onOpen");
        try {
            currentSession = session;
            this.endpointConfig=ec;
        	//This is workaround to put the queue bottom of the priority queue.
        	//Thread.yield();
        	GarageSaleWebSocketsUtil.updateCounters(garageSaleDashboardApplicationScoppedBean, PRODUCT_DEMO_VIDEOS_MAP_KEY+"-"+requestType, "onOpen",session.getOpenSessions().size());
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    // using the OnMessage annotation for this method will cause this method to get called by WebSockets when this connection has received 
    // a WebSocket message from the other side of the connection.  
    // The message is derived from the WebSocket frame payloads of one, and only one, WebSocket message.

    
    
    
   @OnMessage(maxMessageSize=999999999)
    public void readDemoFile(Session session,ByteBuffer videoFile,@PathParam("requestType") String requestType){
    	String methodName="readDemoFile";
    	logger.logp(Level.FINE, className, methodName, "Inside readDemoFile: ");
        try {
        	if(requestType !=null && requestType.equalsIgnoreCase("uploadRequest")){
        		if(videoFile !=null){
        			String message="Success:"+videoFile.array().length;
            		session.getBasicRemote().sendText(message);
                	GarageSaleWebSocketsUtil.updateCounters(garageSaleDashboardApplicationScoppedBean, PRODUCT_DEMO_VIDEOS_MAP_KEY+"-"+requestType, "onMessage",session.getOpenSessions().size());
                	if(videoFile.array().length==0){
                    	GarageSaleWebSocketsUtil.updateCounters(garageSaleDashboardApplicationScoppedBean, PRODUCT_DEMO_VIDEOS_MAP_KEY+"-"+requestType, "misMatch",session.getOpenSessions().size());
                	}
        		}
        	}
        	
        } catch (Exception e) {
			e.printStackTrace();
			String message="Failure:"+videoFile.array().length;
    		try {
				session.getBasicRemote().sendText(message);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
    }
    
    @OnMessage
    public void sendProductDemoFile(Session session, String fileName,@PathParam("requestType") String requestType) {
    	String methodName="sendProductDemoFile";
    	logger.logp(Level.FINE, className, methodName, "Inside receiveMessage and Message is: ");
    	ByteBuffer videoFile=null;
        try {
        	if(requestType !=null && requestType.equalsIgnoreCase("demoRequest")){
            	Map<String,Object> userProperties=endpointConfig.getUserProperties();
            	String path=userProperties.get("filePath").toString();
            	//String filePath = path+System.getProperty("file.separator")+"2013_04_12_0907_April_12_Opportunity_Codes_Mp.mp4";
            	if(garageSaleProdctVideosCacheBean.getProductDemoVideosMap()==null || garageSaleProdctVideosCacheBean.getProductDemoVideosMap().size()==0){
            		garageSaleProdctVideosCacheBean.setProductDemoVideosMap(GarageSaleWebSocketsUtil.loadVideosFromFileSystem(path));
            	}
            	else if(garageSaleProdctVideosCacheBean.getPublishProductVideos()==null || garageSaleProdctVideosCacheBean.getPublishProductVideos().size()==0){
            		garageSaleProdctVideosCacheBean.setPublishProductVideos(GarageSaleWebSocketsUtil.loadVideosFromFileSystem(path));
            	}
            	videoFile=garageSaleProdctVideosCacheBean.getProductDemoVideosMap().get(fileName);
            	ByteBuffer videoFileCopy= videoFile.duplicate();
            	
            	logger.logp(Level.FINE, className, methodName, "The fileName is :  "+fileName);
            	logger.logp(Level.FINE, className, methodName, "The request is :  "+requestType);
            	if(videoFileCopy==null){
                	logger.logp(Level.SEVERE, className, methodName, "The videos file in map is : NULL");
            		
            	}
            	logger.logp(Level.FINE, className, methodName, "The Video file size  :  "+videoFile.array().length);
            	videoFileCopy.position(0);
            	session.setMaxBinaryMessageBufferSize(videoFileCopy.array().length);
               	session.getBasicRemote().sendBinary(videoFileCopy);
               	//session.getBasicRemote().sendText("Success:"+videoFileCopy.array().length);
            	GarageSaleWebSocketsUtil.updateCounters(garageSaleDashboardApplicationScoppedBean, PRODUCT_DEMO_VIDEOS_MAP_KEY+"-"+requestType, "onMessage",session.getOpenSessions().size());
        		
        	}
        	else {
            	logger.logp(Level.FINE, className, methodName, "Inside receiveMessage and Message is: ");
            	logger.logp(Level.FINE, className, methodName, "The Message is : "+fileName);
        		
        	}
        } catch (Exception e) {
			e.printStackTrace();
			
		}
        
    }


    // Using the OnClose annotation will cause this method to be called when the WebSocket Session is being closed.
    @OnClose
    public void onClose(Session session, CloseReason reason,@PathParam("requestType") String requestType) throws Exception{
    	String methodName="onClose";
    	logger.logp(Level.FINE, className, methodName, "Inside onClose");
    	GarageSaleWebSocketsUtil.updateCounters(garageSaleDashboardApplicationScoppedBean, PRODUCT_DEMO_VIDEOS_MAP_KEY+"-"+requestType, "onClose",session.getOpenSessions().size());
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
    public void onError(Throwable t,@PathParam("requestType") String requestType,Session session) throws Exception{
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
    	GarageSaleWebSocketsUtil.updateCounters(garageSaleDashboardApplicationScoppedBean, PRODUCT_DEMO_VIDEOS_MAP_KEY+"-"+requestType, "onError",session.getOpenSessions().size());
    	try {
    		if(session!=null){
    			session.close();
    		}
    		currentSession=null;
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

}

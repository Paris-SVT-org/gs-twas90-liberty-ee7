/**
 * 
 */
package com.ibm.websphere.svt.gs.gsjsfweb;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.ContainerProvider;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import com.ibm.websphere.svt.gs.gsjsfweb.websockets.DatabaseDashboardClientEndpoint;
import com.ibm.websphere.svt.gs.gsjsfweb.websockets.GarageSaleDemoVideoClientEndpoint;
import com.ibm.websphere.svt.gs.gsjsfweb.websockets.GarageSaleProdctVideosCacheBean;
import com.ibm.websphere.svt.gs.gsjsfweb.websockets.GarageSaleUploadVideoClientEndpoint;
import com.ibm.websphere.svt.gs.gsjsfweb.websockets.GarageSaleWebSocketsUtil;
import com.ibm.websphere.svt.gs.gsjsfweb.websockets.JsfWSCJsonBean;
import com.ibm.websphere.svt.gs.gsjsfweb.websockets.OnSaleContentClientEndpoint;
import com.ibm.websphere.svt.gs.gsjsfweb.websockets.OnSaleContentForUploadsClientEndpoint;
import com.ibm.websphere.svt.gs.gsjsfweb.websockets.ProductDemoBean;
import com.ibm.websphere.svt.gs.gsjsfweb.websockets.ProductUploadManagedBean;
import com.ibm.websphere.svt.gs.gsjsfweb.websockets.WebSocketsDashboardClientEndpoint;
import com.ibm.websphere.svt.gs.gsjsfweb.websockets.WebSocketsDataBean;

/**
 * @author JAGRAJ
 *
 */
@ManagedBean(name="jsfWebSocketsClientActions")
@RequestScoped
public class JSFWebSocketsClientActions implements Serializable{

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -67022638858019141L;
	private static String componentName = "com.ibm.websphere.svt.gs.gsjsfweb";
	private static Logger logger = Logger.getLogger(componentName);
	private static String className = JSFWebSocketsClientActions.class.getName();
	
	@Inject
	private GarageSaleCDIApplicationScopped garageSaleCDIApplicationScopped;
	

	
	@Inject
	private GarageSaleProdctVideosCacheBean garageSaleProdctVideosCacheBean;
	
	@ManagedProperty(name="productUploadManagedBean",value="#{productUploadManagedBean}")
	private ProductUploadManagedBean productUploadManagedBean;
	
	@ManagedProperty(name="jsfWSCJsonBean",value="#{jsfWSCJsonBean}")
	private JsfWSCJsonBean jsfWSCJsonBean;
	
	@ManagedProperty(name="productDemoBean",value="#{productDemoBean}")
	private ProductDemoBean productDemoBean;
	
	
	
	/**
	 * @return the productDemoBean
	 */
	public ProductDemoBean getProductDemoBean() {
		return productDemoBean;
	}

	/**
	 * @param productDemoBean the productDemoBean to set
	 */
	public void setProductDemoBean(ProductDemoBean productDemoBean) {
		this.productDemoBean = productDemoBean;
	}

	/**
	 * @return the jsfWSCJsonBean
	 */
	public JsfWSCJsonBean getJsfWSCJsonBean() {
		return jsfWSCJsonBean;
	}

	/**
	 * @param jsfWSCJsonBean the jsfWSCJsonBean to set
	 */
	public void setJsfWSCJsonBean(JsfWSCJsonBean jsfWSCJsonBean) {
		this.jsfWSCJsonBean = jsfWSCJsonBean;
	}

	/**
	 * @return the productUploadManagedBean
	 */
	public ProductUploadManagedBean getProductUploadManagedBean() {
		return productUploadManagedBean;
	}

	/**
	 * @param productUploadManagedBean the productUploadManagedBean to set
	 */
	public void setProductUploadManagedBean(
			ProductUploadManagedBean productUploadManagedBean) {
		this.productUploadManagedBean = productUploadManagedBean;
	}

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
	 * @return
	 * @throws Exception
	 */
	public String inventoryOnSale() throws Exception{
		String outCome="/facelets/jsfWSCShowOnSaleInventory";
		try{
			outCome="/facelets/jsfWSCShowOnSaleInventory";
			//initiateOnSaleContentWebSocketRequest();
			return outCome;
		}
		catch(Exception e){
			outCome="error";
			return outCome;
		}
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public String uploadDemos() throws Exception{
		String outCome="/facelets/jsfWSCShowOnSaleInventoryForUploads";
		try{
			outCome="/facelets/jsfWSCShowOnSaleInventoryForUploads";
			//initiateOnSaleContentForUploadsWebSocketRequest();
			return outCome;
		}
		catch(Exception e){
			outCome="error";
			return outCome;
		}
	}
	
	
	

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public String gsDashboard() throws Exception{
		String outCome="/dashboard/jsfWSCGsDashboardFacelet";
		try{
			outCome="/dashboard/jsfWSCGsDashboardFacelet";
			//initiateDatabaseDashboardWebSocketRequest();
			return outCome;
		}
		catch(Exception e){
			outCome="error";
			return outCome;
		}
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public String webSocketsDashboard() throws Exception{
		String outCome="/dashboard/jsfWSCWebSocketsDashboardFacelet";
		try{
			outCome="/dashboard/jsfWSCWebSocketsDashboardFacelet";
			//initiateWebSocketsDashboardWebSocketRequest();
			
			return outCome;
		}
		catch(Exception e){
			outCome="error";
			return outCome;
		}
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
/*	public String personaPMIMetricsDashboard() throws Exception{
		String outCome="index1.jsf";
		try{
			FacesContext facesContext= FacesContext.getCurrentInstance();
			ExternalContext externalContext = facesContext.getExternalContext();
			HttpServletResponse response = (HttpServletResponse)externalContext.getResponse();
			ServletContext servletContext = (ServletContext) externalContext.getContext();
			ServletContext pmiWebAppContext = servletContext.getContext("/WASPersonaWebServicesPMIWeb");
			String url = garageSaleCDIApplicationScopped.getWebModuleContextPath()+"/"+outCome;
			response.sendRedirect(url);
			return null;
			
		}
		catch(Exception e){
			outCome="error";
			return outCome;
		}
	}*/
	
	
	/**
	 * 
	 * @return
	 */
	public String upload(){
		String methodName="upload";
		String outCome="uploadGarageSaleDemoVideoFileStatus";
		Session session=null;
		try {
			ExternalContext externalContext= FacesContext.getCurrentInstance().getExternalContext();
			WebSocketContainer webSocketContainer = ContainerProvider.getWebSocketContainer();
			String endPointServerAddress =System.getProperty("GarageSaleWebSocket.endPoint.Address");
			String serverEP =  endPointServerAddress+"/garageSaleProductDemosWebSocketEndpoint/uploadRequest";
			URI serverEndpointURI =null;
			serverEndpointURI = new URI(String.format(serverEP));
			
			ServletContext context = (ServletContext)externalContext.getContext();
			String path=context.getRealPath("resources/videos");
			session =webSocketContainer.connectToServer(GarageSaleUploadVideoClientEndpoint.class, serverEndpointURI);
        	if(garageSaleProdctVideosCacheBean.getPublishProductVideos()==null || garageSaleProdctVideosCacheBean.getPublishProductVideos().size()==0){
        		ConcurrentHashMap<String,ByteBuffer> videosMap= GarageSaleWebSocketsUtil.loadVideosFromFileSystem(path);
        		garageSaleProdctVideosCacheBean.setPublishProductVideos(videosMap);
        	}
        	else if(garageSaleProdctVideosCacheBean.getProductDemoVideosMap()==null || garageSaleProdctVideosCacheBean.getProductDemoVideosMap().size()==0){
        		ConcurrentHashMap<String,ByteBuffer> videosMap= GarageSaleWebSocketsUtil.loadVideosFromFileSystem(path);
        		garageSaleProdctVideosCacheBean.setProductDemoVideosMap(videosMap);
        	}
        	logger.logp(Level.FINE, className, methodName, "The fileName is :  "+productUploadManagedBean.getFileName());        	
        	ByteBuffer videoFile=garageSaleProdctVideosCacheBean.getPublishProductVideos().get(productUploadManagedBean.getFileName());
        	ByteBuffer videoFileCopy= videoFile.duplicate();
        	
        	if(videoFileCopy==null){
            	logger.logp(Level.SEVERE, className, methodName, "The videos file in map is : NULL");
        	}
        	videoFileCopy.position(0);
        	logger.logp(Level.FINE, className, methodName, "The Video file size from client :  "+videoFileCopy.array().length);
        	session.setMaxBinaryMessageBufferSize(videoFileCopy.array().length);
        	logger.logp(Level.FINE, className, methodName, "videoFile position: " + videoFileCopy.position() + " limit: " + videoFileCopy.limit());
           	session.getBasicRemote().sendBinary(videoFileCopy);
		} catch (Exception e) {
        	try {
        		if(session!=null){
        			session.close();
        		}
        		session=null;
    		} catch (IOException io) {
    			io.printStackTrace();
    		}
			e.printStackTrace();
			outCome="error";
		}
		
		return outCome;
	}
	
	/**
	 * 
	 */
	public void updateInventoryOnSale(){
		String methodName="updateInventoryOnSale";
		try {
			initiateOnSaleContentWebSocketRequest();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 
	 */
	public void initiateOnSaleContentWebSocketRequest(){
		String methodName="initiateOnSaleContentWebSocketRequest";
		Session session=null;
		try {
			WebSocketContainer webSocketContainer = ContainerProvider.getWebSocketContainer();
			String endPointServerAddress =System.getProperty("GarageSaleWebSocket.endPoint.Address");
			String serverEP =  endPointServerAddress+"/inventoryOnSaleEndPoint";
			URI serverEndpointURI =null;
			serverEndpointURI = new URI(String.format(serverEP));
			session =webSocketContainer.connectToServer(OnSaleContentClientEndpoint.class, serverEndpointURI);
			jsfWSCJsonBean.setInventoryOnSaleData(WebSocketsDataBean.getInventoryOnSaleJsonData());
		} catch (Exception e) {
        	try {
        		if(session!=null){
        			session.close();
        		}
        		session=null;
    		} catch (IOException io) {
    			io.printStackTrace();
    		}
			e.printStackTrace();
		}

		
	}
	
	/**
	 * 
	 */
	public void updateOnSaleContentForUploads(){
		String methodName="updateInventoryOnSaleForUploads";
		try {
			initiateOnSaleContentForUploadsWebSocketRequest();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 
	 */
	public void initiateOnSaleContentForUploadsWebSocketRequest(){
		String methodName="initiateOnSaleContentForUploadsWebSocketRequest";
		Session session=null;
		try {
			WebSocketContainer webSocketContainer = ContainerProvider.getWebSocketContainer();
			String endPointServerAddress =System.getProperty("GarageSaleWebSocket.endPoint.Address");
			String serverEP =  endPointServerAddress+"/inventoryOnSaleProgEndpoint";
			URI serverEndpointURI =null;
			serverEndpointURI = new URI(String.format(serverEP));
			session =webSocketContainer.connectToServer(OnSaleContentForUploadsClientEndpoint.class, serverEndpointURI);
			jsfWSCJsonBean.setOnSaleContentForUploadsData(WebSocketsDataBean.getInventoryOnSaleProgJsonData());
		} catch (Exception e) {
        	try {
        		if(session!=null){
        			session.close();
        		}
        		session=null;
    		} catch (IOException io) {
    			io.printStackTrace();
    		}
			e.printStackTrace();
		}

		
	}

	/**
	 * 
	 */
	public void updateDatabaseDashboard(){
		String methodName="updateDatabaseDashboard";
		try {
			initiateDatabaseDashboardWebSocketRequest();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 
	 */
	public void initiateDatabaseDashboardWebSocketRequest(){
		String methodName="initiateDatabaseDashboardWebSocketRequest";
		Session session=null;
		try {
			WebSocketContainer webSocketContainer = ContainerProvider.getWebSocketContainer();
			String endPointServerAddress =System.getProperty("GarageSaleWebSocket.endPoint.Address");
			String serverEP =  endPointServerAddress+"/garageSaleDashboardEndPoint/databaseMetrics";
			URI serverEndpointURI =null;
			serverEndpointURI = new URI(String.format(serverEP));
			session =webSocketContainer.connectToServer(DatabaseDashboardClientEndpoint.class, serverEndpointURI);
			jsfWSCJsonBean.setDatabaseJsonDashboardData(WebSocketsDataBean.getDatabaseDashboardData());
		} catch (Exception e) {
        	try {
        		if(session!=null){
        			session.close();
        		}
        		session=null;
    		} catch (IOException io) {
    			io.printStackTrace();
    		}
			e.printStackTrace();
		}

		
	}

	/**
	 * 
	 */
	public void updateWebSocketsDashboard(){
		String methodName="updateWebSocketsDashboard";
		try {
			initiateWebSocketsDashboardWebSocketRequest();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 
	 */
	public void initiateWebSocketsDashboardWebSocketRequest(){
		String methodName="initiateWebSocketsDashboardWebSocketRequest";
		Session session=null;
		try {
			WebSocketContainer webSocketContainer = ContainerProvider.getWebSocketContainer();
			String endPointServerAddress =System.getProperty("GarageSaleWebSocket.endPoint.Address");
			String serverEP =  endPointServerAddress+"/garageSaleDashboardEndPoint/webSocketsMetrics";
			URI serverEndpointURI =null;
			serverEndpointURI = new URI(String.format(serverEP));
			session =webSocketContainer.connectToServer(WebSocketsDashboardClientEndpoint.class, serverEndpointURI);
			jsfWSCJsonBean.setWebSocketsJsonDashboardData(WebSocketsDataBean.getWebSocketsDashboardData());
		} catch (Exception e) {
        	try {
        		if(session!=null){
        			session.close();
        		}
        		session=null;
    		} catch (IOException io) {
    			io.printStackTrace();
    		}
			e.printStackTrace();
		}

		
	}
	
	public String getProductDemoVideo(){
		String methodName="upload";
		String outCome="readGarageSaleDemoVideoFileStatus";
		Session session=null;
		try {
			//ExternalContext externalContext= FacesContext.getCurrentInstance().getExternalContext();
			//HttpServletRequest request = (HttpServletRequest)externalContext.getRequest();
			//String fileName=request.getParameter("fileName");
			WebSocketContainer webSocketContainer = ContainerProvider.getWebSocketContainer();
			String endPointServerAddress =System.getProperty("GarageSaleWebSocket.endPoint.Address");
			String serverEP =  endPointServerAddress+"/garageSaleProductDemosWebSocketEndpoint/demoRequest";
			URI serverEndpointURI =null;
			serverEndpointURI = new URI(String.format(serverEP));
			
			//ServletContext context = (ServletContext)externalContext.getContext();
			//String path=context.getRealPath("resources/videos");
			session =webSocketContainer.connectToServer(GarageSaleDemoVideoClientEndpoint.class, serverEndpointURI);
        	/*if(garageSaleProdctVideosCacheBean.getPublishProductVideos()==null || garageSaleProdctVideosCacheBean.getPublishProductVideos().size()==0){
        		ConcurrentHashMap<String,ByteBuffer> videosMap= GarageSaleWebSocketsUtil.loadVideosFromFileSystem(path);
        		garageSaleProdctVideosCacheBean.setPublishProductVideos(videosMap);
        	}
        	else if(garageSaleProdctVideosCacheBean.getProductDemoVideosMap()==null || garageSaleProdctVideosCacheBean.getProductDemoVideosMap().size()==0){
        		ConcurrentHashMap<String,ByteBuffer> videosMap= GarageSaleWebSocketsUtil.loadVideosFromFileSystem(path);
        		garageSaleProdctVideosCacheBean.setProductDemoVideosMap(videosMap);
        	}
        	logger.logp(Level.FINE, className, methodName, "The fileName is :  "+productUploadManagedBean.getFileName());        	
        	ByteBuffer videoFile=garageSaleProdctVideosCacheBean.getPublishProductVideos().get(productUploadManagedBean.getFileName());
        	ByteBuffer videoFileCopy= videoFile.duplicate();
        	
        	if(videoFileCopy==null){
            	logger.logp(Level.SEVERE, className, methodName, "The videos file in map is : NULL");
        	}
        	videoFileCopy.position(0);
        	logger.logp(Level.FINE, className, methodName, "The Video file size from client :  "+videoFileCopy.array().length);
        	//session.setMaxBinaryMessageBufferSize(videoFileCopy.array().length);
        	logger.logp(Level.FINE, className, methodName, "videoFile position: " + videoFileCopy.position() + " limit: " + videoFileCopy.limit());
        	*/
           	session.getBasicRemote().sendText(productUploadManagedBean.getFileName());
           	productDemoBean.setReadStatus("Your read request submitted successfully");
           	
           	//externalContext.getRequestMap().put("videoFile", videoFileCopy);
           	//jsfWSCJsonBean.setVideoFile(videoFileCopy);
		} catch (Exception e) {
        	try {
        		if(session!=null){
        			session.close();
        		}
        		session=null;
    		} catch (IOException io) {
    			io.printStackTrace();
    		}
			e.printStackTrace();
			outCome="error";
		}
		return outCome;
	}
	
}

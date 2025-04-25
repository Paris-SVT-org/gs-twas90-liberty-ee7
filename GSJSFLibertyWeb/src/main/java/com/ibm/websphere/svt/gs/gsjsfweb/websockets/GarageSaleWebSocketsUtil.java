package com.ibm.websphere.svt.gs.gsjsfweb.websockets;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;
import com.ibm.websphere.svt.gs.inventory.InventoryOnSaleWrapper;
import com.ibm.websphere.svt.gs.tax.entity.Inventory;

/**
 * 
 * @author JAGRAJ
 *
 */
public class GarageSaleWebSocketsUtil {
	
	private static String componentName = "com.ibm.websphere.svt.gs.gsjsfweb.websockets";
	private static Logger logger = Logger.getLogger(componentName);
	private static String className = GarageSaleWebSocketsUtil.class.getName();
	private static List<Inventory> inventoryList;
	private static boolean enableGSMetrics=false;
	private static String garageSaleVideosLocation=null;
	private static boolean enableConcurrency = false;
	//private static int refreshInterval=1;
	private static AtomicLong ioErrorsCounter = new AtomicLong();
	private static long currentIoErrorsCounter =0;

	
	static {
			garageSaleVideosLocation=System.getProperty("GarageSale.Product.Videos.Dir.Location");
			setGarageSaleVideosLocation(garageSaleVideosLocation);

			/*refreshInterval=Integer.parseInt(System.getProperty("GarageSale.WebSockets.refresh.interval"));
			setRefreshInterval(refreshInterval);*/
			
			enableGSMetrics=(Boolean.parseBoolean(System.getProperty("GarageSale.Enable.Dashboard.Metrics")));
			setEnableGSMetrics(enableGSMetrics);
			
			enableConcurrency = (Boolean.parseBoolean(System.getProperty("GarageSale.Enable.WebSockets.Concurrency")));
			setEnableConcurrency(enableConcurrency);
	}

	public static List<Inventory> getInventoryList() {
		return inventoryList;
	}
	public static void setInventoryList(List<Inventory> inventoryList) {
		GarageSaleWebSocketsUtil.inventoryList = inventoryList;
	}
	public static ArrayList<InventoryOnSaleWrapper> getInventoryOnSaleList(List<Inventory> inventoryList ) throws Exception{
		
		String methodName="getInventoryOnSaleList";
		logger.logp(Level.FINE, className, methodName, "Inside getInventoryOnSaleList");
		ArrayList<InventoryOnSaleWrapper> inventoryOnSaleArrayList = new ArrayList<InventoryOnSaleWrapper>();
		if(inventoryList!=null && inventoryList.size()>0){
			Iterator<Inventory> inventoryIterator= inventoryList.iterator();
			while(inventoryIterator.hasNext()){
				Inventory inventory = inventoryIterator.next();
				
				InventoryOnSaleWrapper inventoryOnSaleWrapper= new InventoryOnSaleWrapper();
				inventoryOnSaleWrapper.setCategoryName(inventory.getCategoryName());
				inventoryOnSaleWrapper.setMfgName(inventory.getMfgName());
				inventoryOnSaleWrapper.setItemID(inventory.getItemId());
				inventoryOnSaleWrapper.setUnitPrice(inventory.getUnitPrice());
				float salePrice=0.00f;
				salePrice= inventory.getUnitPrice()- (inventory.getUnitPrice()*0.20f);
				inventoryOnSaleWrapper.setSalePrice(salePrice);
				inventoryOnSaleArrayList.add(inventoryOnSaleWrapper);
				
			}
		}
		return inventoryOnSaleArrayList;
	}
	/**
	 * 
	 * @param inventoryOnSaleWrapper
	 * @return
	 */
	public static JSONObject getInventoryOnSaleJSONObject(InventoryOnSaleWrapper inventoryOnSaleWrapper){
		String methodName="getInventoryOnSaleJSONObject";
		logger.logp(Level.FINE, className, methodName, "Inside getInventoryOnSaleJSONObject");
		JSONObject inventoryOnSaleJSONObject= new JSONObject();
		inventoryOnSaleJSONObject.put("categoryName", inventoryOnSaleWrapper.getCategoryName());
		inventoryOnSaleJSONObject.put("description", inventoryOnSaleWrapper.getDescription());
		inventoryOnSaleJSONObject.put("inventorySold", inventoryOnSaleWrapper.getInventorySold());
		inventoryOnSaleJSONObject.put("itemID", inventoryOnSaleWrapper.getItemID());
		inventoryOnSaleJSONObject.put("mfgName", inventoryOnSaleWrapper.getMfgName());
		inventoryOnSaleJSONObject.put("unitPrice", inventoryOnSaleWrapper.getUnitPrice());
		inventoryOnSaleJSONObject.put("salePrice", inventoryOnSaleWrapper.getSalePrice());
		
		return inventoryOnSaleJSONObject;
	}
	
	/**
	 * 
	 * @param inventoryOnSaleArrayList
	 * @return
	 * @throws Exception
	 */
	public static JSONArray getInventoryOnSaleJSONArray(List<InventoryOnSaleWrapper> inventoryOnSaleArrayList) throws Exception{
		String methodName="getInventoryOnSaleJSONArray";
		logger.logp(Level.FINE, className, methodName, "Inside getInventoryOnSaleJSONArray");
		JSONArray inventoryOnSaleJSONArray= new JSONArray();
		if(inventoryOnSaleArrayList!=null && inventoryOnSaleArrayList.size()>0){
			for(InventoryOnSaleWrapper inventoryOnSaleWrapper:inventoryOnSaleArrayList){
				inventoryOnSaleJSONArray.add(getInventoryOnSaleJSONObject(inventoryOnSaleWrapper));
			}
		}
		
		return inventoryOnSaleJSONArray;
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<InventoryOnSaleWrapper> getPartialList(List<InventoryOnSaleWrapper> inventoryOnSaleList) throws Exception{
		String methodName="getPartialList";
		logger.logp(Level.FINE, className, methodName, "Inside getPartialList");
		ArrayList<InventoryOnSaleWrapper> partialList = new ArrayList<InventoryOnSaleWrapper>();
		//ArrayList<InventoryOnSaleWrapper> inventoriesOnSaleList=getInventoryOnSaleList(inventoryList);
		int inventorySize=inventoryOnSaleList.size();
		Random random= new Random();
		int productVideosCacheSize = Integer.parseInt(System.getProperty("GarageSale.Product.Videos.Cache.Size"));		
		if(inventorySize!=0 && inventorySize>productVideosCacheSize){
			for(int i=0;i<productVideosCacheSize;i++){
				partialList.add(inventoryOnSaleList.get(random.nextInt(inventorySize)));
			}
			
		}
		return partialList;
	}
	
    public static GarageSaleWebSocketSessionBean getGarageSaleWebSocketSessionBean(){
    	String methodName="getPartialList";
    	logger.logp(Level.FINE, className, methodName, "Inside getGarageSaleWebSocketSessionBean");
    	InitialContext initialContext;
    	GarageSaleWebSocketSessionBean garageSaleWebSocketSessionBean=null;
		try {
				initialContext = new InitialContext();
				BeanManager beanManager=(BeanManager)initialContext.lookup("java:comp/BeanManager");
				Bean<GarageSaleWebSocketSessionBean> bean =(Bean<GarageSaleWebSocketSessionBean>)beanManager.getBeans(GarageSaleWebSocketSessionBean.class).iterator().next();
				CreationalContext<GarageSaleWebSocketSessionBean> ctx= beanManager.createCreationalContext(bean);
				garageSaleWebSocketSessionBean=(GarageSaleWebSocketSessionBean)beanManager.getReference(bean, GarageSaleWebSocketSessionBean.class, ctx);
	 		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return garageSaleWebSocketSessionBean;
    }
    
    /**
     * 
     * @return
     */
    public static GarageSaleDashboardApplicationScoppedBean getGarageSaleDashboardApplicationScoppedBean(){
    	String methodName="getPartialList";
    	logger.logp(Level.FINE, className, methodName, "Inside getGarageSaleWebSocketSessionBean");
    	InitialContext initialContext;
    	GarageSaleDashboardApplicationScoppedBean garageSaleDashboardApplicationScoppedBean=null;
		try {
				initialContext = new InitialContext();
				BeanManager beanManager=(BeanManager)initialContext.lookup("java:comp/BeanManager");
				Bean<GarageSaleDashboardApplicationScoppedBean> bean =(Bean<GarageSaleDashboardApplicationScoppedBean>)beanManager.getBeans(GarageSaleDashboardApplicationScoppedBean.class).iterator().next();
				CreationalContext<GarageSaleDashboardApplicationScoppedBean> ctx= beanManager.createCreationalContext(bean);
				garageSaleDashboardApplicationScoppedBean=(GarageSaleDashboardApplicationScoppedBean)beanManager.getReference(bean, GarageSaleDashboardApplicationScoppedBean.class, ctx);
	 		} catch (NamingException e) {
			e.printStackTrace();
		}
		return garageSaleDashboardApplicationScoppedBean;
    }
    
    
    /**
     * 
     * @param directoryLocation
     * @param fileName
     * @return
     * @throws Exception
     */
    public static ByteBuffer readFileContent(String directoryLocation,String fileName) throws Exception{
    	
    	String methodName="readFileContent(String directoryLocation,String fileName)";
 	    logger.logp(Level.FINE, className, methodName, "readFileContent(String directoryLocation,String fileName)");
    			
    	DataInputStream dataInputStream=null;
    	ByteBuffer byteBuffer=null;
        try {
        	String fullFilePath = directoryLocation+System.getProperty("file.separator")+fileName;
        	logger.logp(Level.FINE, className, methodName, "readFileContent(String directoryLocation,String fileName) Fullpath" +fullFilePath);
        	File file = new File(fullFilePath);
        	byte[] data = new byte[(int)file.length()];
        	dataInputStream= new DataInputStream(new FileInputStream(file));
        	dataInputStream.readFully(data);
        	byteBuffer = ByteBuffer.wrap(data);
    		return byteBuffer;
        } catch (Exception e) {
			e.printStackTrace();
			
		}
        finally{
        	if (dataInputStream !=null){
        		try {
        			dataInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        }
		return byteBuffer;
    }
    
    /**
     * 
     * @param directoryLocation
     * @return
     * @throws Exception
     */
    public static ConcurrentHashMap<String, ByteBuffer> loadVideosFromFileSystem(String directoryLocation) throws Exception{
    	
    	String methodName="loadVideosFromFileSystem(String directoryLocation)";
    	logger.logp(Level.FINE, className, methodName, "Inside loadVideosFromFileSystem(String directoryLocation) ");
    	ConcurrentHashMap<String, ByteBuffer> productDemosCacheMap= new  ConcurrentHashMap<String, ByteBuffer>();
        	ArrayList<String> fileNames = getFileNames(directoryLocation);
        	int productVideosCacheSize = Integer.parseInt(System.getProperty("GarageSale.Product.Videos.Cache.Size"));
        	ConcurrentHashMap<String, ByteBuffer> totalFilesInDir= new  ConcurrentHashMap<String, ByteBuffer>();
        	Iterator<String> iterator= fileNames.iterator();
        	while(iterator.hasNext()){
        		String fileName = iterator.next();
            	logger.logp(Level.FINE, className, methodName, "Inside loadVideosFromFileSystem(String directoryLocation) While"+fileName);
        		totalFilesInDir.put(fileName, readFileContent(directoryLocation, fileName));
        	}
        	int index=1;
        	for(int i=0;i<productVideosCacheSize;i=i+totalFilesInDir.size()){
            	logger.logp(Level.FINE, className, methodName, "Inside loadVideosFromFileSystem(String directoryLocation) for loop index is "+i);
        		
        		Set<String> keys = totalFilesInDir.keySet();
        		Iterator<String> keysIterator=keys.iterator();
        		while(keysIterator.hasNext()){
            		String fileName= "ProductDemo"+index+".mp4";
        			productDemosCacheMap.put(fileName, totalFilesInDir.get(keysIterator.next()));
        			index=index+1;
        		}
        	}
        	
        return productDemosCacheMap;
    }
    
    
    /**
     * 
     * @param directoryLocation
     * @return
     * @throws Exception
     */
    public static ArrayList<String> getFileNames(String directoryLocation) throws Exception {
    	String methodName = "getFileNames(String directoryLocation)";
    	logger.logp(Level.FINE, className, methodName, "Inside getFileNames(String directoryLocation)");
    	
    	ArrayList<String> fileNames = new ArrayList<String>();
    	if (directoryLocation !=null){
    		final File folder= new File(directoryLocation);
    		
    		if(folder.isDirectory()){
    	    	for (final File fileEntry : folder.listFiles()) {
    	    		if (fileEntry.isFile()) {
  	    	          String cuurentFileName = fileEntry.getName();
  	    	          logger.logp(Level.FINE, className, methodName, "Inside getFileNames(String directoryLocation): "+cuurentFileName);
  	    	          fileNames.add(cuurentFileName);
    	    		}
    	    	}
    			
    		}
    	}
    	return fileNames;
    }
    
    /**
     * 
     * @param garageSaleDashboardApplicationScoppedBean
     * @param key
     * @param counterType
     * @throws Exception
     */
    public static void updateCounters(GarageSaleDashboardApplicationScoppedBean garageSaleDashboardApplicationScoppedBean, String key,String counterType,long openSessionsCount ) throws Exception {
    	
    	String methodName = "updateCounters";
    	logger.logp(Level.FINE, className, methodName, "Inside updateCounters");
		enableGSMetrics=(Boolean.parseBoolean(System.getProperty("GarageSale.Enable.Dashboard.Metrics")));

    	if(enableGSMetrics){
        	ConcurrentHashMap<String,GarageSaleWebSocketsDashboardBean> currentMap=garageSaleDashboardApplicationScoppedBean.getGarageSaleWebSocketsDashboardMap();
	        	if(currentMap!=null){
	            	GarageSaleWebSocketsDashboardBean currentMapEntry =currentMap.get(key);
	            	
	            	if(currentMapEntry ==null){
	            		currentMapEntry=garageSaleDashboardApplicationScoppedBean.getDashboardWebSocketsBean();
	            		currentMapEntry.setWebSocketEndpointName(key);
	            		currentMap.put(key, currentMapEntry);
	            	}
	            	if(currentMapEntry !=null){
	            		if(counterType !=null && counterType.equals("onOpen")){
	            			currentMapEntry.setOnOpenCount(currentMapEntry.getOnOpenAtomicLong().incrementAndGet());
	            			currentMapEntry.setOpenSessionsCount(openSessionsCount);
	            		}
	            		else if(counterType !=null && counterType.equals("onMessage")){
	            			currentMapEntry.setOnMessageCount(currentMapEntry.getOnMessageAtomicLong().incrementAndGet());
	            			currentMapEntry.setOpenSessionsCount(openSessionsCount);
	            		}
	            		else if(counterType !=null && counterType.equals("onClose")){
	            			currentMapEntry.setOpenSessionsCount(openSessionsCount);
	            			currentMapEntry.setOnCLoseCount(currentMapEntry.getOnCloseAtomicLong().incrementAndGet());
	            		}
	            		else if(counterType !=null && counterType.equals("onError")){
	            			currentMapEntry.setOnErrorCount(currentMapEntry.getOnErrorAtomicLong().incrementAndGet());
	            			currentMapEntry.setOpenSessionsCount(openSessionsCount);
	            		}
	            		else if(counterType !=null && counterType.equals("misMatch")){
	            			currentMapEntry.setDataMistMatchCount(currentMapEntry.getDataMistMatchAtomicLong().incrementAndGet());
	            			currentMapEntry.setOpenSessionsCount(openSessionsCount);
	            		}
	            		else if(counterType !=null && counterType.equals("openSessions")){
	            			currentMapEntry.setOpenSessionsCount(openSessionsCount);
	            		}
	            	}
	        	}
        		
        	}
    }
	/**
	 * @return the garageSaleVideosLocation
	 */
	public static String getGarageSaleVideosLocation() {
		return garageSaleVideosLocation;
	}
	/**
	 * @param garageSaleVideosLocation the garageSaleVideosLocation to set
	 */
	public static void setGarageSaleVideosLocation(String garageSaleVideosLocation) {
		GarageSaleWebSocketsUtil.garageSaleVideosLocation = garageSaleVideosLocation;
	}
	/**
	 * @return the enableGSMetrics
	 */
	public static boolean isEnableGSMetrics() {
		return enableGSMetrics;
	}
	/**
	 * @param enableGSMetrics the enableGSMetrics to set
	 */
	public static void setEnableGSMetrics(boolean enableGSMetrics) {
		GarageSaleWebSocketsUtil.enableGSMetrics = enableGSMetrics;
	}
	/**
	 * @return the ioErrorsCounter
	 */
	public static AtomicLong getIoErrorsCounter() {
		return ioErrorsCounter;
	}
	/**
	 * @param ioErrorsCounter the ioErrorsCounter to set
	 */
	public static void setIoErrorsCounter(AtomicLong ioErrorsCounter) {
		GarageSaleWebSocketsUtil.ioErrorsCounter = ioErrorsCounter;
	}
	/**
	 * @return the currentIoErrorsCounter
	 */
	public static long getCurrentIoErrorsCounter() {
		return currentIoErrorsCounter;
	}
	/**
	 * @param currentIoErrorsCounter the currentIoErrorsCounter to set
	 */
	public static void setCurrentIoErrorsCounter(long currentIoErrorsCounter) {
		GarageSaleWebSocketsUtil.currentIoErrorsCounter = currentIoErrorsCounter;
	}
	/**
	 * @return the enableConcurrency
	 */
	public static boolean isEnableConcurrency() {
		return enableConcurrency;
	}
	/**
	 * @param enableConcurrency the enableConcurrency to set
	 */
	public static void setEnableConcurrency(boolean enableConcurrency) {
		GarageSaleWebSocketsUtil.enableConcurrency = enableConcurrency;
	}
	/**
	 * @return the refreshInterval
	 */
	/*public static int getRefreshInterval() {
		
		return refreshInterval;
	}*/
	/**
	 * @param refreshInterval the refreshInterval to set
	 */
	/*public static void setRefreshInterval(int refreshInterval) {
		GarageSaleWebSocketsUtil.refreshInterval = refreshInterval;
	}*/
    
    
}

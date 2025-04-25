package com.ibm.websphere.svt.gs.gsjsfweb.utils;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author JAGRAJ
 *
 */
public class GarageSaleLargeSessionUtil {
	
	public static int LS_TEST_MAX_SESSION_SIZE=60;
	public static int LS_TEST_USER_RANGE_MAX=2_00;
	public static String LS_TEST_ENABLE="false";


	private static String componentName = "com.ibm.websphere.svt.gs.gsjsfweb.utils";
	private static Logger logger = Logger.getLogger(componentName);
	private static String className = GarageSaleLargeSessionUtil.class.getName();
	
	public static final String SESSION_OBJECT_SIZE_MAX="garageSale.session.max.size";
	public static final String GARAGESALE_LARGESE_SESSION_USER_RANGE="garageSale.user.max.size";
	public static final String GARAGESALE_LARGE_SESSION_TEST_ENABLE="garageSale.large.session.test.enable";
	
	private static final long   largeString05MBlength=524_288;
	private static final long   largeString1MBLength =1_048_576;
	private static final long   largeString15MBLength =1572864;
	
	private static String  largeString05MB="";
	private static String  largeString1MB="";
	private static String  largeString15MB="";
	private static ArrayList<Float> memorySizeList = new ArrayList<Float>();
	
	static {
		/**try{
			initializeLimits();
			if(LS_TEST_ENABLE!=null && LS_TEST_ENABLE.equalsIgnoreCase("true")){
				largeString05MB=getLargeString(largeString05MBlength);
				largeString1MB=getLargeString(largeString1MBLength);
				largeString15MB=getLargeString(largeString15MBLength);
				memorySizeList.add(new Float(0.5f));
				memorySizeList.add(new Float(1.0f));
				memorySizeList.add(new Float(1.5f));
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}*/
	}
	
	
	/**
	 * 
	 * @param length
	 * @return
	 */
	public static String getLargeString(long length) throws Exception{
		String methodName="getLargeString";
		logger.logp(Level.FINEST, className, methodName, "Called getLargeString method" );
		
		StringBuffer buffer = new StringBuffer();
		if(length!=0 && length>0) {
			for(int i=0; i<length; i++) {
						buffer.append("A");
			}
		}
		
		return buffer.toString();
	}
	
	/**
	 * 
	 * @param max
	 * @return
	 */
	public static int getRandomNumberForRange(int max) throws Exception{
		String methodName="getRandomNumberForRange";
		logger.logp(Level.FINEST, className, methodName, "Called getLargeString method" );
		Random random = new Random();
		return random.nextInt(max)+1;
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public static LargeSessionRangeBean getLargeSessionRangeBean() throws Exception{
		String methodName="getLargeSessionRangeBean";
		logger.logp(Level.FINEST, className, methodName, "Called getLargeSessionRangeBean method" );
		LargeSessionRangeBean rangeBean = new LargeSessionRangeBean();
		int currentMAXSize = getRandomNumberForRange(LS_TEST_MAX_SESSION_SIZE)+1;
		Float currentObjectSize = memorySizeList.get(getRandomNumberForRange(memorySizeList.size())-1);
		int range = (int) (currentMAXSize/currentObjectSize.floatValue());
		rangeBean.setCounter(range);
		rangeBean.setCurrentObjectSize(currentObjectSize);
		rangeBean.setObjectSize(currentObjectSize.floatValue());
		return rangeBean;
	}
	
	/**
	 * 
	 * @param objectSize
	 * @return
	 * @throws Exception
	 */
	public static String getLargeObjectBySize(float objectSize) throws Exception{
		String methodName="getLargeObjectBySize";
		logger.logp(Level.FINEST, className, methodName, "Called getLargeSessionRangeBean method" );
		String largeObject="";
		if(objectSize > 0){
			if(objectSize==0.5f){
				largeObject= largeString05MB;
			}
			else if(objectSize==1.0f){
				largeObject=largeString1MB;
			}
			else if(objectSize==1.5f){
				largeObject=largeString15MB;
			}
		}
		
		return largeObject;
	}

	/**
	 * @return the largestring05mblength
	 */
	public static long getLargestring05mblength() {
		return largeString05MBlength;
	}

	/**
	 * @return the largestring1mblength
	 */
	public static long getLargestring1mblength() {
		return largeString1MBLength;
	}

	/**
	 * @return the largestring15mblength
	 */
	public static long getLargestring15mblength() {
		return largeString15MBLength;
	}
	
	/**
	 * 
	 * @throws Exception
	 */
/**	public static void initializeLimits() throws Exception{
		String methodName="initializeLimits";
		logger.logp(Level.FINEST, className, methodName, "Called initializeLimits method" );
		LS_TEST_ENABLE=AccessPropFile.getProperty(GARAGESALE_LARGE_SESSION_TEST_ENABLE);
		
		if(LS_TEST_ENABLE!=null && LS_TEST_ENABLE.equalsIgnoreCase("true")){
			LS_TEST_MAX_SESSION_SIZE=Integer.parseInt(AccessPropFile.getProperty(SESSION_OBJECT_SIZE_MAX));
			LS_TEST_USER_RANGE_MAX=Integer.parseInt(AccessPropFile.getProperty(GARAGESALE_LARGESE_SESSION_USER_RANGE));
		}
		else{
			LS_TEST_ENABLE="false";
		}
		
	}*/
	
}

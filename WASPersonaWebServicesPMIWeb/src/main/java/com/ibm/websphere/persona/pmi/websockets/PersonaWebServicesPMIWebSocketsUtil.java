/**
 * 
 */
package com.ibm.websphere.persona.pmi.websockets;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author JAGRAJ
 *
 */
public class PersonaWebServicesPMIWebSocketsUtil {
	private static boolean enableConcurrency = false;
	//private static int refreshInterval=1;
	private static AtomicLong ioErrorsCounter = new AtomicLong();
	private static long currentIoErrorsCounter =0;
	private static AtomicLong illegalStateCounter = new AtomicLong();
	private static long currentIllegalStateCount =0;
	
	
	
	static {
			/*refreshInterval=Integer.parseInt(System.getProperty("GarageSale.WebSockets.refresh.interval"));
			setRefreshInterval(refreshInterval);*/
			
			enableConcurrency = (Boolean.parseBoolean(System.getProperty("GarageSale.Enable.WebSockets.Concurrency")));
			setEnableConcurrency(enableConcurrency);
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
		PersonaWebServicesPMIWebSocketsUtil.enableConcurrency = enableConcurrency;
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
		PersonaWebServicesPMIWebSocketsUtil.refreshInterval = refreshInterval;
	}*/

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
		PersonaWebServicesPMIWebSocketsUtil.ioErrorsCounter = ioErrorsCounter;
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
		PersonaWebServicesPMIWebSocketsUtil.currentIoErrorsCounter = currentIoErrorsCounter;
	}

	/**
	 * @return the illegalStateCounter
	 */
	public static AtomicLong getIllegalStateCounter() {
		return illegalStateCounter;
	}

	/**
	 * @param illegalStateCounter the illegalStateCounter to set
	 */
	public static void setIllegalStateCounter(AtomicLong illegalStateCounter) {
		PersonaWebServicesPMIWebSocketsUtil.illegalStateCounter = illegalStateCounter;
	}

	/**
	 * @return the currentIllegalStateCount
	 */
	public static long getCurrentIllegalStateCount() {
		return currentIllegalStateCount;
	}

	/**
	 * @param currentIllegalStateCount the currentIllegalStateCount to set
	 */
	public static void setCurrentIllegalStateCount(long currentIllegalStateCount) {
		PersonaWebServicesPMIWebSocketsUtil.currentIllegalStateCount = currentIllegalStateCount;
	}
	
	


}

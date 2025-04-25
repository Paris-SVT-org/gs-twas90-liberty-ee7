/**
 * 
 */
package com.ibm.websphere.svt.gs.gsjsfweb.websockets;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

import com.ibm.json.java.JSONObject;

/**
 * @author JAGRAJ
 *
 */
public class GarageSaleWebSocketsDashboardBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3491610349128218132L;
	
	private String webSocketEndpointName;
	private AtomicLong openSessionsAtomicLong;
	private AtomicLong onMessageAtomicLong;
	private AtomicLong onCloseAtomicLong;
	private AtomicLong onErrorAtomicLong;
	private AtomicLong dataMistMatchAtomicLong;
	private AtomicLong onOpenAtomicLong;
	private AtomicLong ioErrorsCounter;
	
	private long onOpenCount;
	private long openSessionsCount;
	private long onMessageCount;
	private long onCLoseCount;
	private long onErrorCount;
	private long dataMistMatchCount;

	private long currentIoErrorsCounter =0;

	/**
	 * @return the onOpenAtomicLong
	 */
	public AtomicLong getOnOpenAtomicLong() {
		return onOpenAtomicLong;
	}
	/**
	 * @param onOpenAtomicLong the onOpenAtomicLong to set
	 */
	public void setOnOpenAtomicLong(AtomicLong onOpenAtomicLong) {
		this.onOpenAtomicLong = onOpenAtomicLong;
	}
	/**
	 * @return the onOpenCount
	 */
	public long getOnOpenCount() {
		return onOpenCount;
	}
	/**
	 * @param onOpenCount the onOpenCount to set
	 */
	public void setOnOpenCount(long onOpenCount) {
		this.onOpenCount = onOpenCount;
	}
	/**
	 * @return the webSocketEndpointName
	 */
	public String getWebSocketEndpointName() {
		return webSocketEndpointName;
	}
	/**
	 * @param webSocketEndpointName the webSocketEndpointName to set
	 */
	public void setWebSocketEndpointName(String webSocketEndpointName) {
		this.webSocketEndpointName = webSocketEndpointName;
	}
	/**
	 * @return the openSessionsAtomicLong
	 */
	public AtomicLong getOpenSessionsAtomicLong() {
		return openSessionsAtomicLong;
	}
	/**
	 * @param openSessionsAtomicLong the openSessionsAtomicLong to set
	 */
	public void setOpenSessionsAtomicLong(AtomicLong openSessionsAtomicLong) {
		this.openSessionsAtomicLong = openSessionsAtomicLong;
	}
	/**
	 * @return the onMessageAtomicLong
	 */
	public AtomicLong getOnMessageAtomicLong() {
		return onMessageAtomicLong;
	}
	/**
	 * @param onMessageAtomicLong the onMessageAtomicLong to set
	 */
	public void setOnMessageAtomicLong(AtomicLong onMessageAtomicLong) {
		this.onMessageAtomicLong = onMessageAtomicLong;
	}
	/**
	 * @return the onCloseAtomicLong
	 */
	public AtomicLong getOnCloseAtomicLong() {
		return onCloseAtomicLong;
	}
	/**
	 * @param onCloseAtomicLong the onCloseAtomicLong to set
	 */
	public void setOnCloseAtomicLong(AtomicLong onCloseAtomicLong) {
		this.onCloseAtomicLong = onCloseAtomicLong;
	}
	/**
	 * @return the onErrorAtomicLong
	 */
	public AtomicLong getOnErrorAtomicLong() {
		return onErrorAtomicLong;
	}
	/**
	 * @param onErrorAtomicLong the onErrorAtomicLong to set
	 */
	public void setOnErrorAtomicLong(AtomicLong onErrorAtomicLong) {
		this.onErrorAtomicLong = onErrorAtomicLong;
	}
	/**
	 * @return the dataMistMatchAtomicLong
	 */
	public AtomicLong getDataMistMatchAtomicLong() {
		return dataMistMatchAtomicLong;
	}
	/**
	 * @param dataMistMatchAtomicLong the dataMistMatchAtomicLong to set
	 */
	public void setDataMistMatchAtomicLong(AtomicLong dataMistMatchAtomicLong) {
		this.dataMistMatchAtomicLong = dataMistMatchAtomicLong;
	}
	/**
	 * @return the openSessionsCount
	 */
	public long getOpenSessionsCount() {
		return openSessionsCount;
	}
	/**
	 * @param openSessionsCount the openSessionsCount to set
	 */
	public void setOpenSessionsCount(long openSessionsCount) {
		this.openSessionsCount = openSessionsCount;
	}
	/**
	 * @return the openSessionsOnMessageCount
	 */
	public long getOnMessageCount() {
		return onMessageCount;
	}
	/**
	 * @param openSessionsOnMessageCount the openSessionsOnMessageCount to set
	 */
	public void setOnMessageCount(long onMessageCount) {
		this.onMessageCount = onMessageCount;
	}
	/**
	 * @return the onCLoseCount
	 */
	public long getOnCLoseCount() {
		return onCLoseCount;
	}
	/**
	 * @param onCLoseCount the onCLoseCount to set
	 */
	public void setOnCLoseCount(long onCLoseCount) {
		this.onCLoseCount = onCLoseCount;
	}
	/**
	 * @return the onErrorCount
	 */
	public long getOnErrorCount() {
		return onErrorCount;
	}
	/**
	 * @param onErrorCount the onErrorCount to set
	 */
	public void setOnErrorCount(long onErrorCount) {
		this.onErrorCount = onErrorCount;
	}
	/**
	 * @return the dataMistMatchCount
	 */
	public long getDataMistMatchCount() {
		return dataMistMatchCount;
	}
	/**
	 * @param dataMistMatchCount the dataMistMatchCount to set
	 */
	public void setDataMistMatchCount(long dataMistMatchCount) {
		this.dataMistMatchCount = dataMistMatchCount;
	}

	
	
	/**
	 * @return the ioErrorsCounter
	 */
	public  AtomicLong getIoErrorsCounter() {
		return ioErrorsCounter;
	}
	/**
	 * @param ioErrorsCounter the ioErrorsCounter to set
	 */
	public  void setIoErrorsCounter(AtomicLong ioErrorsCounter) {
		this.ioErrorsCounter = ioErrorsCounter;
	}
	/**
	 * @return the currentIoErrorsCounter
	 */
	public long getCurrentIoErrorsCounter() {
		return currentIoErrorsCounter;
	}
	/**
	 * @param currentIoErrorsCounter the currentIoErrorsCounter to set
	 */
	public void setCurrentIoErrorsCounter(long currentIoErrorsCounter) {
		this.currentIoErrorsCounter = currentIoErrorsCounter;
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GarageSaleWebSocketsDashboardBean [webSocketEndpointName="
				+ webSocketEndpointName + ", openSessionsAtomicLong="
				+ openSessionsAtomicLong + ", onMessageAtomicLong="
				+ onMessageAtomicLong + ", onCloseAtomicLong="
				+ onCloseAtomicLong + ", onErrorAtomicLong="
				+ onErrorAtomicLong + ", dataMistMatchAtomicLong="
				+ dataMistMatchAtomicLong + ", onOpenAtomicLong="
				+ onOpenAtomicLong + ", onOpenCount=" + onOpenCount
				+ ", openSessionsCount=" + openSessionsCount
				+ ", onMessageCount=" + onMessageCount + ", onCLoseCount="
				+ onCLoseCount + ", onErrorCount=" + onErrorCount
				+ ", dataMistMatchCount=" + dataMistMatchCount
				+ ", currentIoErrorsCounter=" + currentIoErrorsCounter + "]";
	}
	/**
	 * 
	 * @return
	 */
	
	public JSONObject getJSONObject(){
		
		JSONObject jsonObject= new JSONObject();
		jsonObject.put("webSocketEndpointName", this.webSocketEndpointName);
		jsonObject.put("onOpenCount", this.onOpenCount);
		jsonObject.put("openSessionsCount", this.openSessionsCount);
		jsonObject.put("onMessageCount", this.onMessageCount);
		jsonObject.put("onCLoseCount", this.onCLoseCount);
		jsonObject.put("onErrorCount", this.onErrorCount);
		jsonObject.put("dataMistMatchCount", this.dataMistMatchCount);
		jsonObject.put("currentIoErrorsCounter", this.currentIoErrorsCounter);
		
		return jsonObject;
		
	}
	
}

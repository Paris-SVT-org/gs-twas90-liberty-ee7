/**
 * 
 */
package com.ibm.websphere.svt.gs.gsjsfweb.websockets;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 * @author JAGRAJ
 *
 */
@ManagedBean(name="jsfWSCJsonBean")
@RequestScoped
public class JsfWSCJsonBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4100782449891849036L;
	
	private String inventoryOnSaleData;
	private String onSaleContentForUploadsData;
	private String databaseJsonDashboardData;
	private String webSocketsJsonDashboardData;
	//private ByteBuffer videoFile;

	/**
	 * @return the inventoryOnSaleData
	 */
	public String getInventoryOnSaleData() {
		return inventoryOnSaleData;
	}

	/**
	 * @param inventoryOnSaleData the inventoryOnSaleData to set
	 */
	public void setInventoryOnSaleData(String inventoryOnSaleData) {
		this.inventoryOnSaleData = inventoryOnSaleData;
	}

	/**
	 * @return the onSaleContentForUploadsData
	 */
	public String getOnSaleContentForUploadsData() {
		return onSaleContentForUploadsData;
	}

	/**
	 * @param onSaleContentForUploadsData the onSaleContentForUploadsData to set
	 */
	public void setOnSaleContentForUploadsData(String onSaleContentForUploadsData) {
		this.onSaleContentForUploadsData = onSaleContentForUploadsData;
	}

	public String getDatabaseJsonDashboardData() {
		return databaseJsonDashboardData;
	}

	public void setDatabaseJsonDashboardData(String databaseJsonDashboardData) {
		this.databaseJsonDashboardData = databaseJsonDashboardData;
	}

	public String getWebSocketsJsonDashboardData() {
		return webSocketsJsonDashboardData;
	}

	public void setWebSocketsJsonDashboardData(
			String webSocketsJsonDashboardData) {
		this.webSocketsJsonDashboardData = webSocketsJsonDashboardData;
	}

	/*public ByteBuffer getVideoFile() {
		return videoFile;
	}

	public void setVideoFile(ByteBuffer videoFile) {
		this.videoFile = videoFile;
	}*/


	
}

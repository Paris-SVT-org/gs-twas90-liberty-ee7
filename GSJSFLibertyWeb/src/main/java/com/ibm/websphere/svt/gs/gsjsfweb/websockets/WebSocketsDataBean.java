/**
 * 
 */
package com.ibm.websphere.svt.gs.gsjsfweb.websockets;

/**
 * @author JAGRAJ
 *
 */
public class WebSocketsDataBean {
	
	private static String inventoryOnSaleJsonData="";
	private static String inventoryOnSaleProgJsonData="";
	private static String databaseDashboardData="";
	private static String webSocketsDashboardData="";
	

	/**
	 * @return the inventoryOnSaleJsonData
	 */
	public static String getInventoryOnSaleJsonData() {
		return inventoryOnSaleJsonData;
	}

	/**
	 * @param inventoryOnSaleJsonData the inventoryOnSaleJsonData to set
	 */
	public static void setInventoryOnSaleJsonData(String inventoryOnSaleJsonData) {
		WebSocketsDataBean.inventoryOnSaleJsonData = inventoryOnSaleJsonData;
	}

	/**
	 * @return the inventoryOnSaleProgJsonData
	 */
	public static String getInventoryOnSaleProgJsonData() {
		return inventoryOnSaleProgJsonData;
	}

	/**
	 * @param inventoryOnSaleProgJsonData the inventoryOnSaleProgJsonData to set
	 */
	public static void setInventoryOnSaleProgJsonData(
			String inventoryOnSaleProgJsonData) {
		WebSocketsDataBean.inventoryOnSaleProgJsonData = inventoryOnSaleProgJsonData;
	}

	public static String getDatabaseDashboardData() {
		return databaseDashboardData;
	}

	public static void setDatabaseDashboardData(String databaseDashboardData) {
		WebSocketsDataBean.databaseDashboardData = databaseDashboardData;
	}

	public static String getWebSocketsDashboardData() {
		return webSocketsDashboardData;
	}

	public static void setWebSocketsDashboardData(
			String webSocketsDashboardData) {
		WebSocketsDataBean.webSocketsDashboardData = webSocketsDashboardData;
	}
	
	

}

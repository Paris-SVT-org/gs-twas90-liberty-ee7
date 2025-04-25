/**
 * 
 */
package com.ibm.websphere.svt.gs.gsjsfweb.websockets;

import java.util.logging.Logger;

/**
 * @author JAGRAJ
 *
 */
//@Startup
//@Singleton
public class GarageSaleWebSocketTimerService {
	
	private static String componentName = "com.ibm.websphere.svt.gs.gsjsfweb.websockets";
	private static Logger logger = Logger.getLogger(componentName);
	private static String className = GarageSaleWebSocketTimerService.class.getName();
	
	//@Resource 
	//private TimerService timerServiceForInventory;
	//@Resource 
	//private TimerService timerServiceForDashboard;
	
	/**@PostConstruct
	public void init(){
		String methodName="init()";
		//Initialize EJB and create a Timer
    	logger.logp(Level.FINE, className, methodName, "Initialize EJB and create a Timer");
    	timerServiceForInventory.createIntervalTimer(10*1000, 10*1000, new TimerConfig("Inventory",false));
    	timerServiceForDashboard.createIntervalTimer(20*1000, 20*1000, new TimerConfig("Dashboard",false));
		
	}*/
	
	/**
	 * 
	 */
	/**
	@Timeout
	public void timerTimeout(Timer currentTimmer){
		// Refresh data only if there are active sessions for this endpoint.
		Session currentInventoryEndpointSession = InventoryOnSaleEndPoint
				.getCurrentSession();
		Session currentDashboardSession=GarageSaleDashboardEndPoint.getCurrentSession();
		if(currentTimmer!=null && currentTimmer.getInfo().equals("Inventory")){
			if (currentInventoryEndpointSession != null
					&& currentInventoryEndpointSession.getOpenSessions().size() > 0) {

						try {
							InventoryOnSaleEndPoint.refreshInventoryOnSale();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			}
			
		}
		else if(currentTimmer!=null && currentTimmer.getInfo().equals("Dashboard")){
			if(currentDashboardSession !=null && currentDashboardSession.getOpenSessions().size()>0){
				try {
					GarageSaleDashboardEndPoint.refreshDashboard();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	
			
		}
	}*/
	
}

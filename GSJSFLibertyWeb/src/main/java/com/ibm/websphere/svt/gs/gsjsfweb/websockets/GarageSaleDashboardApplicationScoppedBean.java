/**
 * 
 */
package com.ibm.websphere.svt.gs.gsjsfweb.websockets;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 * @author JAGRAJ
 *
 */
/**
 * 
 * @author JAGRAJ
 *
 */

@Named
@ApplicationScoped
public class GarageSaleDashboardApplicationScoppedBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7706936545296773041L;
	private ConcurrentHashMap<String,GarageSaleWebSocketsDashboardBean> garageSaleWebSocketsDashboardMap;
	/**
	 * @return the garageSaleWebSocketsDashboardMap
	 */
	public ConcurrentHashMap<String, GarageSaleWebSocketsDashboardBean> getGarageSaleWebSocketsDashboardMap() {
		return garageSaleWebSocketsDashboardMap;
	}
	/**
	 * @param garageSaleWebSocketsDashboardMap the garageSaleWebSocketsDashboardMap to set
	 */
	public void setGarageSaleWebSocketsDashboardMap(
			ConcurrentHashMap<String, GarageSaleWebSocketsDashboardBean> garageSaleWebSocketsDashboardMap) {
		this.garageSaleWebSocketsDashboardMap = garageSaleWebSocketsDashboardMap;
	}
	
	/**
	 * 
	 */
	@PostConstruct
	public void initializeMaps(){
		garageSaleWebSocketsDashboardMap = new ConcurrentHashMap<String,GarageSaleWebSocketsDashboardBean>();
		//garageSaleWebSocketsDashboardMap.put(GarageSaleDashboardEndPoint.class.getSimpleName()+"-databaseMetrics", getDashboardWebSocketsBean());
		//garageSaleWebSocketsDashboardMap.put(GarageSaleDashboardEndPoint.class.getSimpleName()+"-webSocketsMetrics", getDashboardWebSocketsBean());
		//garageSaleWebSocketsDashboardMap.put(GarageSaleProductDemosWebSocketEndpoint.class.getSimpleName()+"-demoRequest", getDashboardWebSocketsBean());
		//garageSaleWebSocketsDashboardMap.put(GarageSaleProductDemosWebSocketEndpoint.class.getSimpleName()+"-uploadRequest", getDashboardWebSocketsBean());
		//garageSaleWebSocketsDashboardMap.put(InventoryOnSaleEndPoint.class.getSimpleName(), getDashboardWebSocketsBean());
	}
	
	/**
	 * 
	 * @return
	 */
	public GarageSaleWebSocketsDashboardBean getDashboardWebSocketsBean() {
		GarageSaleWebSocketsDashboardBean garageSaleWebSocketsDashboardBean =new GarageSaleWebSocketsDashboardBean();
		garageSaleWebSocketsDashboardBean.setDataMistMatchAtomicLong(new AtomicLong());
		garageSaleWebSocketsDashboardBean.setOnCloseAtomicLong(new AtomicLong());
		garageSaleWebSocketsDashboardBean.setOnErrorAtomicLong(new AtomicLong());
		garageSaleWebSocketsDashboardBean.setOnMessageAtomicLong(new AtomicLong());
		garageSaleWebSocketsDashboardBean.setOpenSessionsAtomicLong(new AtomicLong());
		garageSaleWebSocketsDashboardBean.setOnOpenAtomicLong(new AtomicLong());
		garageSaleWebSocketsDashboardBean.setIoErrorsCounter(new AtomicLong());
		return garageSaleWebSocketsDashboardBean;
	}

}

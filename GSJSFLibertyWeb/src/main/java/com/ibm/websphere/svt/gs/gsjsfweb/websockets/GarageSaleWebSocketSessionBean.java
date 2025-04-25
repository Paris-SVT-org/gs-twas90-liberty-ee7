/**
 * 
 */
package com.ibm.websphere.svt.gs.gsjsfweb.websockets;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import com.ibm.websphere.svt.gs.inventory.InventoryOnSaleWrapper;
import com.ibm.websphere.svt.gs.tax.entity.Inventory;
import com.ibm.websphere.svt.gs.tax.session.InventorySession;

/**
 * @author JAGRAJ
 *
 */
@Named
@ApplicationScoped
public class GarageSaleWebSocketSessionBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1974828192873654L;
	private List<Inventory> inventoryList;
	private List<InventoryOnSaleWrapper> inventoryOnSaleList;
	private static String componentName = "com.ibm.websphere.svt.gs.gsjsfweb.websockets";
	private static Logger logger = Logger.getLogger(componentName);
	private static String className = GarageSaleWebSocketSessionBean.class.getName();

	@EJB
	private InventorySession inventorySession;
	
	public List<Inventory> getInventoryList() {
		return inventoryList;
	}

	public void setInventoryList(List<Inventory> inventoryList) {
		this.inventoryList = inventoryList;
	}
	
	public List<InventoryOnSaleWrapper> getInventoryOnSaleList() {
		return inventoryOnSaleList;
	}

	public void setInventoryOnSaleList(
			List<InventoryOnSaleWrapper> inventoryOnSaleList) {
		this.inventoryOnSaleList = inventoryOnSaleList;
	}

	@PostConstruct
	public void postConstruct(){
		String methodName="postConstruct";
		try{
			logger.logp(Level.FINE, className, methodName, "PostConstruct Initializing Data");
			if(inventoryList==null || inventoryList.size()==0){
				setInventoryList(inventorySession.getAllItemsByOrder());
			}
			if(inventoryOnSaleList==null || inventoryOnSaleList.size()==0){
				setInventoryOnSaleList(GarageSaleWebSocketsUtil.getInventoryOnSaleList(inventoryList));
			}
			
		}catch(Exception e){
			e.printStackTrace();
			logger.logp(Level.SEVERE, className, methodName, "Failed in PostConstruct operation:   "+e.getMessage());
		}
	}
	
	/**
	 * 
	 */
	@PreDestroy
	public void preDestroy(){
		String methodName="preDestroy";
		logger.logp(Level.FINE, className, methodName, "Making ShipRate and TaxRate data null.");
		setInventoryList(null);
		setInventoryOnSaleList(null);
	}
}

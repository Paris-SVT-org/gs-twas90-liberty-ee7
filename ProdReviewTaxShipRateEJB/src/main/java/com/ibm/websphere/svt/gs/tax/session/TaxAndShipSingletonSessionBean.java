package com.ibm.websphere.svt.gs.tax.session;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.TransactionManagement;

import com.ibm.websphere.svt.gs.tax.entity.ShipRate;
import com.ibm.websphere.svt.gs.tax.entity.TaxRate;

/**
 * Session Bean implementation class TaxAndShipSingletonSessionBean
 */
@Singleton
@LocalBean
@TransactionManagement(javax.ejb.TransactionManagementType.CONTAINER)
public class TaxAndShipSingletonSessionBean {
	
	private static String componentName = "com.ibm.websphere.svt.gs.tax.session";
	private static Logger logger = Logger.getLogger(componentName);
	private static String className = TaxAndShipSingletonSessionBean.class.getName();
	private List<TaxRate> taxRateList=null;
	private List<ShipRate> shipRateList=null;
	
	@EJB 
	public TaxRateSession taxRateSession;
	
	@EJB
	public ShipRateSession shipRateSession;

    /**
     * Default constructor. 
     */
    public TaxAndShipSingletonSessionBean() {
        // TODO Auto-generated constructor stub
    }
    
	/**
	 * @return the taxRateList
	 */
	@Lock(LockType.READ)
	public List<TaxRate> getTaxRateList() {
		return taxRateList;
	}

	/**
	 * @param taxRateList the taxRateList to set
	 */
	public void setTaxRateList(List<TaxRate> taxRateList) {
		this.taxRateList = taxRateList;
	}

	/**
	 * @return the shipRateList
	 */
	@Lock(LockType.READ)
	public List<ShipRate> getShipRateList() {
		return shipRateList;
	}

	/**
	 * @param shipRateList the shipRateList to set
	 */
	public void setShipRateList(List<ShipRate> shipRateList) {
		this.shipRateList = shipRateList;
	}

	/**
	 * 
	 */
	
	@PostConstruct
	public void postConstruct(){
		String methodName="postConstruct";
		try{
			logger.logp(Level.FINE, className, methodName, "PostConstruct Initializing Data");
			setTaxRateList(taxRateSession.getAllTaxRatesByOrder());
			setShipRateList(shipRateSession.getAllShipRatesByOrder());
			
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
		setTaxRateList(null);
		setShipRateList(null);
	}
	
	/**
	 * 
	 * @param itemId
	 * @return
	 * @throws Exception
	 */
	public ShipRate getShipRateByItemID(String itemId) throws Exception {
		String methodName ="getShipRateByItemID";
		ShipRate shipRate=null;
		try{
			logger.logp(Level.FINE, className, methodName, "Calling getShipRateByItemID");
			shipRate= shipRateSession.findShipRateById(itemId);
		}catch(Exception e){
			e.printStackTrace();
		}
		return shipRate;
	}

}

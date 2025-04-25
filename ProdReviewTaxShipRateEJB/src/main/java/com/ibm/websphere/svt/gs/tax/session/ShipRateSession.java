package com.ibm.websphere.svt.gs.tax.session;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ibm.websphere.svt.gs.tax.entity.ShipRate;

/**
 * Session Bean implementation class ShipRateSession
 */
@Stateless
@LocalBean
@TransactionManagement(javax.ejb.TransactionManagementType.CONTAINER)
public class ShipRateSession {

    /**
     * Default constructor. 
     */
    public ShipRateSession() {
        // TODO Auto-generated constructor stub
    }
    
	private static String componentName = "com.ibm.websphere.svt.gs.tax.session";
	private static Logger logger = Logger.getLogger(componentName);
	private static String className = ShipRateSession.class.getName();

	@PersistenceContext(unitName = "ProdReviewTaxShipRateEJB")
	private EntityManager prodReviewManager;

	/**
	 * ejbStart
	 */
	@javax.annotation.PostConstruct
	protected void ejbStart() {

	}

	/**
	 * ejbFinish
	 * 
	 * Closes Managers that were injected.
	 */
	@javax.annotation.PreDestroy
	protected void ejbFinish() {

		/*if (prodReviewManager != null) {
			prodReviewManager.close();
		}*/
	}
	
	/**
	 * 
	 * @return
	 */
	private EntityManager getEntityManager() {
		return prodReviewManager;
	}


	/**
	 * 
	 * @param itemId
	 * @return
	 * @throws Exception
	 */
	public ShipRate findShipRateById(String itemId) throws Exception {
		String methodName="findShipRateById";
		logger.logp(Level.FINE, className, methodName, "Finding shipRate by ID:  "+itemId);
		ShipRate shipRate = null;
		EntityManager em = getEntityManager();
		try {
			shipRate = (ShipRate) em.find(ShipRate.class,
					itemId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return shipRate;

	}

	/**
	 * 
	 * @param shipRate
	 * @return
	 * @throws Exception
	 */
	public String createShipRate(ShipRate shipRate) throws Exception {
		String methodName = "createShipRate";
		logger.logp(Level.FINE, className, methodName, "Creating shipRate:  "+shipRate.getItemId());
		EntityManager em = getEntityManager();
		try {
			em.persist(shipRate);
		} catch (Exception e) {
			logger.logp(Level.SEVERE, className, methodName,
					"Error: " + e.getMessage());
			e.printStackTrace();
		}
		return "";

	}
	
	/**
	 * 
	 * @param shipRate
	 * @return
	 * @throws Exception
	 */
	public String deleteShipRate(ShipRate shipRate) throws Exception {
		String methodName = "deleteShipRate";
		logger.logp(Level.FINE, className, methodName, "Deleting shipRate:  "+shipRate.getItemId());
		EntityManager em = getEntityManager();
		try {
			shipRate = em.merge(shipRate);
			em.remove(shipRate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";

	}
	
	/**
	 * 
	 * @param shipRate
	 * @return
	 * @throws Exception
	 */
	public String updateShipRate(ShipRate shipRate) throws Exception {
		String methodName = "updateShipRate";
		logger.logp(Level.FINE, className, methodName, "Update shipRate:  "+shipRate.getItemId());
		
		EntityManager em = getEntityManager();
		try {
			shipRate = em.merge(shipRate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<ShipRate> getAllShipRatesByOrder() throws Exception{
		String methodName = "getAllShipRatesByOrder";
		logger.logp(Level.FINE, className, methodName, "Getting all shipRates in order");
		
		EntityManager em = getEntityManager();
		List<ShipRate> results = null;
		try {
			Query query = em.createNamedQuery("getAllProdShipRatesByOrder");
			results = (List<ShipRate>) query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return results;
		
	}
}

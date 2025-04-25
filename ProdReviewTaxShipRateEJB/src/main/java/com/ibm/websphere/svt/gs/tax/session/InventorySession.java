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

import com.ibm.websphere.svt.gs.tax.entity.Inventory;

/**
 * Session Bean implementation class InventorySession
 */
@Stateless
@LocalBean
@TransactionManagement(javax.ejb.TransactionManagementType.CONTAINER)
public class InventorySession {

    /**
     * Default constructor. 
     */
    public InventorySession() {
        // TODO Auto-generated constructor stub
    }
    
    
	private static String componentName = "com.ibm.websphere.svt.gs.tax.session";
	private static Logger logger = Logger.getLogger(componentName);
	private static String className = InventorySession.class.getName();

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
	public Inventory findInventoryById(String itemId) throws Exception {
		String methodName="findInventoryById";
		logger.logp(Level.FINE, className, methodName, "Finding Inventory by ID:  "+itemId);
		Inventory item = null;
		EntityManager em = getEntityManager();
		try {
			item = (Inventory) em.find(Inventory.class,
					itemId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return item;

	}

	/**
	 * 
	 * @param inventory
	 * @return
	 * @throws Exception
	 */
	public String createInventory(Inventory inventory) throws Exception {
		String methodName = "createInventory";
		logger.logp(Level.FINE, className, methodName, "Creating inventory:  "+inventory.getItemId());
		EntityManager em = getEntityManager();
		try {
			em.persist(inventory);
		} catch (Exception e) {
			logger.logp(Level.SEVERE, className, methodName,
					"Error: " + e.getMessage());
			e.printStackTrace();
		}
		return "";

	}
	
	/**
	 * 
	 * @param inventory
	 * @return
	 * @throws Exception
	 */
	public String deleteInventory(Inventory inventory) throws Exception {
		String methodName = "deleteInventory";
		logger.logp(Level.FINE, className, methodName, "Deleting inventory:  "+inventory.getItemId());
		EntityManager em = getEntityManager();
		try {
			inventory = em.merge(inventory);
			em.remove(inventory);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";

	}
	
	/**
	 * 
	 * @param inventory
	 * @return
	 * @throws Exception
	 */
	public String updateInventory(Inventory inventory) throws Exception {
		String methodName = "updateInventory";
		logger.logp(Level.FINE, className, methodName, "Update inventory:  "+inventory.getItemId());
		
		EntityManager em = getEntityManager();
		try {
			inventory = em.merge(inventory);
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
	public List<Inventory> getAllItemsByOrder() throws Exception{
		String methodName = "getAllItemsByOrder";
		logger.logp(Level.FINE, className, methodName, "Getting all inventorys in order");
		
		EntityManager em = getEntityManager();
		List<Inventory> results = null;
		try {
			Query query = em.createNamedQuery("getAllItemsByOrder");
			results = (List<Inventory>) query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return results;
		
	}
}

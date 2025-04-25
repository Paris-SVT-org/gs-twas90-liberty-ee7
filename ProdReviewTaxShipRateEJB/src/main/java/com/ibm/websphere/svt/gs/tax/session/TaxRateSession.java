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

import com.ibm.websphere.svt.gs.tax.entity.TaxRate;

/**
 * Session Bean implementation class TaxRateSession
 */
@Stateless
@LocalBean
@TransactionManagement(javax.ejb.TransactionManagementType.CONTAINER)
public class TaxRateSession {

    /**
     * Default constructor. 
     */
    public TaxRateSession() {
        // TODO Auto-generated constructor stub
    }
    
	private static String componentName = "com.ibm.websphere.svt.gs.tax.session";
	private static Logger logger = Logger.getLogger(componentName);
	private static String className = TaxRateSession.class.getName();

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
	 * @param stateId
	 * @return
	 * @throws Exception
	 */
	public TaxRate findTaxRateById(String stateId) throws Exception {
		String methodName="findTaxRateById";
		logger.logp(Level.FINE, className, methodName, "Finding taxRate by ID:  "+stateId);
		TaxRate taxRate = null;
		EntityManager em = getEntityManager();
		try {
			taxRate = (TaxRate) em.find(TaxRate.class,
					stateId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return taxRate;

	}

	/**
	 * 
	 * @param taxRate
	 * @return
	 * @throws Exception
	 */
	public String createTaxRate(TaxRate taxRate) throws Exception {
		String methodName = "createTaxRate";
		logger.logp(Level.FINE, className, methodName, "Creating taxRate:  "+taxRate.getStateId());
		EntityManager em = getEntityManager();
		try {
			em.persist(taxRate);
		} catch (Exception e) {
			logger.logp(Level.SEVERE, className, methodName,
					"Error: " + e.getMessage());
			e.printStackTrace();
		}
		return "";

	}
	
	/**
	 * 
	 * @param taxRate
	 * @return
	 * @throws Exception
	 */
	public String deleteTaxRate(TaxRate taxRate) throws Exception {
		String methodName = "deleteTaxRate";
		logger.logp(Level.FINE, className, methodName, "Deleting taxRate:  "+taxRate.getStateId());
		EntityManager em = getEntityManager();
		try {
			taxRate = em.merge(taxRate);
			em.remove(taxRate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";

	}
	
	/**
	 * 
	 * @param taxRate
	 * @return
	 * @throws Exception
	 */
	public String updateTaxRate(TaxRate taxRate) throws Exception {
		String methodName = "updateTaxRate";
		logger.logp(Level.FINE, className, methodName, "Update taxRate:  "+taxRate.getStateId());
		
		EntityManager em = getEntityManager();
		try {
			taxRate = em.merge(taxRate);
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
	public List<TaxRate> getAllTaxRatesByOrder() throws Exception{
		String methodName = "getAllTaxRatesByOrder";
		logger.logp(Level.FINE, className, methodName, "Getting all taxRates in order");
		
		EntityManager em = getEntityManager();
		List<TaxRate> results = null;
		try {
			Query query = em.createNamedQuery("getTaxInfoForAllStates");
			results = (List<TaxRate>) query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return results;
		
	}
    

}

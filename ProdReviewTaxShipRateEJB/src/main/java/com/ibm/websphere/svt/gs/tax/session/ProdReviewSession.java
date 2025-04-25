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

import com.ibm.websphere.svt.gs.tax.entity.ProdReview;

/**
 * Session Bean implementation class ProdReviewSession
 */
@Stateless
@LocalBean
@TransactionManagement(javax.ejb.TransactionManagementType.CONTAINER)
public class ProdReviewSession {

    /**
     * Default constructor. 
     */
    public ProdReviewSession() {
        // TODO Auto-generated constructor stub
    }
    
    
	private static String componentName = "com.ibm.websphere.svt.gs.tax.session";
	private static Logger logger = Logger.getLogger(componentName);
	private static String className = ProdReviewSession.class.getName();

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
	 * @param reviewId
	 * @return
	 * @throws Exception
	 */
	public ProdReview findProductReviewById(String reviewId) throws Exception {
		String methodName="findProductReviewById";
		logger.logp(Level.FINE, className, methodName, "Finding prodyct review  by ID:  "+reviewId);
		ProdReview prodReview = null;
		EntityManager em = getEntityManager();
		try {
			prodReview = (ProdReview) em.find(ProdReview.class,
					reviewId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return prodReview;

	}

	/**
	 * 
	 * @param prodReview
	 * @return
	 * @throws Exception
	 */
	public String createProdReview(ProdReview prodReview) throws Exception {
		String methodName = "createProdReview";
		logger.logp(Level.FINE, className, methodName, "Creating prodReview:  "+prodReview.getReviewId());
		EntityManager em = getEntityManager();
		try {
			em.persist(prodReview);
		} catch (Exception e) {
			logger.logp(Level.SEVERE, className, methodName,
					"Error: " + e.getMessage());
			e.printStackTrace();
		}
		return "";

	}
	
	/**
	 * 
	 * @param prodReview
	 * @return
	 * @throws Exception
	 */
	public String deleteProdReview(ProdReview prodReview) throws Exception {
		String methodName = "deleteProdReview";
		logger.logp(Level.FINE, className, methodName, "Deleting prodReview:  "+prodReview.getReviewId());
		EntityManager em = getEntityManager();
		try {
			prodReview = em.merge(prodReview);
			em.remove(prodReview);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";

	}
	
	/**
	 * 
	 * @param prodReview
	 * @return
	 * @throws Exception
	 */
	public String updateProdReview(ProdReview prodReview) throws Exception {
		String methodName = "updateProdReview";
		logger.logp(Level.FINE, className, methodName, "Update prodReview:  "+prodReview.getReviewId());
		
		EntityManager em = getEntityManager();
		try {
			prodReview = em.merge(prodReview);
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
	public List<ProdReview> getAllProdReviewsByOrder() throws Exception{
		String methodName = "getAllProdReviewsByOrder";
		logger.logp(Level.FINE, className, methodName, "Getting all prodReviews in order");
		
		EntityManager em = getEntityManager();
		List<ProdReview> results = null;
		try {
			Query query = em.createNamedQuery("getAllProdReviewsByOrder");
			results = (List<ProdReview>) query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return results;
		
	}
    

}

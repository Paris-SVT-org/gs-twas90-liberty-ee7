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

import com.ibm.websphere.svt.gs.tax.entity.Customer;

/**
 * Session Bean implementation class CustomerSession
 */
@Stateless
@LocalBean
@TransactionManagement(javax.ejb.TransactionManagementType.CONTAINER)
public class CustomerSession {

	/**
	 * Default constructor.
	 */
	public CustomerSession() {
		// TODO Auto-generated constructor stub
	}

	private static String componentName = "com.ibm.websphere.svt.gs.tax.session";
	private static Logger logger = Logger.getLogger(componentName);
	private static String className = CustomerSession.class.getName();

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
	 * @param custId
	 * @return
	 * @throws Exception
	 */
	public Customer findCustomerById(String custId) throws Exception {
		String methodName="findCustomerById";
		logger.logp(Level.FINE, className, methodName, "Finding customer by ID:  "+custId);
		Customer customer = null;
		EntityManager em = getEntityManager();
		try {
			customer = (Customer) em.find(Customer.class,
					custId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return customer;

	}
	
	/**
	 * 
	 * @param customer
	 * @return
	 * @throws Exception
	 */
	public String createCustomer(Customer customer) throws Exception {
		String methodName = "createCustomer";
		logger.logp(Level.FINE, className, methodName, "Creating customer:  "+customer.getCustId());
		EntityManager em = getEntityManager();
		try {
			em.persist(customer);
		} catch (Exception e) {
			logger.logp(Level.SEVERE, className, methodName,
					"Error: " + e.getMessage());
			e.printStackTrace();
		}
		return "";

	}
	
	/**
	 * 
	 * @param customer
	 * @return
	 * @throws Exception
	 */
	public String deleteCustomer(Customer customer) throws Exception {
		String methodName = "deleteCustomer";
		logger.logp(Level.FINE, className, methodName, "Deleting customer:  "+customer.getCustId());
		EntityManager em = getEntityManager();
		try {
			customer = em.merge(customer);
			em.remove(customer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";

	}
	
	/**
	 * 
	 * @param customer
	 * @return
	 * @throws Exception
	 */
	public String updateCustomer(Customer customer) throws Exception {
		String methodName = "updateCustomer";
		logger.logp(Level.FINE, className, methodName, "Update customer:  "+customer.getCustId());
		
		EntityManager em = getEntityManager();
		try {
			customer = em.merge(customer);
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
	public List<Customer> getAllCustomersByOrder() throws Exception{
		String methodName = "getAllCustomersByOrder";
		logger.logp(Level.FINE, className, methodName, "Getting all customers in order");
		
		EntityManager em = getEntityManager();
		List<Customer> results = null;
		try {
			Query query = em.createNamedQuery("getAllCustomersByOrder");
			results = (List<Customer>) query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return results;
		
	}

	public Object[] getTotalNoOfInvoices() throws Exception{
		String methodName = "getAllCustomersByOrder";
		logger.logp(Level.FINE, className, methodName, "Getting all customers in order");
		
		EntityManager em = getEntityManager();
		Object[] result = null;
		//List<Customer> results = null;
		try {
			Query query = em.createNamedQuery("getTotalNoOfInvoices");
			result =  (Object[]) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
		
	}
	
}

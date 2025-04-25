package com.ibm.websphere.svt.gs.gsdb.session;

import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;

import com.ibm.websphere.svt.gs.gsdb.entities.Customer;
import com.ibm.websphere.svt.gs.gsdb.session.view.CustomerSessionLocal;
import com.ibm.websphere.svt.gs.wrapper.CustomerWrapper;

@SuppressWarnings("unchecked")
@Stateless
@Local(CustomerSessionLocal.class)
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
@Resource(name = "jdbc/gsdb", type = DataSource.class, authenticationType = Resource.AuthenticationType.CONTAINER, shareable = true)
public class CustomerSessionBean {
	
	@PersistenceContext(unitName="GarageSaleDBJPA")
	private EntityManager em;
	public CustomerSessionBean() {
	
	}

	private EntityManager getEntityManager() {
		
		return em;
	}

	public String createCustomer(Customer customer) throws Exception {
		EntityManager em = getEntityManager();
		try {
			
			em.joinTransaction();
			em.persist(customer);
			
		} catch (Exception ex) {
			try {
				em.getTransaction().rollback();
			} catch (Exception e) {
				ex.printStackTrace();
				throw e;
			}
			throw ex;
		} finally {
			//em.close();
		}
		return "";
	}

	public String deleteCustomer(Customer customer) throws Exception {
		EntityManager em = getEntityManager();
		try {
			
			em.joinTransaction();
			customer = em.merge(customer);
			em.remove(customer);
			
		} catch (Exception ex) {
			try {
				em.getTransaction().rollback();
			} catch (Exception e) {
				ex.printStackTrace();
				throw e;
			}
			throw ex;
		} finally {
			//em.close();
		}
		return "";
	}

	public String updateCustomer(Customer customer) throws Exception {
		EntityManager em = getEntityManager();
		try {
			em.joinTransaction();
			customer = em.merge(customer);
		} catch (Exception ex) {
			try {
				em.getTransaction().rollback();
			} catch (Exception e) {
				ex.printStackTrace();
				throw e;
			}
			throw ex;
		} finally {
			//em.close();
		}
		return "";
	}

	public Customer findCustomerByCustId(String custId) {
		Customer customer = null;
		EntityManager em = getEntityManager();
		try {
			customer = (Customer) em.find(Customer.class, custId);
		} finally {
			//em.close();
		}
		return customer;
	}

	public Customer getNewCustomer() {
	
		Customer customer = new Customer();
	
		return customer;
	}

	public List<Customer> getAllCustomers() {
		EntityManager em = getEntityManager();
		List<Customer> results = null;
		try {
			Query query = em.createNamedQuery("getAllCustomers");
			results = (List<Customer>) query.getResultList();
		} finally {
			//em.close();
		}
		return results;
	}
	
	/**
	 * 
	 * @param customer
	 * @return
	 */
	public CustomerWrapper getWrapper(Customer customer){
		CustomerWrapper wrapper = new CustomerWrapper();
		wrapper.setCustID(customer.getCustId());
		wrapper.setCustName(customer.getCustName());
		wrapper.setCustPwd(customer.getCustPwd());
		wrapper.setNumInvoicesCreated(customer.getNumInvoicesCreated());
		wrapper.setNumInvoicesCompleted(customer.getNumInvoicesCompleted());
		return wrapper;
	}


}
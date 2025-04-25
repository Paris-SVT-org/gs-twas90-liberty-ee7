package com.ibm.websphere.svt.gs.gsdb.session;

import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import com.ibm.websphere.svt.gs.gsdb.entities.CustomerInfo;
import com.ibm.websphere.svt.gs.gsdb.session.view.CustomerInfoSessionLocal;
import com.ibm.websphere.svt.gs.wrapper.CustomerInfoWrapper;

@SuppressWarnings("unchecked")
@Stateless
@Local(CustomerInfoSessionLocal.class)
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
@Resource(name = "jdbc/gsdb", type = DataSource.class, authenticationType = Resource.AuthenticationType.CONTAINER, shareable = true)
public class CustomerInfoSessionBean {

	@PersistenceContext(unitName="GarageSaleDBJPA")
	private EntityManager em;
	public CustomerInfoSessionBean() {
	
	}

	private EntityManager getEntityManager() {
		return em;
	}

	public String createCustomerInfo(CustomerInfo customerInfo)
			throws Exception {
		EntityManager em = getEntityManager();
		try {
			em.joinTransaction();
			em.persist(customerInfo);
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

	public String deleteCustomerInfo(CustomerInfo customerInfo)
			throws Exception {
		EntityManager em = getEntityManager();
		try {
			
			em.joinTransaction();
			customerInfo = em.merge(customerInfo);
			em.remove(customerInfo);
			
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

	public String updateCustomerInfo(CustomerInfo customerInfo)
			throws Exception {
		EntityManager em = getEntityManager();
		try {
			
			em.joinTransaction();
			customerInfo = em.merge(customerInfo);
			
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

	public CustomerInfo findCustomerInfoByCustId(String custId) {
		CustomerInfo customerInfo = null;
		EntityManager em = getEntityManager();
		try {
			customerInfo = (CustomerInfo) em.find(CustomerInfo.class, custId);
		} finally {
			//em.close();
		}
		return customerInfo;
	}

	public CustomerInfo getNewCustomerInfo() {
	
		CustomerInfo customerInfo = new CustomerInfo();
	
		return customerInfo;
	}
	
	/**
	 * 
	 * @param customerInfo
	 * @return
	 */
	public CustomerInfoWrapper getWrapper(CustomerInfo customerInfo){
		CustomerInfoWrapper wrapper = new CustomerInfoWrapper();
		wrapper.setCustID(customerInfo.getCustId());
		wrapper.setAddress1(customerInfo.getAddress1());
		wrapper.setAddress2(customerInfo.getAddress2());
		wrapper.setPhone(customerInfo.getPhone());
		wrapper.setEmail(customerInfo.getEMail());
		return wrapper;
	}

}
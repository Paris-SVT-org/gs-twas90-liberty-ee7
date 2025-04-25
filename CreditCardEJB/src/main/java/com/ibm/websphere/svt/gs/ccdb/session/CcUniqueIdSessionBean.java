package com.ibm.websphere.svt.gs.ccdb.session;

import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import com.ibm.websphere.svt.gs.ccdb.entities.CcUniqueId;
import com.ibm.websphere.svt.gs.ccdb.session.view.CcUniqueIdSessionLocal;

@SuppressWarnings("unchecked")
@Stateless
@Local(CcUniqueIdSessionLocal.class)
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
@Resource(name = "jdbc/ccdb", type = DataSource.class, authenticationType = Resource.AuthenticationType.CONTAINER, shareable = true)
public class CcUniqueIdSessionBean {

	@PersistenceContext(unitName="CreditCardDBJPA")
	private EntityManager em;
	private EntityManagerFactory emf;
	public CcUniqueIdSessionBean() {
	
	}

	private EntityManager getEntityManager() {
		return em;
	}

	public String createCcUniqueId(CcUniqueId ccUniqueId) throws Exception {
		EntityManager em = getEntityManager();
		try {
			em.joinTransaction();
			em.persist(ccUniqueId);
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

	public String deleteCcUniqueId(CcUniqueId ccUniqueId) throws Exception {
		EntityManager em = getEntityManager();
		try {
			
			em.joinTransaction();
			ccUniqueId = em.merge(ccUniqueId);
			em.remove(ccUniqueId);
			
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

	public String updateCcUniqueId(CcUniqueId ccUniqueId) throws Exception {
		EntityManager em = getEntityManager();
		try {
			
			em.joinTransaction();
			ccUniqueId = em.merge(ccUniqueId);
			
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

	public CcUniqueId findCcUniqueIdByIndex1(String index1) {
		CcUniqueId ccUniqueId = null;
		EntityManager em = getEntityManager();
		try {
			ccUniqueId = (CcUniqueId) em.find(CcUniqueId.class, index1);
		} finally {
			//em.close();
		}
		return ccUniqueId;
	}

	public CcUniqueId getNewCcUniqueId() {
	
		CcUniqueId ccUniqueId = new CcUniqueId();
	
		return ccUniqueId;
	}

}
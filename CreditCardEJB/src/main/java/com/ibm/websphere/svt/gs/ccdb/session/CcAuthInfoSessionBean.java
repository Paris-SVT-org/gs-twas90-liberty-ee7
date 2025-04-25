package com.ibm.websphere.svt.gs.ccdb.session;

import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import com.ibm.websphere.svt.gs.ccdb.entities.CcAuthInfo;
import com.ibm.websphere.svt.gs.ccdb.session.view.CcAuthInfoSessionLocal;

@SuppressWarnings("unchecked")
@Stateless
@Local(CcAuthInfoSessionLocal.class)
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
@Resource(name = "jdbc/ccdb", type = DataSource.class, authenticationType = Resource.AuthenticationType.CONTAINER, shareable = true)
public class CcAuthInfoSessionBean {

	@PersistenceContext(unitName="CreditCardDBJPA")
	private EntityManager em;

	public CcAuthInfoSessionBean() {
	
	}

	private EntityManager getEntityManager() {
		return em;
	}

	public String createCcAuthInfo(CcAuthInfo ccAuthInfo) throws Exception {
		EntityManager em = getEntityManager();
		try {
			em.joinTransaction();
			em.persist(ccAuthInfo);
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

	public String deleteCcAuthInfo(CcAuthInfo ccAuthInfo) throws Exception {
		EntityManager em = getEntityManager();
		try {
			em.joinTransaction();
			ccAuthInfo = em.merge(ccAuthInfo);
			em.remove(ccAuthInfo);
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

	public String updateCcAuthInfo(CcAuthInfo ccAuthInfo) throws Exception {
		EntityManager em = getEntityManager();
		try {
			em.joinTransaction();
			ccAuthInfo = em.merge(ccAuthInfo);
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

	public CcAuthInfo findCcAuthInfoByCcNum(String ccNum) {
		CcAuthInfo ccAuthInfo = null;
		EntityManager em = getEntityManager();
		try {
			ccAuthInfo = (CcAuthInfo) em.find(CcAuthInfo.class, ccNum);
		} finally {
			//em.close();
		}
		return ccAuthInfo;
	}

	public CcAuthInfo getNewCcAuthInfo() {
	
		CcAuthInfo ccAuthInfo = new CcAuthInfo();
	
		return ccAuthInfo;
	}

}
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

import com.ibm.websphere.svt.gs.ccdb.entities.CcInfo;
import com.ibm.websphere.svt.gs.ccdb.session.view.CcInfoSessionLocal;

@SuppressWarnings("unchecked")
@Stateless
@Local(CcInfoSessionLocal.class)
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
@Resource(name = "jdbc/ccdb", type = DataSource.class, authenticationType = Resource.AuthenticationType.CONTAINER, shareable = true)
public class CcInfoSessionBean {

	@PersistenceContext(unitName="CreditCardDBJPA")
	private EntityManager em;
	public CcInfoSessionBean() {
	
	}

	public CcInfoSessionBean(EntityManager em) {
		this.em = em;
	}

	public void setEntityManagerFactory(EntityManager em) {
		this.em = em;
	}

	private EntityManager getEntityManager() {
		return em;
	}

	public String createCcInfo(CcInfo ccInfo) throws Exception {
		EntityManager em = getEntityManager();
		try {
			em.joinTransaction();
			em.persist(ccInfo);
		} catch (Exception ex) {
			try {
				if (em.getTransaction().isActive()) {
					em.getTransaction().rollback();
				}
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

	public String deleteCcInfo(CcInfo ccInfo) throws Exception {
		EntityManager em = getEntityManager();
		try {
			em.joinTransaction();
			ccInfo = em.merge(ccInfo);
			em.remove(ccInfo);
			//em.getTransaction().commit();
		} catch (Exception ex) {
			try {
				if (em.getTransaction().isActive()) {
					em.getTransaction().rollback();
				}
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

	public String updateCcInfo(CcInfo ccInfo) throws Exception {
		EntityManager em = getEntityManager();
		try {
			//em.getTransaction().begin();
			em.joinTransaction();
			ccInfo = em.merge(ccInfo);
			//em.getTransaction().commit();
		} catch (Exception ex) {
			try {
				if (em.getTransaction().isActive()) {
					em.getTransaction().rollback();
				}
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

	public CcInfo findCcInfoByCcNum(String ccNum) {
		CcInfo ccInfo = null;
		EntityManager em = getEntityManager();
		try {
			ccInfo = (CcInfo) em.find(CcInfo.class, ccNum);
		} finally {
			//em.close();
		}
		return ccInfo;
	}

	public CcInfo getNewCcInfo() {
	
		CcInfo ccInfo = new CcInfo();
	
		return ccInfo;
	}

}
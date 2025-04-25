package com.ibm.websphere.svt.gs.ccdb.session;

import java.util.Date;
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

import com.ibm.websphere.svt.gs.ccdb.entities.CcHistory;
import com.ibm.websphere.svt.gs.ccdb.entities.CcHistoryPK;
import com.ibm.websphere.svt.gs.ccdb.session.view.CcHistorySessionLocal;

@SuppressWarnings("unchecked")
@Stateless
@Local(CcHistorySessionLocal.class)
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
@Resource(name = "jdbc/ccdb", type = DataSource.class, authenticationType = Resource.AuthenticationType.CONTAINER, shareable = true)
public class CcHistorySessionBean {


	@PersistenceContext(unitName="CreditCardDBJPA")
	private EntityManager em;
	
	public CcHistorySessionBean() {
	
	}

	private EntityManager getEntityManager() {
		return em;
	}

	public String createCcHistory(CcHistory ccHistory) throws Exception {
		EntityManager em = getEntityManager();
		try {
			em.joinTransaction();
			em.persist(ccHistory);
		} catch (Exception ex) {
			try {
				//em.getTransaction().rollback();
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

	public String deleteCcHistory(CcHistory ccHistory) throws Exception {
		EntityManager em = getEntityManager();
		try {
			em.joinTransaction();
			ccHistory = em.merge(ccHistory);
			em.remove(ccHistory);
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

	public String updateCcHistory(CcHistory ccHistory) throws Exception {
		EntityManager em = getEntityManager();
		try {
			em.joinTransaction();
			ccHistory = em.merge(ccHistory);
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

	public CcHistory findCcHistoryByPrimaryKey(String ccNum, Date timestamp1) {
		CcHistory ccHistory = null;
		EntityManager em = getEntityManager();
		CcHistoryPK pk = new CcHistoryPK();
		pk.setCcNum(ccNum);
		pk.setTimestamp1(timestamp1);
		try {
			ccHistory = (CcHistory) em.find(CcHistory.class, pk);
		} finally {
			//em.close();
		}
		return ccHistory;
	}

	public CcHistory getNewCcHistory() {
	
		CcHistory ccHistory = new CcHistory();
	
		CcHistoryPK id = new CcHistoryPK();
		ccHistory.setId(id);
	
		return ccHistory;
	}

	//@NamedQueryTarget("getCcHistoryByCCNum")
	public List<CcHistory> getCcHistoryByCCNum(String id_ccNum) {
		EntityManager em = getEntityManager();
		List<CcHistory> results = null;
		try {
			Query query = em.createNamedQuery("getCcHistoryByCCNum");
			query.setParameter("id_ccNum", id_ccNum);
			results = (List<CcHistory>) query.getResultList();
		} finally {
			//em.close();
		}
		return results;
	}

}
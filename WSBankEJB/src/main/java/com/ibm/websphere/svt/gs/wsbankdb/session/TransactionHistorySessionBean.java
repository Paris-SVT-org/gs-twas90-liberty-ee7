package com.ibm.websphere.svt.gs.wsbankdb.session;

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

import com.ibm.websphere.svt.gs.wsbankdb.entities.TransactionHistory;
import com.ibm.websphere.svt.gs.wsbankdb.entities.TransactionHistoryPK;
import com.ibm.websphere.svt.gs.wsbankdb.session.view.TransactionHistorySessionLocal;

@SuppressWarnings("unchecked")
@Stateless
@Local(TransactionHistorySessionLocal.class)
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
@Resource(name = "jdbc/wsbankdb", type = DataSource.class, authenticationType = Resource.AuthenticationType.CONTAINER, shareable = true)
public class TransactionHistorySessionBean {

	@PersistenceContext(unitName="WSBankDBJPA")
	private EntityManager emf;
	public TransactionHistorySessionBean() {
	
	}

	private EntityManager getEntityManager() {
		return emf;
	}

	public String createTransactionHistory(TransactionHistory transactionHistory)
			throws Exception {
		EntityManager em = getEntityManager();
		try {
			em.joinTransaction();
			em.persist(transactionHistory);
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

	public String deleteTransactionHistory(TransactionHistory transactionHistory)
			throws Exception {
		EntityManager em = getEntityManager();
		try {
			
			em.joinTransaction();
			transactionHistory = em.merge(transactionHistory);
			em.remove(transactionHistory);
			
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

	public String updateTransactionHistory(TransactionHistory transactionHistory)
			throws Exception {
		EntityManager em = getEntityManager();
		try {
			
			em.joinTransaction();
			transactionHistory = em.merge(transactionHistory);
			
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

	public TransactionHistory findTransactionHistoryByPrimaryKey(int id,
			int suffix, Date time1) {
		TransactionHistory transactionHistory = null;
		EntityManager em = getEntityManager();
		TransactionHistoryPK pk = new TransactionHistoryPK();
		pk.setId(id);
		pk.setSuffix(suffix);
		pk.setTime1(time1);
		try {
			transactionHistory = (TransactionHistory) em.find(
					TransactionHistory.class, pk);
		} finally {
			//em.close();
		}
		return transactionHistory;
	}

	public TransactionHistory getNewTransactionHistory() {
	
		TransactionHistory transactionHistory = new TransactionHistory();
	
		TransactionHistoryPK id = new TransactionHistoryPK();
		transactionHistory.setId(id);
	
		return transactionHistory;
	}

	public List<TransactionHistory> getTransactionHistoryBySubAccount(
			int id_id, int id_suffix) {
		EntityManager em = getEntityManager();
		List<TransactionHistory> results = null;
		try {
			Query query = em
					.createNamedQuery("getTransactionHistoryBySubAccount");
			query.setParameter("id_id", id_id);
			query.setParameter("id_suffix", id_suffix);
			results = (List<TransactionHistory>) query.getResultList();
		} finally {
			//em.close();
		}
		return results;
	}

}
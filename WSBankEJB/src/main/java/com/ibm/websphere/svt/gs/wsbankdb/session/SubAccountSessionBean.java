package com.ibm.websphere.svt.gs.wsbankdb.session;

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

import com.ibm.websphere.svt.gs.wsbankdb.entities.SubAccount;
import com.ibm.websphere.svt.gs.wsbankdb.entities.SubAccountPK;
import com.ibm.websphere.svt.gs.wsbankdb.session.view.SubAccountSessionLocal;

@SuppressWarnings("unchecked")
@Stateless
@Local(SubAccountSessionLocal.class)
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
@Resource(name = "jdbc/wsbankdb", type = DataSource.class, authenticationType = Resource.AuthenticationType.CONTAINER, shareable = true)
public class SubAccountSessionBean {

	@PersistenceContext(unitName="WSBankDBJPA")
	private EntityManager em;
	public SubAccountSessionBean() {
	
	}

	private EntityManager getEntityManager() {
		return em;
	}

	public String createSubAccount(SubAccount subAccount) throws Exception {
		EntityManager em = getEntityManager();
		try {
			em.joinTransaction();
			em.persist(subAccount);
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

	public String deleteSubAccount(SubAccount subAccount) throws Exception {
		EntityManager em = getEntityManager();
		try {
			em.joinTransaction();
			subAccount = em.merge(subAccount);
			em.remove(subAccount);
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

	public String updateSubAccount(SubAccount subAccount) throws Exception {
		EntityManager em = getEntityManager();
		try {
			em.joinTransaction();
			subAccount = em.merge(subAccount);
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

	public SubAccount findSubAccountByPrimaryKey(int suffix, int id) {
		SubAccount subAccount = null;
		EntityManager em = getEntityManager();
		SubAccountPK pk = new SubAccountPK();
		pk.setSuffix(suffix);
		pk.setId(id);
		try {
			subAccount = (SubAccount) em.find(SubAccount.class, pk);
		} finally {
			//em.close();
		}
		return subAccount;
	}

	public SubAccount getNewSubAccount() {
	
		SubAccount subAccount = new SubAccount();
	
		SubAccountPK id = new SubAccountPK();
		subAccount.setId(id);
	
		return subAccount;
	}

	public List<SubAccount> getSubAccountsById(int id_id) {
		EntityManager em = getEntityManager();
		List<SubAccount> results = null;
		try {
			Query query = em.createNamedQuery("getSubAccountsById");
			query.setParameter("id_id", id_id);
			results = (List<SubAccount>) query.getResultList();
		} finally {
			//em.close();
		}
		return results;
	}

}
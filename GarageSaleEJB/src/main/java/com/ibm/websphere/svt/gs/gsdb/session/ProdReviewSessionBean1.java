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

import com.ibm.websphere.svt.gs.gsdb.entities.ProdReview;
import com.ibm.websphere.svt.gs.gsdb.session.view.ProdReviewSessionLocal1;

@SuppressWarnings("unchecked")
@Stateless
@Local(ProdReviewSessionLocal1.class)
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
@Resource(name = "jdbc/gsdb", type = DataSource.class, authenticationType = Resource.AuthenticationType.CONTAINER, shareable = true)
public class ProdReviewSessionBean1 {

	@PersistenceContext(unitName = "GarageSaleDBJPA")
	private EntityManager em;

	public ProdReviewSessionBean1() {

	}

	private EntityManager getEntityManager() {

		return em;
	}

	public String createProdReview(ProdReview prodReview) throws Exception {
		EntityManager em = getEntityManager();
		try {
			em.joinTransaction();
			em.persist(prodReview);
			
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

	public String deleteProdReview(ProdReview prodReview) throws Exception {
		EntityManager em = getEntityManager();
		try {
			em.joinTransaction();
			prodReview = em.merge(prodReview);
			em.remove(prodReview);
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

	public String updateProdReview(ProdReview prodReview) throws Exception {
		EntityManager em = getEntityManager();
		try {
			em.joinTransaction();
			prodReview = em.merge(prodReview);
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

	public ProdReview findProdReviewByReviewId(long reviewId) {
		ProdReview prodReview = null;
		EntityManager em = getEntityManager();
		try {
			prodReview = (ProdReview) em.find(ProdReview.class, reviewId);
		} finally {
			//em.close();
		}
		return prodReview;
	}

	public ProdReview getNewProdReview() {

		ProdReview prodReview = new ProdReview();

		return prodReview;
	}

}
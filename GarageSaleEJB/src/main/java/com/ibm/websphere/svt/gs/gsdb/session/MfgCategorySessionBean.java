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

import com.ibm.websphere.svt.gs.gsdb.entities.MfgCategory;
import com.ibm.websphere.svt.gs.gsdb.entities.MfgCategoryPK;
import com.ibm.websphere.svt.gs.gsdb.session.view.MfgCategorySessionLocal;
import com.ibm.websphere.svt.gs.wrapper.MfgCategoryWrapper;

@SuppressWarnings("unchecked")
@Stateless
@Local(MfgCategorySessionLocal.class)
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
@Resource(name = "jdbc/gsdb", type = DataSource.class, authenticationType = Resource.AuthenticationType.CONTAINER, shareable = true)
public class MfgCategorySessionBean {

	@PersistenceContext(unitName="GarageSaleDBJPA")
	private EntityManager em;
	public MfgCategorySessionBean() {
	}

	private EntityManager getEntityManager() {
		return em;
	}

	public String createMfgCategory(MfgCategory mfgCategory) throws Exception {
		EntityManager em = getEntityManager();
		try {
			em.joinTransaction();
			em.persist(mfgCategory);
		} catch (Exception ex) {
			try {
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

	public String deleteMfgCategory(MfgCategory mfgCategory) throws Exception {
		EntityManager em = getEntityManager();
		try {
			em.joinTransaction();
			mfgCategory = em.merge(mfgCategory);
			em.remove(mfgCategory);
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

	public String updateMfgCategory(MfgCategory mfgCategory) throws Exception {
		EntityManager em = getEntityManager();
		try {
			em.joinTransaction();
			mfgCategory = em.merge(mfgCategory);
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

	public MfgCategory findMfgCategoryByPrimaryKey(String mfgName,
			String categoryCategoryName) {
		MfgCategory mfgCategory = null;
		EntityManager em = getEntityManager();
		MfgCategoryPK pk = new MfgCategoryPK();
		pk.setMfgName(mfgName);
		pk.setCategoryCategoryName(categoryCategoryName);
		try {
			mfgCategory = (MfgCategory) em.find(MfgCategory.class, pk);
		} finally {
			//em.close();
		}
		return mfgCategory;
	}

	public MfgCategory getNewMfgCategory() {
	
		MfgCategory mfgCategory = new MfgCategory();
	
		MfgCategoryPK id = new MfgCategoryPK();
		mfgCategory.setId(id);
	
		return mfgCategory;
	}

	public List<MfgCategory> getMfgCategory(String id_categoryCategoryName) {
		EntityManager em = getEntityManager();
		List<MfgCategory> results = null;
		try {
			Query query = em.createNamedQuery("getMfgCategory");
			query.setParameter("id_categoryCategoryName",
					id_categoryCategoryName);
			results = (List<MfgCategory>) query.getResultList();
		} finally {
			//em.close();
		}
		return results;
	}
	
	public MfgCategoryWrapper getWrapper(MfgCategory mfgCategory){
		MfgCategoryWrapper wrapper = new MfgCategoryWrapper();
		wrapper.setCategoryName(mfgCategory.getId().getCategoryCategoryName());
		wrapper.setMfgName(mfgCategory.getId().getMfgName());
		return wrapper;
	}
	
	public List<MfgCategory> getAllMfgCategories() {
		EntityManager em = getEntityManager();
		List<MfgCategory> results = null;
		try {
			Query query = em.createNamedQuery("getAllMfgCategories");
			results = (List<MfgCategory>) query.getResultList();
		} finally {
			//em.close();
		}
		return results;
	}



}
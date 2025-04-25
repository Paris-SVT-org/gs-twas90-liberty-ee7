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

import com.ibm.websphere.svt.gs.gsdb.entities.Category;
import com.ibm.websphere.svt.gs.gsdb.session.view.CategorySessionLocal;
import com.ibm.websphere.svt.gs.wrapper.CategoryWrapper;

@SuppressWarnings("unchecked")
@Stateless
@Local(CategorySessionLocal.class)
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
@Resource(name = "jdbc/gsdb", type = DataSource.class, authenticationType = Resource.AuthenticationType.CONTAINER, shareable = true)
public class CategorySessionBean {

	@PersistenceContext(unitName="GarageSaleDBJPA")
	private EntityManager em;
	private List<Category> categories;
	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public CategorySessionBean() {
	
	}

	private EntityManager getEntityManager() {
		return em;
	}

	public String createCategory(Category category) throws Exception {
		EntityManager em = getEntityManager();
		try {
			em.joinTransaction();
			em.persist(category);
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

	public String deleteCategory(Category category) throws Exception {
		EntityManager em = getEntityManager();
		try {
			em.joinTransaction();
			category = em.merge(category);
			em.remove(category);
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

	public String updateCategory(Category category) throws Exception {
		EntityManager em = getEntityManager();
		try {
			em.joinTransaction();
			category = em.merge(category);
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

	public Category findCategoryByCategoryName(String categoryName) {
		Category category = null;
		EntityManager em = getEntityManager();
		try {
			category = (Category) em.find(Category.class, categoryName);
		} finally {
			//em.close();
		}
		return category;
	}

	public Category getNewCategory() {
	
		Category category = new Category();
	
		return category;
	}

	public List<Category> getCategories() {
		EntityManager em = getEntityManager();
		try {
			Query query = em.createNamedQuery("getCategories");
			categories = (List<Category>) query.getResultList();
		} finally {
			//em.close();
		}
		return categories;
	}
	
	/**
	 * 
	 * @param category
	 * @return
	 */
	public CategoryWrapper getWrapper(Category category){
		CategoryWrapper wrapper = new CategoryWrapper();
		wrapper.setCategoryName(category.getCategoryName());
		wrapper.setCategoryDescription(category.getCategoryDescription());
		wrapper.setNumItemsInCategory(category.getNumItemsInCategory());
		return wrapper;			
	}
	
	/**
	 * 
	 * @param categoryLocal
	 * @return
	 */
	public String getNextItemID(Category categoryLocal){
		String itemID = categoryLocal.getCategoryName();
		int itemNum = categoryLocal.getNumItemsInCategory();
		//System.out.println("before: " +itemNum);
		itemNum= itemNum + 1;
		//System.out.println("after: " + itemNum);
		categoryLocal.setNumItemsInCategory(itemNum);
		
		if(itemNum < 10)
			itemID = itemID + "000" + itemNum;
		else if (itemNum < 100)
			itemID = itemID + "00" + itemNum;
		else if (itemNum < 1000)
			itemID = itemID + "0" + itemNum;			
		else 
			itemID = itemID + itemNum;
		
		//System.out.println("return: " + itemID);
		return itemID;
	}



}
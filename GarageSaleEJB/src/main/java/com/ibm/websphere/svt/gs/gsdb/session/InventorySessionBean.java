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

import com.ibm.websphere.svt.gs.gsdb.entities.Inventory;
import com.ibm.websphere.svt.gs.gsdb.session.view.InventorySessionLocal;
import com.ibm.websphere.svt.gs.wrapper.InventoryWrapper;

@SuppressWarnings("unchecked")
@Stateless
@Local(InventorySessionLocal.class)
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
@Resource(name = "jdbc/gsdb", type = DataSource.class, authenticationType = Resource.AuthenticationType.CONTAINER, shareable = true)
public class InventorySessionBean {

	@PersistenceContext(unitName="GarageSaleDBJPA")
	private EntityManager em;
	public InventorySessionBean() {
	
	}

	private EntityManager getEntityManager() {
		return em;
	}

	public String createInventory(Inventory inventory) throws Exception {
		EntityManager em = getEntityManager();
		try {
			em.joinTransaction();
			em.persist(inventory);
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

	public String deleteInventory(Inventory inventory) throws Exception {
		EntityManager em = getEntityManager();
		try {
			em.joinTransaction();
			inventory = em.merge(inventory);
			em.remove(inventory);
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

	public String updateInventory(Inventory inventory) throws Exception {
		EntityManager em = getEntityManager();
		try {
			em.joinTransaction();
			inventory = em.merge(inventory);
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

	public Inventory findInventoryByItemId(String itemId) {
		Inventory inventory = null;
		EntityManager em = getEntityManager();
		try {
			inventory = (Inventory) em.find(Inventory.class, itemId);
		} finally {
			//em.close();
		}
		return inventory;
	}

	public Inventory getNewInventory() {
	
		Inventory inventory = new Inventory();
	
		return inventory;
	}

	public List<Inventory> getAllInventory() {
		EntityManager em = getEntityManager();
		List<Inventory> results = null;
		try {
			Query query = em.createNamedQuery("getAllInventory");
			results = (List<Inventory>) query.getResultList();
		} finally {
			//em.close();
		}
		return results;
	}

	public List<Inventory> getInventoryListByCatName(String categoryName) {
		EntityManager em = getEntityManager();
		List<Inventory> results = null;
		try {
			Query query = em.createNamedQuery("getInventoryListByCatName");
			query.setParameter("categoryName", categoryName);
			results = (List<Inventory>) query.getResultList();
		} finally {
			//em.close();
		}
		return results;
	}

	public List<Inventory> getInventoryListByMfgName(String mfgName) {
		EntityManager em = getEntityManager();
		List<Inventory> results = null;
		try {
			Query query = em.createNamedQuery("getInventoryListByMfgName");
			query.setParameter("mfgName", mfgName);
			results = (List<Inventory>) query.getResultList();
		} finally {
			//em.close();
		}
		return results;
	}

	public List<Inventory> getInventoryByCatAndMfg(String categoryName,
			String mfgName) {
		EntityManager em = getEntityManager();
		List<Inventory> results = null;
		try {
			Query query = em.createNamedQuery("getInventoryByCatAndMfg");
			query.setParameter("categoryName", categoryName);
			query.setParameter("mfgName", mfgName);
			results = (List<Inventory>) query.getResultList();
		} finally {
			//em.close();
		}
		return results;
	}
	/**
	 * 
	 * @param inventory
	 * @return
	 */
	public InventoryWrapper getWrapper(Inventory inventory){
		InventoryWrapper wrapper = new InventoryWrapper();
		wrapper.setCategoryName(inventory.getCategoryName());
		wrapper.setDescription(inventory.getDescription());
		wrapper.setInventorySold(inventory.getInventorySold());
		wrapper.setItemID(inventory.getItemId());
		wrapper.setMfgName(inventory.getMfgName());
		wrapper.setUnitPrice(inventory.getUnitPrice());
		return wrapper;
	}
	
}
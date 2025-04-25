package com.ibm.websphere.svt.gs.tax.session;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;

import com.ibm.websphere.svt.gs.tax.entity.ProdReview;
import com.ibm.websphere.svt.gs.tax.entity.ShipRate;
import com.ibm.websphere.svt.gs.tax.entity.TaxRate;

/**
 * Session Bean implementation class ProductTaxShipSessionBean
 */
@Stateless
@Local(ProductTaxShipSessionBeanLocal.class)
@Remote(ProductTaxShipSessionBeanRemote.class)
@TransactionManagement(javax.ejb.TransactionManagementType.CONTAINER)
public class ProductTaxShipSessionBean {

	@EJB 
	private ProdReviewSession prodReviewSession;
	
	@EJB
	private TaxAndShipSingletonSessionBean  taxAndShipRateSingletonBean;
	
	private static String componentName = "com.ibm.websphere.svt.gs.tax.session";
	private static Logger logger = Logger.getLogger(componentName);
	private static String className = ProductTaxShipSessionBean.class.getName();

    /**
     * Default constructor. 
     */
    public ProductTaxShipSessionBean() {
        // TODO Auto-generated constructor stub
    }

    /**
     * 
     */
	public List<TaxRate> getAllStatesTaxInfo() throws Exception {
		String methodName="getAllStatesTaxInfo";
		logger.logp(Level.FINE, className, methodName, "Getting All States Tax Info");
		return taxAndShipRateSingletonBean.getTaxRateList();
	}
	
	public List<ShipRate> getAllProductsShippingInfo() throws Exception {
		String methodName="getAllProductsShippingInfo";
		logger.logp(Level.FINE, className, methodName, "Getting All Products Shipping Info");
		return taxAndShipRateSingletonBean.getShipRateList();
	}

	public void saveProductReview(ProdReview prodReview) throws Exception {
		String methodName="saveProductReview";
		logger.logp(Level.FINE, className, methodName, "Persist Product review into the Database.");
		prodReviewSession.createProdReview(prodReview);
	}
	/**
	 * 
	 * @param itemId
	 * @return
	 * @throws Exception
	 */
	public ShipRate getShipRateByItemID(String itemID) throws Exception {
		String methodName ="getShipRateByItemID";
		ShipRate shipRate=null;
		try{
			logger.logp(Level.FINE, className, methodName, "Calling getShipRateByItemID");
			shipRate= taxAndShipRateSingletonBean.getShipRateByItemID(itemID);
		}catch(Exception e){
			e.printStackTrace();
		}
		return shipRate;
	}

}

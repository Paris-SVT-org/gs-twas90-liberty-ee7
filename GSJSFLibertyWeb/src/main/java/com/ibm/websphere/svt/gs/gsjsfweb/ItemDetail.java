package com.ibm.websphere.svt.gs.gsjsfweb;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import com.ibm.websphere.svt.gs.gsdb.jaxws.client.GarageSaleStoreManagerLocal;
import com.ibm.websphere.svt.gs.gsdb.jaxws.client.InventoryWrapper;
import com.ibm.websphere.svt.gs.gsjsfweb.utils.GSJSFWebUtil;
import com.ibm.websphere.svt.gs.gsjsfweb.utils.GarageSaleJSFWSClientUtil;


@Named
@RequestScoped
//@ManagedBean(name="itemDetail")
//@RequestScoped
public class ItemDetail implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9085926261032798843L;
	
	private static String componentName = "com.ibm.websphere.svt.gs.cdi";
	private static Logger logger = Logger.getLogger(componentName);
	private static String className = ItemDetail.class.getName();
	
	
	private String categoryName;
	private String manufacturerName;
	private String itemName;
	private String itemImageName=null;
	private float itemUnitPrice;
	@javax.annotation.Resource(lookup="java:module/GarageSaleJSFWSClientUtil")
	private GarageSaleJSFWSClientUtil garageSaleJSFWSClientUtil;

	
//	@GSStoreService @Inject
	//private StoreManagerWrapperInterface gsService;
	
	//@ManagedProperty(value="#{garageSaleStoreManagerUtil}")
	/*@EJB
	private GarageSaleStoreManager gsService;*/
	
	//private GarageSaleStoreManagerLocal gsService=GarageSaleWSClientUtils.getStoreManager();
	private GarageSaleStoreManagerLocal gsService;
	/**
	 * @return the gsService
	 */
	public GarageSaleStoreManagerLocal getGsService() {
		return gsService;
	}


	/**
	 * @param gsService the gsService to set
	 */
	public void setGsService(GarageSaleStoreManagerLocal gsService) {
		this.gsService = gsService;
	}


	public ItemDetail() {
		gsService=garageSaleJSFWSClientUtil.injectStoreManagerEndPoint(gsService);
	}
	
    
    @PostConstruct
    public void setValues() {
    	String methodName = "setValues";
    	
    	FacesContext fc = FacesContext.getCurrentInstance();
		Application app = fc.getApplication();
		ExpressionFactory elFactory = app.getExpressionFactory();
		ELContext elContext = fc.getELContext();
		ValueExpression itemIDExp = elFactory.createValueExpression(elContext, "#{sessionScope.inventoryChosen}", java.lang.String.class);
		this.itemName = (String) itemIDExp.getValue(elContext);
		logger.logp(Level.FINE, className, methodName, "itemName : " + this.itemName);
		this.itemImageName = this.itemName + ".jpg";
		logger.logp(Level.FINE, className, methodName, "itemImageName : " + this.itemImageName);
		
		ValueExpression mfgCategoryExp = elFactory.createValueExpression(elContext, "#{sessionScope.mfgChosen}", java.lang.String.class);
		this.manufacturerName = (String) mfgCategoryExp.getValue(elContext);
		logger.logp(Level.FINE, className, methodName, "manufacturerName : " + this.manufacturerName);
		
		ValueExpression categoryExp = elFactory.createValueExpression(elContext, "#{sessionScope.categoryChosen}", java.lang.String.class);
		this.categoryName = (String) categoryExp.getValue(elContext);
		logger.logp(Level.FINE, className, methodName, "categoryName " + this.categoryName);
		
		/*
		StoreManagerWrapperInterface tempService = GSJSFWebUtil.getStoreManagerPort();
    	if (tempService == null) {
    		logger.logp(Level.SEVERE, className, methodName, "null service, cannot continue");
    		GSJSFWebUtil.addErrorMessage("null service, cannot continue");	
    	}
    	InventoryWrapper tempInventory = tempService.getInventory(this.itemName);
    	*/
		GSJSFWebUtil.printCurrentTimestamp(className, methodName,"Start");
		InventoryWrapper tempInventory = this.gsService.getInventory(this.itemName);
		GSJSFWebUtil.printCurrentTimestamp(className, methodName,"End");
    	this.itemUnitPrice = tempInventory.getUnitPrice();
		logger.logp(Level.FINE, className, methodName, "Item " + this.itemName 
				+ " : " + this.itemUnitPrice);
		
    }
    

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getManufacturerName() {
		return manufacturerName;
	}

	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public float getItemUnitPrice() {
		return itemUnitPrice;
	}

	public void setItemUnitPrice(float itemUnitPrice) {
		this.itemUnitPrice = itemUnitPrice;
	}

	public String getItemImageName() {
		return itemImageName;
	}

	public void setItemImageName(String itemImageName) {
		this.itemImageName = itemImageName;
	}
    
}

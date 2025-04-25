/**
 * 
 */
package com.ibm.websphere.svt.gs.gsjsfweb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ViewExpiredException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.xml.ws.WebServiceRef;

import com.ibm.websphere.svt.gs.cart.beans.CartItemWrapper;
import com.ibm.websphere.svt.gs.gsdb.jaxws.client.CartItem;
import com.ibm.websphere.svt.gs.gsdb.jaxws.client.CategoryWrapper;
import com.ibm.websphere.svt.gs.gsdb.jaxws.client.CustomerInfoWrapper;
import com.ibm.websphere.svt.gs.gsdb.jaxws.client.GarageSaleStoreManagerLocal;
import com.ibm.websphere.svt.gs.gsdb.jaxws.client.GarageSaleStoreManagerService;
import com.ibm.websphere.svt.gs.gsdb.jaxws.client.InventoryWrapper;
import com.ibm.websphere.svt.gs.gsdb.jaxws.client.MfgCategoryWrapper;
import com.ibm.websphere.svt.gs.gsjsfweb.exceptions.GSJSF12WebException;
import com.ibm.websphere.svt.gs.gsjsfweb.facesflow.ProductReviewFlowBean;
import com.ibm.websphere.svt.gs.gsjsfweb.utils.GSJSFWebUtil;
import com.ibm.websphere.svt.gs.gsjsfweb.utils.GarageSaleCDIWSClientUtil;
import com.ibm.websphere.svt.gs.gsjsfweb.utils.GarageSaleManagedBeanUtil;
import com.ibm.websphere.svt.gs.tax.entity.ProdReview;
import com.ibm.websphere.svt.gs.tax.entity.TaxRate;


/**
 * @author root
 *
 */
@ManagedBean(name="garageSaleJSFActions")
@RequestScoped
public class GarageSaleJSFActions implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4821725565264721794L;
	private static String componentName = "com.ibm.websphere.svt.gs.gsjsfweb";
	private static Logger logger = Logger.getLogger(componentName);
	private static String className = GarageSaleJSFActions.class.getName();
	private static String FOUNDNULL = "foundNull";	
	/*@javax.annotation.Resource(lookup="java:module/GarageSaleJSFWSClientUtil")
	private GarageSaleJSFWSClientUtil garageSaleJSFWSClientUtil;*/
	
	@Inject
	private GarageSaleCDIWSClientUtil garageSaleCDIWSClientUtil;
	
	@WebServiceRef(name="GarageSaleStoreManagerService",value=GarageSaleStoreManagerService.class)
	private GarageSaleStoreManagerLocal garageSaleStoreManager;
	  
	
	@javax.annotation.PostConstruct
	    public void init() {
	        garageSaleStoreManager=garageSaleCDIWSClientUtil.injectStoreManagerEndPoint(garageSaleStoreManager); 
	    }
	/**
	 * @return the garageSaleStoreManager
	 */
	public GarageSaleStoreManagerLocal getGarageSaleStoreManager() {
		return garageSaleStoreManager;
	}

	/**
	 * @param garageSaleStoreManager the garageSaleStoreManager to set
	 */
	public void setGarageSaleStoreManager(
			GarageSaleStoreManagerLocal garageSaleStoreManager) {
		this.garageSaleStoreManager = garageSaleStoreManager;
	}

	private CategoryWrapper _category;
	private CustomerInfoWrapper _customerInfo = null;
	
    private transient DataModel<CategoryWrapper> _categoryModel= null;
    private transient MfgCategoryWrapper _mfgCategory;
    private transient DataModel<MfgCategoryWrapper> _mfgModel = null;
    private transient InventoryWrapper _inventory;
    private transient DataModel<InventoryWrapper> _inventoryModel = null;
    private transient CartItem _cartItem;
    private transient CartItemWrapper _cartItemWrapper;
	private transient DataModel<CartItemWrapper> _myPurchaseModel = null;
	private transient DataModel<CartItemWrapper> _myCartModel = null;


    private String itemImageName=null;
    
    @Inject
	//@ManagedProperty(name="stateTaxShipRateBean",value="#{stateTaxShipRateBean}")
    private StateTaxShipRateBean stateTaxShipRateBean;
	
    @Inject
    private ProductReviewFlowBean productReviewFlowBean;

    /**
	 * @return the productReviewFlowBean
	 */
	public ProductReviewFlowBean getProductReviewFlowBean() {
		return productReviewFlowBean;
	}
	/**
	 * @param productReviewFlowBean the productReviewFlowBean to set
	 */
	public void setProductReviewFlowBean(ProductReviewFlowBean productReviewFlowBean) {
		this.productReviewFlowBean = productReviewFlowBean;
	}

 	/**
	 * @return the stateTaxShipRateBean
	 */
	public StateTaxShipRateBean getStateTaxShipRateBean() {
		return stateTaxShipRateBean;
	}

	/**
	 * @param stateTaxShipRateBean the stateTaxShipRateBean to set
	 */
	public void setStateTaxShipRateBean(StateTaxShipRateBean stateTaxShipRateBean) {
		/*ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		stateTaxShipRateBean = (StateTaxShipRateBean) FacesContext.getCurrentInstance().getApplication()
			    .getELResolver().getValue(elContext, null, "stateTaxShipRateBean");*/
		this.stateTaxShipRateBean = stateTaxShipRateBean;
	}

	/**
	 * @return the productReviewBean
	 */
	public ProductReviewBean getProductReviewBean() {
		return productReviewBean;
	}

	/**
	 * @param productReviewBean the productReviewBean to set
	 */
	public void setProductReviewBean(ProductReviewBean productReviewBean) {
		this.productReviewBean = productReviewBean;
	}

	@Inject
	//@ManagedProperty(name="productReviewBean",value="#{productReviewBean}")
    private ProductReviewBean productReviewBean;
    
    public GarageSaleJSFActions() {
    }
    
    public String getItemImageName() {
		return itemImageName;
	}

	public void setItemImageName(String itemImageName) {
		this.itemImageName = "imgdir:"+itemImageName;
	}

	public CartItemWrapper get_cartItemWrapper() {
		return _cartItemWrapper;
	}

	public void set_cartItemWrapper(CartItemWrapper _cartItemWrapper) {
		this._cartItemWrapper = _cartItemWrapper;
	}

    public DataModel<CartItemWrapper> get_myCartModel() {
    	String methodName="get_myCartModel";
		if (_myCartModel == null) {
			logger.logp(Level.FINER, className, methodName, "null cart, build from session...");
			_myCartModel = new ListDataModel<CartItemWrapper>(shoppingCartManagedBean.getCartItemWrapperList() );
		}
		
		if (_myCartModel.getRowCount() == 0) {
			logger.logp(Level.FINER, className, methodName, "the rowCount is 0");
		} else {
			logger.logp(Level.FINEST, className, methodName, "rowCount is: " + _myCartModel.getRowCount());
		}
		
		return _myCartModel;
	}

	public void set_myCartModel(DataModel<CartItemWrapper> _myCartModel) {
		this._myCartModel = _myCartModel;
	}

	public DataModel<CartItemWrapper> get_myPurchaseModel() {
		return _myPurchaseModel;
	}

	public void set_myPurchaseModel(DataModel<CartItemWrapper> _myPurchaseModel) {
		this._myPurchaseModel = _myPurchaseModel;
	}

	private String checkOutMessage="";
	
	
	@ManagedProperty(name="garageSaleSessionBean",value="#{garageSaleSessionBean}")
	private GarageSaleSessionBean garageSaleSessionBean;
	
	@ManagedProperty(name="garageSaleManagedBeanUtil",value="#{garageSaleManagedBeanUtil}")
	private GarageSaleManagedBeanUtil garageSaleManagedBeanUtil;
	
	@ManagedProperty(name="shoppingCartManagedBean",value="#{shoppingCartManagedBean}")
	private  ShoppingCartManagedBean shoppingCartManagedBean;

	
	public  ShoppingCartManagedBean getShoppingCartManagedBean() {
		return shoppingCartManagedBean;
	}

	public void setShoppingCartManagedBean(
			ShoppingCartManagedBean shoppingCartManagedBean) {
		this.shoppingCartManagedBean = shoppingCartManagedBean;
	}

	
    public GarageSaleManagedBeanUtil getGarageSaleManagedBeanUtil() {
		return garageSaleManagedBeanUtil;
	}


	public void setGarageSaleManagedBeanUtil(
			GarageSaleManagedBeanUtil garageSaleManagedBeanUtil) {
		this.garageSaleManagedBeanUtil = garageSaleManagedBeanUtil;
	}


	public GarageSaleSessionBean getGarageSaleSessionBean() {
		return garageSaleSessionBean;
	}


	public void setGarageSaleSessionBean(GarageSaleSessionBean garageSaleSessionBean) {
		this.garageSaleSessionBean = garageSaleSessionBean;
	}


	public String getCheckOutMessage() {
		return checkOutMessage;
	}


	public void setCheckOutMessage(String checkOutMessage) {
		this.checkOutMessage = checkOutMessage;
	}


	public String checkOut() throws Exception{
    	String methodName = "checkOut";
		FacesContext facesContext=FacesContext.getCurrentInstance();
    	String output = "checkOutFailed";
    	// theoretically this checkOut should work on the cartItemList 
    	// so as to check everything out
    	
    	boolean isCheckOutGood = false;
    	List<CartItem> itemsList = null;
    	String customerId = null;
    	long startTime, endTime, duration;
    	
    	startTime = System.currentTimeMillis();
    	
    	//itemsList = garageSaleManagedBeanUtil.convertToObjectList();
    	
    	itemsList=garageSaleManagedBeanUtil.transferCartItemWrapperToCartItem(shoppingCartManagedBean.getCartItemWrapperList());
    	if (itemsList.isEmpty()) {
    		logger.logp(Level.FINE, className, methodName, "empty basket to check out?");
    		return FOUNDNULL;
    	}
    	
    	try {
    		customerId = garageSaleSessionBean.getCustomerID();
    	} catch (GSJSF12WebException gse) {
    		logger.logp(Level.SEVERE, className, methodName, "failed to obtain customerID: " + gse.getMessage());
    		gse.printStackTrace();
            return FOUNDNULL;
    	}
    	if (customerId == null) {
    		logger.logp(Level.SEVERE, className, methodName, "null customer ?");
            return FOUNDNULL;
    	} else if (customerId == "") {
    		logger.logp(Level.SEVERE, className, methodName, "empty customer ID?");
            return FOUNDNULL;
    	}
    	
    	logger.logp(Level.FINEST, className, methodName, "will check out for " + customerId + " with items " + itemsList );
		GSJSFWebUtil.printCurrentTimestamp(className, methodName,"Start");
    	isCheckOutGood = garageSaleStoreManager.checkOut(customerId, itemsList );
		GSJSFWebUtil.printCurrentTimestamp(className, methodName,"End");

    	if (isCheckOutGood) {
    		// we'll clean up the added cart list
    		// set up the purchase
    		_myPurchaseModel = _myCartModel;
    		ArrayList<CartItemWrapper> myPurchaseList= new ArrayList<CartItemWrapper>(shoppingCartManagedBean.getCartItemWrapperList());
			facesContext.getExternalContext().getSessionMap().put("myPurchaseList", myPurchaseList);
    		
    		// then set the cartItemList and cart to 0
    		shoppingCartManagedBean.setCartItemWrapperList(new ArrayList<CartItemWrapper>());
    		this._myCartModel = new ListDataModel<CartItemWrapper>(shoppingCartManagedBean.getCartItemWrapperList());
    		garageSaleSessionBean.setCartEmpty(true);
    		output = "checkOutSucceed";
    		//Move this destroy for invalidation logic. Other wise we create one more object.
    		//shoppingCartManagedBean.destroyScope();
    		// only care about when the checkout succeeds
    		endTime = System.currentTimeMillis();
    		duration = (endTime - startTime) / 1000;
    		logger.logp(Level.FINE, className, methodName, "checkout time (in seconds): " + duration);
    	} else {
    		GSJSFWebUtil.addErrorMessage("Checkout failed");
    	}
    	
    	return output;
    }


	public String fromCartToItem() {
		
		CartItemWrapper cartItemWrapper = (CartItemWrapper) _myCartModel.getRowData();
		set_cartItemWrapper(cartItemWrapper);
		
		// here I need to set the inventory
		return "fromCartToItem";
	}
	
	public String addToCart() throws Exception {
		String methodName = "addToCart";
    	String output = "notadded";
    	TaxRate taxRate=null;
    	// the page of this action is the shoppingCart
    	//this.set_locationString(SHOPPINGCARTPAGE);
    	
    	// for this action, we need to do the following:
    	// 1. obtain the cartitem model
    	// 2. obtain the current cartitem
    	// 3. add the cartitem into the datamodel list
    	//Added for WAS855 alpha
    	/**ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		stateTaxShipRateBean = (StateTaxShipRateBean) FacesContext.getCurrentInstance().getApplication()
			    .getELResolver().getValue(elContext, null, "stateTaxShipRateBean");*/
		
    	if(stateTaxShipRateBean.getCustomerStateID()==null || stateTaxShipRateBean.getCustomerStateID().trim().length()==0){
    		taxRate=garageSaleManagedBeanUtil.getTaxRate();
    		stateTaxShipRateBean.setCustomerStateID(taxRate.getStateId());
    		stateTaxShipRateBean.setTaxRate(taxRate.getTaxRate());
    	}
   	
    	String inventoryToUse = garageSaleSessionBean.get_inventoryChosen();
    	if (inventoryToUse == null || inventoryToUse.isEmpty()) {
    		logger.logp(Level.FINEST, className, methodName, "inventoryName attribute null, will try session variable");
    		inventoryToUse = garageSaleManagedBeanUtil.retrieveInventoryChosen();
    	}
    	 
    	if (inventoryToUse.equalsIgnoreCase("") || inventoryToUse == null) {
    		logger.logp(Level.SEVERE, className, methodName, "null inventory from session data?");
    		return FOUNDNULL;
    	}
    	
    	InventoryWrapper tempInventory = this.get_inventory();
    	if (tempInventory == null) {
    		logger.logp(Level.FINER, className, methodName, "null inventory?  need to retrive it");
			GSJSFWebUtil.printCurrentTimestamp(className, methodName,"Start");
    		tempInventory = garageSaleStoreManager.getInventory(inventoryToUse);
			GSJSFWebUtil.printCurrentTimestamp(className, methodName,"End");

    		if (tempInventory == null) {
    			logger.logp(Level.SEVERE, className, methodName, "retrieved null inventory? have to return...");
    			return FOUNDNULL;
    		}
    		
    		logger.logp(Level.FINER, className, methodName, "retrieved Inventory: " + tempInventory.getItemID());
    		// reset the inventory attribute
    		this.set_inventory(tempInventory);
    		//return FOUNDNULL;
    	} 
    	
    	FacesContext fc = FacesContext.getCurrentInstance();
		Application app = fc.getApplication();
		ExpressionFactory elFactory = app.getExpressionFactory();
		ELContext elContext = fc.getELContext();
		
    	CartItemWrapper thisItem = new CartItemWrapper();
    	thisItem.setItemID(tempInventory.getItemID());
    	garageSaleSessionBean.setNumberToPurchase(Integer.parseInt(garageSaleSessionBean.get_quantityString()));
    	thisItem.setItemCount(garageSaleSessionBean.getNumberToPurchase());
    	thisItem.setDescription(tempInventory.getDescription());
    	thisItem.setMfgName(tempInventory.getMfgName());
    	thisItem.setUnitPrice(tempInventory.getUnitPrice());
    	thisItem.setShipped(false);
		
    	
    	logger.logp(Level.FINEST, className, methodName, "set item [ id: " + thisItem.getItemID()
    			+ ", count: " + thisItem.getItemCount() 
    			+ ", description: " + thisItem.getDescription()
    			+ ", mfgName: " + thisItem.getMfgName()
    			+ ", unitPrice: " + thisItem.getUnitPrice() + " ]");

    	ArrayList<CartItemWrapper> localList=shoppingCartManagedBean.getCartItemWrapperList();
    	if (localList == null) {
    		logger.logp(Level.SEVERE, className, methodName, "how could the list still being null?" );
            return FOUNDNULL;
    	}
    	localList.add(thisItem);
    	
    	_myCartModel = new ListDataModel<CartItemWrapper>(localList);
    	garageSaleSessionBean.setCartEmpty(false);
    	// reset the numberToPurchase 
    	garageSaleSessionBean.setNumberToPurchase(0);
    	//localItems.
    	shoppingCartManagedBean.setCartItemWrapperList(localList);
    	ValueExpression itemListExp = elFactory.createValueExpression(elContext, "#{sessionScope.itemList}", List.class);
		itemListExp.setValue(elContext, localList);
		
    	garageSaleManagedBeanUtil.calculateSubTotal(localList);
		
		output = "addToCart";
    	return output;
    }

	
	public DataModel<CartItemWrapper> getMyPurchase() {
		String methodName = "getMyPurchase";
		FacesContext facesContext=FacesContext.getCurrentInstance();
		if (this._myPurchaseModel == null) {
			logger.logp(Level.FINE, className, methodName, "null purchase model, will get from session");
			ArrayList<CartItemWrapper>myPurchaseList=(ArrayList<CartItemWrapper>) facesContext.getExternalContext().getSessionMap().get("myPurchaseList");
			if(myPurchaseList!=null){
				this._myPurchaseModel = new ListDataModel<CartItemWrapper>( myPurchaseList );
			}
			
		}
		
		return _myPurchaseModel;
	}
	
    public DataModel<InventoryWrapper> getInventories() {
    	String methodName = "getInventories";
    	
    	String categoryToUse = garageSaleSessionBean.get_categoryChosen();
    	if (categoryToUse == null || categoryToUse.isEmpty()) {
    		logger.logp(Level.FINEST, className, methodName, "categoryName attribute null, will try session variable");
    		categoryToUse = garageSaleManagedBeanUtil.retrieveCategoryChosen();
    	}
    	
    	String mfgNameToUse = garageSaleSessionBean.get_mfgCategoryChosen();
    	if (mfgNameToUse == null || mfgNameToUse.isEmpty()) {
    		logger.logp(Level.FINEST, className, methodName, "mfgName attribute null, will try session variable");
    		mfgNameToUse = garageSaleManagedBeanUtil.retrieveMfgNameChosen();
    	}
		
		if ( mfgNameToUse == null || mfgNameToUse.isEmpty()) {
    		logger.logp(Level.SEVERE, className, methodName, "null mfgName from session expression?");
    		GSJSFWebUtil.addErrorMessage("null mfg name, cannot continue");	
            return null;
    	}
    	if ( categoryToUse == null || categoryToUse.isEmpty() ) {
    		logger.logp(Level.SEVERE, className, methodName, "null categoryName from session expression?");
    		GSJSFWebUtil.addErrorMessage("null category name, cannot continue");	
            return null;
    	}
    	
    	if (garageSaleStoreManager == null) {
    		logger.logp(Level.SEVERE, className, methodName, "null service, cannot continue");
    		GSJSFWebUtil.addErrorMessage("null service, cannot continue");	
    		return null;
    	}

    	logger.logp(Level.FINER, className, methodName, "will work on category : " + categoryToUse + " and mfg : " + mfgNameToUse);
		GSJSFWebUtil.printCurrentTimestamp(className, methodName,"Start");
		List<InventoryWrapper> inventoryList= garageSaleStoreManager.listInventory( categoryToUse, mfgNameToUse );
    	List<Object> tempInventoryList = new ArrayList<Object>(inventoryList);
		GSJSFWebUtil.printCurrentTimestamp(className, methodName,"End");

    	if ( tempInventoryList == null) {
    		logger.logp(Level.SEVERE, className, methodName, "null inventoryList for category: " 
    				+ categoryToUse + " for mfg: " + mfgNameToUse );
    		GSJSFWebUtil.addErrorMessage("Null inventorylist, cannot continue");
            return null;
    	}
    	if ( tempInventoryList.isEmpty()) {
    		logger.logp(Level.SEVERE, className, methodName, "empty inventoryList for category: " + categoryToUse);
    		GSJSFWebUtil.addErrorMessage("Empty inventorylist, cannot continue");	
    		return null;
    	} else {
    		if ( logger.isLoggable(Level.FINEST) ) {
    			Iterator<Object> itor = tempInventoryList.iterator();
    			String inventoriesString = "Inventories are : [ ";
    			while ( itor.hasNext() ) {
    				inventoriesString = inventoriesString + ((InventoryWrapper) itor.next()).getItemID() + " ";
    			}
    			inventoriesString = inventoriesString + " ]";
    			logger.logp(Level.FINEST, className, methodName, inventoriesString);
    		}
    	}
    	
    	this._inventoryModel = new ListDataModel(tempInventoryList);
    	logger.logp(Level.FINER, className, methodName, "set the inventoryModel ...");
    	
    	return this._inventoryModel;

    }
    
    public DataModel<MfgCategoryWrapper> getMfgCategories() {
    	String methodName = "getMfgCategories";
    	
    	String categoryNameToUse = garageSaleSessionBean.get_categoryChosen();
    	if (categoryNameToUse == null || categoryNameToUse.isEmpty()) {
    		logger.logp(Level.FINEST, className, methodName, "will try the saved variable");
    		categoryNameToUse = garageSaleManagedBeanUtil.retrieveCategoryChosen();
    	}
    	
    	if ( categoryNameToUse == null || categoryNameToUse.isEmpty() ) {
    		logger.logp(Level.SEVERE, className, methodName, "null category Name");
    		GSJSFWebUtil.addErrorMessage("null category name, cannot continue");	
    		return null;
    	}
    	
    	
    	//StoreManagerWrapperInterface tempService = GSJSFWebUtil.getStoreManagerPort();
    	
    	if (garageSaleStoreManager == null) {
    		logger.logp(Level.SEVERE, className, methodName, "null service entry obtained, cannot continue");
    		GSJSFWebUtil.addErrorMessage("null service, cannot continue");	
    		return null;
    	}
    	
    	logger.logp(Level.FINER, className, methodName, "work on category : " + categoryNameToUse );
		GSJSFWebUtil.printCurrentTimestamp(className, methodName,"Start");
    	List<Object> tempMfgCategoryList = new ArrayList<Object>(garageSaleStoreManager.listMfgCategories( categoryNameToUse ));
		GSJSFWebUtil.printCurrentTimestamp(className, methodName,"End");

    	if ( tempMfgCategoryList == null ) {
    		logger.logp(Level.SEVERE, className, methodName, "null mfgCategory for category: " + categoryNameToUse);
    		GSJSFWebUtil.addErrorMessage("null mfgCategory list, cannot continue");	
    		return null;	
    	}
    	if ( tempMfgCategoryList.isEmpty()) {
    		logger.logp(Level.SEVERE, className, methodName, "empty mfgCategory for category: " + categoryNameToUse );
    		GSJSFWebUtil.addErrorMessage("Empty mfgCategory list, cannot continue");	
    		return null;
    	} else {
    		if ( logger.isLoggable(Level.FINEST) ) {
    			Iterator<Object> itor = tempMfgCategoryList.iterator();
    			String mfgCategoriesString = "mfgCategories are : [ ";
    			while ( itor.hasNext() ) {
    				mfgCategoriesString = mfgCategoriesString + ((MfgCategoryWrapper) itor.next()).getMfgName() + " ";
    			}
    			mfgCategoriesString = mfgCategoriesString + " ]";
    			logger.logp(Level.FINEST, className, methodName, mfgCategoriesString);
    			
    		}
    	}
    	
    	this._mfgModel = new ListDataModel( tempMfgCategoryList );
    	logger.logp(Level.FINER, className, methodName, "set the categoryModel ...");
    	
    	return this._mfgModel;
    }

    public DataModel<CategoryWrapper> getCategories() {
    	String methodName = "getCategories";
    	
    	// set stage, hopefully this is only set at the beginning
    	/**if ( this.get_locationString() == null ) {
    		this.set_locationString(WELCOMEPAGE);
    	}*/
    	
    	if (this._categoryModel != null) {
    		logger.logp(Level.FINEST, className, methodName, "already have the categories model, go ahead and return...");
    		return this._categoryModel;
    	}
    	
    	//StoreManagerWrapperInterface localService = GSJSFWebUtil.getStoreManagerPort();
    	
    	if (garageSaleStoreManager == null) {
    		logger.logp(Level.SEVERE, className, methodName, "service entry is null? return");
    		GSJSFWebUtil.addErrorMessage("failed retrieving service, please try again later");
    		return null;
    	}
    	//List<Object> tempCategoryList = getListFromVector(localService.listCategories());
		GSJSFWebUtil.printCurrentTimestamp(className, methodName,"Start");
    	List<Object> tempCategoryList =new ArrayList<Object>(garageSaleStoreManager.listCategories());
		GSJSFWebUtil.printCurrentTimestamp(className, methodName,"End");

    	if (tempCategoryList == null) {
    		logger.logp(Level.SEVERE, className, methodName, "categoryList is null? return");
    		GSJSFWebUtil.addErrorMessage("failed retrieving categories, please try again later");
    		return null;
    	}
    	if (tempCategoryList.isEmpty()) {
    		logger.logp(Level.SEVERE, className, methodName, "categoryList is empty");
    		GSJSFWebUtil.addErrorMessage("category retriving failed, please try again later");
            return null;
    	} else {
    		if ( logger.isLoggable(Level.FINEST) ) {
    			Iterator<Object> itor = tempCategoryList.iterator();
    			
    			String categoriesString = "Categories are: [ ";
    			while ( itor.hasNext() ) {
    				categoriesString = categoriesString + ((CategoryWrapper)itor.next()).getCategoryName() + " " ;
    				
    			}
    			categoriesString = categoriesString + "]";
    			logger.logp(Level.FINER, className, methodName, categoriesString);
    			
    		}
    	}
        
    	if ( this._categoryModel==null ){
            this._categoryModel = new ListDataModel( tempCategoryList );
            logger.logp(Level.FINER, className, methodName, "set the categoryModel ...");
        }
    	
        return this._categoryModel;    	
    }
    
	public CategoryWrapper findCategory(String id) throws GSJSF12WebException {
		String methodName = "findCategory";
		
		//StoreManagerWrapperInterface localService = GSJSFWebUtil.getStoreManagerPort();
		
		if (garageSaleStoreManager == null) {
    		logger.logp(Level.SEVERE, className, methodName, "null wrapper service?");
            return null;
    	}
    	logger.logp(Level.FINEST, className, className, "about to find category: " + id);
		GSJSFWebUtil.printCurrentTimestamp(className, methodName,"Start");
    	CategoryWrapper localCategory = garageSaleStoreManager.getCategory(id);
		GSJSFWebUtil.printCurrentTimestamp(className, methodName,"End");

		if (localCategory == null) {
			logger.logp(Level.SEVERE, className, methodName, "category " + id + " found null?");
            throw new GSJSF12WebException("failed to obtain category: " + id);
		}
		return localCategory;
	}
	
	
    public String detailSetup() {
    	String methodName = "detailSetup";
    	
    	// set the location to Mfg
    	//this.set_locationString(MFGPAGE);
    	
    	FacesContext fc = FacesContext.getCurrentInstance();
		Application app = fc.getApplication();
		ExpressionFactory elFactory = app.getExpressionFactory();
		ELContext elContext = fc.getELContext();
    	
    	if (this._categoryModel == null) {
    		logger.logp(Level.SEVERE, className, methodName, "the _categoryModel is still null, go to error page");
    		GSJSFWebUtil.addErrorMessage("found unexpected condition, will jump to error page");
    		return FOUNDNULL;
    	}
    	CategoryWrapper tempCategory = (CategoryWrapper) this._categoryModel.getRowData();
    	if (tempCategory == null) {
    		logger.logp(Level.SEVERE, className, methodName, "the category from the dataModel is null, go to error page");
    		GSJSFWebUtil.addErrorMessage("found unexpected condition, will jump to error page");
    		return FOUNDNULL;
    	}
    	logger.logp(Level.FINER, className, methodName, "chosen category : " + tempCategory.getCategoryName());
    	set_category(tempCategory);
    	
    	String categoryString = tempCategory.getCategoryName();
    	ValueExpression categoryExp = elFactory.createValueExpression(elContext, "#{sessionScope.categoryChosen}", java.lang.String.class);
		categoryExp.setValue(elContext, categoryString);
		
		// also save to attribute
		garageSaleSessionBean.set_categoryChosen( categoryString );
		logger.logp(Level.FINER, className, methodName, "saved categoryName : " + garageSaleSessionBean.get_categoryChosen());
    		
        return "category_detail";
    }
    
    
    public String prFlowDetailSetup() {
    	String methodName = "detailSetup";
    	
    	// set the location to Mfg
    	//this.set_locationString(MFGPAGE);
    	
    	FacesContext fc = FacesContext.getCurrentInstance();
		Application app = fc.getApplication();
		ExpressionFactory elFactory = app.getExpressionFactory();
		ELContext elContext = fc.getELContext();
    	
    	if (this._categoryModel == null) {
    		logger.logp(Level.SEVERE, className, methodName, "the _categoryModel is still null, go to error page");
    		GSJSFWebUtil.addErrorMessage("found unexpected condition, will jump to error page");
    		return FOUNDNULL;
    	}
    	CategoryWrapper tempCategory = (CategoryWrapper) this._categoryModel.getRowData();
    	if (tempCategory == null) {
    		logger.logp(Level.SEVERE, className, methodName, "the category from the dataModel is null, go to error page");
    		GSJSFWebUtil.addErrorMessage("found unexpected condition, will jump to error page");
    		return FOUNDNULL;
    	}
    	logger.logp(Level.FINER, className, methodName, "chosen category : " + tempCategory.getCategoryName());
    	set_category(tempCategory);
    	
    	String categoryString = tempCategory.getCategoryName();
    	ValueExpression categoryExp = elFactory.createValueExpression(elContext, "#{sessionScope.categoryChosen}", java.lang.String.class);
		categoryExp.setValue(elContext, categoryString);
		
		// also save to attribute
		garageSaleSessionBean.set_categoryChosen( categoryString );
		try {
			productReviewFlowBean.setCustomerID(garageSaleSessionBean.getCustomerID());
		} catch (GSJSF12WebException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		productReviewFlowBean.setCategoryChosen(categoryString);
		logger.logp(Level.FINER, className, methodName, "saved categoryName : " + garageSaleSessionBean.get_categoryChosen());
    		
        return "productReviewFlow-Mfg";
    }

    
    public String goToItem() {
    	String methodName = "goToItem";
    	
    	// the page will be the item
    	//this.set_locationString(ITEMPAGE);
    	
    	FacesContext fc = FacesContext.getCurrentInstance();
		Application app = fc.getApplication();
		ExpressionFactory elFactory = app.getExpressionFactory();
		ELContext elContext = fc.getELContext();
    	
    	if (this._inventoryModel == null) {
    		logger.logp(Level.SEVERE, className, methodName, "the _inventoryModel is still null, go to error page");
    		GSJSFWebUtil.addErrorMessage("found unexpected condition, will jump to error page");
    		return FOUNDNULL;
    	}
    	
    	InventoryWrapper tempInventory = (InventoryWrapper) this._inventoryModel.getRowData();
    	if (tempInventory == null) {
    		// what if this happens
    		logger.logp(Level.SEVERE, className, methodName, "the mfgCategory from the dataModel is null, go to error page");
    		GSJSFWebUtil.addErrorMessage("found unexpected condition, will jump to error page");
    		return FOUNDNULL;
    	}
    	logger.logp(Level.FINEST, className, methodName, "chosen inventory : " + tempInventory.getItemID());
    	this.set_inventory(tempInventory);
    	
    	String itemID = tempInventory.getItemID();
    	ValueExpression inventoryExp = elFactory.createValueExpression(elContext, "#{sessionScope.inventoryChosen}", java.lang.String.class);
		inventoryExp.setValue(elContext, itemID);
		this.setItemImageName(itemID+".jpg");
		
		ResourceHandler rHandler = fc.getApplication().getResourceHandler();
		Resource resource = rHandler.createResource(itemID+".jpg","imgdir");
		if(resource == null){
			logger.logp(Level.FINEST, className, methodName, "The resource not found setting random name for the image.");
			this.setItemImageName("CATEGORY80059.jpg");
		}

		garageSaleSessionBean.set_inventoryChosen(itemID);
		logger.logp(Level.FINEST, className, methodName, "saved item : " + garageSaleSessionBean.get_inventoryChosen());
    	return "item_detail";
    }
    
    public String goToPRFlowItem() {
    	String methodName = "goToPRFlowItem";
    	
    	// the page will be the item
    	//this.set_locationString(ITEMPAGE);
    	
    	FacesContext fc = FacesContext.getCurrentInstance();
		Application app = fc.getApplication();
		ExpressionFactory elFactory = app.getExpressionFactory();
		ELContext elContext = fc.getELContext();
    	
    	if (this._inventoryModel == null) {
    		logger.logp(Level.SEVERE, className, methodName, "the _inventoryModel is still null, go to error page");
    		GSJSFWebUtil.addErrorMessage("found unexpected condition, will jump to error page");
    		return FOUNDNULL;
    	}
    	
    	InventoryWrapper tempInventory = (InventoryWrapper) this._inventoryModel.getRowData();
    	if (tempInventory == null) {
    		// what if this happens
    		logger.logp(Level.SEVERE, className, methodName, "the mfgCategory from the dataModel is null, go to error page");
    		GSJSFWebUtil.addErrorMessage("found unexpected condition, will jump to error page");
    		return FOUNDNULL;
    	}
    	logger.logp(Level.FINEST, className, methodName, "chosen inventory : " + tempInventory.getItemID());
    	this.set_inventory(tempInventory);
    	
    	String itemID = tempInventory.getItemID();
    	ValueExpression inventoryExp = elFactory.createValueExpression(elContext, "#{sessionScope.inventoryChosen}", java.lang.String.class);
		inventoryExp.setValue(elContext, itemID);
		this.setItemImageName(itemID+".jpg");
		
		ResourceHandler rHandler = fc.getApplication().getResourceHandler();
		Resource resource = rHandler.createResource(itemID+".jpg","imgdir");
		if(resource == null){
			logger.logp(Level.FINEST, className, methodName, "The resource not found setting random name for the image.");
			this.setItemImageName("CATEGORY80059.jpg");
		}

		garageSaleSessionBean.set_inventoryChosen(itemID);
		productReviewFlowBean.setInventoryChosen(itemID);
		logger.logp(Level.FINEST, className, methodName, "saved item : " + garageSaleSessionBean.get_inventoryChosen());
    	return "productReviewFlow-Item";
    }

    public String goToInventory() {
    	String methodName = "goToInventory";
    	
    	// the location will be inventory
    	//this.set_locationString(INVENTORYPAGE);
    	
    	FacesContext fc = FacesContext.getCurrentInstance();
		Application app = fc.getApplication();
		ExpressionFactory elFactory = app.getExpressionFactory();
		ELContext elContext = fc.getELContext();
    	
    	if (this._mfgModel == null) {
    		logger.logp(Level.SEVERE, className, methodName, "the _mfgModel is still null, go to error page");
    		GSJSFWebUtil.addErrorMessage("found unexpected condition, will jump to error page");
    		return FOUNDNULL;
    	}
    	
    	MfgCategoryWrapper tempMfg = (MfgCategoryWrapper) this._mfgModel.getRowData();
    	if (tempMfg == null) {
    		// what if this happens
    		logger.logp(Level.SEVERE, className, methodName, "the mfgCategory from the dataModel is null, go to error page");
    		GSJSFWebUtil.addErrorMessage("found unexpected condition, will jump to error page");
    		return FOUNDNULL;
    	}
    	logger.logp(Level.FINEST, className, methodName, "chosen mfgCategory : " + tempMfg.getMfgName());
    	set_mfgCategory(tempMfg);
    	
    	String mfgName = tempMfg.getMfgName();
    	ValueExpression mfgCategoryExp = elFactory.createValueExpression(elContext, "#{sessionScope.mfgChosen}", java.lang.String.class);
		mfgCategoryExp.setValue(elContext, mfgName);
		
		garageSaleSessionBean.set_mfgCategoryChosen(mfgName);
		logger.logp(Level.FINEST, className, methodName, "saved mfgName : " + garageSaleSessionBean.get_mfgCategoryChosen());
        return "inventory_detail";
    }

    public String goToPRFlowInventory() {
    	String methodName = "goToPRFlowInventory";
    	
    	// the location will be inventory
    	//this.set_locationString(INVENTORYPAGE);
    	
    	FacesContext fc = FacesContext.getCurrentInstance();
		Application app = fc.getApplication();
		ExpressionFactory elFactory = app.getExpressionFactory();
		ELContext elContext = fc.getELContext();
    	
    	if (this._mfgModel == null) {
    		logger.logp(Level.SEVERE, className, methodName, "the _mfgModel is still null, go to error page");
    		GSJSFWebUtil.addErrorMessage("found unexpected condition, will jump to error page");
    		return FOUNDNULL;
    	}
    	
    	MfgCategoryWrapper tempMfg = (MfgCategoryWrapper) this._mfgModel.getRowData();
    	if (tempMfg == null) {
    		// what if this happens
    		logger.logp(Level.SEVERE, className, methodName, "the mfgCategory from the dataModel is null, go to error page");
    		GSJSFWebUtil.addErrorMessage("found unexpected condition, will jump to error page");
    		return FOUNDNULL;
    	}
    	logger.logp(Level.FINEST, className, methodName, "chosen mfgCategory : " + tempMfg.getMfgName());
    	set_mfgCategory(tempMfg);
    	
    	String mfgName = tempMfg.getMfgName();
    	ValueExpression mfgCategoryExp = elFactory.createValueExpression(elContext, "#{sessionScope.mfgChosen}", java.lang.String.class);
		mfgCategoryExp.setValue(elContext, mfgName);
		
		garageSaleSessionBean.set_mfgCategoryChosen(mfgName);
		productReviewFlowBean.setMfgChosen(mfgName);
		logger.logp(Level.FINEST, className, methodName, "saved mfgName : " + garageSaleSessionBean.get_mfgCategoryChosen());
        return "productReviewFlow-Inventory";
    }

    
    
    
	public DataModel<CartItemWrapper> getCartItems() {
		String methodName = "getCartItems";
		
		// every time this method is called, the caller is responsible
		// for making sure that the data is meaningful
		if (_myCartModel == null) {
			logger.logp(Level.FINER, className, methodName, "cartModel is null, will construct");
			_myCartModel = new ListDataModel<CartItemWrapper>();
		}
		return _myCartModel;
	}

	/**
	 * 
	 * @return
	 * @throws Exception 
	 */
	public String updateCart() throws Exception {
		String methodName="updateCart";
		String output = "updateFailed";
	    FacesContext fc = FacesContext.getCurrentInstance();
	    Application app = fc.getApplication();
	    ExpressionFactory elFactory = app.getExpressionFactory();
	    ELContext elContext = fc.getELContext();
		logger.logp(Level.FINE, className, methodName, "Updating quntity and calculating subTotal in shoppingCart." );
		
	    //Retrieve current update list from the ShoppingCart view.
		ArrayList<CartItemWrapper> localList=convertDataModelToList(_myCartModel);
    	if (localList == null) {
    		logger.logp(Level.SEVERE, className, methodName, "how could the list still being null?" );
            return FOUNDNULL;
    	}
    	
    	garageSaleSessionBean.setCartEmpty(false);
    	shoppingCartManagedBean.setCartItemWrapperList(localList);
    	//localItems.
    	ValueExpression itemListExp = elFactory.createValueExpression(elContext, "#{sessionScope.itemList}", List.class);
		itemListExp.setValue(elContext, localList);
    	_myCartModel = new ListDataModel<CartItemWrapper>(localList);
		
    	garageSaleManagedBeanUtil.calculateSubTotal(localList);
		output = "updateSuccess";
		return output;
	}


	/**
	 * 
	 * @return
	 */
	public String showItem(){
		String implNavig="error";
		String methodName="showItem";
		CartItemWrapper cartItem = (CartItemWrapper) _myCartModel.getRowData();
    	//StoreManagerWrapperInterface localService = GSJSFWebUtil.getStoreManagerPort();
    	
    	
    	if (garageSaleStoreManager == null) {
    		logger.logp(Level.SEVERE, className, methodName, "service entry is null? return");
    		GSJSFWebUtil.addErrorMessage("failed retrieving service, please try again later");
    		return implNavig;
    	}
		GSJSFWebUtil.printCurrentTimestamp(className, methodName,"Start");

		InventoryWrapper inventory = garageSaleStoreManager.getInventory(cartItem.getItemID());
		GSJSFWebUtil.printCurrentTimestamp(className, methodName,"End");

		this._inventory=inventory;
		this.setItemImageName(inventory.getItemID()+".jpg");
		set_cartItemWrapper(cartItem);
		if(cartItem!=null){
			implNavig="showItem";
		}
		return implNavig;
	}
	
	public CategoryWrapper get_category() {
		return _category;
	}

	public void set_category(CategoryWrapper _category) {
		this._category = _category;
	}
	
	public CartItem getCartItem() {
		String methodName = "getCartItem";
		if (this._cartItem == null) {
			logger.logp(Level.SEVERE, className, methodName, "null valued cartitem will be returned");
		}
		return this._cartItem;
	}

	public void setCartItem(CartItem cartItem) {
		String methodName = "setCartItem";
		if (cartItem == null) {
			logger.logp(Level.SEVERE, className, methodName, "null cartItem will be set");
			this._cartItem = null;
		} else {
			this._cartItem = cartItem;
			logger.logp(Level.FINER, className, methodName, "cartItem set: " + this.getCartItem().getItemID());
		}
	}
	
	public InventoryWrapper get_inventory() {
		return _inventory;
	}

	public void set_inventory(InventoryWrapper inventory) {
		this._inventory = inventory;
	}
	
	
	public CustomerInfoWrapper get_customerInfo() {
    	String methodName = "get_customerInfo";
    	
    	if (this._customerInfo == null) {
    		CustomerInfoWrapper tempUserInfo;
			try {
				GSJSFWebUtil.printCurrentTimestamp(className, methodName,"Start");
				tempUserInfo = garageSaleStoreManager.getCustomerInfo(garageSaleSessionBean.getCustomerID());
				GSJSFWebUtil.printCurrentTimestamp(className, methodName,"End");

	    		logger.logp(Level.FINER, className, methodName, "customer id: " + tempUserInfo.getCustID());
			} catch (GSJSF12WebException e) {
				e.printStackTrace();
			}
    	}
		return _customerInfo;
	}

	public void set_customerInfo(CustomerInfoWrapper _customerInfo) {
		
		this._customerInfo = _customerInfo;
	}
	
	public MfgCategoryWrapper get_mfgCategory() {
		return _mfgCategory;
	}

	public void set_mfgCategory(MfgCategoryWrapper category) {
		_mfgCategory = category;
	}
	
	/**
	 * 
	 * @param currentDataModel
	 * @return
	 * @throws Exception
	 */
	public ArrayList<CartItemWrapper> convertDataModelToList(DataModel<CartItemWrapper> currentDataModel) throws Exception{
		ArrayList<CartItemWrapper> cartItemList=new ArrayList<>();		
		if(currentDataModel!=null){
			
			Iterator<CartItemWrapper> iterator=currentDataModel.iterator();
			while(iterator.hasNext()){
				CartItemWrapper cartItem=iterator.next();
				cartItemList.add(cartItem);
			}
		}
		return cartItemList;
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public String productReview() throws Exception {
		String methodName="productReview";
		String reviewProduct="/facelets/reviewProduct";
		logger.logp(Level.FINER, className, methodName, "Returning productReviewFacelet");
		return reviewProduct;
	}

	public String prFlowProductReview() throws Exception {
		String methodName="prFlowProductReview";
		String reviewProduct="productReviewFlow-Review";
		logger.logp(Level.FINER, className, methodName, "Returning productReviewFacelet");
		return reviewProduct;
	}

	public String productReviewFlow(){
		String methodName="productReviewFlow";
		String outCome="productReviewFlow";
		try{
			outCome="productReviewFlow";
			return outCome;
		}
		catch(Exception e){
			outCome="error";
			return outCome;
		}
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public String saveProductReview() throws Exception {
		String methodName="saveProductReview";
		String outCome="/facelets/saveReviewSuccess";
		logger.logp(Level.FINER, className, methodName, "saving product review");
		FacesContext fCtx=FacesContext.getCurrentInstance();
		try{
			ProdReview prodReview = new ProdReview();
			prodReview.setComments(productReviewBean.getProductReviewText());
			prodReview.setCustId(garageSaleSessionBean.getCustomerID());
			prodReview.setItemId(garageSaleSessionBean.get_inventoryChosen());
			garageSaleManagedBeanUtil.saveProductReview(prodReview);
		}catch(Exception e){
			FacesMessage message= 
				new FacesMessage(FacesMessage.SEVERITY_ERROR,"Save Product Review Failed",null);
			fCtx.addMessage(null, message);
			outCome="error";
			e.printStackTrace();
			return outCome;
		}
		return outCome;
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public String saveProductReviewFlow() throws Exception {
		String methodName="saveProductReviewFlow";
		String outCome="/facelets/productReviewFlow-Return";
		logger.logp(Level.FINER, className, methodName, "saving product review");
		FacesContext fCtx=FacesContext.getCurrentInstance();
		try{
			ProdReview prodReview = new ProdReview();
			prodReview.setComments(productReviewFlowBean.getProductReview());
			prodReview.setCustId(productReviewFlowBean.getCustomerID());
			prodReview.setItemId(productReviewFlowBean.getInventoryChosen());
			garageSaleManagedBeanUtil.saveProductReview(prodReview);
		}catch(Exception e){
			FacesMessage message= 
				new FacesMessage(FacesMessage.SEVERITY_ERROR,"Save Product Review Failed",null);
			fCtx.addMessage(null, message);
			outCome="error";
			e.printStackTrace();
			return outCome;
		}
		return outCome;
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public String welcomePage() throws Exception{
		String outCome="/facelets/welcomeUser";
		try{
			outCome="/facelets/welcomeUser";
			return outCome;
		}
		catch(Exception e){
			outCome="error";
			return outCome;
		}
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public String showShoppingCart() throws Exception{
		String outCome="/facelets/showShoppingCart";
		try{
			outCome="/facelets/showShoppingCart";
			return outCome;
		}
		catch(Exception e){
			outCome="error";
			return outCome;
		}
	}

	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public String customerInfoCC() throws Exception{
		String outCome="customerInfoCC";
		try{
			outCome="customerInfoCC";
			return outCome;
		}
		catch(Exception e){
			outCome="error";
			return outCome;
		}
	}
	
	
	
	/**
	 * 
	 */
	public void triggerException(){
		throw new ViewExpiredException("ViewExpired");
	}
	
}

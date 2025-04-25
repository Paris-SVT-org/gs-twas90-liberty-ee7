/**
 * 
 */
package com.ibm.websphere.svt.gs.gsjsfweb.utils;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;

import org.apache.wink.client.ClientConfig;
import org.apache.wink.client.EntityType;
import org.apache.wink.client.Resource;
import org.apache.wink.client.RestClient;

import com.ibm.websphere.svt.gs.cart.beans.CartItemWrapper;
import com.ibm.websphere.svt.gs.gsdb.jaxws.client.CartItem;
import com.ibm.websphere.svt.gs.gsjsfweb.GarageSaleSessionBean;
import com.ibm.websphere.svt.gs.gsjsfweb.ShoppingCartManagedBean;
import com.ibm.websphere.svt.gs.gsjsfweb.StateTaxShipRateBean;
import com.ibm.websphere.svt.gs.tax.entity.ProdReview;
import com.ibm.websphere.svt.gs.tax.entity.ShipRate;
import com.ibm.websphere.svt.gs.tax.entity.TaxRate;

/**
 * @author root
 *
 */
@ManagedBean(name="garageSaleManagedBeanUtil")
@RequestScoped
public class GarageSaleManagedBeanUtil implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8638355277735476891L;
	private static String componentName = "com.ibm.websphere.svt.gs.gsjsfweb.utils";
	private static Logger logger = Logger.getLogger(componentName);
	private static String className = GarageSaleManagedBeanUtil.class.getName();
	@ManagedProperty(name="shoppingCartManagedBean",value="#{shoppingCartManagedBean}")
	private  ShoppingCartManagedBean shoppingCartManagedBean;
    @Inject
	//@ManagedProperty(name="stateTaxShipRateBean",value="#{stateTaxShipRateBean}")	
    private StateTaxShipRateBean stateTaxShipRateBean;
	
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
		this.stateTaxShipRateBean = stateTaxShipRateBean;
	}


        
    
    public GarageSaleManagedBeanUtil() {
    }
    
	@ManagedProperty(name="garageSaleSessionBean",value="#{garageSaleSessionBean}")
	private GarageSaleSessionBean garageSaleSessionBean;
	
	public GarageSaleSessionBean getGarageSaleSessionBean() {
		return garageSaleSessionBean;
	}

	public void setGarageSaleSessionBean(GarageSaleSessionBean garageSaleSessionBean) {
		this.garageSaleSessionBean = garageSaleSessionBean;
	}
	public  ShoppingCartManagedBean getShoppingCartManagedBean() {
		return shoppingCartManagedBean;
	}

	public void setShoppingCartManagedBean(
			ShoppingCartManagedBean shoppingCartManagedBean) {
		this.shoppingCartManagedBean = shoppingCartManagedBean;
	}

	/**
	 * 
	 * @param localList
	 */
	
	public void calculateSubTotal(List<CartItemWrapper> localList){
		String methodName = "saveItemListToSession";
		FacesContext fc = FacesContext.getCurrentInstance();
		Application app = fc.getApplication();
		try{
			ExpressionFactory elFactory = app.getExpressionFactory();
			ELContext elContext = fc.getELContext();
			double shipCostTotal=0.00d;
			int listSize = localList.size();
			ValueExpression itemListSizeExp = elFactory.createValueExpression(elContext, "#{sessionScope.itemListSize}", Integer.class);
			itemListSizeExp.setValue(elContext, listSize);
			logger.logp(Level.FINE, className, methodName, "will save a " + listSize + " element list");
			double tempSubTotal=0.00d;
			double tempTotal=0.00d;
			NumberFormat formatter = new DecimalFormat("#.00");
	    	
			CartItemWrapper tempItem;
			Iterator<CartItemWrapper> itor = localList.iterator();
			int i = 0;
			while (itor.hasNext()) {
				tempItem = itor.next();
				String itemDesc = tempItem.getDescription();
				String itemID = tempItem.getItemID();
				String itemMfgName = tempItem.getMfgName();
				int itemCount = tempItem.getItemCount();
				float itemPrice = tempItem.getUnitPrice();
				ShipRate shipRate=getShipRateByItemId(itemID);
				shipCostTotal=shipCostTotal+shipRate.getCost();
				tempSubTotal=tempSubTotal+(itemCount*itemPrice);
				logger.logp(Level.FINEST, className, methodName, "saved the " + i + "th element, " +
						"\titemID: " + itemID + "\titemDesc: " + itemDesc +
						"\tmfgName: " + itemMfgName + "\tprice:" + itemPrice +
						"\tcount: " + itemCount);
				i++;
			}
			shoppingCartManagedBean.setSubTotal(formatter.format(tempSubTotal));
			double taxAmount=(tempSubTotal*(stateTaxShipRateBean.getTaxRate()/100));
			stateTaxShipRateBean.setTaxAmount(formatter.format(taxAmount));
			stateTaxShipRateBean.setShippingCost(formatter.format(shipCostTotal));
			tempTotal= tempSubTotal+taxAmount+shipCostTotal;
			stateTaxShipRateBean.setTotal(formatter.format(tempTotal));
			
			
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
    public  String retrieveCategoryChosen() {
    	String methodName = "retrieveCategoryChosen";
    	FacesContext fc = FacesContext.getCurrentInstance();
		Application app = fc.getApplication();
		ExpressionFactory elFactory = app.getExpressionFactory();
		ELContext elContext = fc.getELContext();
    	
		ValueExpression categoryExp = elFactory.createValueExpression(elContext, "#{sessionScope.categoryChosen}", java.lang.String.class);
		if (categoryExp == null) {
			logger.logp(Level.SEVERE, className, methodName, "categoryExpression null ? ");
			GSJSFWebUtil.addErrorMessage("null category expression, cannot continue");	
			return null;
		}
		String tempCategoryName = (String) categoryExp.getValue(elContext);
		if (tempCategoryName == null || tempCategoryName.equals("")) {
			logger.logp(Level.SEVERE, className, methodName, "how come the category entry is null");
			GSJSFWebUtil.addErrorMessage("null category, cannot continue");	
			return null;
		}
		logger.logp(Level.FINER, className, methodName, "found categoryName : " + tempCategoryName);
    	return tempCategoryName;
    }
    
    public  String retrieveMfgNameChosen() {
		String methodName = "retrieveMfgNameChosen";
		
		FacesContext fc = FacesContext.getCurrentInstance();
		Application app = fc.getApplication();
		ExpressionFactory elFactory = app.getExpressionFactory();
		ELContext elContext = fc.getELContext();
    	
		ValueExpression mfgExp = elFactory.createValueExpression(elContext, "#{sessionScope.mfgChosen}", java.lang.String.class);
		if (mfgExp == null) {
			logger.logp(Level.SEVERE, className, methodName, "mfgExpression null ? ");
			GSJSFWebUtil.addErrorMessage("null mfg expression, cannot continue");	
			return null;
		}
		String tempMfgName = (String) mfgExp.getValue(elContext);
		if (tempMfgName == null || tempMfgName.equalsIgnoreCase("")) {
			logger.logp(Level.SEVERE, className, methodName, "how come the mfg entry is null");
			GSJSFWebUtil.addErrorMessage("null mfg, cannot continue");	
			return null;
		}
    	
    	logger.logp(Level.FINER, className, methodName, "found mfgName : " + tempMfgName);
    	return tempMfgName;
	}

    public  String retrieveInventoryChosen() {
    	String methodName = "retrieveInventoryChosen";
    	FacesContext fc = FacesContext.getCurrentInstance();
		Application app = fc.getApplication();
		ExpressionFactory elFactory = app.getExpressionFactory();
		ELContext elContext = fc.getELContext();
    	
		ValueExpression inventoryExp = elFactory.createValueExpression(elContext, "#{sessionScope.inventoryChosen}", java.lang.String.class);
		if (inventoryExp == null) {
			logger.logp(Level.SEVERE, className, methodName, "categoryExpression null ? ");
			GSJSFWebUtil.addErrorMessage("null category expression, cannot continue");	
			return null;
		}
		String tempInventoryName = (String) inventoryExp.getValue(elContext);
		if (tempInventoryName == null || tempInventoryName.equals("")) {
			logger.logp(Level.SEVERE, className, methodName, "how come the category entry is null");
			GSJSFWebUtil.addErrorMessage("null category, cannot continue");	
			return null;
		}
		logger.logp(Level.FINER, className, methodName, "found inventoryName : " + tempInventoryName);
    	return tempInventoryName;
    }
    
    
    public  List<Object> convertToObjectList()throws Exception{
    	//String methodName = "convertToObjectList";
		List<CartItemWrapper> cartItemWrapperList=shoppingCartManagedBean.getCartItemWrapperList();
    	List<CartItem> retrievedList = transferCartItemWrapperToCartItem(cartItemWrapperList);
    	List<Object> objectList = new ArrayList<Object>();
    	Iterator<CartItem> itor = retrievedList.iterator();
    	//Added for JAVA 8 Lamda Expressions
    	itor.forEachRemaining((item)-> objectList.add(item));
    	/*while (itor.hasNext()) {
    		objectList.add( itor.next());
    	}*/
    	return objectList;
    }
    
    
    public  void addSuccessMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        FacesContext fc = FacesContext.getCurrentInstance();
        fc.addMessage("successInfo", facesMsg);
    }
	
    /**
     * 
     * @param cartItemWrapperList
     * @return
     * @throws Exception
     */
	public List<CartItem> transferCartItemWrapperToCartItem(List<CartItemWrapper> cartItemWrapperList) throws Exception{
		String methodName="transferCartItemWrapperToCartItem";
		logger.logp(Level.FINE, className, methodName, "Doing  transferCartItemWrapperToCartItem");
		List<CartItem> cartItemList=new ArrayList<CartItem>();
		if(cartItemWrapperList!=null && !cartItemWrapperList.isEmpty()){
			// Added for JAVA 8 Lamda Expressions
			cartItemWrapperList.forEach(cartItemWrapper -> {
				CartItem cartItem=new CartItem();
				cartItem.setDescription(cartItemWrapper.getDescription());
				cartItem.setItemCount(cartItemWrapper.getItemCount());
				cartItem.setItemID(cartItemWrapper.getItemID());
				cartItem.setMfgName(cartItemWrapper.getMfgName());
				cartItem.setShipped(cartItemWrapper.isShipped());
				cartItem.setUnitPrice(cartItemWrapper.getUnitPrice());
				cartItemList.add(cartItem);
			});
			
			//Commented out for JAVA 8 lamda expressions.
			/*Iterator<CartItemWrapper> cartItemWrapperIterator=cartItemWrapperList.iterator();
			while(cartItemWrapperIterator.hasNext()){
				CartItemWrapper cartItemWrapper=cartItemWrapperIterator.next();
				CartItem cartItem=new CartItem();
				cartItem.setDescription(cartItemWrapper.getDescription());
				cartItem.setItemCount(cartItemWrapper.getItemCount());
				cartItem.setItemID(cartItemWrapper.getItemID());
				cartItem.setMfgName(cartItemWrapper.getMfgName());
				cartItem.setShipped(cartItemWrapper.isShipped());
				cartItem.setUnitPrice(cartItemWrapper.getUnitPrice());
				cartItemList.add(cartItem);
			}*/
			return cartItemList;
		}
		else {
			logger.logp(Level.SEVERE, className, methodName, "How come shopping cart is empty");
			return cartItemList;
			
		}
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<TaxRate> getAllStatesTaxInfo() throws Exception {
		String methodName ="getAllStatesTaxInfo";
		logger.logp(Level.FINE, className, methodName, "Getting all StatesTaxInfo");
		String serviceEndPoint=GSJSFWebUtil.getTaxShipRateServiceEndPoint();
		String uri=serviceEndPoint+"/"+"rest/productTaxShipService/getAllStatesTaxInfo";
		//RestClient client = new RestClient(new ApacheHttpClientConfig(new DefaultHttpClient()));
		RestClient client = getRestCLient();
		logger.logp(Level.FINE, className, methodName, "Calling JAX-RS resource "+uri);
		//System.out.println("Calling JAX-RS resource getAllStatesTaxInfo  "+uri);
		Resource resource = client.resource(uri);
		resource.accept(MediaType.APPLICATION_XML);
		//List<TaxRate> taxRateList = resource.get(new EntityType<List<TaxRate>>() {});	
		//System.out.println("Debug for  Unmarshal Exception "+resource.get(String.class));
		List<TaxRate> taxRateList = resource.get(new EntityType<List<TaxRate>>() {});	
		return taxRateList;
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<ShipRate> getAllProductsShippingInfo() throws Exception {
		String methodName ="getAllProductsShippingInfo";
		logger.logp(Level.FINE, className, methodName, "Getting all ShipRateInfo");
		String serviceEndPoint=GSJSFWebUtil.getTaxShipRateServiceEndPoint();
		String uri=serviceEndPoint+"/"+"rest/productTaxShipService/getAllProductsShippingInfo";
		RestClient client = getRestCLient();
		logger.logp(Level.FINE, className, methodName, "Calling JAX-RS resource "+uri);
		//System.out.println("Calling JAX-RS resource getAllProductsShippingInfo  "+uri);
		Resource resource = client.resource(uri);
		resource.accept(MediaType.APPLICATION_XML);
		List<ShipRate> shipRateList  = resource.get(new EntityType<List<ShipRate>>() {});	
		return shipRateList;
	}
	

	/**vb
	 * 
	 * @param prodReview
	 * @throws Exception
	 */
	public void saveProductReview(ProdReview prodReview) throws Exception {
		String methodName="saveProductReview";
		logger.logp(Level.FINE, className, methodName, "Saving Product Review");
		String serviceEndPoint=GSJSFWebUtil.getTaxShipRateServiceEndPoint();
		String uri=serviceEndPoint+"/"+"rest/productTaxShipService/saveProductReview";
		RestClient client = getRestCLient();
		logger.logp(Level.FINE, className, methodName, "Calling JAX-RS resource "+uri);
		Resource resource = client.resource(uri);
		resource.contentType(MediaType.APPLICATION_XML);
		resource.accept(MediaType.APPLICATION_XML);
		resource.put(prodReview);		
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public TaxRate getTaxRate() throws Exception {
		String methodName="getStateID";
		TaxRate taxRate=null;
		logger.logp(Level.FINE, className, methodName, "Get Random State ID");
		List<TaxRate> taxRateList=getAllStatesTaxInfo();
		Random random= new Random();
		int nextIntValue=random.nextInt(49);
		if(taxRateList !=null && taxRateList.size()>0){
			taxRate=taxRateList.get(nextIntValue);
		}
		return taxRate;
		
	}
	
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public ShipRate getShipRateByItemId(String itemID) throws Exception {
		String methodName ="getShipRateByItemId";
		logger.logp(Level.FINE, className, methodName, "Getting ShipRate by itemID.");
		String serviceEndPoint=GSJSFWebUtil.getTaxShipRateServiceEndPoint();
		String uri=serviceEndPoint+"/"+"rest/productTaxShipService/getShipRateByItemID"+ "?" + "itemID=" + itemID;
		RestClient client = getRestCLient();
		logger.logp(Level.FINE, className, methodName, "Calling JAX-RS resource "+uri);
		Resource resource = client.resource(uri);
		resource.contentType(MediaType.APPLICATION_XML);
		resource.accept(MediaType.APPLICATION_XML);
		ShipRate shipRate  = resource.get(new EntityType<ShipRate>() {});	
		return shipRate;
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public RestClient getRestCLient() throws Exception{
		String methodName="getRestCLient";
		logger.logp(Level.FINE, className, methodName, "Returning REST client with ClientConfig object");
		ClientConfig config = new ClientConfig();
		//config.getProperties().setProperty("wink.client.sslSocketFactory", "javax.net.ssl.SSLSocketFactory");
		//config.getProperties().put("wink.client.sslSocketFactory", SSLSocketFactory.getDefault());
		if(GSJSFWebUtil.getReadTimeout() !=null){
			config.readTimeout(new Integer(GSJSFWebUtil.getReadTimeout()).intValue());
		}
		if(GSJSFWebUtil.getConnectTimeout()!=null){
			config.connectTimeout(new Integer(GSJSFWebUtil.getConnectTimeout()).intValue());
		}
		RestClient client = new RestClient(config);
		return client;
		
	}

}

/**
 * 
 */
package com.ibm.websphere.svt.gs.gsjsfweb;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.ibm.websphere.svt.gs.gsjsfweb.exceptions.GSJSF12WebException;

/**
 * @author root
 *
 */
@ManagedBean(name="garageSaleSessionBean")
@SessionScoped
public class GarageSaleSessionBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6400278283722041014L;

	private static String componentName = "com.ibm.websphere.svt.gs.gsjsfweb";
	private static Logger logger = Logger.getLogger(componentName);
	private static String className = GarageSaleSessionBean.class.getName();

    private int _numberToPurchase;
    private String _quantityString;
    private String customerID = null;
    private boolean _isCartEmpty = true;
    private String _categoryChosen = null;
    private String _mfgCategoryChosen = null;
    private String _inventoryChosen = null;
    

	public String get_categoryChosen() {
    	
    	return _categoryChosen;
	}

	public void set_categoryChosen(String chosen) {
		String methodName = "set_categoryChosen";
		
		if (chosen == null) {
			logger.logp(Level.SEVERE, className, methodName, "will set the categoryChosen attribute to null");
		}
		_categoryChosen = chosen;
	}

	public int getNumberToPurchase() {
		return _numberToPurchase;
	}

	public void setNumberToPurchase(int numberToPurchase) {
		this._numberToPurchase = numberToPurchase;
	}
	
    public String get_quantityString() {
		return _quantityString;
	}

	public void set_quantityString(String _quantityString) {
		this._quantityString = _quantityString;
	}

	public String getCustomerID() throws GSJSF12WebException {
		String methodName = "getCustomerID";
		//String tempID = null;
		if (this.customerID == null) {
			
		    FacesContext fc = FacesContext.getCurrentInstance();
		    Application app = fc.getApplication();
		    ExpressionFactory elFactory = app.getExpressionFactory();
		    ELContext elContext = fc.getELContext();
		    logger.logp(Level.FINEST, className, methodName, "about to find customer id...");
		    ValueExpression userIDExp = elFactory.createValueExpression(elContext, "#{sessionScope.custID}", String.class);
		    this.customerID = (String) userIDExp.getValue(elContext);
		    
		}
		if ( this.customerID == null ) {
			logger.logp( Level.SEVERE, className, methodName, "still null value for customerID" );
            throw new GSJSF12WebException("non-valid customer id");
		} else if ( this.customerID == "" ) {
			logger.logp(Level.SEVERE, className, methodName, "empty string for customerID");
            throw new GSJSF12WebException("non-valid customer id");
		} 

		logger.logp(Level.FINER, className, methodName, "Found customer id... " + this.customerID );
		return this.customerID;
		
	}

	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	public boolean isCartEmpty() {
		return _isCartEmpty;
	}

	public void setCartEmpty(boolean cartEmpty) {
		this._isCartEmpty = cartEmpty;
	} 
	
	public String get_mfgCategoryChosen() {
		return _mfgCategoryChosen;
	}

	public void set_mfgCategoryChosen(String categoryChosen) {
		String methodName = "set_mfgCategoryChosen";
		
		if (categoryChosen == null ) {
			logger.logp(Level.SEVERE, className, methodName, "will set the mfgChosen attribute to null?");
					
		}
		_mfgCategoryChosen = categoryChosen;
	}

	public String get_inventoryChosen() {
		return _inventoryChosen;
	}

	public void set_inventoryChosen(String inventoryChosen) {
		String methodName = "set_inventoryChosen";
		
		if (inventoryChosen == null) {
			logger.logp(Level.SEVERE, className, methodName, "will set the inventoryChosen attribute to null");
		}
		
		_inventoryChosen = inventoryChosen;
	}

}

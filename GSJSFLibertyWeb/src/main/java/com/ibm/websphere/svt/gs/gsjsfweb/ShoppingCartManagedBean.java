/**
 * 
 */
package com.ibm.websphere.svt.gs.gsjsfweb;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import com.ibm.websphere.svt.gs.cart.beans.CartItemWrapper;


/**
 * @author root
 *
 */
@ManagedBean(name="shoppingCartManagedBean")
@CustomScoped(value="#{customScope}")
public class ShoppingCartManagedBean implements Serializable{
	
	private static final long serialVersionUID = 6400978223722041014L;
	private static String componentName = "com.ibm.websphere.svt.gs.gsjsfweb";

	private static Logger logger = Logger.getLogger(componentName);
	private static String className = ShoppingCartManagedBean.class.getName();
	private ArrayList<CartItemWrapper> cartItemWrapperList;
	
	public ShoppingCartManagedBean () {
	} 
	public ArrayList<CartItemWrapper> getCartItemWrapperList() {
		if(cartItemWrapperList==null){
			cartItemWrapperList= new ArrayList<CartItemWrapper>();
		}
		return cartItemWrapperList;
	}

	public void setCartItemWrapperList(ArrayList<CartItemWrapper> cartItemWrapperList) {
		this.cartItemWrapperList = cartItemWrapperList;
	}

	private String subTotal=null;
	private Date myDate=null;

	public String getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(String subTotal) {
		this.subTotal = subTotal;
	}

    
    public Date getMyDate() {
		return myDate;
	}

	public void setMyDate(Date myDate) {
		this.myDate = myDate;
	}
	
    // ------------------------------------------------------- Lifecycle Methods



	@PostConstruct
    public void postConstruct() {
    	String methodName="postConstruct";
        FacesContext ctx = FacesContext.getCurrentInstance();
        try{
    		DateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
			//Date currentDate = new Date();
			Calendar calendar=Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			String currentDateText=dateFormat.format(calendar.getTime());
			Date parsedCurrentDate=dateFormat.parse(currentDateText);
			this.myDate=parsedCurrentDate;
        	
        }catch(Exception e){
        	e.printStackTrace();
        }
        ctx.getExternalContext().getRequestMap().put("postConstructStatus", "Invoked");
		logger.logp(Level.FINE, className, methodName, "postConstruct invoked");
    }


    @PreDestroy
    public void preDestroy() {
    	String methodName="preDestroy";
        FacesContext ctx = FacesContext.getCurrentInstance();
        ctx.getExternalContext().getRequestMap().put("preDestroyStatus", "Invoked");
		logger.logp(Level.FINE, className, methodName, "preDestroyStatus invoked");
    }


    // ---------------------------------------------------------- Public Methods


    public String destroyScope() {
    	String methodName="destroyScope";
		logger.logp(Level.FINE, className, methodName, "Destroy current scope");
        FacesContext ctx = FacesContext.getCurrentInstance();
        CustomScopeELResolver.destroyScope(ctx);
        return null;

    }
	

}

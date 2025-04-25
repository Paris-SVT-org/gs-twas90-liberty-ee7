/**
 * 
 */
package com.ibm.websphere.svt.gs.gsjsfweb;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;


/**
 * @author Administrator
 *
 */
@Named
@SessionScoped
//@ManagedBean(name="stateTaxShipRateBean")
//@SessionScoped
public class StateTaxShipRateBean implements Serializable{
	
	public StateTaxShipRateBean(){
		
	}
	public String getCustomerStateID() {
		return customerStateID;
	}

	public void setCustomerStateID(String customerStateID) {
		this.customerStateID = customerStateID;
	}

	public String getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(String taxAmount) {
		this.taxAmount = taxAmount;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 8132237453176281893L;
	
	private String customerStateID;

	private String stateName;

	private float taxRate;
	
	private String shippingCost;
	
	private String total;
	
	private String taxAmount;

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getShippingCost() {
		return shippingCost;
	}

	public void setShippingCost(String shippingCost) {
		this.shippingCost = shippingCost;
	}


	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public float getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(float taxRate) {
		this.taxRate = taxRate;
	}

}

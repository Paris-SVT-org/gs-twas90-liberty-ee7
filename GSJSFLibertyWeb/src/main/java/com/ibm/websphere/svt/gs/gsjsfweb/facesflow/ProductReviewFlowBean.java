/**
 * 
 */
package com.ibm.websphere.svt.gs.gsjsfweb.facesflow;

import java.io.Serializable;

import javax.faces.flow.FlowScoped;
import javax.inject.Named;

/**
 * @author Administrator
 *
 */
@Named
@FlowScoped("productReviewFlow")
public class ProductReviewFlowBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -992453100512513247L;
	private String customerID;
	private String categoryChosen;
	private String mfgChosen;
	private String inventoryChosen;
	private String productReview;

	/**
	 * @return the customerID
	 */
	public String getCustomerID() {
		return customerID;
	}
	/**
	 * @param customerID the customerID to set
	 */
	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}
	/**
	 * @return the categoryChosen
	 */
	public String getCategoryChosen() {
		return categoryChosen;
	}
	/**
	 * @param categoryChosen the categoryChosen to set
	 */
	public void setCategoryChosen(String categoryChosen) {
		this.categoryChosen = categoryChosen;
	}
	/**
	 * @return the mfgChosen
	 */
	public String getMfgChosen() {
		return mfgChosen;
	}
	/**
	 * @param mfgChosen the mfgChosen to set
	 */
	public void setMfgChosen(String mfgChosen) {
		this.mfgChosen = mfgChosen;
	}
	/**
	 * @return the inventoryChosen
	 */
	public String getInventoryChosen() {
		return inventoryChosen;
	}
	/**
	 * @param inventoryChosen the inventoryChosen to set
	 */
	public void setInventoryChosen(String inventoryChosen) {
		this.inventoryChosen = inventoryChosen;
	}
	/**
	 * @return the productReview
	 */
	public String getProductReview() {
		return productReview;
	}
	/**
	 * @param productReview the productReview to set
	 */
	public void setProductReview(String productReview) {
		this.productReview = productReview;
	}

}

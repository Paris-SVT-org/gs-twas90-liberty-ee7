/**
 * 
 */
package com.ibm.websphere.svt.gs.gsjsfweb;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;


/**
 * @author Administrator
 *
 */
@Named
@RequestScoped
//@ManagedBean(name="productReviewBean")
//@RequestScoped
public class ProductReviewBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3651873301340994770L;
	public ProductReviewBean(){
		
	}
	
	private String productReviewText;
	private String categoryName;
	private String itemName;
	public String getProductReviewText() {
		return productReviewText;
	}
	public void setProductReviewText(String productReviewText) {
		this.productReviewText = productReviewText;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
}

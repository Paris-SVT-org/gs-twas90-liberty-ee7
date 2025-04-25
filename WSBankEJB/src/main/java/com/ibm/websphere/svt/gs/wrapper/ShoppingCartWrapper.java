package com.ibm.websphere.svt.gs.wrapper;

import java.io.Serializable;
import java.util.Vector;

import javax.xml.bind.annotation.XmlType;

@XmlType(name="ShoppingCartWrapper", namespace="http://wrapper.gs.svt.websphere.ibm.com")
public class ShoppingCartWrapper implements Serializable {
	private static final long serialVersionUID = 1L;
	private CustomerWrapper customer = null;
	private CustomerInfoWrapper customerInfo = null;
	private PaymentInfoWrapper paymentInfo = null;
	private Vector cartItems = null;

	public ShoppingCartWrapper(){}
	
	public void setCustomerWrapper(CustomerWrapper customer) { this.customer=customer; }
	public void setCustomerInfoWrapper(CustomerInfoWrapper customerInfo) { this.customerInfo=customerInfo; }
	public void setPaymentInfoWrapper(PaymentInfoWrapper paymentInfo) { this.paymentInfo=paymentInfo; }
	public void setCartItems(Vector cartItems) { this.cartItems=cartItems; }

	public CustomerWrapper getCustomerWrapper() {return customer;}
	public CustomerInfoWrapper getCustomerInfoWrapper() {return customerInfo;}
	public PaymentInfoWrapper getPaymentInfoWrapper() {return paymentInfo;}
	public Vector getCartItems() {return cartItems;}
}

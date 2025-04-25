package com.ibm.websphere.svt.gs.cinfocc;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CInfo implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7635593566795244961L;
	
	private static String componentName = "com.ibm.websphere.svt.gs.cinfocc";
	private static Logger logger = Logger.getLogger(componentName);
	private static String className = CInfo.class.getName();
	
	public static String[] CINFOATTRS = {"customerID", "address1", "email", "phoneNumber"};
	
	private String customerID;
	private String email;
	private String address1;
	private String address2;
	private String phoneNumber;
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getCustomerID() {
		return customerID;
	}
	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String changeCustomerInfo() {
		String methodName = "changeCustomerInfo";
		
		
		return methodName;
	}
	
	public void updateCustomerInfo() {
		String methodName = "updateCustomerInfo";
		
		logger.logp(Level.FINE, className, methodName, "will do the update here");
		
	}
	
	

}

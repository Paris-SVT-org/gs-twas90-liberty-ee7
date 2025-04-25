package com.ibm.websphere.svt.gs.gsjsfweb;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.ibm.websphere.svt.gs.cinfocc.CInfo;
import com.ibm.websphere.svt.gs.gsdb.jaxws.client.CustomerInfoWrapper;
import com.ibm.websphere.svt.gs.gsjsfweb.exceptions.GSJSF12WebException;
import com.ibm.websphere.svt.gs.gsjsfweb.utils.GSJSFWebUtil;


@ManagedBean(name="customerInfo")
@ViewScoped
public class CustomerInfo implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7635593566795244961L;
	
	private static String componentName = "com.ibm.websphere.svt.gs.gsjsfweb";
	private static Logger logger = Logger.getLogger(componentName);
	private static String className = CustomerInfo.class.getName();
	private String _customerID;
	private PhoneNumber _phone;
	@Size(min = 1, message = "Please enter the Email")
    @Pattern(regexp = "[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+\\.[a-zA-Z0-9]+", message = "Email format is invalid.")
	private String _email;
	private String _address1;
	private String _address2;
	
	/**
	 * @return the garageSaleStoreManagerUtil
	 */
	public PhoneNumber get_phone() {
		return _phone;
	}
	public void set_phone(PhoneNumber _phone) {
		this._phone = _phone;
	}
	public String get_email() {
		return _email;
	}
	public void set_email(String _email) {
		this._email = _email;
	}
	public String get_address1() {
		return _address1;
	}
	public void set_address1(String _address1) {
		this._address1 = _address1;
	}
	
	public String get_address2() {
		return _address2;
	}
	public void set_address2(String _address2) {
		this._address2 = _address2;
	}
	public String changeCustomerInfo() {
		String methodName = "changeCustomerInfo";
		
		FacesContext facesContext=FacesContext.getCurrentInstance();
		CInfo cInfoBean =(CInfo) facesContext.getExternalContext().getSessionMap().get("customerData");
		if(cInfoBean!=null){
			cInfoBean.setAddress1(this._address1);
			cInfoBean.setEmail(this._email);
			cInfoBean.setPhoneNumber(this._phone.toString());
			facesContext.getExternalContext().getSessionMap().put("customerData", cInfoBean);
		}
		
		
		return methodName;
	}
	
	public void updateCustomerInfo() {
		
	}
	
	public String gotoCustomerInfo() {
		String methodName = "gotoCustomerInfo";
		
		String custID = GSJSFWebUtil.getUserID();
	    
	    if (custID == null ) {
			logger.logp(Level.SEVERE, className, methodName, "Severe error Customer ID is null: ");
	    	return "foundNull";
	    }
	    initializeCustomerInfo();
		return "gotocustomer";
	}
	
	@PostConstruct
	public void initializeCustomerInfo() {
		
		String methodName = "initializeCustomerInfo";
		logger.logp(Level.FINER, className, methodName, "Entering into initializeCustomerInfo @PostConstruct" );
		FacesContext facesContext=FacesContext.getCurrentInstance();
		CInfo cInfoBean =(CInfo) facesContext.getExternalContext().getSessionMap().get("customerData");
		if(cInfoBean!=null){
			logger.logp(Level.FINER, className, methodName, "Customer Info Bean is in Session - Initializing ViewScoped Customer Managed Bean." );
			this._customerID=cInfoBean.getCustomerID();
			this._address1=cInfoBean.getAddress1();
			this._email=cInfoBean.getEmail();
			try{
				this._phone=getPhoneNumberFromString(cInfoBean.getPhoneNumber());
				
			}catch(GSJSF12WebException e){
				logger.logp(Level.SEVERE, className, methodName, "exception found : " + e.getMessage());
			}
		}else{
			logger.logp(Level.FINER, className, methodName, "Customer Info Bean not in session - fetching from Database." );
			cInfoBean = new CInfo();
			String custID = GSJSFWebUtil.getUserID();
		    
		    if (custID == null ) {
				logger.logp(Level.SEVERE, className, methodName, "Severe error Customer ID is null: ");
		    }
		    
		    CustomerInfoWrapper tempUserInfo;
			tempUserInfo =(CustomerInfoWrapper) facesContext.getExternalContext().getSessionMap().get("tempUserInfo");
;
			logger.logp(Level.FINER, className, methodName, "\tcustomer id: " + tempUserInfo.getCustID() );
			String tempAddress1 = tempUserInfo.getAddress1();
			String tempEmail = tempUserInfo.getEmail();
			String tempPhoneString = tempUserInfo.getPhone();
			if (tempPhoneString == null) {
				tempPhoneString = "";
			}
			
			logger.logp(Level.FINE, className, methodName, "\tFound customer : " + custID
			                   + "\n\tAddress: " + tempAddress1
			                   + "\n\tEmail: " + tempEmail
			                   + "\n\tPhone: " + tempPhoneString ) ;
		
			this._customerID = custID;
			this._address1 = tempAddress1;
			this._email = tempEmail;
			//this._phone.setNumber(tempPhoneString);
			try {
				this._phone = getPhoneNumberFromString(tempPhoneString);
			} catch (GSJSF12WebException e) {
				logger.logp(Level.SEVERE, className, methodName, "exception found : " + e.getMessage());
			}
			cInfoBean.setCustomerID(custID);
			cInfoBean.setAddress1(tempAddress1);
			cInfoBean.setEmail(tempEmail);
			cInfoBean.setPhoneNumber(tempPhoneString);
			logger.logp(Level.FINER, className, methodName, "Saving customerData into session for future use." );
			facesContext.getExternalContext().getSessionMap().put("customerData", cInfoBean);
			
		}
		
		
	}
	
	private PhoneNumber getPhoneNumberFromString(String rawValue) throws GSJSF12WebException {
		String methodName = "getPhoneNumberFromString";
		PhoneNumber phone = null;
		
		if (rawValue != null && rawValue.length()>0) {
			String parts[] = rawValue.split("-");

			if (parts.length != 3) {
				logger.logp(Level.SEVERE, className, methodName, "Phone number String wrong : " + rawValue);
				throw new GSJSF12WebException("Phone number String wrong : " + rawValue);
				
			}
			phone = new PhoneNumber(parts[0], parts[1], parts[2]);
		}
		return phone;
	}

}

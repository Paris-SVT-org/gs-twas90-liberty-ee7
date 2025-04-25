package com.ibm.websphere.svt.gs.gsjsfweb;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.ibm.websphere.svt.gs.cinfocc.CInfo;
import com.ibm.websphere.svt.gs.gsdb.jaxws.client.CustomerInfoWrapper;
import com.ibm.websphere.svt.gs.gsjsfweb.utils.GSJSFWebUtil;




@ManagedBean(name="cinfocc")
@SessionScoped
public class CinfoCC implements Serializable {

	private static final long serialVersionUID = -5986149738319914218L;
	private static String componentName = "com.ibm.websphere.svt.gs.gsjsfweb";
	private static Logger logger = Logger.getLogger(componentName);
	private static String className = CinfoCC.class.getName();
	final static private String text = "Hi!";
	/*@ManagedProperty(value="#{garageSaleStoreManagerUtil}")
	private transient GarageSaleStoreManagerUtil garageSaleStoreManagerUtil;*/
	
	/**
	 * @return the garageSaleStoreManagerUtil
	 */
	/*public GarageSaleStoreManagerUtil getGarageSaleStoreManagerUtil() {
		return garageSaleStoreManagerUtil;
	}*/
	/**
	 * @param garageSaleStoreManagerUtil the garageSaleStoreManagerUtil to set
	 */
	/*public void setGarageSaleStoreManagerUtil(
			GarageSaleStoreManagerUtil garageSaleStoreManagerUtil) {
		this.garageSaleStoreManagerUtil = garageSaleStoreManagerUtil;
	}*/

	private CInfo cinfo;
    
    private String name;
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public static String getText() {
		return text;
	}
	
	public CinfoCC() {
		
	}
	
	public void setCinfo(CInfo cusinfo) {
		String methodName = "setCinfo";
		
		logger.logp(Level.FINE, className, methodName, "for the Hello bean, set cinfo");
		this.cinfo = cusinfo;
		
	}
	
	public CInfo getCinfo() {
		String methodName = "getCInfo";
		if (this.cinfo != null) {
			logger.logp(Level.FINE, className, methodName, "for the bean, return a cinfo");
			return cinfo;
		} else {
			
			String customerID = GSJSFWebUtil.getUserID();
			logger.logp(Level.FINE, className, methodName, "Found customerID from session : " + customerID);
		    
		    //CustomerInfoWrapper tempUserInfo = garageSaleStoreManagerUtil.getCustomerInfo(customerID);
		    CustomerInfoWrapper tempUserInfo = (CustomerInfoWrapper) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("tempUserInfo");
		    
		    
			logger.logp(Level.FINER, className, methodName, "\tcustomer id: " + tempUserInfo.getCustID() );
			
			String tempAddress1 = tempUserInfo.getAddress1();
			String tempEmail = tempUserInfo.getEmail();
			String tempPhoneNumber = tempUserInfo.getPhone();
			logger.logp(Level.FINE, className, methodName, "User info, address: " + tempAddress1 
					+ " Email: " + tempEmail + " phone number: " + tempPhoneNumber);
			
			this.cinfo = new CInfo();
			cinfo.setAddress1(tempAddress1);
			cinfo.setEmail(tempEmail);
			cinfo.setPhoneNumber(tempPhoneNumber);
			cinfo.setCustomerID(customerID);
			
			return cinfo;
			
		}
		
		
	}

}

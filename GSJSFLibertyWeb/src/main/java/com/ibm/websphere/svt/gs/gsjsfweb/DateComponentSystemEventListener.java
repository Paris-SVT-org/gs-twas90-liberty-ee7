/**
 * 
 */
package com.ibm.websphere.svt.gs.gsjsfweb;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ComponentSystemEventListener;
import javax.faces.event.ListenerFor;
import javax.servlet.http.HttpServletRequest;


/**
 * @author root
 *
 */

@RequestScoped
@ManagedBean(name="dateComponentSystemEventListener")
@ListenerFor(systemEventClass=javax.faces.event.PostValidateEvent.class)
public class DateComponentSystemEventListener implements
		ComponentSystemEventListener {

	@ManagedProperty(name="shoppingCartManagedBean",value="#{shoppingCartManagedBean}")
	private  ShoppingCartManagedBean shoppingCartManagedBean;
	private static String componentName = "com.ibm.websphere.svt.gs.gsjsfweb";
	private static Logger logger = Logger.getLogger(componentName);
	private static String className = DateComponentSystemEventListener.class.getName();

	
	
	public DateComponentSystemEventListener(){
		//do nothing
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
	 * @param source
	 * @return
	 */
    public boolean isListenerForSource( Object source ){
        
        return true;
    }
	
	/* (non-Javadoc)
	 * @see javax.faces.event.ComponentSystemEventListener#processEvent(javax.faces.event.ComponentSystemEvent)
	 */
	
	@Override
	public void processEvent(ComponentSystemEvent arg0) {
		String methodName="processEvent";
		logger.logp(Level.FINE, className, methodName, "ComponentSystemEvent is got invoked  "+arg0.getComponent().getId() );
		UIComponent myDateComponent=arg0.getComponent();
		FacesContext fCtx=FacesContext.getCurrentInstance();
		ExternalContext externalContext=fCtx.getExternalContext();
		HttpServletRequest servletRequest = (HttpServletRequest) externalContext.getRequest();
		DateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
		String myDateText=servletRequest.getParameter(myDateComponent.getClientId());
		Date checkOutDate=null;
		try {
			if(myDateText!=null && myDateText.length()>=10){
				checkOutDate = dateFormat.parse(myDateText);
				Date currentDate = new Date();
				String currentDateText=dateFormat.format(currentDate);
				Date parsedCurrentDate=dateFormat.parse(currentDateText);

				if(checkOutDate!=null){
					logger.logp(Level.FINE, className, methodName, "The checkOutDate from shoppingCartBean  "+checkOutDate.toString() );
					if(checkOutDate.before(parsedCurrentDate)){
						logger.logp(Level.FINE, className, methodName, "The checkOutDate from shoppingCartBean  "+checkOutDate.toString() );
						FacesMessage message= 
							new FacesMessage(FacesMessage.SEVERITY_ERROR,"Checkout Date: "+getFormattedDate(checkOutDate, "MM/dd/yyyy")
									+"  can not be lesser than current date: "+getFormattedDate(currentDate, "MM/dd/yyyy")+".",null);
						fCtx.addMessage(null, message);
						fCtx.renderResponse();
					}
				}else {
					logger.logp(Level.FINE, className, methodName, "The checkOutDate from shoppingCartBean  is null");
					FacesMessage message= 
						new FacesMessage(FacesMessage.SEVERITY_ERROR,"Checkout Date "
								+"can not be blank and it has to be in valid \"MM/DD/YYYY\" format.",null);
					fCtx.addMessage(null, message);
					fCtx.renderResponse();
				}
			}
			else {
				FacesMessage message= 
					new FacesMessage(FacesMessage.SEVERITY_ERROR,"Checkout Date "
							+"can not be blank and it has to be in valid \"MM/DD/YYYY\" format.",null);
				fCtx.addMessage(null, message);
				fCtx.renderResponse();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 * @throws Exception
	 */
	public String getFormattedDate(Date date,String pattern) throws Exception {
		String methodName="getFormattedDate";
		logger.logp(Level.FINE, className, methodName, "Before date format "+date.toString());
		DateFormat dateFormat= new SimpleDateFormat(pattern);
		String formattedDate=dateFormat.format(date);
		logger.logp(Level.FINE, className, methodName, "After date format  "+formattedDate);
		return formattedDate;
		
	}

}

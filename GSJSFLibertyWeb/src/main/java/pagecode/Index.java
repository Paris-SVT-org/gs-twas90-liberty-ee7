/**
 * 
 */
package pagecode;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.ibm.websphere.svt.gs.gsdb.jaxws.client.CustomerInfoWrapper;
import com.ibm.websphere.svt.gs.gsdb.jaxws.client.CustomerWrapper;
import com.ibm.websphere.svt.gs.gsdb.jaxws.client.GarageSaleStoreManagerLocal;
import com.ibm.websphere.svt.gs.gsdb.jaxws.client.SettingsWrapper;
import com.ibm.websphere.svt.gs.gsjsfweb.GarageSaleCDIApplicationScopped;
import com.ibm.websphere.svt.gs.gsjsfweb.utils.GSJSFWebUtil;
import com.ibm.websphere.svt.gs.gsjsfweb.utils.GarageSaleLargeSessionUtil;
import com.ibm.websphere.svt.gs.gsjsfweb.utils.LargeSessionRangeBean;
import com.ibm.websphere.svt.gs.ws.client.utils.GarageSaleWSClientUtils;

@ManagedBean(name="pc_Index")
@RequestScoped
/**
 * @author Administrator
 *
 */
public class Index extends PageCodeBase {
	
	public static final String OUTCOME_FAILED = "failed";
	public static final String OUTCOME_LOGIN = "login";
	private static String componentName = "pagecode";
	private static Logger logger = Logger.getLogger(componentName);
	private static String className = Index.class.getName();		
	
	//private StoreManagerWrapperInterface localService;
	//@ManagedProperty(value="#{garageSaleStoreManagerUtil}")
	
	/*@ EJB private GarageSaleStoreManager localService;*/
	
	@Inject
	private GarageSaleCDIApplicationScopped garageSaleCDIApplicationScopped;
	
	private GarageSaleStoreManagerLocal localService=GarageSaleWSClientUtils.getStoreManager();
	private SettingsWrapper settings = null;
	
	public String login() {
		String outcomeString = OUTCOME_FAILED;
		String methodName="login";
		CustomerWrapper localCustomer=null;
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			Application app = fc.getApplication();
			ExpressionFactory elFactory = app.getExpressionFactory();
			ELContext elContext = fc.getELContext();
			ValueExpression userIDExp = elFactory.createValueExpression(elContext, "#{sessionScope.custID}", String.class);
			//Store context path of other web modules.
			setOtherWebModuleContextPath();
			
			if (userIDExp == null) {
				logger.logp(Level.SEVERE, className, methodName, "null userIDExp, return");
				addErrorMessage("loging failed, please try later");
				return outcomeString;
				
			}
        
			String userID = (String) userIDExp.getValue(elContext);
			if (userID == null) {
				logger.logp(Level.SEVERE, className, methodName, "null userID, return");
				addErrorMessage("loging failed, please try later");
				return outcomeString;
			}	
        
			ValueExpression passwordExp = elFactory.createValueExpression(elContext, "#{sessionScope.password}", String.class);
			if (passwordExp == null) {
				logger.logp(Level.SEVERE, className, methodName, "null passwordExp, return");
				addErrorMessage("loging failed, please try later");
				return outcomeString;
			}
		
			String password = (String) passwordExp.getValue(elContext);
			if (password == null) {
				logger.logp(Level.SEVERE, className, methodName, "null password, return");
				addErrorMessage("loging failed, please try later");
				return outcomeString;
			}
        
			logger.logp(Level.FINER, className, methodName, "in the login() method got userID: " + userID + " and password: " + password);
			//localService = GSJSFWebUtil.getStoreManagerPort();

			if (localService == null) {
				logger.logp(Level.SEVERE, className, methodName, "null service, return");
				addErrorMessage("cannot obtain the service, please try later");
				return outcomeString;
			}
			//ExternalContext extContext = fc.getExternalContext();
			//localCustomer =(CustomerWrapper) extContext.getSessionMap().get("customer");
			GSJSFWebUtil.printCurrentTimestamp(className, methodName,"Start");
			localCustomer = localService.getCustomer( userID, password );
			GSJSFWebUtil.printCurrentTimestamp(className, methodName,"End");
			if (localCustomer == null) {
				logger.logp(Level.SEVERE, className, methodName, "user: " + userID + " failed to log in");
				addErrorMessage("Login failed, please make sure you entered correct userid and password");
				outcomeString = OUTCOME_FAILED; // doubly 
				return outcomeString; // go ahead and return
			} else {
				// seems nothing to be done
				// may need to set up a login boolean here
				String customerName = localCustomer.getCustName();
				int numberOfInvoices = localCustomer.getNumInvoicesCompleted();
				logger.logp(Level.FINEST, className, methodName, "User " + customerName + " has " + numberOfInvoices + " invoices");
			
					/**if(GarageSaleLargeSessionUtil.LS_TEST_ENABLE!=null && (GarageSaleLargeSessionUtil.LS_TEST_ENABLE).equalsIgnoreCase("true")){
						int maxUserSession=GarageSaleLargeSessionUtil.LS_TEST_USER_RANGE_MAX;
						int currentUserNumber=Integer.parseInt(customerName.substring(4));
						
						if(currentUserNumber <= maxUserSession){
							LargeSessionRangeBean userRangeBean=GarageSaleLargeSessionUtil.getLargeSessionRangeBean();
							addDataToTheSession(userRangeBean);
						}
						
					}*/
				// set the customer
				ValueExpression customerExp = elFactory.createValueExpression(elContext, "#{sessionScope.customer}", CustomerWrapper.class);
				customerExp.setValue(elContext, localCustomer);
				CustomerInfoWrapper tempUserInfo=localService.getCustomerInfo(userID);
				fc.getExternalContext().getSessionMap().put("tempUserInfo", tempUserInfo);

			
				// obtain the settings
				settings = localService.getSettings();
				ValueExpression settingsExp = elFactory.createValueExpression(elContext, "#{sessionScope.settings}", SettingsWrapper.class);
				settingsExp.setValue(elContext, settings);
				
				outcomeString = OUTCOME_LOGIN;
			}
		}/* catch (NullPointerException npe) {
			logger.logp(Level.SEVERE, className, methodName, npe.getMessage());
			npe.printStackTrace();
			addErrorMessage("login failed, please try later");
			return OUTCOME_FAILED;
		}*/ catch (Exception e){
			logger.logp(Level.SEVERE, className, methodName, "Exception: " + e.getMessage());
			e.printStackTrace();
			addErrorMessage("login failed, please try later");
			return OUTCOME_FAILED;

		}
		
		return outcomeString;
	}

	/**
	 * @return the localService
	 */
	public GarageSaleStoreManagerLocal getLocalService() {
		return localService;
	}

	/**
	 * @param localService the localService to set
	 */
	public void setLocalService(GarageSaleStoreManagerLocal localService) {
		this.localService = localService;
	}

	public static void addErrorMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        FacesContext fc = FacesContext.getCurrentInstance();
        fc.addMessage(null, facesMsg);
    }
    
    public static void addSuccessMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        FacesContext fc = FacesContext.getCurrentInstance();
        fc.addMessage("successInfo", facesMsg);
    }
    
    /**
     * 
     * @param userRangeBean
     * @throws Exception
     */
    public void addDataToTheSession(LargeSessionRangeBean userRangeBean) throws Exception {
    	String methodName = "addDataToTheSession";
    	logger.logp(Level.FINEST, className, methodName, "Storing data into the session");
    	FacesContext context = FacesContext.getCurrentInstance();
    	HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();   
    	HttpSession httpSession = request.getSession(false);  
		String largeObject =GarageSaleLargeSessionUtil.getLargeObjectBySize(userRangeBean.getObjectSize());
    	for (int i=0;i<=userRangeBean.getCounter();i++){
    		httpSession.setAttribute("largeObject"+userRangeBean.getObjectSize()+"_"+i+httpSession.getId(), new String(largeObject));
    	}

    }
    
    /**
     * 
     * @throws Exception
     */
    public void setOtherWebModuleContextPath() throws Exception{
    	
    	if(garageSaleCDIApplicationScopped !=null && garageSaleCDIApplicationScopped.getWebModuleContextPath()==null){
    		
    		FacesContext facesContext= FacesContext.getCurrentInstance();
    		ExternalContext externalContext = facesContext.getExternalContext();
    		ServletContext servletContext = (ServletContext) externalContext.getContext();
    		ServletContext pmiWebAppContext = servletContext.getContext("/WASPersonaWebServicesPMIWeb");
    		String uri = pmiWebAppContext.getContextPath();
    		garageSaleCDIApplicationScopped.setWebModuleContextPath(uri);
    	}
    	
    }
    
    
    
}
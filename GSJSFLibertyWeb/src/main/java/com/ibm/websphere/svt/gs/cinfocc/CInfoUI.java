package com.ibm.websphere.svt.gs.cinfocc;


import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIInput;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ListenerFor;
import javax.faces.event.PostAddToViewEvent;

@FacesComponent(value = "cinfocc")
@ListenerFor(systemEventClass = PostAddToViewEvent.class)
@ResourceDependency(name = "jsf.js", library = "javax.faces", target = "body")
public class CInfoUI extends UIInput {
	
	
	private static String componentName = "com.ibm.websphere.svt.gs.cinfocc";
	private static Logger logger = Logger.getLogger(componentName);
	private static String className = CInfoUI.class.getName();
	
	public CInfoUI() {
        setImmediate(true);
    }

    public CInfo getCustomerInfo() {
        return (CInfo) getValue();
    }
    
    @Override
    public void processEvent(ComponentSystemEvent event) throws AbortProcessingException {
    	String methodName = "processEvent";
    	
    	logger.logp(Level.FINE, className, methodName, "working on event : " + event.toString() );
    	
    	Map<String, String> requestParameterMap = getFacesContext().getExternalContext().getRequestParameterMap();
    	String exec = requestParameterMap.get("javax.faces.source");
    	logger.logp(Level.FINE, className, methodName, "Found the request string : " + exec);
    	
    	if((getClientId()+ "_update").equals(exec)) {
    		logger.logp(Level.FINE, className, methodName, "working on the update part...");
    		getCustomerInfo().updateCustomerInfo();
        } 
        logger.logp(Level.FINE, className, methodName, getClientId() + ":" + exec + "|" );
        logger.logp(Level.FINE, className, methodName, ":" + requestParameterMap );
        
        createFields();
    	
    }
    
    private void createFields() {
    	String methodName = "createFields";
    	
    	CInfo custInfo = this.getCustomerInfo();
    	logger.logp(Level.FINE, className, methodName, "working on customerInfo : " + custInfo);
    	
    	FacesContext fc = FacesContext.getCurrentInstance();
	    Application app = fc.getApplication();
	    ExpressionFactory elFactory = app.getExpressionFactory();
	    ELContext elContext = fc.getELContext();
	    
	    ValueExpression ve = this.getValueExpression("value");
        String exp = ve.getExpressionString();       // we got "#{hello.cinfo}"
        logger.logp(Level.FINE, className, methodName, "=== exp : " + exp);
        exp = exp.substring(0, exp.length() - 1);    // get rid of the last '}'
        
        int custInfoLen = CInfo.CINFOATTRS.length;
        
        for (int cl = 0; cl < custInfoLen; cl++) {
        	String id = getId() + "_" + cl;
        	logger.logp(Level.FINE, className, methodName, "=== id : " + id );
            if(findComponent(id) != null) {
            	logger.logp(Level.FINE, className, methodName, "null component, continue");
                continue;
            }
            HtmlInputText in = new HtmlInputText();
            String expString = exp + "." + CInfo.CINFOATTRS[cl] + "}";
            logger.logp(Level.FINE, className, methodName, "=== expString : " + expString);
            ValueExpression el = elFactory.createValueExpression(elContext, expString, String.class);
            
            String valueStringToSet = findCInfoAttributeValue(cl);
            logger.logp(Level.FINE, className, methodName, "try to set value " + valueStringToSet);
            el.setValue(elContext, valueStringToSet);
            
            in.setId(id);
            in.setValueExpression("value", el);
            in.setLocalValueSet(false);
            
            in.setOnkeyup("jsf.ajax.request(this,event);");
            
            getChildren().add(in);
        }
        
    }
    
    private String findCInfoAttributeValue(int index) {
    	//String methodName = "findCInfoAttributeValue";
    	String resultString = "temp";
    	
    	CInfo custInfo = this.getCustomerInfo();
    	if (index == 0) { // customer id
    		resultString = custInfo.getCustomerID();
    	} else if (index == 1) {
    		resultString = custInfo.getAddress1();
    	} else if (index == 2) {
    		resultString = custInfo.getEmail();
    	} else if (index == 3) {
    		resultString = custInfo.getPhoneNumber();
    	}
    	return resultString;
    	
    }

}

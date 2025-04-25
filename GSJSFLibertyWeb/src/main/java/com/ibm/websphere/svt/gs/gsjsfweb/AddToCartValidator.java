package com.ibm.websphere.svt.gs.gsjsfweb;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.validator.FacesValidator;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;


@FacesValidator(value="checkQuantity")
public class AddToCartValidator implements Validator {
	
	private static String componentName = "com.ibm.websphere.svt.gs.gsjsfweb";
	private static Logger logger = Logger.getLogger(componentName);
	private static String className = AddToCartValidator.class.getName();
	
	public static final String VALIDATOR_ID = "checkQuantity";
	public static final String VALIDATION_ERROR = "Only integer is allowed";
	
	public AddToCartValidator() { }

	public void validate(FacesContext facesContext, UIComponent uiComponent, Object value) throws ValidatorException {
		String methodName = "validate";
		
		String quantityString = (String) value;
		
		// will do some validation here
		try{
			logger.logp(Level.FINE, className, methodName, "To valideate string: " + quantityString);
			Integer.parseInt( quantityString );
		} catch(NumberFormatException nfe) {
			logger.logp(Level.FINE, className, methodName, "Trouble with " + quantityString);
		    throw new ValidatorException(new FacesMessage(VALIDATION_ERROR));
		}
		
	}
	
}

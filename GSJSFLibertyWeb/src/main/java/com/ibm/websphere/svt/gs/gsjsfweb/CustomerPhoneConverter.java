package com.ibm.websphere.svt.gs.gsjsfweb;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "phoneConverter")
public class CustomerPhoneConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String rawValue)
			throws ConverterException {
		PhoneNumber phone = null;
		if (rawValue != null && rawValue.length()>0) {
			String parts[] = rawValue.split("-");

			if (parts.length != 3)
				throw new ConverterException("CustomerPhoneConverter bad string");
				//throw new ConverterException(MessageFactory.getMessage(arg0,
				//		"phoneconverter-bad-string", new PhoneNumber("49", "123", "456789")));
			phone = new PhoneNumber(parts[0], parts[1], parts[2]);
		}
		return phone;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object value)
			throws ConverterException {
		
		// We are friendly and return at least an empty String
		String stringValue = "";
		if (value != null) {
			// Default components check if the value is a String and return it as it is
			if (value instanceof String)
				stringValue = (String) value;
			else if (value instanceof PhoneNumber) {
				PhoneNumber phoneValue = (PhoneNumber) value;
				stringValue = String.format("%s-%s-%s", phoneValue.getAreaCode(), phoneValue.getMiddleCode(),
						phoneValue.getEndCode());
			} else
				throw new ConverterException("CustomerPhoneConverter bad phone");
				//throw new ConverterException(MessageFactory.getMessage(facesContext,"phoneconverter-bad-phone"));
		}
		return stringValue;
	}

}

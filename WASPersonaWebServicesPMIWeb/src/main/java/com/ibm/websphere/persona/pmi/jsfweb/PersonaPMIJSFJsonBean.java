/**
 * 
 */
package com.ibm.websphere.persona.pmi.jsfweb;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 * @author JAGRAJ
 *
 */
@ManagedBean(name="personaPMIJSFJsonBean")
@RequestScoped
public class PersonaPMIJSFJsonBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7580003004826271731L;
	
	private String personaPMIJsonData;

	public String getPersonaPMIJsonData() {
		return personaPMIJsonData;
	}

	public void setPersonaPMIJsonData(String personaPMIJsonData) {
		this.personaPMIJsonData = personaPMIJsonData;
	}

}

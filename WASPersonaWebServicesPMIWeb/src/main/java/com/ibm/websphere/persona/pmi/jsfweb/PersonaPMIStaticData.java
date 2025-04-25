/**
 * 
 */
package com.ibm.websphere.persona.pmi.jsfweb;

/**
 * @author JAGRAJ
 *
 */
public class PersonaPMIStaticData {
	
	private static String personaPMIJsonData;

	public static String getPersonaPMIJsonData() {
		return personaPMIJsonData;
	}

	public static void setPersonaPMIJsonData(String personaPMIJsonData) {
		PersonaPMIStaticData.personaPMIJsonData = personaPMIJsonData;
	}

}

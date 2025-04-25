/**
 * 
 */
package com.ibm.websphere.svt.gs.gsjsfweb.websockets;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 * @author JAGRAJ
 *
 */
@ManagedBean(name="productDemoBean")
@RequestScoped
public class ProductDemoBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1592552451432324686L;
	
	//@ManagedProperty("#{param.fileName}")
	private String fileName;
	private String readStatus;
	/**
	 * @return the fileNameId
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * @param fileNameId the fileNameId to set
	 */
	public void setFileNameId(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * @return the readStatus
	 */
	public String getReadStatus() {
		return readStatus;
	}
	/**
	 * @param readStatus the readStatus to set
	 */
	public void setReadStatus(String readStatus) {
		this.readStatus = readStatus;
	}
	
	

}

package com.ibm.websphere.svt.gs.gsjsfweb;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@Named
@ApplicationScoped
public class GarageSaleCDIApplicationScopped implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9171747136029014337L;
	
	private String webModuleContextPath;

	/**
	 * @return the webModuleContextPath
	 */
	public String getWebModuleContextPath() {
		return webModuleContextPath;
	}

	/**
	 * @param webModuleContextPath the webModuleContextPath to set
	 */
	public void setWebModuleContextPath(String webModuleContextPath) {
		this.webModuleContextPath = webModuleContextPath;
	}


}

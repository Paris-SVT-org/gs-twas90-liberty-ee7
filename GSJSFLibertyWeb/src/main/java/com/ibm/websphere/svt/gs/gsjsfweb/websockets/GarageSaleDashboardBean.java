/**
 * 
 */
package com.ibm.websphere.svt.gs.gsjsfweb.websockets;

import java.io.Serializable;

/**
 * @author JAGRAJ
 *
 */
public class GarageSaleDashboardBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -986964788206716052L;
	private Long noOfInvoicesCreated;
	private Long noOfInvoicesCompleted;
	private Long noOfWebSocketsOpenSessions;
	
	/**
	 * 
	 * @return
	 */
	public Long getNoOfInvoicesCreated() {
		return noOfInvoicesCreated;
	}
	
	/**
	 * 
	 * @param noOfInvoicesCreated
	 */
	public void setNoOfInvoicesCreated(Long noOfInvoicesCreated) {
		this.noOfInvoicesCreated = noOfInvoicesCreated;
	}
	
	/**
	 * 
	 * @return
	 */
	public Long getNoOfInvoicesCompleted() {
		return noOfInvoicesCompleted;
	}
	
	/**
	 * 
	 * @param noOfInvoicesCompleted
	 */
	public void setNoOfInvoicesCompleted(Long noOfInvoicesCompleted) {
		this.noOfInvoicesCompleted = noOfInvoicesCompleted;
	}
	
	/**
	 * 
	 * @return
	 */
	public Long getNoOfWebSocketsOpenSessions() {
		return noOfWebSocketsOpenSessions;
	}
	
	/**
	 * 
	 * @param noOfWebSocketsOpenSessions
	 */
	public void setNoOfWebSocketsOpenSessions(Long noOfWebSocketsOpenSessions) {
		this.noOfWebSocketsOpenSessions = noOfWebSocketsOpenSessions;
	}
	
	

}

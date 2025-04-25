/*
 * Created on Jul 17, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.websphere.svt.gs.wrapper;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlType;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
@XmlType(name="StoreCreditWrapper", namespace="http://ejbwrapper.gs.svt.websphere.ibm.com")
public class StoreCreditWrapper implements Serializable {
	private static final long serialVersionUID = 16L;
	private String custID;
	private java.sql.Timestamp time;
	private float amount;
	
	public StoreCreditWrapper(){}

	public String getCustID(){return custID;}
	public java.sql.Timestamp getTime(){return time;}
	public float getAmount(){return amount;}
	
	public void setCustID(String custID){this.custID=custID;}
	public void setTime(java.sql.Timestamp time){this.time=time;}
	public void setAmount(float amount){this.amount=amount;}
		
	
}

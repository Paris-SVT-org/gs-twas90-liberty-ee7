/*
 * Created on Jul 14, 2004
 * Created by Michael E Concini
 */
package com.ibm.websphere.svt.gs.wrapper;
import java.util.Date;
import java.io.Serializable;

public class TransactionHistoryWrapper implements Serializable {
	private static final long serialVersionUID = 10L;
	
	private int id;
	private int suffix;
	private Date[] times;
	private float[] amounts;
	private String[] partners;
		
	public TransactionHistoryWrapper(){}
		
	public TransactionHistoryWrapper(int id, int suffix){
		this.id=id;
		this.suffix=suffix;			
	}
		
	public int getId(){return id;}
	public int getSuffix(){return suffix;}
	public Date[] getTimes(){return times;}
	public float[] getAmounts(){return amounts;}
	public String[] getPartners(){return partners;}
		
	public void setId(int id){this.id=id;}
	public void setSuffix(int suffix){this.suffix=suffix;}
	public void setTimes(Date[] times){this.times=times;}
	public void setAmounts(float[] amounts){this.amounts=amounts;}
	public void setPartners(String[] partners){this.partners=partners;}
		
}

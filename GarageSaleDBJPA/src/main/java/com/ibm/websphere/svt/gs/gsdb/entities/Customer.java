package com.ibm.websphere.svt.gs.gsdb.entities;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlType;


/**
 * The persistent class for the CUSTOMER database table.
 * 
 */
@Entity
@NamedQuery(name = "getAllCustomers", query = "SELECT c FROM Customer c")
@XmlType(name="Customer", namespace="http://entities.gsdb.gs.svt.websphere.ibm.com")
public class Customer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String custId;

	private String custName;

	private String custPwd;

	private int numInvoicesCompleted;

	private int numInvoicesCreated;

    public Customer() {
    }

	public String getCustId() {
		return this.custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getCustName() {
		return this.custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getCustPwd() {
		return this.custPwd;
	}

	public void setCustPwd(String custPwd) {
		this.custPwd = custPwd;
	}

	public int getNumInvoicesCompleted() {
		return this.numInvoicesCompleted;
	}

	public void setNumInvoicesCompleted(int numInvoicesCompleted) {
		this.numInvoicesCompleted = numInvoicesCompleted;
	}

	public int getNumInvoicesCreated() {
		return this.numInvoicesCreated;
	}

	public void setNumInvoicesCreated(int numInvoicesCreated) {
		this.numInvoicesCreated = numInvoicesCreated;
	}

}
package com.ibm.websphere.svt.gs.gsdb.entities;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlType;


/**
 * The persistent class for the CUSTOMERINFO database table.
 * 
 */
@Entity
@XmlType(name="CustomerInfo", namespace="http://entities.gsdb.gs.svt.websphere.ibm.com")
public class CustomerInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String custId;

	private String address1;

	private String address2;

	private String ccNum;

	private String eMail;

	private String phone;

    public CustomerInfo() {
    }

	public String getCustId() {
		return this.custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getAddress1() {
		return this.address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return this.address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCcNum() {
		return this.ccNum;
	}

	public void setCcNum(String ccNum) {
		this.ccNum = ccNum;
	}

	public String getEMail() {
		return this.eMail;
	}

	public void setEMail(String eMail) {
		this.eMail = eMail;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
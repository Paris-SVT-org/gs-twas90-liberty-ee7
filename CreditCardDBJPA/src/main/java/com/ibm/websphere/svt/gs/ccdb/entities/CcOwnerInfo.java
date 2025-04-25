package com.ibm.websphere.svt.gs.ccdb.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the CCOWNERINFO database table.
 * 
 */
@Entity
public class CcOwnerInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String ccNum;

	private String address1;

	private String address2;

	private String city;

	private String fName;

	private String lName;

	private String mName;

	private String phone;

	private String state1;

	private int zip;

    public CcOwnerInfo() {
    }

	public String getCcNum() {
		return this.ccNum;
	}

	public void setCcNum(String ccNum) {
		this.ccNum = ccNum;
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

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getFName() {
		return this.fName;
	}

	public void setFName(String fName) {
		this.fName = fName;
	}

	public String getLName() {
		return this.lName;
	}

	public void setLName(String lName) {
		this.lName = lName;
	}

	public String getMName() {
		return this.mName;
	}

	public void setMName(String mName) {
		this.mName = mName;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getState1() {
		return this.state1;
	}

	public void setState1(String state1) {
		this.state1 = state1;
	}

	public int getZip() {
		return this.zip;
	}

	public void setZip(int zip) {
		this.zip = zip;
	}

}
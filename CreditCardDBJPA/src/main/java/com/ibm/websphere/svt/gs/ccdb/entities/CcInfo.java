package com.ibm.websphere.svt.gs.ccdb.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the CCINFO database table.
 * 
 */
@Entity
public class CcInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String ccNum;

	private float balance;

	private float cLimit;

	private String expDate;

    public CcInfo() {
    }

	public String getCcNum() {
		return this.ccNum;
	}

	public void setCcNum(String ccNum) {
		this.ccNum = ccNum;
	}

	public float getBalance() {
		return this.balance;
	}

	public void setBalance(float balance) {
		this.balance = balance;
	}

	public float getCLimit() {
		return this.cLimit;
	}

	public void setCLimit(float cLimit) {
		this.cLimit = cLimit;
	}

	public String getExpDate() {
		return this.expDate;
	}

	public void setExpDate(String expDate) {
		this.expDate = expDate;
	}

}
package com.ibm.websphere.svt.gs.ccdb.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the CCAUTHINFO database table.
 * 
 */
@Entity
public class CcAuthInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String ccNum;

	private String password1;

	private String userId;

    public CcAuthInfo() {
    }

	public String getCcNum() {
		return this.ccNum;
	}

	public void setCcNum(String ccNum) {
		this.ccNum = ccNum;
	}

	public String getPassword1() {
		return this.password1;
	}

	public void setPassword1(String password1) {
		this.password1 = password1;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
package com.ibm.websphere.svt.gs.ccdb.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the CCUNIQUEID database table.
 * 
 */
@Entity
public class CcUniqueId implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String index1;

	private int ccNum;

	private int transId;

    public CcUniqueId() {
    }

	public String getIndex1() {
		return this.index1;
	}

	public void setIndex1(String index1) {
		this.index1 = index1;
	}

	public int getCcNum() {
		return this.ccNum;
	}

	public void setCcNum(int ccNum) {
		this.ccNum = ccNum;
	}

	public int getTransId() {
		return this.transId;
	}

	public void setTransId(int transId) {
		this.transId = transId;
	}

}
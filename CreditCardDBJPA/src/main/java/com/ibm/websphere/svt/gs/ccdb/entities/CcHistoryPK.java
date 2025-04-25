package com.ibm.websphere.svt.gs.ccdb.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the CCHISTORY database table.
 * 
 */
@Embeddable
public class CcHistoryPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	private String ccNum;

    @Temporal( TemporalType.TIMESTAMP)
	private java.util.Date timestamp1;

    public CcHistoryPK() {
    }
	public String getCcNum() {
		return this.ccNum;
	}
	public void setCcNum(String ccNum) {
		this.ccNum = ccNum;
	}
	public java.util.Date getTimestamp1() {
		return this.timestamp1;
	}
	public void setTimestamp1(java.util.Date timestamp1) {
		this.timestamp1 = timestamp1;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof CcHistoryPK)) {
			return false;
		}
		CcHistoryPK castOther = (CcHistoryPK)other;
		return 
			this.ccNum.equals(castOther.ccNum)
			&& this.timestamp1.equals(castOther.timestamp1);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.ccNum.hashCode();
		hash = hash * prime + this.timestamp1.hashCode();
		
		return hash;
    }
}
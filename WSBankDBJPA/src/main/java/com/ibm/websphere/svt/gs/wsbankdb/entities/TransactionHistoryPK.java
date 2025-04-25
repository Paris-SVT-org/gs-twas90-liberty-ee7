package com.ibm.websphere.svt.gs.wsbankdb.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the TRANSACTIONHISTORY database table.
 * 
 */
@Embeddable
public class TransactionHistoryPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	private int id;

	private int suffix;

    @Temporal( TemporalType.TIMESTAMP)
	private java.util.Date time1;

    public TransactionHistoryPK() {
    }
	public int getId() {
		return this.id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getSuffix() {
		return this.suffix;
	}
	public void setSuffix(int suffix) {
		this.suffix = suffix;
	}
	public java.util.Date getTime1() {
		return this.time1;
	}
	public void setTime1(java.util.Date time1) {
		this.time1 = time1;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TransactionHistoryPK)) {
			return false;
		}
		TransactionHistoryPK castOther = (TransactionHistoryPK)other;
		return 
			(this.id == castOther.id)
			&& (this.suffix == castOther.suffix)
			&& this.time1.equals(castOther.time1);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.id;
		hash = hash * prime + this.suffix;
		hash = hash * prime + this.time1.hashCode();
		
		return hash;
    }
}
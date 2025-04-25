package com.ibm.websphere.svt.gs.gsdb.entities;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlType;

/**
 * The primary key class for the STORECREDIT database table.
 * 
 */
@Embeddable
@XmlType(name="StoreCreditPK", namespace="http://entities.gsdb.gs.svt.websphere.ibm.com")
public class StoreCreditPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	private String custId;

    @Temporal( TemporalType.TIMESTAMP)
	private java.util.Date time1;

    public StoreCreditPK() {
    }
	public String getCustId() {
		return this.custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
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
		if (!(other instanceof StoreCreditPK)) {
			return false;
		}
		StoreCreditPK castOther = (StoreCreditPK)other;
		return 
			this.custId.equals(castOther.custId)
			&& this.time1.equals(castOther.time1);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.custId.hashCode();
		hash = hash * prime + this.time1.hashCode();
		
		return hash;
    }
}
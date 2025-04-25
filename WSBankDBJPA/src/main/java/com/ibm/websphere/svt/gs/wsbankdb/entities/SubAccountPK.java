package com.ibm.websphere.svt.gs.wsbankdb.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the SUBACCOUNT database table.
 * 
 */
@Embeddable
public class SubAccountPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	private int suffix;

	private int id;

    public SubAccountPK() {
    }
	public int getSuffix() {
		return this.suffix;
	}
	public void setSuffix(int suffix) {
		this.suffix = suffix;
	}
	public int getId() {
		return this.id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof SubAccountPK)) {
			return false;
		}
		SubAccountPK castOther = (SubAccountPK)other;
		return 
			(this.suffix == castOther.suffix)
			&& (this.id == castOther.id);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.suffix;
		hash = hash * prime + this.id;
		
		return hash;
    }
}
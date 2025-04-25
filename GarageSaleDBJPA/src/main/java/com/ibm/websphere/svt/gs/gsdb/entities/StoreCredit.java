package com.ibm.websphere.svt.gs.gsdb.entities;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlType;


/**
 * The persistent class for the STORECREDIT database table.
 * 
 */
@Entity
@NamedQuery(name = "getAllStoreCredit", query = "SELECT s FROM StoreCredit s")
@XmlType(name="StoreCredit", namespace="http://entities.gsdb.gs.svt.websphere.ibm.com")
public class StoreCredit implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private StoreCreditPK id;

	private float amount;

    public StoreCredit() {
    }

	public StoreCreditPK getId() {
		return this.id;
	}

	public void setId(StoreCreditPK id) {
		this.id = id;
	}
	
	public float getAmount() {
		return this.amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

}
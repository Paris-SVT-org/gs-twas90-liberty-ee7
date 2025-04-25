package com.ibm.websphere.svt.gs.wsbankdb.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the SUBACCOUNT database table.
 * 
 */
@Entity
@NamedQuery(name = "getSubAccountsById", query = "SELECT s FROM SubAccount s WHERE s.id.id = :id_id")
public class SubAccount implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private SubAccountPK id;

	private float balance;

	private Timestamp lastTransactionTime;

	private String type1;

    public SubAccount() {
    }

	public SubAccountPK getId() {
		return this.id;
	}

	public void setId(SubAccountPK id) {
		this.id = id;
	}
	
	public float getBalance() {
		return this.balance;
	}

	public void setBalance(float balance) {
		this.balance = balance;
	}

	public Timestamp getLastTransactionTime() {
		return this.lastTransactionTime;
	}

	public void setLastTransactionTime(Timestamp lastTransactionTime) {
		this.lastTransactionTime = lastTransactionTime;
	}

	public String getType1() {
		return this.type1;
	}

	public void setType1(String type1) {
		this.type1 = type1;
	}

}
package com.ibm.websphere.svt.gs.wsbankdb.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the TRANSACTIONHISTORY database table.
 * 
 */
@Entity
@NamedQuery(name = "getTransactionHistoryBySubAccount", query = "SELECT t FROM TransactionHistory t WHERE t.id.id = :id_id AND  t.id.suffix = :id_suffix")
public class TransactionHistory implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TransactionHistoryPK id;

	private float amount;

	private float newBalance;

	private String partner;

    public TransactionHistory() {
    }

	public TransactionHistoryPK getId() {
		return this.id;
	}

	public void setId(TransactionHistoryPK id) {
		this.id = id;
	}
	
	public float getAmount() {
		return this.amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public float getNewBalance() {
		return this.newBalance;
	}

	public void setNewBalance(float newBalance) {
		this.newBalance = newBalance;
	}

	public String getPartner() {
		return this.partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

}
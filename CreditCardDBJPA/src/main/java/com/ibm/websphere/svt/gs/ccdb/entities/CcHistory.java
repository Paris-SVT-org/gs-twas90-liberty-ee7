package com.ibm.websphere.svt.gs.ccdb.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the CCHISTORY database table.
 * 
 */
@Entity
@NamedQuery(name = "getCcHistoryByCCNum", query = "SELECT c FROM CcHistory c WHERE c.id.ccNum = :id_ccNum")
public class CcHistory implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private CcHistoryPK id;

	private float amount;

	private String companyId;

	private String transactionId;

    public CcHistory() {
    }

	public CcHistoryPK getId() {
		return this.id;
	}

	public void setId(CcHistoryPK id) {
		this.id = id;
	}
	
	public float getAmount() {
		return this.amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public String getCompanyId() {
		return this.companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getTransactionId() {
		return this.transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

}
package com.ibm.websphere.svt.gs.gsdb.entities;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlType;


/**
 * The persistent class for the TAXRATE database table.
 * 
 */
@Entity
@NamedQuery(name = "getAllTaxRateList", query = "SELECT t FROM TaxRate t ORDER BY t.stateId")
@XmlType(name="TaxRate", namespace="http://entities.gsdb.gs.svt.websphere.ibm.com")
public class TaxRate implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String stateId;

	private String stateName;

	private float taxRate;

    public TaxRate() {
    }

	public String getStateId() {
		return this.stateId;
	}

	public void setStateId(String stateId) {
		this.stateId = stateId;
	}

	public String getStateName() {
		return this.stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public float getTaxRate() {
		return this.taxRate;
	}

	public void setTaxRate(float taxRate) {
		this.taxRate = taxRate;
	}

}
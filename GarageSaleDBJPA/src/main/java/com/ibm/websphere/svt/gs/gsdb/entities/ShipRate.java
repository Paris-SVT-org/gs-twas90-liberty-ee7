package com.ibm.websphere.svt.gs.gsdb.entities;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlType;


/**
 * The persistent class for the SHIPRATE database table.
 * 
 */
@Entity
@NamedQuery(name = "getAllShipRateList", query = "SELECT s FROM ShipRate s ORDER BY s.itemId")
@XmlType(name="ShipRate", namespace="http://entities.gsdb.gs.svt.websphere.ibm.com")
public class ShipRate implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String itemId;

	private float cost;

    public ShipRate() {
    }

	public String getItemId() {
		return this.itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public float getCost() {
		return this.cost;
	}

	public void setCost(float cost) {
		this.cost = cost;
	}

}
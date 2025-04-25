package com.ibm.websphere.svt.gs.gsdb.entities;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlType;


/**
 * The persistent class for the INVENTORY database table.
 * 
 */
@Entity
@NamedQueries({@NamedQuery(name = "getInventoryByCatAndMfg", query = "SELECT i FROM Inventory i WHERE i.categoryName = :categoryName AND  i.mfgName = :mfgName"),@NamedQuery(name = "getInventoryListByCatName", query = "SELECT i FROM Inventory i WHERE i.categoryName = :categoryName"),
@NamedQuery(name = "getInventoryListByMfgName", query = "SELECT i FROM Inventory i WHERE i.mfgName = :mfgName"),
@NamedQuery(name = "getAllInventory", query = "SELECT i FROM Inventory i")})
@XmlType(name="Inventory", namespace="http://entities.gsdb.gs.svt.websphere.ibm.com")
public class Inventory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String itemId;

	private String categoryName;

	private String description;

	private int inventorySold;

	private String mfgName;

	private byte[] pic;

	private float unitPrice;

    public Inventory() {
    }

	public String getItemId() {
		return this.itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getCategoryName() {
		return this.categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getInventorySold() {
		return this.inventorySold;
	}

	public void setInventorySold(int inventorySold) {
		this.inventorySold = inventorySold;
	}

	public String getMfgName() {
		return this.mfgName;
	}

	public void setMfgName(String mfgName) {
		this.mfgName = mfgName;
	}

	public byte[] getPic() {
		return this.pic;
	}

	public void setPic(byte[] pic) {
		this.pic = pic;
	}

	public float getUnitPrice() {
		return this.unitPrice;
	}

	public void setUnitPrice(float unitPrice) {
		this.unitPrice = unitPrice;
	}

}
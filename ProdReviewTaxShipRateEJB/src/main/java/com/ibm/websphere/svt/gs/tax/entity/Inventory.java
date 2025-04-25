package com.ibm.websphere.svt.gs.tax.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * The persistent class for the INVENTORY database table.
 * 
 */
@Entity
@Table(name="INVENTORY")
@NamedQuery(name = "getAllItemsByOrder",  query = "SELECT t from Inventory t ORDER BY t.itemId")
@XmlRootElement(name="Inventory", namespace="http://entity.tax.gs.svt.websphere.ibm.com")
public class Inventory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ITEMID",unique=true, nullable=false, length=250)
	private String itemId;

	@Column(name="CATEGORYNAME",length=250)
	private String categoryName;

	@Column(name="DESCRIPTION",length=250)
	private String description;

	@Column(name="INVENTORYSOLD",nullable=false)
	private int inventorySold;

	@Column(name="MFGNAME",length=250)
	private String mfgName;

	private byte[] pic;

	@Column(name="UNITPRICE",nullable=false)
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
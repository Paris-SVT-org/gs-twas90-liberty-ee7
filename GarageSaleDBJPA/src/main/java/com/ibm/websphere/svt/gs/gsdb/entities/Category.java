package com.ibm.websphere.svt.gs.gsdb.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlType;


/**
 * The persistent class for the CATEGORY database table.
 * 
 */
@Entity
@NamedQuery(name = "getCategories", query = "SELECT c FROM Category c ORDER BY c.categoryName")
@XmlType(name="Category", namespace="http://entities.gsdb.gs.svt.websphere.ibm.com")
public class Category implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String categoryName;

	private String categoryDescription;

	private int numItemsInCategory;

    public Category() {
    }

	public String getCategoryName() {
		return this.categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategoryDescription() {
		return this.categoryDescription;
	}

	public void setCategoryDescription(String categoryDescription) {
		this.categoryDescription = categoryDescription;
	}

	public int getNumItemsInCategory() {
		return this.numItemsInCategory;
	}

	public void setNumItemsInCategory(int numItemsInCategory) {
		this.numItemsInCategory = numItemsInCategory;
	}

}
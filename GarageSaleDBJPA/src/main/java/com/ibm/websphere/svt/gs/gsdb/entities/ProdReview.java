package com.ibm.websphere.svt.gs.gsdb.entities;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlType;


/**
 * The persistent class for the PRODREVIEW database table.
 * 
 */
@Entity
@XmlType(name="ProdReview", namespace="http://entities.gsdb.gs.svt.websphere.ibm.com")
public class ProdReview implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private long reviewId;

	private String comments;

	private String custId;

	private String itemId;

    public ProdReview() {
    }

	public long getReviewId() {
		return this.reviewId;
	}

	public void setReviewId(long reviewId) {
		this.reviewId = reviewId;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getCustId() {
		return this.custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getItemId() {
		return this.itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

}
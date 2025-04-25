package com.ibm.websphere.svt.gs.tax.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * The persistent class for the PRODREVIEW database table.
 * 
 */
@Entity
@Table(name="PRODREVIEW")
@NamedQuery(name = "getAllProdReviewsByOrder", query = "SELECT t from ProdReview t ORDER BY t.itemId")
@XmlRootElement(name="ProdReview", namespace="http://entity.tax.gs.svt.websphere.ibm.com")
public class ProdReview implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	//@GeneratedValue(strategy=GenerationType.AUTO)
	@TableGenerator(name = "OPENJPA_SEQUENCE_TABLE", table = "OPENJPA_SEQUENCE_TABLE", pkColumnName = "ID", valueColumnName = "SEQUENCE_VALUE", pkColumnValue = "0")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "OPENJPA_SEQUENCE_TABLE")
	@Column(name="REVIEWID",unique=true, nullable=false)
	private long reviewId;

	@Column(name="COMMENTS",nullable=false, length=1024)
	private String comments;

	@Column(name="CUSTID",unique=true, nullable=false, length=250)
	private String custId;

	@Column(name="ITEMID",unique=true, nullable=false, length=250)
	@Basic(optional=false)
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
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	
}
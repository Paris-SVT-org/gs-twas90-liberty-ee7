package com.ibm.websphere.svt.gs.gsdb.entities;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlType;

/**
 * The primary key class for the MFGCATEGORY database table.
 * 
 */
@Embeddable
@XmlType(name="MfgCategoryPK", namespace="http://entities.gsdb.gs.svt.websphere.ibm.com")
public class MfgCategoryPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	private String mfgName;

	@Column(name="CATEGORY_CATEGORYNAME")
	private String categoryCategoryName;

    public MfgCategoryPK() {
    }
	public String getMfgName() {
		return this.mfgName;
	}
	public void setMfgName(String mfgName) {
		this.mfgName = mfgName;
	}
	public String getCategoryCategoryName() {
		return this.categoryCategoryName;
	}
	public void setCategoryCategoryName(String categoryCategoryName) {
		this.categoryCategoryName = categoryCategoryName;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MfgCategoryPK)) {
			return false;
		}
		MfgCategoryPK castOther = (MfgCategoryPK)other;
		return 
			this.mfgName.equals(castOther.mfgName)
			&& this.categoryCategoryName.equals(castOther.categoryCategoryName);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.mfgName.hashCode();
		hash = hash * prime + this.categoryCategoryName.hashCode();
		
		return hash;
    }
}
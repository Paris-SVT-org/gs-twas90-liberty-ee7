package com.ibm.websphere.svt.gs.gsdb.entities;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlType;


/**
 * The persistent class for the MFGCATEGORY database table.
 * 
 */
@Entity
@NamedQueries({@NamedQuery(name = "getAllMfgCategories", query = "SELECT m FROM MfgCategory m"),@NamedQuery(name = "getMfgCategory", query = "SELECT m FROM MfgCategory m WHERE m.id.categoryCategoryName = :id_categoryCategoryName ORDER BY m.id.mfgName")})
@XmlType(name="MfgCategory", namespace="http://entities.gsdb.gs.svt.websphere.ibm.com")
public class MfgCategory implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MfgCategoryPK id;

    public MfgCategory() {
    }

	public MfgCategoryPK getId() {
		return this.id;
	}

	public void setId(MfgCategoryPK id) {
		this.id = id;
	}
	
}
package com.ibm.websphere.svt.gs.wsbankdb.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the ACCOUNT database table.
 * 
 */
@Entity
public class Account implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

    public Account() {
    }

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
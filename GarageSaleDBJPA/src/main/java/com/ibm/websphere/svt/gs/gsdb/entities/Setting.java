package com.ibm.websphere.svt.gs.gsdb.entities;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlType;


/**
 * The persistent class for the SETTINGS database table.
 * 
 */
@Entity
@Table(name="SETTINGS")
@XmlType(name="Setting", namespace="http://entities.gsdb.gs.svt.websphere.ibm.com")
public class Setting implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	private int numCategories;

	private int numCustomers;

	private int numItemsPerMfg;

	private int numMfgCategories;

	private int numSubAccounts;

	private short sdoEnabled;

	private short wsatEnabled;

	private short wsnEnabled;

	private String wsnVariables;

    public Setting() {
    }

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNumCategories() {
		return this.numCategories;
	}

	public void setNumCategories(int numCategories) {
		this.numCategories = numCategories;
	}

	public int getNumCustomers() {
		return this.numCustomers;
	}

	public void setNumCustomers(int numCustomers) {
		this.numCustomers = numCustomers;
	}

	public int getNumItemsPerMfg() {
		return this.numItemsPerMfg;
	}

	public void setNumItemsPerMfg(int numItemsPerMfg) {
		this.numItemsPerMfg = numItemsPerMfg;
	}

	public int getNumMfgCategories() {
		return this.numMfgCategories;
	}

	public void setNumMfgCategories(int numMfgCategories) {
		this.numMfgCategories = numMfgCategories;
	}

	public int getNumSubAccounts() {
		return this.numSubAccounts;
	}

	public void setNumSubAccounts(int numSubAccounts) {
		this.numSubAccounts = numSubAccounts;
	}

	public short getSdoEnabled() {
		return this.sdoEnabled;
	}

	public void setSdoEnabled(short sdoEnabled) {
		this.sdoEnabled = sdoEnabled;
	}

	public short getWsatEnabled() {
		return this.wsatEnabled;
	}

	public void setWsatEnabled(short wsatEnabled) {
		this.wsatEnabled = wsatEnabled;
	}

	public short getWsnEnabled() {
		return this.wsnEnabled;
	}

	public void setWsnEnabled(short wsnEnabled) {
		this.wsnEnabled = wsnEnabled;
	}

	public String getWsnVariables() {
		return this.wsnVariables;
	}

	public void setWsnVariables(String wsnVariables) {
		this.wsnVariables = wsnVariables;
	}

}
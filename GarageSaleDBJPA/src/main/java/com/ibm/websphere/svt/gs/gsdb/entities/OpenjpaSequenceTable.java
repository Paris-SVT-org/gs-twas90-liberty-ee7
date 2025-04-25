package com.ibm.websphere.svt.gs.gsdb.entities;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlType;


/**
 * The persistent class for the OPENJPA_SEQUENCE_TABLE database table.
 * 
 */
@Entity
@Table(name="OPENJPA_SEQUENCE_TABLE")
@XmlType(name="OpenjpaSequenceTable", namespace="http://entities.gsdb.gs.svt.websphere.ibm.com")
public class OpenjpaSequenceTable implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	@Column(name="SEQUENCE_VALUE")
	private long sequenceValue;

    public OpenjpaSequenceTable() {
    }

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getSequenceValue() {
		return this.sequenceValue;
	}

	public void setSequenceValue(long sequenceValue) {
		this.sequenceValue = sequenceValue;
	}

}
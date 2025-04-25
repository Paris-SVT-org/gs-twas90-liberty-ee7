/**
 * 
 */
package com.ibm.websphere.svt.gs.gsdb.session.view;

import com.ibm.websphere.svt.gs.gsdb.entities.OpenjpaSequenceTable;

/**
 * @author JAGRAJ
 *
 */
public interface OpenjpaSequenceTableSessionLocal {

	String createOpenjpaSequenceTable(OpenjpaSequenceTable openjpaSequenceTable)
			throws Exception;

	String deleteOpenjpaSequenceTable(OpenjpaSequenceTable openjpaSequenceTable)
			throws Exception;

	String updateOpenjpaSequenceTable(OpenjpaSequenceTable openjpaSequenceTable)
			throws Exception;

	OpenjpaSequenceTable findOpenjpaSequenceTableById(int id);

	OpenjpaSequenceTable getNewOpenjpaSequenceTable();

}

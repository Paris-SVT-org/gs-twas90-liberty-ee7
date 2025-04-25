/**
 * 
 */
package com.ibm.websphere.svt.gs.gsdb.session.view;

import com.ibm.websphere.svt.gs.gsdb.entities.TaxRate;
import java.util.List;

/**
 * @author JAGRAJ
 *
 */
public interface TaxRateSessionLocal1 {

	String createTaxRate(TaxRate taxRate) throws Exception;

	String deleteTaxRate(TaxRate taxRate) throws Exception;

	String updateTaxRate(TaxRate taxRate) throws Exception;

	TaxRate findTaxRateByStateId(String stateId);

	TaxRate getNewTaxRate();

	List<TaxRate> getAllTaxRateList();

}

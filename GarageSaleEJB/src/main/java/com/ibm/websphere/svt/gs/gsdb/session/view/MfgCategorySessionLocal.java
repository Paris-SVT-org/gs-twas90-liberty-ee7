/**
 * 
 */
package com.ibm.websphere.svt.gs.gsdb.session.view;

import com.ibm.websphere.svt.gs.gsdb.entities.MfgCategory;
import com.ibm.websphere.svt.gs.wrapper.MfgCategoryWrapper;
import java.util.List;

/**
 * @author JAGRAJ
 *
 */
public interface MfgCategorySessionLocal {

	String createMfgCategory(MfgCategory mfgCategory) throws Exception;

	String deleteMfgCategory(MfgCategory mfgCategory) throws Exception;

	String updateMfgCategory(MfgCategory mfgCategory) throws Exception;

	MfgCategory findMfgCategoryByPrimaryKey(String mfgName,
			String categoryCategoryName);

	MfgCategory getNewMfgCategory();

	List<MfgCategory> getMfgCategory(String id_categoryCategoryName);

	MfgCategoryWrapper getWrapper(MfgCategory mfgCategory);

	List<MfgCategory> getAllMfgCategories();

}

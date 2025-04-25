/**
 * 
 */
package com.ibm.websphere.svt.gs.gsdb.session.view;

import com.ibm.websphere.svt.gs.gsdb.entities.Category;
import com.ibm.websphere.svt.gs.wrapper.CategoryWrapper;
import java.util.List;

/**
 * @author JAGRAJ
 *
 */
public interface CategorySessionLocal {

	void setCategories(List<Category> categories);

	String createCategory(Category category) throws Exception;

	String deleteCategory(Category category) throws Exception;

	String updateCategory(Category category) throws Exception;

	Category findCategoryByCategoryName(String categoryName);

	Category getNewCategory();

	List<Category> getCategories();

	CategoryWrapper getWrapper(Category category);

	String getNextItemID(Category categoryLocal);

}

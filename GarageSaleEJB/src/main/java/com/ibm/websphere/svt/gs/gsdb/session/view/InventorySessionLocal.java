package com.ibm.websphere.svt.gs.gsdb.session.view;

import com.ibm.websphere.svt.gs.gsdb.entities.Inventory;
import com.ibm.websphere.svt.gs.wrapper.InventoryWrapper;
import java.util.List;

public interface InventorySessionLocal {

	String createInventory(Inventory inventory) throws Exception;

	String deleteInventory(Inventory inventory) throws Exception;

	String updateInventory(Inventory inventory) throws Exception;

	Inventory findInventoryByItemId(String itemId);

	Inventory getNewInventory();

	List<Inventory> getAllInventory();

	List<Inventory> getInventoryListByCatName(String categoryName);

	List<Inventory> getInventoryListByMfgName(String mfgName);

	List<Inventory> getInventoryByCatAndMfg(String categoryName, String mfgName);

	InventoryWrapper getWrapper(Inventory inventory);

}

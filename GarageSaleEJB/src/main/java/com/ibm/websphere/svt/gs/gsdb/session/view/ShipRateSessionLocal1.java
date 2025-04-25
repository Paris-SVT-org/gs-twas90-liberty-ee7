/**
 * 
 */
package com.ibm.websphere.svt.gs.gsdb.session.view;

import com.ibm.websphere.svt.gs.gsdb.entities.ShipRate;
import java.util.List;

/**
 * @author JAGRAJ
 *
 */
public interface ShipRateSessionLocal1 {

	String createShipRate(ShipRate shipRate) throws Exception;

	String deleteShipRate(ShipRate shipRate) throws Exception;

	String updateShipRate(ShipRate shipRate) throws Exception;

	ShipRate findShipRateByItemId(String itemId);

	ShipRate getNewShipRate();

	List<ShipRate> getAllShipRateList();

}

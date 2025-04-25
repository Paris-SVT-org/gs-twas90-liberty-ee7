/**
 * 
 */
package com.ibm.websphere.svt.gs.gsdb.session.view;

import com.ibm.websphere.svt.gs.gsdb.entities.Setting;

/**
 * @author JAGRAJ
 *
 */
public interface SettingSessionLocal {

	String createSetting(Setting setting) throws Exception;

	String deleteSetting(Setting setting) throws Exception;

	String updateSetting(Setting setting) throws Exception;

	Setting findSettingById(int id);

	Setting getNewSetting();

}

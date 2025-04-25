/*
 * Copyright 2009 Alexander Bell
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Author: Alexander Bell (latest modification by $Author: ganeshpuri $)
 * Version: $Revision: 1.3 $ $Date: 2009/08/31 17:29:17 $
 */
package com.ibm.websphere.svt.gs.gsjsfweb;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class DojoBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String style = "soria";
	private String selectedTab = "exampleForm:button";
	private String color;
	private boolean showSoria = true;
	private boolean showTundra = true;
	private boolean showNihilo = true;
	private String menuTest;
	private boolean showMenuTest1 = true;
	private boolean showMenuTest2 = true;
	private boolean showMenuTest3 = true;

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}
	
	public String getSelectedTab() {
		return selectedTab;
	}

	public void setSelectedTab(String selectedTab) {
		this.selectedTab = selectedTab;
	}

	public boolean isShowSoria() {
		return showSoria;
	}

	public void setShowSoria(boolean showSoria) {
		this.showSoria = showSoria;
	}

	public boolean isShowTundra() {
		return showTundra;
	}

	public void setShowTundra(boolean showTundra) {
		this.showTundra = showTundra;
	}

	public boolean isShowNihilo() {
		return showNihilo;
	}

	public void setShowNihilo(boolean showNihilo) {
		this.showNihilo = showNihilo;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getColor() {
		return color;
	}

	public void setMenuTest(String menuTest) {
		this.menuTest = menuTest;
	}

	public String getMenuTest() {
		return menuTest;
	}

	public void setShowMenuTest1(boolean showMenuTest1) {
		this.showMenuTest1 = showMenuTest1;
	}

	public boolean isShowMenuTest1() {
		return showMenuTest1;
	}

	public void setShowMenuTest2(boolean showMenuTest2) {
		this.showMenuTest2 = showMenuTest2;
	}

	public boolean isShowMenuTest2() {
		return showMenuTest2;
	}

	public void setShowMenuTest3(boolean showMenuTest3) {
		this.showMenuTest3 = showMenuTest3;
	}

	public boolean isShowMenuTest3() {
		return showMenuTest3;
	}
	
}


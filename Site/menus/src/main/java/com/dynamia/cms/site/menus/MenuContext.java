/* 
 * Copyright 2016 Dynamia Soluciones IT SAS and the original author or authors.
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
 */
package com.dynamia.cms.site.menus;

import java.io.Serializable;

import com.dynamia.cms.site.menus.domain.Menu;
import com.dynamia.cms.site.menus.domain.MenuItem;
import com.dynamia.cms.site.menus.domain.MenuItemParameter;

/**
 *
 * @author Mario Serrano Leones
 */
public class MenuContext implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3721516938528381332L;

	private MenuItem menuItem;

	private Menu parent;

	public MenuContext(MenuItem menuItem, Menu parent) {
		this.menuItem = menuItem;
		this.parent = parent;
	}

	public MenuItem getMenuItem() {
		return menuItem;
	}

	public Menu getParent() {
		return parent;
	}

	public MenuItemParameter getParameter(String name) {
		return menuItem.getParameter(name);
	}

	public void update(MenuItem item) {
		this.menuItem = item;
	}

}

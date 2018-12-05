/*
 *  Copyright (c) 2018 Dynamia Soluciones IT SAS and the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package tools.dynamia.cms.menus
/**
 *
 * @author Mario Serrano Leones
 */
class MenuContext implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3721516938528381332L

    private tools.dynamia.cms.menus.domain.MenuItem menuItem

    private tools.dynamia.cms.menus.domain.Menu parent

    MenuContext(tools.dynamia.cms.menus.domain.MenuItem menuItem, tools.dynamia.cms.menus.domain.Menu parent) {
		this.menuItem = menuItem
        this.parent = parent
    }

    tools.dynamia.cms.menus.domain.MenuItem getMenuItem() {
		return menuItem
    }

    tools.dynamia.cms.menus.domain.Menu getParent() {
		return parent
    }

    tools.dynamia.cms.menus.domain.MenuItemParameter getParameter(String name) {
		return menuItem.getParameter(name)
    }

    void update(tools.dynamia.cms.menus.domain.MenuItem item) {
		this.menuItem = item
    }

}

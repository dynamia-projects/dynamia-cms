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

package tools.dynamia.cms.menus.admin.controllers

import tools.dynamia.cms.menus.domain.Menu
import tools.dynamia.cms.menus.domain.MenuItem
import tools.dynamia.domain.query.QueryConditions
import tools.dynamia.domain.query.QueryParameters
import tools.dynamia.zk.crud.TreeCrudController

class MenuItemTreeCrudController extends TreeCrudController<MenuItem> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L
    private Menu menu

    void setMenu(Menu menu) {
		this.menu = menu
    }

    Menu getMenu() {
		return menu
    }

	@Override
	protected void beforeQuery() {
		if (menu != null) {
			setParemeter("menu", menu)
        }
	}

	@Override
	protected void afterCreate() {
        entity.menu = menu
    }

	@Override
	protected boolean isLeaf(MenuItem data) {
		return data.subitems.empty
    }
	@Override
    String getRootLabel() {
		return menu.name
    }
	
	@Override
	protected Collection<MenuItem> loadRoots() {
		return crudService.find(MenuItem.class, QueryParameters.with("menu", menu)
				.add(parentName, QueryConditions.isNull())
				.orderBy("order"))
    }
}

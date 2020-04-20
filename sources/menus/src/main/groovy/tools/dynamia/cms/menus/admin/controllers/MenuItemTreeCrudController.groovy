/*
 * Copyright (C) 2009 - 2020 Dynamia Soluciones IT S.A.S - NIT 900302344-1
 * Colombia - South America
 * All Rights Reserved.
 *
 * DynamiaCMS is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License (LGPL v3) as
 * published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DynamiaCMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with DynamiaCMS.  If not, see <https://www.gnu.org/licenses/>.
 *
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

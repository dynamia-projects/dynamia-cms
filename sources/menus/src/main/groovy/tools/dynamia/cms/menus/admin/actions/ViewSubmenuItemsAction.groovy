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

package tools.dynamia.cms.menus.admin.actions

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.actions.InstallAction
import tools.dynamia.cms.menus.admin.controllers.MenuItemTreeCrudController
import tools.dynamia.cms.menus.domain.Menu
import tools.dynamia.cms.menus.domain.MenuItem
import tools.dynamia.commons.ApplicableClass
import tools.dynamia.crud.AbstractCrudAction
import tools.dynamia.crud.CrudActionEvent
import tools.dynamia.crud.CrudState
import tools.dynamia.domain.services.CrudService
import tools.dynamia.ui.MessageType
import tools.dynamia.ui.UIMessages
import tools.dynamia.viewers.util.Viewers
import tools.dynamia.zk.crud.CrudView
import tools.dynamia.zk.navigation.ComponentPage
import tools.dynamia.zk.navigation.ZKNavigationManager

@InstallAction
class ViewSubmenuItemsAction extends AbstractCrudAction {

	@Autowired
	private CrudService crudService

    ViewSubmenuItemsAction() {
        name = "Items"
        image = "menu"
        menuSupported = true
    }

	@Override
    CrudState[] getApplicableStates() {
		return CrudState.get(CrudState.READ)
    }

	@Override
    ApplicableClass[] getApplicableClasses() {
		return ApplicableClass.get(Menu.class)
    }

	@Override
    void actionPerformed(CrudActionEvent evt) {
		Menu menu = (Menu) evt.data
        if (menu != null) {
			menu = crudService.reload(menu)
            CrudView<MenuItem> ui = (CrudView<MenuItem>) Viewers.getView(MenuItem.class, "crud", null)
            MenuItemTreeCrudController controller = (MenuItemTreeCrudController) ui.controller
            controller.menu = menu
            controller.doQuery()
            ZKNavigationManager.instance.currentPage = new ComponentPage("viewSubmenus" + menu.id,  menu.name +" Items", ui)
        } else {
			UIMessages.showMessage("Select menu to view submenus", MessageType.ERROR)
        }

	}
}

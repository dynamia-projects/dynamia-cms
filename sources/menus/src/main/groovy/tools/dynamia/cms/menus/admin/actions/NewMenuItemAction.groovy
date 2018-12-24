/*
 * Copyright (C) 2009 - 2019 Dynamia Soluciones IT S.A.S - NIT 900302344-1
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
import tools.dynamia.cms.menus.admin.ui.MenuItemsUI
import tools.dynamia.cms.menus.domain.Menu
import tools.dynamia.cms.menus.domain.MenuItem
import tools.dynamia.commons.ApplicableClass
import tools.dynamia.crud.CrudActionEvent
import tools.dynamia.crud.CrudState
import tools.dynamia.crud.actions.NewAction
import tools.dynamia.domain.services.CrudService
import tools.dynamia.zk.crud.CrudView
import tools.dynamia.zk.util.ZKUtil

@InstallAction
class NewMenuItemAction extends NewAction {

    @Autowired
    private CrudService crudService

    @Override
    CrudState[] getApplicableStates() {
        return CrudState.get(CrudState.READ)
    }

    @Override
    ApplicableClass[] getApplicableClasses() {
        return ApplicableClass.get(MenuItem.class)
    }

    @Override
    void actionPerformed(CrudActionEvent evt) {
        CrudView<MenuItem> crudView = (CrudView<MenuItem>) evt.crudView
        MenuItemTreeCrudController controller = (MenuItemTreeCrudController) evt.controller
        MenuItem selectedItem = (MenuItem) evt.data
        Menu menu = controller.menu

        controller.doCreate()

        MenuItemsUI ui = new MenuItemsUI(controller.entity)
        ui.addAction(new SaveMenuItemAction(crudService, evt))

        String title = "New Item for " + menu.name
        if (selectedItem != null) {
            title = "New Subitem for " + menu.name + " - " + selectedItem.name

        }

        ZKUtil.showDialog(title, ui, "90%", "90%").maximizable = true

    }
}

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

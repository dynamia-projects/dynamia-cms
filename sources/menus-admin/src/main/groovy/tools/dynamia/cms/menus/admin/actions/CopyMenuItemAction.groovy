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
import tools.dynamia.actions.ActionGroup
import tools.dynamia.actions.InstallAction
import tools.dynamia.cms.menus.admin.ui.MenuItemsUI
import tools.dynamia.cms.menus.domain.MenuItem
import tools.dynamia.commons.ApplicableClass
import tools.dynamia.crud.AbstractCrudAction
import tools.dynamia.crud.CrudActionEvent
import tools.dynamia.crud.CrudState
import tools.dynamia.domain.services.CrudService
import tools.dynamia.ui.MessageType
import tools.dynamia.ui.UIMessages
import tools.dynamia.zk.util.ZKUtil

@InstallAction
class CopyMenuItemAction extends AbstractCrudAction {

    @Autowired
    private CrudService crudService

    CopyMenuItemAction() {
        group = ActionGroup.get("CRUD")
        name = "Copy"
        image = "copy"
    }

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
        MenuItem menuItem = (MenuItem) evt.data
        if (menuItem != null) {
            menuItem = crudService.reload(menuItem)
            menuItem = menuItem.clone()
            MenuItemsUI ui = new MenuItemsUI(menuItem)
            ui.addAction(new SaveMenuItemAction(crudService, evt))
            ZKUtil.showDialog(menuItem.title, ui, "90%", "90%")
        } else {
            UIMessages.showMessage("Select menu item", MessageType.ERROR)
        }

    }
}

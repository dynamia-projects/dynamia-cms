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
import tools.dynamia.cms.menus.domain.MenuItem
import tools.dynamia.commons.ApplicableClass
import tools.dynamia.crud.CrudActionEvent
import tools.dynamia.crud.actions.DeleteAction
import tools.dynamia.domain.query.QueryParameters
import tools.dynamia.domain.services.CrudService
import tools.dynamia.ui.MessageType
import tools.dynamia.ui.UIMessages

@InstallAction
class DeleteMenuItemAction extends DeleteAction {

    @Autowired
    private CrudService crudService

    @Override
    void actionPerformed(CrudActionEvent evt) {
        MenuItem submenu = (MenuItem) evt.data
        if (submenu != null) {
            UIMessages.showQuestion("Are you sure?", {
                crudService.executeWithinTransaction {

                    try {
                        crudService.execute("DELETE FROM MenuItem mi where mi.parentItem.id = :id",
                                QueryParameters.with("id", submenu.id))

                        crudService.execute("DELETE FROM MenuItem mi where mi.id = :id",
                                QueryParameters.with("id", submenu.id))
                        UIMessages.showMessage("Menu item deleted succesfull")
                        evt.controller.doQuery()
                    } catch (Exception e) {
                        UIMessages
                                .showMessage("Error deleting item. Try deleting subitems first: " + e.message)
                        e.printStackTrace()
                    }
                }
            })
        } else {
            UIMessages.showMessage("Select menu item to delete", MessageType.WARNING)
        }
    }

    @Override
    ApplicableClass[] getApplicableClasses() {
        return ApplicableClass.get(MenuItem.class)
    }

}

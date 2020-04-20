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

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

import org.zkoss.util.Locales
import tools.dynamia.actions.AbstractAction
import tools.dynamia.actions.ActionEvent
import tools.dynamia.actions.ActionRenderer
import tools.dynamia.cms.menus.admin.ui.MenuItemsUI
import tools.dynamia.cms.menus.domain.MenuItem
import tools.dynamia.cms.menus.domain.MenuItemParameter
import tools.dynamia.commons.BeanMessages
import tools.dynamia.crud.CrudActionEvent
import tools.dynamia.crud.actions.SaveAction
import tools.dynamia.domain.query.Parameter
import tools.dynamia.domain.services.CrudService
import tools.dynamia.integration.Containers
import tools.dynamia.ui.UIMessages
import tools.dynamia.zk.actions.ToolbarbuttonActionRenderer

class SaveMenuItemAction extends AbstractAction {

    private CrudService crudService
    private CrudActionEvent sourceEvent

    SaveMenuItemAction(CrudService crudService, CrudActionEvent evt) {
        def messages = new BeanMessages(MenuItem, Locales.current)
        SaveAction saveAction = Containers.get().findObject(SaveAction)

        name = "${saveAction.name} ${messages.localizedName}"
        image = "save"
        setAttribute("background", "#ff5722")
        setAttribute("color", "white")
        this.crudService = crudService
        this.sourceEvent = evt
    }

    @Override
    void actionPerformed(ActionEvent evt) {
        List<Parameter> parameters = (List<Parameter>) evt.data

        MenuItemsUI ui = (MenuItemsUI) evt.source
        MenuItem menuItem = ui.menuItem

        if (needSave(menuItem)) {
            crudService.save(menuItem)
        }

        if (parameters != null) {
            for (Parameter parameter : parameters) {
                MenuItemParameter itemParameter = menuItem.getParameter(parameter.name)
                if (itemParameter == null) {
                    String value = parameter.value
                    if (value != null && !value.empty) {
                        itemParameter = new MenuItemParameter(parameter.name, parameter.value)
                        itemParameter.enabled = true
                        itemParameter.menuItem = menuItem
                        if (needSave(menuItem)) {
                            crudService.save(itemParameter)
                        }
                    }
                } else {
                    itemParameter.value = parameter.value
                    if (needSave(menuItem)) {
                        crudService.save(itemParameter)
                    }
                }
            }
        }

        UIMessages.showMessage("Menu item saved")

        if (sourceEvent != null) {
            sourceEvent.controller.doQuery()
        }

        ui.parent.detach()

    }

    private boolean needSave(MenuItem menuItem) {
        return true
    }

    @Override
    ActionRenderer getRenderer() {
        return new ToolbarbuttonActionRenderer(true)
    }

}

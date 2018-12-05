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

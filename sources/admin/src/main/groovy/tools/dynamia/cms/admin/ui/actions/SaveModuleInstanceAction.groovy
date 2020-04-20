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
package tools.dynamia.cms.admin.ui.actions

import org.zkoss.util.Locales
import tools.dynamia.actions.AbstractAction
import tools.dynamia.actions.ActionEvent
import tools.dynamia.actions.ActionRenderer
import tools.dynamia.cms.core.domain.ModuleInstance
import tools.dynamia.cms.core.domain.ModuleInstanceParameter
import tools.dynamia.commons.BeanMessages
import tools.dynamia.crud.CrudActionEvent
import tools.dynamia.crud.actions.SaveAction
import tools.dynamia.domain.query.Parameter
import tools.dynamia.domain.services.CrudService
import tools.dynamia.integration.Containers
import tools.dynamia.ui.UIMessages
import tools.dynamia.zk.actions.ToolbarbuttonActionRenderer
import tools.dynamia.zk.navigation.ZKNavigationManager

class SaveModuleInstanceAction extends AbstractAction {

    private CrudService crudService
    private CrudActionEvent sourceEvent

    SaveModuleInstanceAction(CrudService crudService, CrudActionEvent evt) {
        def messages = new BeanMessages(ModuleInstance, Locales.current)
        SaveAction saveAction = Containers.get().findObject(SaveAction)

        name = "${saveAction.name} ${messages.localizedName}"
        image = "check"
        setAttribute("background", "#ff5722")
        setAttribute("color", "white")
        this.crudService = crudService
        this.sourceEvent = evt
    }

    @Override
    void actionPerformed(ActionEvent evt) {

        ModuleInstance moduleInstance = (ModuleInstance) evt.getParam("moduleInstance")
        crudService.executeWithinTransaction {

            crudService.save(moduleInstance)

            if (evt.data instanceof List) {
                List<Parameter> parameters = (List<Parameter>) evt.data

                for (Parameter parameter : parameters) {

                    ModuleInstanceParameter instanceParameter = moduleInstance.getParameter(parameter.name)
                    if (instanceParameter == null) {
                        String value = parameter.value
                        if (value != null && !value.empty) {
                            instanceParameter = new ModuleInstanceParameter(parameter.name, parameter.value)
                            instanceParameter.enabled = true
                            instanceParameter.moduleInstance = moduleInstance
                            crudService.save(instanceParameter)
                        }
                    } else {
                        instanceParameter.value = parameter.value
                        crudService.save(instanceParameter)
                    }
                }
            }
        }

        UIMessages.showMessage("Module instance saved")

        sourceEvent.controller.doQuery()

        ZKNavigationManager.instance.closeCurrentPage()
        EditModuleInstanceAction edit = Containers.get().findObject(EditModuleInstanceAction.class)
        edit.actionPerformed(new CrudActionEvent(moduleInstance, sourceEvent.source, sourceEvent.crudView,
                sourceEvent.controller))

    }

    @Override
    ActionRenderer getRenderer() {
        ToolbarbuttonActionRenderer renderer = new ToolbarbuttonActionRenderer(true)
        return renderer
    }

}

/* 
 * Copyright 2016 Dynamia Soluciones IT SAS and the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tools.dynamia.cms.admin.ui.actions

import tools.dynamia.actions.AbstractAction
import tools.dynamia.actions.ActionEvent
import tools.dynamia.actions.ActionRenderer
import tools.dynamia.cms.core.domain.ModuleInstance
import tools.dynamia.cms.core.domain.ModuleInstanceParameter
import tools.dynamia.crud.CrudActionEvent
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
        name = "Save Module"
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

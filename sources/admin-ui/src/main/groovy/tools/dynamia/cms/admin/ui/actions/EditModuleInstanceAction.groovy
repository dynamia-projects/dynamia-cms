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
package tools.dynamia.cms.admin.ui.actions

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.actions.InstallAction
import tools.dynamia.cms.admin.ui.ModuleInstanceUI
import tools.dynamia.cms.core.domain.ModuleInstance
import tools.dynamia.commons.ApplicableClass
import tools.dynamia.crud.CrudActionEvent
import tools.dynamia.crud.CrudState
import tools.dynamia.crud.actions.EditAction
import tools.dynamia.domain.services.CrudService
import tools.dynamia.ui.MessageType
import tools.dynamia.ui.UIMessages
import tools.dynamia.zk.navigation.ComponentPage
import tools.dynamia.zk.navigation.ZKNavigationManager

@InstallAction
class EditModuleInstanceAction extends EditAction {

	@Autowired
	private CrudService crudService

    @Override
    CrudState[] getApplicableStates() {
		return CrudState.get(CrudState.READ)
    }

	@Override
    ApplicableClass[] getApplicableClasses() {
		return ApplicableClass.get(ModuleInstance.class)
    }

	@Override
    void actionPerformed(CrudActionEvent evt) {
		ModuleInstance moduleInstance = (ModuleInstance) evt.data
        if (moduleInstance != null) {
			moduleInstance = crudService.reload(moduleInstance)
            moduleInstance.parameters.size()
            ModuleInstanceUI ui = new ModuleInstanceUI(moduleInstance)
            ui.addAction(new SaveModuleInstanceAction(crudService, evt))
            ZKNavigationManager.instance.currentPage = new ComponentPage("editModule" + moduleInstance.id,
					"Edit Module Instance - " + moduleInstance.alias, ui)

        } else {
			UIMessages.showMessage("Select module instance", MessageType.ERROR)
        }

	}
}

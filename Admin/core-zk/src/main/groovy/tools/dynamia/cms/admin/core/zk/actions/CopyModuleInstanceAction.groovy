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
package tools.dynamia.cms.admin.core.zk.actions

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.actions.ActionGroup
import tools.dynamia.actions.InstallAction
import tools.dynamia.cms.admin.core.zk.ui.ModuleInstanceUI
import tools.dynamia.cms.site.core.domain.ModuleInstance
import tools.dynamia.cms.site.core.services.impl.ModulesService
import tools.dynamia.commons.ApplicableClass
import tools.dynamia.crud.AbstractCrudAction
import tools.dynamia.crud.CrudActionEvent
import tools.dynamia.crud.CrudState
import tools.dynamia.domain.services.CrudService
import tools.dynamia.ui.MessageType
import tools.dynamia.ui.UIMessages
import tools.dynamia.zk.util.ZKUtil

@InstallAction
class CopyModuleInstanceAction extends AbstractCrudAction {

	@Autowired
	private ModulesService modulesService

    @Autowired
	private CrudService crudService

    CopyModuleInstanceAction() {
		setGroup(ActionGroup.get("CRUD"))
        setName("Copy")
        setImage("copy")
    }

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
		ModuleInstance moduleInstance = (ModuleInstance) evt.getData()
        if (moduleInstance != null) {
			moduleInstance = crudService.reload(moduleInstance)
            moduleInstance = moduleInstance.clone()
            ModuleInstanceUI ui = new ModuleInstanceUI(moduleInstance)
            ui.addAction(new SaveModuleInstanceAction(crudService, evt))
            ZKUtil.showDialog(moduleInstance.getTitle(), ui, "90%", "90%")
        } else {
			UIMessages.showMessage("Select module instance", MessageType.ERROR)
        }

	}
}

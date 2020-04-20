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

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.actions.ActionGroup
import tools.dynamia.actions.InstallAction
import tools.dynamia.cms.admin.ui.ModuleInstanceUI
import tools.dynamia.cms.core.domain.ModuleInstance
import tools.dynamia.cms.core.services.impl.ModulesService
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
		return ApplicableClass.get(ModuleInstance.class)
    }

	@Override
    void actionPerformed(CrudActionEvent evt) {
		ModuleInstance moduleInstance = (ModuleInstance) evt.data
        if (moduleInstance != null) {
			moduleInstance = crudService.reload(moduleInstance)
            moduleInstance = moduleInstance.clone()
            ModuleInstanceUI ui = new ModuleInstanceUI(moduleInstance)
            ui.addAction(new SaveModuleInstanceAction(crudService, evt))
            ZKUtil.showDialog(moduleInstance.title, ui, "90%", "90%")
        } else {
			UIMessages.showMessage("Select module instance", MessageType.ERROR)
        }

	}
}

/*
 * Copyright (C) 2009 - 2019 Dynamia Soluciones IT S.A.S - NIT 900302344-1
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

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
import org.zkoss.util.Locales
import tools.dynamia.actions.InstallAction
import tools.dynamia.cms.admin.ui.ModuleInstanceUI
import tools.dynamia.cms.core.SiteContext
import tools.dynamia.cms.core.domain.ModuleInstance
import tools.dynamia.commons.ApplicableClass
import tools.dynamia.commons.BeanMessages
import tools.dynamia.crud.CrudActionEvent
import tools.dynamia.crud.CrudState
import tools.dynamia.crud.actions.NewAction
import tools.dynamia.domain.services.CrudService
import tools.dynamia.zk.navigation.ComponentPage
import tools.dynamia.zk.navigation.ZKNavigationManager

@InstallAction
class NewModuleInstanceAction extends NewAction {

    @Autowired
    private CrudService crudService

    NewModuleInstanceAction() {
        setAttribute("background", "#ff5722")
        setAttribute("color", "white")
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
        def moduleInstance = new ModuleInstance()
        moduleInstance.site = SiteContext.get().current
        def messages = new BeanMessages(ModuleInstance, Locales.current)

        def ui = new ModuleInstanceUI(moduleInstance)
        ui.addAction(new SaveModuleInstanceAction(crudService, evt))
        ZKNavigationManager.instance.currentPage = new ComponentPage("newModule" + System.currentTimeMillis(), "${super.getName()} ${messages.localizedName}", ui)

    }
}

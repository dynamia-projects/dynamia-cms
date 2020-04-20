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
import tools.dynamia.actions.ActionGroup
import tools.dynamia.actions.ActionRenderer
import tools.dynamia.actions.InstallAction
import tools.dynamia.cms.core.SiteContext
import tools.dynamia.cms.core.domain.ModuleInstance
import tools.dynamia.cms.core.services.impl.ModulesService
import tools.dynamia.commons.ApplicableClass
import tools.dynamia.crud.AbstractCrudAction
import tools.dynamia.crud.CrudActionEvent
import tools.dynamia.crud.CrudState
import tools.dynamia.zk.actions.ComboboxActionRenderer

@InstallAction
class ModulePositionFilterAction extends AbstractCrudAction {


    @Autowired
    private ModulesService service

    private String all = getLocalizedDescription(Locales.current)

    ModulePositionFilterAction() {
        name = "Module Position"
        group = ActionGroup.get("CRUD")
        position = 10
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
        String position = (String) evt.data
        if (position == null || position == all) {
            evt.controller.params.remove("position")
        } else {
            evt.controller.setParemeter("position", position)
        }
        evt.controller.doQuery()

    }

    @Override
    ActionRenderer getRenderer() {

        List<String> positions = new ArrayList<>(service.getAllUsedPositions(SiteContext.get().current))
        positions.sort()
        positions.add(0, all)
        ComboboxActionRenderer renderer = new ComboboxActionRenderer(positions, all)

        return renderer
    }

}

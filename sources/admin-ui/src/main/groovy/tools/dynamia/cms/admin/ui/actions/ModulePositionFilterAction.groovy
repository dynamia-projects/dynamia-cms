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
        positions.add(0, all)
        ComboboxActionRenderer renderer = new ComboboxActionRenderer(positions, all)

        return renderer
    }

}

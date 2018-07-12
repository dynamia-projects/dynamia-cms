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

package tools.dynamia.cms.admin.core.zk.actions

import tools.dynamia.actions.ActionGroup
import tools.dynamia.actions.ActionRenderer
import tools.dynamia.actions.InstallAction
import tools.dynamia.cms.site.core.domain.ModuleInstance
import tools.dynamia.commons.ApplicableClass
import tools.dynamia.crud.AbstractCrudAction
import tools.dynamia.crud.CrudActionEvent
import tools.dynamia.crud.CrudState
import tools.dynamia.zk.actions.ComboboxActionRenderer

@InstallAction
class ModuleStatusFilterAction extends AbstractCrudAction {

	private static final String ENABLED = "Enabled"

    private static final String DISABLED = "Disabled"

    ModuleStatusFilterAction() {
        name = "Status"
        group = ActionGroup.get("CRUD")
        position = 11
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
		String status = (String) evt.data
        evt.controller.setParemeter("enabled", ENABLED.equalsIgnoreCase(status))
        evt.controller.doQuery()

    }

	@Override
    ActionRenderer getRenderer() {

		ComboboxActionRenderer renderer = new ComboboxActionRenderer(Arrays.asList(ENABLED, DISABLED), ENABLED)

        return renderer
    }

}

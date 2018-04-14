package tools.dynamia.cms.admin.core.zk.actions

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.actions.ActionGroup
import tools.dynamia.actions.ActionRenderer
import tools.dynamia.actions.InstallAction
import tools.dynamia.cms.site.core.SiteContext
import tools.dynamia.cms.site.core.domain.ModuleInstance
import tools.dynamia.cms.site.core.services.impl.ModulesService
import tools.dynamia.commons.ApplicableClass
import tools.dynamia.crud.AbstractCrudAction
import tools.dynamia.crud.CrudActionEvent
import tools.dynamia.crud.CrudState
import tools.dynamia.zk.actions.ComboboxActionRenderer

@InstallAction
class ModulePositionFilterAction extends AbstractCrudAction {

	private static final String ALL = "-all positions-"

    @Autowired
	private ModulesService service

    ModulePositionFilterAction() {
		setName("Module Position")
        setGroup(ActionGroup.get("CRUD"))
        setPosition(10)
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
		String position = (String) evt.getData()
        if (position == null || position.equals(ALL)) {
			evt.getController().getParams().remove("position")
        } else {
			evt.getController().setParemeter("position", position)
        }
		evt.getController().doQuery()

    }

	@Override
    ActionRenderer getRenderer() {

		List<String> positions = new ArrayList<>(service.getAllUsedPositions(SiteContext.get().getCurrent()))
        positions.add(0, ALL)
        ComboboxActionRenderer renderer = new ComboboxActionRenderer(positions, ALL)

        return renderer
    }

}

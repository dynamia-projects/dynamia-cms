package tools.dynamia.cms.admin.core.zk.actions;

import java.util.Arrays;

import tools.dynamia.actions.ActionGroup;
import tools.dynamia.actions.ActionRenderer;
import tools.dynamia.actions.InstallAction;
import tools.dynamia.cms.site.core.domain.ModuleInstance;
import tools.dynamia.commons.ApplicableClass;
import tools.dynamia.crud.AbstractCrudAction;
import tools.dynamia.crud.CrudActionEvent;
import tools.dynamia.crud.CrudState;
import tools.dynamia.zk.actions.ComboboxActionRenderer;

@InstallAction
public class ModuleStatusFilterAction extends AbstractCrudAction {

	private static final String ENABLED = "Enabled";

	private static final String DISABLED = "Disabled";

	public ModuleStatusFilterAction() {
		setName("Status");
		setGroup(ActionGroup.get("CRUD"));
		setPosition(11);
	}

	@Override
	public CrudState[] getApplicableStates() {
		return CrudState.get(CrudState.READ);
	}

	@Override
	public ApplicableClass[] getApplicableClasses() {
		return ApplicableClass.get(ModuleInstance.class);
	}

	@Override
	public void actionPerformed(CrudActionEvent evt) {
		String status = (String) evt.getData();
		evt.getController().setParemeter("enabled", ENABLED.equalsIgnoreCase(status));
		evt.getController().doQuery();

	}

	@Override
	public ActionRenderer getRenderer() {

		ComboboxActionRenderer renderer = new ComboboxActionRenderer(Arrays.asList(ENABLED, DISABLED), ENABLED);

		return renderer;
	}

}

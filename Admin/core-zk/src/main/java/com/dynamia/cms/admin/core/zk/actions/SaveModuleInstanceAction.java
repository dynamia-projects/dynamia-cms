package com.dynamia.cms.admin.core.zk.actions;

import java.util.List;

import com.dynamia.cms.admin.core.zk.ui.ModuleInstanceUI;
import com.dynamia.cms.site.core.api.Module;
import com.dynamia.cms.site.core.domain.ModuleInstance;
import com.dynamia.cms.site.core.domain.ModuleInstanceParameter;
import com.dynamia.tools.domain.query.Parameter;
import com.dynamia.tools.domain.services.CrudService;
import com.dynamia.tools.viewers.zk.ui.Viewer;
import com.dynamia.tools.web.actions.AbstractAction;
import com.dynamia.tools.web.actions.ActionEvent;
import com.dynamia.tools.web.actions.ActionRenderer;
import com.dynamia.tools.web.crud.actions.renderers.ToolbarbuttonActionRenderer;
import com.dynamia.tools.web.ui.UIMessages;

public class SaveModuleInstanceAction extends AbstractAction {

	private CrudService crudService;

	public SaveModuleInstanceAction(CrudService crudService) {
		setName("Save");
		setImage("save");
		this.crudService = crudService;
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		List<Parameter> parameters = (List<Parameter>) evt.getData();

		ModuleInstance moduleInstance = (ModuleInstance) evt.getParam("moduleInstance");

		crudService.save(moduleInstance);
		for (Parameter parameter : parameters) {

			ModuleInstanceParameter instanceParameter = moduleInstance.getParameter(parameter.getName());
			if (instanceParameter == null) {
				String value = parameter.getValue();
				if (value != null && !value.isEmpty()) {
					instanceParameter = new ModuleInstanceParameter(parameter.getName(), parameter.getValue());
					instanceParameter.setEnabled(true);
					instanceParameter.setModuleInstance(moduleInstance);
					crudService.save(instanceParameter);
				}
			} else {
				instanceParameter.setValue(parameter.getValue());
				crudService.save(instanceParameter);
			}
		}

		UIMessages.showMessage("Module instance saved");

		ModuleInstanceUI ui = (ModuleInstanceUI) evt.getSource();
		ui.getParent().detach();
	}

	@Override
	public ActionRenderer getRenderer() {
		return new ToolbarbuttonActionRenderer(true);
	}

}

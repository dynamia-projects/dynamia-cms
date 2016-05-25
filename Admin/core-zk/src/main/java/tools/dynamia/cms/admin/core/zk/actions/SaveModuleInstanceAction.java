/* 
 * Copyright 2016 Dynamia Soluciones IT SAS and the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tools.dynamia.cms.admin.core.zk.actions;

import java.util.List;

import tools.dynamia.cms.admin.core.zk.ui.ModuleInstanceUI;
import tools.dynamia.cms.site.core.domain.ModuleInstance;
import tools.dynamia.cms.site.core.domain.ModuleInstanceParameter;

import tools.dynamia.actions.AbstractAction;
import tools.dynamia.actions.ActionEvent;
import tools.dynamia.actions.ActionRenderer;
import tools.dynamia.crud.CrudActionEvent;
import tools.dynamia.domain.query.Parameter;
import tools.dynamia.domain.services.CrudService;
import tools.dynamia.ui.UIMessages;
import tools.dynamia.zk.actions.ToolbarbuttonActionRenderer;

public class SaveModuleInstanceAction extends AbstractAction {

	private CrudService crudService;
	private CrudActionEvent sourceEvent;

	public SaveModuleInstanceAction(CrudService crudService, CrudActionEvent evt) {
		setName("Save");
		setImage("save");
		this.crudService = crudService;
		this.sourceEvent = evt;
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

		if (sourceEvent != null) {
			sourceEvent.getController().doQuery();
		}

		ModuleInstanceUI ui = (ModuleInstanceUI) evt.getSource();
		ui.sync(crudService.reload(moduleInstance));

	}

	@Override
	public ActionRenderer getRenderer() {
		return new ToolbarbuttonActionRenderer(true);
	}

}

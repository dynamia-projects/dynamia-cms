package com.dynamia.cms.admin.core.zk.actions;

import org.springframework.beans.factory.annotation.Autowired;

import com.dynamia.cms.admin.core.zk.ui.ModuleInstanceUI;
import com.dynamia.cms.site.core.domain.ModuleInstance;
import com.dynamia.cms.site.core.services.impl.ModulesService;

import tools.dynamia.actions.InstallAction;
import tools.dynamia.commons.ApplicableClass;
import tools.dynamia.crud.CrudActionEvent;
import tools.dynamia.crud.CrudState;
import tools.dynamia.crud.actions.EditAction;
import tools.dynamia.domain.services.CrudService;
import tools.dynamia.ui.MessageType;
import tools.dynamia.ui.UIMessages;
import tools.dynamia.zk.navigation.ComponentPage;
import tools.dynamia.zk.navigation.ZKNavigationManager;
import tools.dynamia.zk.util.ZKUtil;

@InstallAction
public class EditModuleInstanceAction extends EditAction {

	@Autowired
	private CrudService crudService;

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
		ModuleInstance moduleInstance = (ModuleInstance) evt.getData();
		if (moduleInstance != null) {
			moduleInstance = crudService.reload(moduleInstance);
			ModuleInstanceUI ui = new ModuleInstanceUI(moduleInstance);
			ui.addAction(new SaveModuleInstanceAction(crudService, evt));
			ZKNavigationManager.getInstance()
					.setCurrentPage(
							new ComponentPage("editModule" + moduleInstance.getId(), "Edit Module Instance - " + moduleInstance.getAlias(),
									ui));

		} else {
			UIMessages.showMessage("Select module instance", MessageType.ERROR);
		}

	}
}

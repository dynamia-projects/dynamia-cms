package com.dynamia.cms.admin.core.zk.actions;

import org.springframework.beans.factory.annotation.Autowired;

import com.dynamia.cms.admin.core.zk.ui.ModuleInstanceUI;
import com.dynamia.cms.site.core.domain.ModuleInstance;
import com.dynamia.cms.site.core.services.impl.ModulesService;
import com.dynamia.tools.commons.ApplicableClass;
import com.dynamia.tools.domain.services.CrudService;
import com.dynamia.tools.web.actions.InstallAction;
import com.dynamia.tools.web.crud.CrudActionEvent;
import com.dynamia.tools.web.crud.CrudState;
import com.dynamia.tools.web.crud.actions.EditAction;
import com.dynamia.tools.web.ui.MessageType;
import com.dynamia.tools.web.ui.UIMessages;
import com.dynamia.tools.web.util.ZKUtil;

@InstallAction
public class EditModuleInstanceAction extends EditAction {

	@Autowired
	private ModulesService modulesService;

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
			ui.addAction(new SaveModuleInstanceAction(crudService));
			ZKUtil.showDialog(moduleInstance.getTitle(), ui, "90%", "90%");
		} else {
			UIMessages.showMessage("Select module instance", MessageType.ERROR);
		}

	}
}

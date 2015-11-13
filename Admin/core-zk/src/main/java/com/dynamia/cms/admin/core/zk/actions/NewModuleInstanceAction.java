package com.dynamia.cms.admin.core.zk.actions;

import org.springframework.beans.factory.annotation.Autowired;

import com.dynamia.cms.admin.core.zk.ui.ModuleInstanceUI;
import com.dynamia.cms.site.core.SiteContext;
import com.dynamia.cms.site.core.domain.ModuleInstance;
import com.dynamia.cms.site.core.services.impl.ModulesService;

import tools.dynamia.actions.InstallAction;
import tools.dynamia.commons.ApplicableClass;
import tools.dynamia.crud.CrudActionEvent;
import tools.dynamia.crud.CrudState;
import tools.dynamia.crud.actions.NewAction;
import tools.dynamia.domain.services.CrudService;
import tools.dynamia.zk.util.ZKUtil;

@InstallAction
public class NewModuleInstanceAction extends NewAction {

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
		ModuleInstance moduleInstance = new ModuleInstance();
		moduleInstance.setSite(SiteContext.get().getCurrent());

		ModuleInstanceUI ui = new ModuleInstanceUI(moduleInstance);
		ui.addAction(new SaveModuleInstanceAction(crudService, evt));
		ZKUtil.showDialog("New Module Instance", ui, "90%", "90%");

	}
}

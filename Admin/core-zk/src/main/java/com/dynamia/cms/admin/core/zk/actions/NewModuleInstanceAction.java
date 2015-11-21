package com.dynamia.cms.admin.core.zk.actions;

import org.springframework.beans.factory.annotation.Autowired;

import com.dynamia.cms.admin.core.zk.ui.ModuleInstanceUI;
import com.dynamia.cms.site.core.SiteContext;
import com.dynamia.cms.site.core.domain.ModuleInstance;

import tools.dynamia.actions.InstallAction;
import tools.dynamia.commons.ApplicableClass;
import tools.dynamia.crud.CrudActionEvent;
import tools.dynamia.crud.CrudState;
import tools.dynamia.crud.actions.NewAction;
import tools.dynamia.domain.services.CrudService;
import tools.dynamia.zk.navigation.ComponentPage;
import tools.dynamia.zk.navigation.ZKNavigationManager;

@InstallAction
public class NewModuleInstanceAction extends NewAction {


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
		ZKNavigationManager.getInstance()
				.setCurrentPage(new ComponentPage("newModule" + System.currentTimeMillis(), "New Module Instance", ui));

	}
}

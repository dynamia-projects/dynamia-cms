package com.dynamia.cms.admin.core.zk.actions;

import org.springframework.beans.factory.annotation.Autowired;

import com.dynamia.cms.admin.core.zk.ui.ModuleInstanceUI;
import com.dynamia.cms.site.core.SiteContext;
import com.dynamia.cms.site.core.domain.ModuleInstance;
import com.dynamia.cms.site.core.services.impl.ModulesService;
import com.dynamia.tools.commons.ApplicableClass;
import com.dynamia.tools.domain.services.CrudService;
import com.dynamia.tools.web.actions.InstallAction;
import com.dynamia.tools.web.crud.CrudActionEvent;
import com.dynamia.tools.web.crud.CrudState;
import com.dynamia.tools.web.crud.actions.NewAction;
import com.dynamia.tools.web.util.ZKUtil;

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
		ui.addAction(new SaveModuleInstanceAction(crudService));
		ZKUtil.showDialog("New Module Instance", ui, "90%", "90%");

	}
}

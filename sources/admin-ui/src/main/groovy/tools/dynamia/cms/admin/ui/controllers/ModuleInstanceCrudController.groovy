package tools.dynamia.cms.admin.ui.controllers

import tools.dynamia.cms.core.domain.ModuleInstance
import tools.dynamia.zk.crud.CrudController

class ModuleInstanceCrudController extends CrudController<ModuleInstance> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9219694872612901136L

    @Override
	protected void beforeQuery() {
		if (getParameter("enabled") == null) {
			setParemeter("enabled", true)
        }
	}

}

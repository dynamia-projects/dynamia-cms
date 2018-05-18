package tools.dynamia.cms.admin.ui.controllers

import tools.dynamia.cms.core.domain.Site
import tools.dynamia.zk.crud.CrudController

class SiteCrudController extends CrudController<Site> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8713660187421756786L

    @Override
	protected void beforeQuery() {

		if (!params.containsKey("offline")) {
			setParemeter("offline", false)
        }

	}

}

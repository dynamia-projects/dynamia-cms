package tools.dynamia.cms.admin.core.zk.controllers

import tools.dynamia.cms.site.core.domain.Site
import tools.dynamia.zk.crud.CrudController

public class SiteCrudController extends CrudController<Site> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8713660187421756786L;

	@Override
	protected void beforeQuery() {

		if (!getParams().containsKey("offline")) {
			setParemeter("offline", false);
		}

	}

}

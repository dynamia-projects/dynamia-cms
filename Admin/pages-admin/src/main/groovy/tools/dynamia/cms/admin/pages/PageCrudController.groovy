package tools.dynamia.cms.admin.pages

import tools.dynamia.cms.site.pages.domain.Page
import tools.dynamia.zk.crud.CrudController

class PageCrudController extends CrudController<Page> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L

    @Override
	protected void beforeQuery() {
		if (!params.containsKey("published")) {
			setParemeter("published", true)
        }
	}

}

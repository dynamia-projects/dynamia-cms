package tools.dynamia.cms.pages.admin

import tools.dynamia.cms.pages.domain.Page
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

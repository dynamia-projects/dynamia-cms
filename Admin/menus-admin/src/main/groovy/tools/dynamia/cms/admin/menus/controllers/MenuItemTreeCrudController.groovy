package tools.dynamia.cms.admin.menus.controllers

import tools.dynamia.cms.site.menus.domain.Menu
import tools.dynamia.cms.site.menus.domain.MenuItem
import tools.dynamia.domain.query.QueryConditions
import tools.dynamia.domain.query.QueryParameters
import tools.dynamia.zk.crud.TreeCrudController

class MenuItemTreeCrudController extends TreeCrudController<MenuItem> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L
    private Menu menu

    void setMenu(Menu menu) {
		this.menu = menu
    }

    Menu getMenu() {
		return menu
    }

	@Override
	protected void beforeQuery() {
		if (menu != null) {
			setParemeter("menu", menu)
        }
	}

	@Override
	protected void afterCreate() {
		getEntity().setMenu(menu)
    }

	@Override
	protected boolean isLeaf(MenuItem data) {
		return data.getSubitems().isEmpty()
    }
	@Override
    String getRootLabel() {
		return menu.getName()
    }
	
	@Override
	protected Collection<MenuItem> loadRoots() {
		return crudService.find(MenuItem.class, QueryParameters.with("menu", menu)
				.add(getParentName(), QueryConditions.isNull())
				.orderBy("order"))
    }
}

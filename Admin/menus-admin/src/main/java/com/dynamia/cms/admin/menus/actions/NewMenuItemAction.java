package com.dynamia.cms.admin.menus.actions;

import org.springframework.beans.factory.annotation.Autowired;

import com.dynamia.cms.admin.menus.ui.MenuItemsUI;
import com.dynamia.cms.site.core.services.impl.ModulesService;
import com.dynamia.cms.site.menus.domain.Menu;
import com.dynamia.cms.site.menus.domain.MenuItem;
import com.dynamia.tools.commons.ApplicableClass;
import com.dynamia.tools.domain.services.CrudService;
import com.dynamia.tools.web.actions.InstallAction;
import com.dynamia.tools.web.crud.CrudActionEvent;
import com.dynamia.tools.web.crud.CrudState;
import com.dynamia.tools.web.crud.SubcrudController;
import com.dynamia.tools.web.crud.actions.NewAction;
import com.dynamia.tools.web.util.ZKUtil;

@InstallAction
public class NewMenuItemAction extends NewAction {

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
		return ApplicableClass.get(MenuItem.class);
	}

	@Override
	public void actionPerformed(CrudActionEvent evt) {

		SubcrudController<MenuItem> controller = (SubcrudController<MenuItem>) evt.getController();

		controller.newEntity();
		MenuItem menuItem = (MenuItem) evt.getController().getEntity();
		menuItem.setMenu((Menu) controller.getParentEntity());

		MenuItemsUI ui = new MenuItemsUI(menuItem);
		ui.addAction(new SaveMenuItemAction(crudService));
		ZKUtil.showDialog("New Menu Item", ui, "90%", "90%");

	}
}

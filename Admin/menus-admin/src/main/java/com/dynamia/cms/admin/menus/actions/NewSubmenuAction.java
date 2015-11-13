package com.dynamia.cms.admin.menus.actions;

import com.dynamia.cms.admin.menus.ui.MenuItemsUI;
import com.dynamia.cms.site.menus.domain.MenuItem;

import tools.dynamia.actions.AbstractAction;
import tools.dynamia.actions.ActionEvent;
import tools.dynamia.actions.FastAction;
import tools.dynamia.domain.services.CrudService;
import tools.dynamia.integration.Containers;
import tools.dynamia.ui.UIMessages;
import tools.dynamia.zk.util.ZKUtil;
import tools.dynamia.zk.viewers.ui.Viewer;

public class NewSubmenuAction extends AbstractAction {

	private CrudService crudService;
	protected MenuItem parent;

	public NewSubmenuAction(MenuItem parent) {
		super();
		this.parent = parent;
		this.crudService = Containers.get().findObject(CrudService.class);
		setName("New Submenu");
		setImage("add");
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		MenuItem submenu = new MenuItem();
		submenu.setParentItem(parent);

		showFormView("New Submenu", submenu, evt);

	}

	protected void showFormView(String title, MenuItem submenu, ActionEvent evt) {
		MenuItemsUI ui = (MenuItemsUI) evt.getSource();
		Viewer viewer = new Viewer("form", MenuItem.class, submenu);
		viewer.addAction(new FastAction("Guardar", e -> {
			save(ui, e);
			viewer.getParent().detach();
			UIMessages.showMessage("Submenu item saved");
		}));

		ZKUtil.showDialog(title, viewer);

	}

	protected void save(MenuItemsUI ui, ActionEvent e) {
		parent.getSubitems().add((MenuItem) e.getData());
		ui.reloadSubitems();
	}

}

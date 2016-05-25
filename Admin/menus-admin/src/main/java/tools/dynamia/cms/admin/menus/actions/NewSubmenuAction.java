package tools.dynamia.cms.admin.menus.actions;

import tools.dynamia.cms.admin.menus.ui.SubmenuItemsUI;
import tools.dynamia.cms.site.menus.domain.MenuItem;

import tools.dynamia.actions.AbstractAction;
import tools.dynamia.actions.ActionEvent;
import tools.dynamia.actions.FastAction;
import tools.dynamia.domain.services.CrudService;
import tools.dynamia.integration.Containers;
import tools.dynamia.ui.UIMessages;
import tools.dynamia.zk.util.ZKUtil;
import tools.dynamia.zk.viewers.ui.Viewer;

public class NewSubmenuAction extends AbstractAction {

	protected CrudService crudService;
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
		SubmenuItemsUI ui = (SubmenuItemsUI) evt.getSource();
		Viewer viewer = new Viewer("form", MenuItem.class, submenu);
		viewer.addAction(new FastAction("Guardar", e -> {
			save(ui, e);
			viewer.getParent().detach();
			UIMessages.showMessage("Submenu item saved");
			ui.reloadSubitems();
		}));

		ZKUtil.showDialog(title, viewer);

	}

	protected void save(SubmenuItemsUI ui, ActionEvent e) {
		MenuItem submenu = (MenuItem) e.getData();
		parent.getSubitems().add(submenu);
		ui.reloadSubitems();
		crudService.save(submenu);
	}

}

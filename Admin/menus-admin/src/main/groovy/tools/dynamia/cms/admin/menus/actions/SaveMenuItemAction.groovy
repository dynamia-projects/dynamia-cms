package tools.dynamia.cms.admin.menus.actions

import tools.dynamia.actions.AbstractAction
import tools.dynamia.actions.ActionEvent
import tools.dynamia.actions.ActionRenderer
import tools.dynamia.cms.admin.menus.ui.MenuItemsUI
import tools.dynamia.cms.site.menus.domain.MenuItem
import tools.dynamia.cms.site.menus.domain.MenuItemParameter
import tools.dynamia.crud.CrudActionEvent
import tools.dynamia.domain.query.Parameter
import tools.dynamia.domain.services.CrudService
import tools.dynamia.ui.UIMessages
import tools.dynamia.zk.actions.ToolbarbuttonActionRenderer

public class SaveMenuItemAction extends AbstractAction {

	private CrudService crudService;
	private CrudActionEvent sourceEvent;

	public SaveMenuItemAction(CrudService crudService, CrudActionEvent evt) {
		setName("Save Menu Item");
		setImage("save");
		setAttribute("background", "#ff5722");
		setAttribute("color", "white");
		this.crudService = crudService;
		this.sourceEvent = evt;
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		List<Parameter> parameters = (List<Parameter>) evt.getData();

		MenuItemsUI ui = (MenuItemsUI) evt.getSource();
		MenuItem menuItem = ui.getMenuItem();

		if (needSave(menuItem)) {
			crudService.save(menuItem);
		}

		if (parameters != null) {
			for (Parameter parameter : parameters) {
				MenuItemParameter itemParameter = menuItem.getParameter(parameter.getName());
				if (itemParameter == null) {
					String value = parameter.getValue();
					if (value != null && !value.isEmpty()) {
						itemParameter = new MenuItemParameter(parameter.getName(), parameter.getValue());
						itemParameter.setEnabled(true);
						itemParameter.setMenuItem(menuItem);
						if (needSave(menuItem)) {
							crudService.save(itemParameter);
						}
					}
				} else {
					itemParameter.setValue(parameter.getValue());
					if (needSave(menuItem)) {
						crudService.save(itemParameter);
					}
				}
			}
		}

		UIMessages.showMessage("Menu item saved");

		if (sourceEvent != null) {
			sourceEvent.getController().doQuery();
		}

		ui.getParent().detach();

	}

	private boolean needSave(MenuItem menuItem) {
		return true;
	}

	@Override
	public ActionRenderer getRenderer() {
		return new ToolbarbuttonActionRenderer(true);
	}

}

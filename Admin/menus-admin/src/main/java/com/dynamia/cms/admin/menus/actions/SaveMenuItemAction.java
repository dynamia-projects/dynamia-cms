package com.dynamia.cms.admin.menus.actions;

import java.util.List;

import com.dynamia.cms.admin.menus.ui.MenuItemsUI;
import com.dynamia.cms.site.menus.domain.MenuItem;
import com.dynamia.cms.site.menus.domain.MenuItemParameter;

import tools.dynamia.actions.AbstractAction;
import tools.dynamia.actions.ActionEvent;
import tools.dynamia.actions.ActionRenderer;
import tools.dynamia.crud.CrudActionEvent;
import tools.dynamia.domain.query.Parameter;
import tools.dynamia.domain.services.CrudService;
import tools.dynamia.ui.UIMessages;
import tools.dynamia.zk.actions.ToolbarbuttonActionRenderer;

public class SaveMenuItemAction extends AbstractAction {

	private CrudService crudService;
	private CrudActionEvent sourceEvent;

	public SaveMenuItemAction(CrudService crudService, CrudActionEvent evt) {
		setName("Save");
		setImage("save");
		this.crudService = crudService;
		this.sourceEvent = evt;
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		List<Parameter> parameters = (List<Parameter>) evt.getData();

		MenuItem menuItem = (MenuItem) evt.getParam("menuItem");

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
	}

	private boolean needSave(MenuItem menuItem) {
		return menuItem.getMenu().getId() != null;
	}

	@Override
	public ActionRenderer getRenderer() {
		return new ToolbarbuttonActionRenderer(true);
	}

}

package com.dynamia.cms.admin.menus.actions;

import java.util.List;

import com.dynamia.cms.admin.menus.ui.MenuItemsUI;
import com.dynamia.cms.site.menus.domain.MenuItem;
import com.dynamia.cms.site.menus.domain.MenuItemParameter;
import com.dynamia.tools.domain.query.Parameter;
import com.dynamia.tools.domain.services.CrudService;
import com.dynamia.tools.web.actions.AbstractAction;
import com.dynamia.tools.web.actions.ActionEvent;
import com.dynamia.tools.web.actions.ActionRenderer;
import com.dynamia.tools.web.crud.actions.renderers.ToolbarbuttonActionRenderer;
import com.dynamia.tools.web.ui.UIMessages;

public class SaveMenuItemAction extends AbstractAction {

	private CrudService crudService;


	public SaveMenuItemAction(CrudService crudService) {
		setName("Save");
		setImage("save");
		this.crudService = crudService;
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

		MenuItemsUI ui = (MenuItemsUI) evt.getSource();
		ui.getParent().detach();
	}

	private boolean needSave(MenuItem menuItem) {
		return menuItem.getMenu().getId() != null;
	}

	@Override
	public ActionRenderer getRenderer() {
		return new ToolbarbuttonActionRenderer(true);
	}

}

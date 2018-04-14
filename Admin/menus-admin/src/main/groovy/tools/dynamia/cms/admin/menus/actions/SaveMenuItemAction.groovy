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

class SaveMenuItemAction extends AbstractAction {

	private CrudService crudService
    private CrudActionEvent sourceEvent

    SaveMenuItemAction(CrudService crudService, CrudActionEvent evt) {
        name = "Save Menu Item"
        image = "save"
        setAttribute("background", "#ff5722")
        setAttribute("color", "white")
        this.crudService = crudService
        this.sourceEvent = evt
    }

	@Override
    void actionPerformed(ActionEvent evt) {
		List<Parameter> parameters = (List<Parameter>) evt.data

        MenuItemsUI ui = (MenuItemsUI) evt.source
        MenuItem menuItem = ui.menuItem

        if (needSave(menuItem)) {
			crudService.save(menuItem)
        }

		if (parameters != null) {
			for (Parameter parameter : parameters) {
				MenuItemParameter itemParameter = menuItem.getParameter(parameter.name)
                if (itemParameter == null) {
					String value = parameter.value
                    if (value != null && !value.empty) {
						itemParameter = new MenuItemParameter(parameter.name, parameter.value)
                        itemParameter.enabled = true
                        itemParameter.menuItem = menuItem
                        if (needSave(menuItem)) {
							crudService.save(itemParameter)
                        }
					}
				} else {
                    itemParameter.value = parameter.value
                    if (needSave(menuItem)) {
						crudService.save(itemParameter)
                    }
				}
			}
		}

		UIMessages.showMessage("Menu item saved")

        if (sourceEvent != null) {
			sourceEvent.controller.doQuery()
        }

        ui.parent.detach()

    }

	private boolean needSave(MenuItem menuItem) {
		return true
    }

	@Override
    ActionRenderer getRenderer() {
		return new ToolbarbuttonActionRenderer(true)
    }

}

package com.dynamia.cms.admin.menus.actions;

import com.dynamia.cms.site.menus.domain.MenuItem;

import tools.dynamia.actions.AbstractAction;
import tools.dynamia.actions.ActionEvent;
import tools.dynamia.ui.MessageType;
import tools.dynamia.ui.UIMessages;

public class DeleteSubmenuAction extends AbstractAction {

	private MenuItem parent;

	public DeleteSubmenuAction(MenuItem parent) {
		this.parent = parent;
		setName("Delete Submenu");
		setImage("delete");
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		MenuItem submenu = (MenuItem) evt.getData();
		if (submenu != null) {
			UIMessages.showQuestion("Are you sure?", () -> {
				parent.getSubitems().remove(submenu);
				submenu.setParentItem(null);
			});
		} else {
			UIMessages.showMessage("Select submenu", MessageType.WARNING);
		}
	}

}
package com.dynamia.cms.admin.core.zk.actions;

import com.dynamia.cms.site.core.DynamiaCMS;

import tools.dynamia.actions.ActionEvent;
import tools.dynamia.actions.InstallAction;
import tools.dynamia.ui.UIMessages;
import tools.dynamia.zk.crud.cfg.SaveConfigAction;
import tools.dynamia.zk.navigation.ZKNavigationManager;

@InstallAction
public class ApplySystemConfigAction extends SaveConfigAction {

	public ApplySystemConfigAction() {
		setName("Apply");
		setImage("refresh");
		setPosition(10);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		super.actionPerformed(evt);
		DynamiaCMS.reloadHomePath();
		ZKNavigationManager.getInstance().refresh();
		UIMessages.showMessage("Done");

	}

}

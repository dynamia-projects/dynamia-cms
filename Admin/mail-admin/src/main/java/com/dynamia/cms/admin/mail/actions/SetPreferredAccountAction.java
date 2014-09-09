/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.admin.mail.actions;

import org.springframework.beans.factory.annotation.Autowired;

import com.dynamia.cms.site.mail.domain.MailAccount;
import com.dynamia.cms.site.mail.services.MailService;
import com.dynamia.tools.commons.ApplicableClass;
import com.dynamia.tools.web.actions.InstallAction;
import com.dynamia.tools.web.crud.AbstractCrudAction;
import com.dynamia.tools.web.crud.CrudActionEvent;
import com.dynamia.tools.web.crud.CrudState;
import com.dynamia.tools.web.ui.MessageType;
import com.dynamia.tools.web.ui.UIMessages;

/**
 *
 * @author mario
 */
@InstallAction
public class SetPreferredAccountAction extends AbstractCrudAction {

	@Autowired
	private MailService service;

	public SetPreferredAccountAction() {
		setMenuSupported(true);
		setName("Set as preferred email account");
		setImage("star");

	}

	@Override
	public void actionPerformed(CrudActionEvent evt) {
		MailAccount account = (MailAccount) evt.getData();
		if (account != null) {
			service.setPreferredEmailAccount(account);
			evt.getController().doQuery();
			UIMessages.showMessage("Account " + account + " set as preferred successfully");
		} else {
			UIMessages.showMessage("Select account", MessageType.WARNING);
		}
	}

	@Override
	public CrudState[] getApplicableStates() {
		return CrudState.get(CrudState.READ);
	}

	@Override
	public ApplicableClass[] getApplicableClasses() {
		return ApplicableClass.get(MailAccount.class);
	}

}

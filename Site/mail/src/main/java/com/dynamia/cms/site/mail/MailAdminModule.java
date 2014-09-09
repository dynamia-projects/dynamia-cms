/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.mail;

import com.dynamia.cms.site.core.api.AdminModule;
import com.dynamia.cms.site.core.api.AdminModuleOption;
import com.dynamia.cms.site.core.api.CMSModule;
import com.dynamia.cms.site.mail.domain.MailAccount;
import com.dynamia.cms.site.mail.domain.MailTemplate;

/**
 *
 * @author mario
 */
@CMSModule
public class MailAdminModule implements AdminModule {

	@Override
	public String getGroup() {
		return "mail";
	}

	@Override
	public String getName() {
		return "Mails Settings";
	}

	@Override
	public AdminModuleOption[] getOptions() {
		return new AdminModuleOption[] { 
				new AdminModuleOption("mailAccounts", "Mail Accounts", MailAccount.class),
				new AdminModuleOption("mailTemplates", "Templates", MailTemplate.class)
				};
	}
}

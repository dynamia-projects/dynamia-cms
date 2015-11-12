package com.dynamia.cms.site.contacts;

import com.dynamia.cms.site.contacts.domain.Contact;
import com.dynamia.cms.site.contacts.domain.ContactCategory;
import com.dynamia.cms.site.core.api.AdminModule;
import com.dynamia.cms.site.core.api.AdminModuleOption;
import com.dynamia.cms.site.core.api.CMSModule;

@CMSModule
public class ContactAdminModule implements AdminModule {

	@Override
	public String getGroup() {
		return "Content";
	}

	@Override
	public String getName() {
		return "Site Content";
	}

	@Override
	public AdminModuleOption[] getOptions() {
		return new AdminModuleOption[] {
				new AdminModuleOption("contacts", "Contacts", Contact.class, true, true, "sitemap", true),
				new AdminModuleOption("contactsCategory", "Contacts Categories", ContactCategory.class)
		};
	}

}

/*
 * Copyright (C) 2009 - 2019 Dynamia Soluciones IT S.A.S - NIT 900302344-1
 * Colombia - South America
 * All Rights Reserved.
 *
 * DynamiaCMS is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License (LGPL v3) as
 * published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DynamiaCMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with DynamiaCMS.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
package tools.dynamia.cms.contacts

import tools.dynamia.cms.contacts.domain.Contact
import tools.dynamia.cms.contacts.domain.ContactCategory
import tools.dynamia.cms.core.api.AdminModule
import tools.dynamia.cms.core.api.AdminModuleOption
import tools.dynamia.cms.core.api.CMSModule

@CMSModule
class ContactAdminModule implements AdminModule {

	@Override
	String getGroup() {
		return "Contacts"
	}

	@Override
	String getName() {
		return "Contacts"
	}
	
	@Override
	String getImage() {
		return "fa-users"
	}

	@Override
	AdminModuleOption[] getOptions() {
		return [
				new AdminModuleOption("contacts", "List", Contact.class, true, true, "sitemap", true),
				new AdminModuleOption("contactsCategory", "Categories", ContactCategory.class)
		]
	}

}

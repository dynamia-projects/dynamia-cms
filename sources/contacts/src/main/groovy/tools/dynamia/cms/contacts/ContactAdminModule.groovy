/*
 *  Copyright (c) 2018 Dynamia Soluciones IT SAS and the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
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

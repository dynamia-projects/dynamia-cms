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
package tools.dynamia.cms.mail

import tools.dynamia.cms.core.api.AdminModule
import tools.dynamia.cms.core.api.AdminModuleOption
import tools.dynamia.cms.core.api.CMSModule
import tools.dynamia.cms.mail.domain.MailAccount
import tools.dynamia.cms.mail.domain.MailTemplate

/**
 *
 * @author Mario Serrano Leones
 */
@CMSModule
class MailAdminModule implements AdminModule {

    @Override
    String getGroup() {
        return "mail"
    }

    @Override
    String getName() {
        return "Email"
    }

    @Override
    String getImage() {
        return "fa-envelope"
    }

    @Override
    AdminModuleOption[] getOptions() {
        return [
                new AdminModuleOption("mailAccounts", "Accounts", MailAccount.class, false, true),
                new AdminModuleOption("mailTemplates", "Templates", MailTemplate.class)
        ]
    }
}

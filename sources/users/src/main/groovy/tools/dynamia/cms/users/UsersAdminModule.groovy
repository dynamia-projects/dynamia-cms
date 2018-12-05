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
package tools.dynamia.cms.users

import tools.dynamia.cms.core.api.AdminModule
import tools.dynamia.cms.core.api.AdminModuleOption
import tools.dynamia.cms.core.api.CMSModule
import tools.dynamia.cms.users.domain.User
import tools.dynamia.cms.users.domain.UserLog
import tools.dynamia.cms.users.domain.UserSiteConfig

/**
 *
 * @author Mario Serrano Leones
 */
@CMSModule
class UsersAdminModule implements AdminModule {

    @Override
    String getGroup() {
        return "users"
    }

    @Override
    String getName() {
        return "Users"
    }

    @Override
    String getImage() {
        return "fa-user"
    }

    @Override
    AdminModuleOption[] getOptions() {
        return [
                new AdminModuleOption("users", "List", User.class, false, true, "users", true),
                new AdminModuleOption("userConfig", "Configuration", UserSiteConfig.class, false, true),
                new AdminModuleOption("log", "Log", UserLog.class, false, true)
        ]
    }

}

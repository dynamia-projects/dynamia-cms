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

package tools.dynamia.cms.users.admin

import tools.dynamia.cms.core.api.AdminModuleOption
import tools.dynamia.cms.core.api.CMSExtension
import tools.dynamia.cms.users.UserHolder
import tools.dynamia.cms.users.api.UserProfile
import tools.dynamia.cms.users.domain.User
import tools.dynamia.navigation.NavigationElement
import tools.dynamia.navigation.NavigationRestriction

@CMSExtension
class UserNavigationRestriction implements NavigationRestriction {

    @Override
    int getOrder() {
        return 1
    }

    @Override
    Boolean allowAccess(NavigationElement element) {
        AdminModuleOption option = (AdminModuleOption) element.getAttribute("OPTION")
        if (option != null) {
            User user = UserHolder.get().current
            if (user != null) {
                return (option.editorAllowed && (user.profile == UserProfile.EDITOR || user.profile == UserProfile.ADMIN)) || (option.adminAllowed && user.profile == UserProfile.ADMIN)
            } else {
                return null
            }
        } else {
            return null
        }
    }

}

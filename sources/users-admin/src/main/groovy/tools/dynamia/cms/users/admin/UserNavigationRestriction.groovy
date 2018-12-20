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

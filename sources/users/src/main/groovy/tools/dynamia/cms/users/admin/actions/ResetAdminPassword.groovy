/*
 * Copyright (C) 2009 - 2020 Dynamia Soluciones IT S.A.S - NIT 900302344-1
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

package tools.dynamia.cms.users.admin.actions

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.actions.InstallAction
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.users.domain.User
import tools.dynamia.cms.users.services.UserService
import tools.dynamia.commons.ApplicableClass
import tools.dynamia.crud.AbstractCrudAction
import tools.dynamia.crud.CrudActionEvent
import tools.dynamia.crud.CrudState
import tools.dynamia.ui.MessageType
import tools.dynamia.ui.UIMessages
import tools.dynamia.zk.util.ZKUtil

@InstallAction
class ResetAdminPassword extends AbstractCrudAction {

    @Autowired
    private UserService service

    ResetAdminPassword() {
        name = "Reset Admin Password"
        image = "user"
        menuSupported = true
    }

    @Override
    CrudState[] getApplicableStates() {
        return CrudState.get(CrudState.READ)
    }

    @Override
    ApplicableClass[] getApplicableClasses() {
        return ApplicableClass.get(Site.class)
    }

    @Override
    void actionPerformed(CrudActionEvent evt) {
        Site site = (Site) evt.data
        if (site != null) {
            String admin = "admin@" + site.key + ".login"
            User user = service.getUser(site, admin)
            if (user == null) {
                service.checkAdminUser(site)
                user = service.getUser(site, admin)
            }

            if (user != null) {
                ZKUtil.showInputDialog("Enter new Password for $admin", String.class, { e ->
                    String password = e.data as String
                    if (password != null && !password.empty) {
                        service.resetPassword(user, password, password)
                        UIMessages.showMessage("Password reset succesfully")
                    }
                })
            }
        } else {
            UIMessages.showMessage("Select site first", MessageType.ERROR)
        }

    }

}

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

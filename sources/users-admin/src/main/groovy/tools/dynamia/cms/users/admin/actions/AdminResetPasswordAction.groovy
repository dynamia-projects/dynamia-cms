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
import tools.dynamia.cms.users.domain.User
import tools.dynamia.cms.users.services.UserService
import tools.dynamia.commons.ApplicableClass
import tools.dynamia.crud.AbstractCrudAction
import tools.dynamia.crud.CrudActionEvent
import tools.dynamia.crud.CrudState
import tools.dynamia.domain.ValidationError
import tools.dynamia.ui.MessageType
import tools.dynamia.ui.UIMessages
import tools.dynamia.zk.util.ZKUtil

@InstallAction
class AdminResetPasswordAction extends AbstractCrudAction {

    @Autowired
    private UserService userService

    AdminResetPasswordAction() {
        name = "Reset Password"
        image = "refresh"
    }

    @Override
    CrudState[] getApplicableStates() {
        return CrudState.get(CrudState.READ)
    }

    @Override
    ApplicableClass[] getApplicableClasses() {
        return ApplicableClass.get(User.class)
    }

    @Override
    void actionPerformed(CrudActionEvent evt) {
        User user = (User) evt.data
        if (user != null) {

            ZKUtil.showInputDialog("Ingrese nuevo password", String.class, { e ->
                String newpassword = e.data as String
                try {
                    userService.resetPassword(user, newpassword, newpassword)
                    UIMessages.showMessage("Password de usuario $user.username  reiniciado exitosamente")
                } catch (ValidationError ex) {
                    UIMessages.showMessage(ex.message, MessageType.WARNING)

                } catch (Exception ex) {
                    UIMessages.showMessage("Error al reiniciar password de usuario " + user.username + ": " + ex.message,
                            MessageType.ERROR)
                    ex.printStackTrace()
                }
            })
        }
    }

}

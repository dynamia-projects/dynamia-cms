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

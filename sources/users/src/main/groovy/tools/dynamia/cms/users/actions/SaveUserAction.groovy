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
package tools.dynamia.cms.users.actions

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.servlet.ModelAndView
import tools.dynamia.cms.core.actions.ActionEvent
import tools.dynamia.cms.core.actions.SiteAction
import tools.dynamia.cms.core.api.CMSAction
import tools.dynamia.cms.users.LoginForm
import tools.dynamia.cms.users.UserForm
import tools.dynamia.cms.users.UsersUtil
import tools.dynamia.cms.users.domain.User
import tools.dynamia.cms.users.services.UserService
import tools.dynamia.domain.ValidationError

/**
 *
 * @author Mario Serrano Leones
 */
@CMSAction
class SaveUserAction implements SiteAction {

    @Autowired
    private UserService userService

    @Override
    String getName() {
        return "saveUser"
    }

    @Override
    void actionPerformed(ActionEvent evt) {
        ModelAndView mv = evt.modelAndView

        mv.addObject("loginForm", new LoginForm(evt.site))
        if (evt.data == null) {
            UsersUtil.setupUserFormVar(mv, new UserForm(evt.site))
            mv.viewName = "redirect:/user/login"
            return
        }

        UserForm userForm = (UserForm) evt.data
        userForm.site = evt.site
        try {
            userService.saveUser(userForm)
            User user = userService.getUser(evt.site, userForm.data.username)
            mv.addObject("user", user)
        } catch (ValidationError e) {
            mv.viewName = "users/login"
            mv.addObject("errormessage", e.message)
            UsersUtil.setupUserFormVar(mv, userForm)
        } 

    }

}

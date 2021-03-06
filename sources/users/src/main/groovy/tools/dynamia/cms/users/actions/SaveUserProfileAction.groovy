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
package tools.dynamia.cms.users.actions

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.servlet.ModelAndView
import tools.dynamia.cms.core.actions.ActionEvent
import tools.dynamia.cms.core.actions.SiteAction
import tools.dynamia.cms.core.api.CMSAction
import tools.dynamia.cms.users.UserForm
import tools.dynamia.cms.users.UserHolder
import tools.dynamia.cms.users.UsersUtil
import tools.dynamia.cms.users.domain.User
import tools.dynamia.cms.users.services.UserService
import tools.dynamia.domain.ValidationError
import tools.dynamia.domain.services.CrudService

/**
 *
 * @author Mario Serrano Leones
 */
@CMSAction
class SaveUserProfileAction implements SiteAction {

	@Autowired
	private UserService userService

    @Autowired
	private CrudService crudService

    @Override
    String getName() {
		return "saveUserProfile"
    }

	@Override
    void actionPerformed(ActionEvent evt) {
		ModelAndView mv = evt.modelAndView

        UserForm userForm = (UserForm) evt.data
        userForm.site = evt.site
        try {
			userService.saveUser(userForm)
            User user = crudService.find(User.class, userForm.data.id)
            UserHolder.get().update(user)
            mv.addObject("successmessage", "Informacion Personal actualizada correctamente")
        } catch (ValidationError e) {
			mv.addObject("errormessage", e.message)
            mv.viewName = "users/profile"
            UsersUtil.setupUserFormVar(mv, userForm)
        }

	}

}

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
import tools.dynamia.cms.core.actions.SiteActionManager
import tools.dynamia.cms.core.api.CMSAction
import tools.dynamia.cms.users.UserForm
import tools.dynamia.cms.users.services.UserService
import tools.dynamia.domain.ValidationError
import tools.dynamia.domain.services.CrudService

/**
 *
 * @author Mario Serrano Leones
 */
@CMSAction
class ChangePasswordAction implements SiteAction {

    @Autowired
    private UserService service

    @Autowired
    private CrudService crudService

    @Override
    String getName() {
        return "changePassword"
    }

    @Override
    void actionPerformed(ActionEvent evt) {
        ModelAndView mv = evt.modelAndView
        mv.viewName = "users/account"
        UserForm form = (UserForm) evt.data
        form.site = evt.site

        try {
            service.changePassword(form)
            mv.addObject("successmessage", "Password modificado correctamente!")
        } catch (ValidationError e) {
            mv.viewName = "users/changepassword"
            mv.addObject("errormessage", e.message)
            SiteActionManager.performAction("updateUser", evt)
        }

    }

}

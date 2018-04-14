/* 
 * Copyright 2016 Dynamia Soluciones IT SAS and the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tools.dynamia.cms.site.users.actions

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.servlet.ModelAndView
import tools.dynamia.cms.site.core.actions.ActionEvent
import tools.dynamia.cms.site.core.actions.SiteAction
import tools.dynamia.cms.site.core.api.CMSAction
import tools.dynamia.cms.site.users.LoginForm
import tools.dynamia.cms.site.users.UserForm
import tools.dynamia.cms.site.users.UsersUtil
import tools.dynamia.cms.site.users.domain.User
import tools.dynamia.cms.site.users.services.UserService
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

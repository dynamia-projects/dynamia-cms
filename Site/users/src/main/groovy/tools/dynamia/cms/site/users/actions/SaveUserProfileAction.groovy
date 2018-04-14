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
import tools.dynamia.cms.site.users.UserForm
import tools.dynamia.cms.site.users.UserHolder
import tools.dynamia.cms.site.users.UsersUtil
import tools.dynamia.cms.site.users.domain.User
import tools.dynamia.cms.site.users.services.UserService
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
		ModelAndView mv = evt.getModelAndView()

        UserForm userForm = (UserForm) evt.getData()
        userForm.setSite(evt.getSite())
        try {
			userService.saveUser(userForm)
            User user = crudService.find(User.class, userForm.getData().getId())
            UserHolder.get().update(user)
            mv.addObject("successmessage", "Informacion Personal actualizada correctamente")
        } catch (ValidationError e) {
			mv.addObject("errormessage", e.getMessage())
            mv.setViewName("users/profile")
            UsersUtil.setupUserFormVar(mv, userForm)
        }

	}

}

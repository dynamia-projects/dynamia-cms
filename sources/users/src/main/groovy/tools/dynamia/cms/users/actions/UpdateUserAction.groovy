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
import tools.dynamia.cms.users.domain.UserContactInfo
import tools.dynamia.cms.users.services.UserService
import tools.dynamia.domain.services.CrudService

/**
 *
 * @author Mario Serrano Leones
 */
@CMSAction
class UpdateUserAction implements SiteAction {

    @Autowired
    private UserService service

    @Autowired
    private CrudService crudService

    @Override
    String getName() {
        return "updateUser"
    }

    @Override
    void actionPerformed(ActionEvent evt) {
        ModelAndView mv = evt.modelAndView

        User user = crudService.reload(UserHolder.get().current)

        UserForm form = new UserForm()
        form.data = user
        form.site = evt.site
        UsersUtil.setupUserFormVar(mv, form)

        List<UserContactInfo> userContactInfos = service.getContactInfos(UserHolder.get().current)
        mv.addObject("userContactInfos", userContactInfos)

    }

}
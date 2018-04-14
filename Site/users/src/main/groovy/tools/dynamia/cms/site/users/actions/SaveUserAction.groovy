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
package tools.dynamia.cms.site.users.actions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import tools.dynamia.cms.site.core.actions.ActionEvent;
import tools.dynamia.cms.site.core.actions.SiteAction;
import tools.dynamia.cms.site.core.api.CMSAction;
import tools.dynamia.cms.site.users.LoginForm;
import tools.dynamia.cms.site.users.UserForm;
import tools.dynamia.cms.site.users.UsersUtil;
import tools.dynamia.cms.site.users.domain.User;
import tools.dynamia.cms.site.users.services.UserService;

import tools.dynamia.domain.ValidationError;

/**
 *
 * @author Mario Serrano Leones
 */
@CMSAction
public class SaveUserAction implements SiteAction {

    @Autowired
    private UserService userService;

    @Override
    public String getName() {
        return "saveUser";
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        ModelAndView mv = evt.getModelAndView();

        mv.addObject("loginForm", new LoginForm(evt.getSite()));
        if (evt.getData() == null) {
            UsersUtil.setupUserFormVar(mv, new UserForm(evt.getSite()));
            mv.setViewName("redirect:/user/login");
            return;
        }

        UserForm userForm = (UserForm) evt.getData();
        userForm.setSite(evt.getSite());
        try {
            userService.saveUser(userForm);
            User user = userService.getUser(evt.getSite(), userForm.getData().getUsername());
            mv.addObject("user", user);
        } catch (ValidationError e) {
            mv.setViewName("users/login");
            mv.addObject("errormessage", e.getMessage());
            UsersUtil.setupUserFormVar(mv, userForm);
        } 

    }

}

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
package com.dynamia.cms.site.users.actions;

import org.springframework.security.web.WebAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.dynamia.cms.site.core.actions.ActionEvent;
import com.dynamia.cms.site.core.actions.SiteAction;
import com.dynamia.cms.site.core.api.CMSAction;
import com.dynamia.cms.site.users.LoginForm;
import com.dynamia.cms.site.users.UserForm;
import com.dynamia.cms.site.users.UsersUtil;

/**
 *
 * @author Mario Serrano Leones
 */
@CMSAction
public class LoginUserAction implements SiteAction {

    @Override
    public String getName() {
        return "loginUser";
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        ModelAndView mv = evt.getModelAndView();

        mv.addObject("loginForm", new LoginForm(evt.getSite()));
        UsersUtil.setupUserFormVar(mv, new UserForm(evt.getSite()));

        try {

            Exception ex = (Exception) evt.getRequest().getSession(false).getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

            if (ex != null) {
                String message = ex.getLocalizedMessage();
                mv.addObject("errormessage", message);
            }
        } catch (Exception e) {
        }

    }

}

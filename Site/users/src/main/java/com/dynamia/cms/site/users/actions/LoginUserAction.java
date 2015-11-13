/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 * @author mario_2
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

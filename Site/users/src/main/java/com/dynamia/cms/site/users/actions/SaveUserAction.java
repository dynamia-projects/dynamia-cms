/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.users.actions;

import com.dynamia.cms.site.core.actions.ActionEvent;
import com.dynamia.cms.site.core.actions.SiteAction;
import com.dynamia.cms.site.core.api.CMSAction;
import com.dynamia.cms.site.users.LoginForm;
import com.dynamia.cms.site.users.UserForm;
import com.dynamia.cms.site.users.UsersUtil;
import com.dynamia.cms.site.users.domain.User;
import com.dynamia.cms.site.users.services.UserService;
import com.dynamia.tools.domain.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author mario_2
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

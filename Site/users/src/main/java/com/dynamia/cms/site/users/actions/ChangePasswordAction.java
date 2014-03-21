/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.users.actions;

import com.dynamia.cms.site.core.actions.ActionEvent;
import com.dynamia.cms.site.core.actions.SiteAction;
import com.dynamia.cms.site.core.actions.SiteActionManager;
import com.dynamia.cms.site.core.api.CMSAction;
import com.dynamia.cms.site.users.UserForm;
import com.dynamia.cms.site.users.UserHolder;
import com.dynamia.cms.site.users.UsersUtil;
import com.dynamia.cms.site.users.domain.User;
import com.dynamia.cms.site.users.services.UserService;
import com.dynamia.tools.domain.ValidationError;
import com.dynamia.tools.domain.services.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author mario_2
 */
@CMSAction
public class ChangePasswordAction implements SiteAction {

    @Autowired
    private UserService service;

    @Autowired
    private CrudService crudService;

    @Override
    public String getName() {
        return "changePassword";
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        ModelAndView mv = evt.getModelAndView();
        mv.setViewName("users/account");
        UserForm form = (UserForm) evt.getData();
        form.setSite(evt.getSite());

        try {
            service.changePassword(form);
            mv.addObject("successmessage", "Password modificado correctamente!");
        } catch (ValidationError e) {
            mv.setViewName("users/changepassword");
            mv.addObject("errormessage", e.getMessage());
            SiteActionManager.performAction("updateUser", evt);
        }

    }

}

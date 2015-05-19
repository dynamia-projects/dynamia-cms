/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.users.actions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import com.dynamia.cms.site.core.actions.ActionEvent;
import com.dynamia.cms.site.core.actions.SiteAction;
import com.dynamia.cms.site.core.api.CMSAction;
import com.dynamia.cms.site.users.UserForm;
import com.dynamia.cms.site.users.UserHolder;
import com.dynamia.cms.site.users.UsersUtil;
import com.dynamia.cms.site.users.domain.User;
import com.dynamia.cms.site.users.services.UserService;
import com.dynamia.tools.domain.ValidationError;
import com.dynamia.tools.domain.services.CrudService;

/**
 *
 * @author mario_2
 */
@CMSAction
public class SaveUserProfileAction implements SiteAction {

	@Autowired
	private UserService userService;

	@Autowired
	private CrudService crudService;

	@Override
	public String getName() {
		return "saveUserProfile";
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		ModelAndView mv = evt.getModelAndView();

		UserForm userForm = (UserForm) evt.getData();
		userForm.setSite(evt.getSite());
		try {
			userService.saveUser(userForm);
			User user = crudService.find(User.class, userForm.getData().getId());
			UserHolder.get().update(user);
			mv.addObject("successmessage", "Informacion Personal actualizada correctamente");
		} catch (ValidationError e) {
			mv.addObject("errormessage", e.getMessage());
			mv.setViewName("users/profile");
			UsersUtil.setupUserFormVar(mv, userForm);
		}

	}

}

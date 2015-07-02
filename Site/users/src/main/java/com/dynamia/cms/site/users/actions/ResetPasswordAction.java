/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.users.actions;

import com.dynamia.cms.site.core.CMSUtil;
import com.dynamia.cms.site.core.actions.ActionEvent;
import com.dynamia.cms.site.core.actions.SiteAction;
import com.dynamia.cms.site.core.actions.SiteActionManager;
import com.dynamia.cms.site.core.api.CMSAction;
import com.dynamia.cms.site.core.domain.Site;
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
public class ResetPasswordAction implements SiteAction {

	@Autowired
	private UserService service;

	@Autowired
	private CrudService crudService;

	@Override
	public String getName() {
		return "resetPassword";
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		ModelAndView mv = evt.getModelAndView();
		mv.setViewName("users/resetpassword");
		UserForm form = (UserForm) evt.getData();

		String username = form.getData().getUsername();
		Site site = evt.getSite();

		try {
			service.resetPassword(site, username);
			form.setData(new User());
			mv.addObject("successmessage", "Se ha enviado un correo a [" + username + "] con un nuevo password generado");
		} catch (ValidationError e) {
			mv.addObject("errormessage", e.getMessage());
		}

	}

}

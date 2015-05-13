/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.users.actions;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import com.dynamia.cms.site.core.actions.ActionEvent;
import com.dynamia.cms.site.core.actions.SiteAction;
import com.dynamia.cms.site.core.api.CMSAction;
import com.dynamia.cms.site.users.UserHolder;
import com.dynamia.cms.site.users.domain.UserContactInfo;
import com.dynamia.cms.site.users.services.UserService;

/**
 *
 * @author mario_2
 */
@CMSAction
public class ShowUserContactInfosAction implements SiteAction {

	@Autowired
	private UserService userService;

	@Override
	public String getName() {
		return "showUserContactInfos";
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		ModelAndView mv = evt.getModelAndView();

		List<UserContactInfo> userContactInfos = userService.getContactInfos(UserHolder.get().getCurrent());
		mv.addObject("userContactInfos", userContactInfos);
		
		mv.addObject("title", "Direcciones de Contacto");
		if (userContactInfos == null || userContactInfos.isEmpty()) {
			mv.addObject("subtitle", "No tiene direcciones registradas");
		} else {
			mv.addObject("subtitle", userContactInfos.size() + " direcciones de contactos registradas");

		}
		

	}

}

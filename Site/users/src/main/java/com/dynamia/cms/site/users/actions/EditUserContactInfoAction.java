/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.users.actions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import com.dynamia.cms.site.core.CMSUtil;
import com.dynamia.cms.site.core.actions.ActionEvent;
import com.dynamia.cms.site.core.actions.SiteAction;
import com.dynamia.cms.site.core.api.CMSAction;
import com.dynamia.cms.site.users.UserHolder;
import com.dynamia.cms.site.users.domain.UserContactInfo;

import tools.dynamia.domain.services.CrudService;

/**
 *
 * @author mario_2
 */
@CMSAction
public class EditUserContactInfoAction implements SiteAction {

	@Autowired
	private CrudService crudService;

	@Override
	public String getName() {
		return "editUserContactInfo";
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		ModelAndView mv = evt.getModelAndView();
		Long id = (Long) evt.getData();
		UserContactInfo userContactInfo = crudService.find(UserContactInfo.class, id);
		if (userContactInfo.getUser().equals(UserHolder.get().getCurrent())) {
			mv.addObject("title", "Editar Direccion de Contacto");
			mv.addObject("userContactInfo", userContactInfo);

			CMSUtil.buildContactInfoOptions(evt.getSite(), mv, "uci", userContactInfo.getInfo());
		} else {
			CMSUtil.addErrorMessage("Direccion de contacto NO pertenece a este usuario -.-", evt.getRedirectAttributes());

		}
	}

}

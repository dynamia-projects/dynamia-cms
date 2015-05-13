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
import com.dynamia.cms.site.core.services.SiteService;
import com.dynamia.cms.site.users.domain.UserContactInfo;
import com.dynamia.tools.domain.services.CrudService;

/**
 *
 * @author mario_2
 */
@CMSAction
public class AddUserContactInfoAction implements SiteAction {

	@Autowired
	private CrudService crudService;

	@Autowired
	private SiteService siteService;

	@Override
	public String getName() {
		return "addUserContactInfo";
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		ModelAndView mv = evt.getModelAndView();

		UserContactInfo userContactInfo = new UserContactInfo();
		mv.addObject("title", "Nueva direccion de contacto");
		mv.addObject("userContactInfo", userContactInfo);

		CMSUtil.buildContactInfoOptions(evt.getSite(), mv, "uci", userContactInfo.getInfo());
	}

}

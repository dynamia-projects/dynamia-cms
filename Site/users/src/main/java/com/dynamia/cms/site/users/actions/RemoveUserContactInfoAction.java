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
import com.dynamia.tools.domain.services.CrudService;

/**
 *
 * @author mario_2
 */
@CMSAction
public class RemoveUserContactInfoAction implements SiteAction {

	@Autowired
	private CrudService crudService;

	@Override
	public String getName() {
		return "removeUserContactInfo";
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		ModelAndView mv = evt.getModelAndView();
		Long id = (Long) evt.getData();
		UserContactInfo userContactInfo = crudService.find(UserContactInfo.class, id);

		try {
			if (userContactInfo.getUser().equals(UserHolder.get().getCurrent())) {
				crudService.delete(userContactInfo);
				CMSUtil.addSuccessMessage("Direccion de contacto elimidada exitosamente", evt.getRedirectAttributes());
			} else {
				CMSUtil.addErrorMessage("Direccion de contacto NO pertenece a este usuario -.-", evt.getRedirectAttributes());
			}
		} catch (Exception e) {
			CMSUtil.addWarningMessage("Direccion de contacto utilizada, no puede ser borrada", evt.getRedirectAttributes());

		}

	}

}

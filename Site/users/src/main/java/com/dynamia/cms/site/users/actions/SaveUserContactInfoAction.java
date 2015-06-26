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
import com.dynamia.tools.domain.ValidationError;
import com.dynamia.tools.domain.contraints.NotEmptyValidator;
import com.dynamia.tools.domain.services.CrudService;

/**
 *
 * @author mario_2
 */
@CMSAction
public class SaveUserContactInfoAction implements SiteAction {

	@Autowired
	private CrudService crudService;

	@Override
	public String getName() {
		return "saveUserContactInfo";
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		ModelAndView mv = evt.getModelAndView();

		UserContactInfo userContactInfo = (UserContactInfo) evt.getData();

		if (userContactInfo.getId() != null) {
			UserContactInfo contactInfo = crudService.find(UserContactInfo.class, userContactInfo.getId());
			contactInfo.setName(userContactInfo.getName());
			contactInfo.setDescription(userContactInfo.getDescription());
			contactInfo.setInfo(userContactInfo.getInfo());
			userContactInfo = contactInfo;
		}

		try {

			validate(userContactInfo);
			userContactInfo.setUser(UserHolder.get().getCurrent());
			crudService.save(userContactInfo);

			CMSUtil.addSuccessMessage("Direccion de usuario guardada exitosamente", evt.getRedirectAttributes());
		} catch (ValidationError e) {
			CMSUtil.addErrorMessage(e.getMessage(), evt.getRedirectAttributes());
			mv.setView(null);
			mv.setViewName("users/addresses/form");
		}
	}

	private void validate(UserContactInfo userContactInfo) {
		NotEmptyValidator validator = new NotEmptyValidator();
		if (!validator.isValid(userContactInfo.getName(), null)) {
			throw new ValidationError("Ingrese nombre de direccion de contacto");
		}

		if (!validator.isValid(userContactInfo.getInfo().getAddress(), null)) {
			throw new ValidationError("Ingrese direccion de contacto");
		}

		if (!validator.isValid(userContactInfo.getInfo().getCountry(), null)) {
			throw new ValidationError("Seleccione pais");
		}

		if (!validator.isValid(userContactInfo.getInfo().getCity(), null)) {
			throw new ValidationError("Seleccione ciudad");
		}

		if (!validator.isValid(userContactInfo.getInfo().getRegion(), null)) {
			throw new ValidationError("Seleccione departamento");
		}

		if (!validator.isValid(userContactInfo.getInfo().getPhoneNumber(), null)) {
			throw new ValidationError("Ingrese telefono de contacto");
		}

	}

}
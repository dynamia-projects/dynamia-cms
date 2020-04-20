/*
 * Copyright (C) 2009 - 2020 Dynamia Soluciones IT S.A.S - NIT 900302344-1
 * Colombia - South America
 * All Rights Reserved.
 *
 * DynamiaCMS is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License (LGPL v3) as
 * published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DynamiaCMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with DynamiaCMS.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
package tools.dynamia.cms.users.actions

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.view.RedirectView
import tools.dynamia.cms.core.CMSUtil
import tools.dynamia.cms.core.actions.ActionEvent
import tools.dynamia.cms.core.actions.SiteAction
import tools.dynamia.cms.core.actions.SiteActionManager
import tools.dynamia.cms.core.api.CMSAction
import tools.dynamia.cms.core.domain.City
import tools.dynamia.cms.users.UserHolder
import tools.dynamia.cms.users.domain.UserContactInfo
import tools.dynamia.domain.ValidationError
import tools.dynamia.domain.contraints.NotEmptyValidator
import tools.dynamia.domain.services.CrudService

/**
 * @author Mario Serrano Leones
 */
@CMSAction
class SaveUserContactInfoAction implements SiteAction {

    @Autowired
    private CrudService crudService

    @Override
    String getName() {
        return "saveUserContactInfo"
    }

    @Override
    void actionPerformed(ActionEvent evt) {
        ModelAndView mv = evt.modelAndView
        mv.viewName = "/users/addresses/form"
        UserContactInfo userContactInfo = (UserContactInfo) evt.data

        if (userContactInfo.id != null) {
            UserContactInfo contactInfo = crudService.find(UserContactInfo.class, userContactInfo.id)
            contactInfo.name = userContactInfo.name
            contactInfo.description = userContactInfo.description
            contactInfo.info = userContactInfo.info
            userContactInfo = contactInfo
        }

        try {

            String cityId = evt.request.getParameter("city.id")
            if (cityId != null) {
                userContactInfo.city = crudService.find(City.class, new Long(cityId))
            }

            validate(userContactInfo)
            userContactInfo.user = UserHolder.get().current
            crudService.save(userContactInfo)

            CMSUtil.addSuccessMessage("Direccion de usuario guardada exitosamente", mv)

            String redirect = (String) mv.model.get("redirect")
            if (redirect == null || redirect.empty) {
                redirect = "/users/addresses"
            }

            mv.view = new RedirectView(redirect, true, true, false)
        } catch (ValidationError e) {
            SiteActionManager.performAction("addUserContactInfo", mv, evt.request)
            mv.addObject("userContactInfo", userContactInfo)
            CMSUtil.addErrorMessage(e.message, mv)

        }
    }

    private void validate(UserContactInfo userContactInfo) {
        NotEmptyValidator validator = new NotEmptyValidator()
        if (!validator.isValid(userContactInfo.name, null)) {
            throw new ValidationError("Ingrese nombre de direccion de contacto")
        }


        if (!validator.isValid(userContactInfo.info.address, null)) {
            throw new ValidationError("Ingrese direccion de contacto")
        }


        if (userContactInfo.city != null && userContactInfo.city.id != null) {
            City city = userContactInfo.city
            userContactInfo.city = city
            userContactInfo.info.city = city.name
            userContactInfo.info.region = city.region.name
            userContactInfo.info.country = city.region.country.name

        }


        if (userContactInfo.city == null) {
            if (!validator.isValid(userContactInfo.info.country, null)) {
                throw new ValidationError("Seleccione pais")
            }

            if (!validator.isValid(userContactInfo.info.city, null)) {
                throw new ValidationError("Seleccione ciudad")
            }

            if (!validator.isValid(userContactInfo.info.region, null)) {
                throw new ValidationError("Seleccione departamento")
            }

            if (!validator.isValid(userContactInfo.info.phoneNumber, null)) {
                throw new ValidationError("Ingrese telefono de contacto")
            }
        }


    }

}

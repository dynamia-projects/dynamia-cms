/* 
 * Copyright 2016 Dynamia Soluciones IT SAS and the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

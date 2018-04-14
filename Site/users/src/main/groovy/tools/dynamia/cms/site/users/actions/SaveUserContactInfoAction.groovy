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
package tools.dynamia.cms.site.users.actions

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.view.RedirectView
import tools.dynamia.cms.site.core.CMSUtil
import tools.dynamia.cms.site.core.actions.ActionEvent
import tools.dynamia.cms.site.core.actions.SiteAction
import tools.dynamia.cms.site.core.actions.SiteActionManager
import tools.dynamia.cms.site.core.api.CMSAction
import tools.dynamia.cms.site.core.domain.City
import tools.dynamia.cms.site.users.UserHolder
import tools.dynamia.cms.site.users.domain.UserContactInfo
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
        ModelAndView mv = evt.getModelAndView()
        mv.setViewName("/users/addresses/form")
        UserContactInfo userContactInfo = (UserContactInfo) evt.getData()

        if (userContactInfo.getId() != null) {
            UserContactInfo contactInfo = crudService.find(UserContactInfo.class, userContactInfo.getId())
            contactInfo.setName(userContactInfo.getName())
            contactInfo.setDescription(userContactInfo.getDescription())
            contactInfo.setInfo(userContactInfo.getInfo())
            userContactInfo = contactInfo
        }

        try {

            String cityId = evt.getRequest().getParameter("city.id")
            if (cityId != null) {
                userContactInfo.setCity(crudService.find(City.class, new Long(cityId)))
            }

            validate(userContactInfo)
            userContactInfo.setUser(UserHolder.get().getCurrent())
            crudService.save(userContactInfo)

            CMSUtil.addSuccessMessage("Direccion de usuario guardada exitosamente", mv)

            String redirect = (String) mv.getModel().get("redirect")
            if (redirect == null || redirect.isEmpty()) {
                redirect = "/users/addresses"
            }

            mv.setView(new RedirectView(redirect, true, true, false))
        } catch (ValidationError e) {
            SiteActionManager.performAction("addUserContactInfo", mv, evt.getRequest())
            mv.addObject("userContactInfo", userContactInfo)
            CMSUtil.addErrorMessage(e.getMessage(), mv)

        }
    }

    private void validate(UserContactInfo userContactInfo) {
        NotEmptyValidator validator = new NotEmptyValidator()
        if (!validator.isValid(userContactInfo.getName(), null)) {
            throw new ValidationError("Ingrese nombre de direccion de contacto")
        }


        if (!validator.isValid(userContactInfo.getInfo().getAddress(), null)) {
            throw new ValidationError("Ingrese direccion de contacto")
        }


        if (userContactInfo.getCity() != null && userContactInfo.getCity().getId() != null) {
            City city = userContactInfo.getCity()
            userContactInfo.setCity(city)
            userContactInfo.getInfo().setCity(city.getName())
            userContactInfo.getInfo().setRegion(city.getRegion().getName())
            userContactInfo.getInfo().setCountry(city.getRegion().getCountry().getName())

        }


        if (userContactInfo.getCity() == null) {
            if (!validator.isValid(userContactInfo.getInfo().getCountry(), null)) {
                throw new ValidationError("Seleccione pais")
            }

            if (!validator.isValid(userContactInfo.getInfo().getCity(), null)) {
                throw new ValidationError("Seleccione ciudad")
            }

            if (!validator.isValid(userContactInfo.getInfo().getRegion(), null)) {
                throw new ValidationError("Seleccione departamento")
            }

            if (!validator.isValid(userContactInfo.getInfo().getPhoneNumber(), null)) {
                throw new ValidationError("Ingrese telefono de contacto")
            }
        }


    }

}
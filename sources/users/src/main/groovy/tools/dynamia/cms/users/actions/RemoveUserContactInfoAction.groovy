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
import tools.dynamia.cms.core.CMSUtil
import tools.dynamia.cms.core.actions.ActionEvent
import tools.dynamia.cms.core.actions.SiteAction
import tools.dynamia.cms.core.api.CMSAction
import tools.dynamia.cms.users.UserHolder
import tools.dynamia.cms.users.domain.UserContactInfo
import tools.dynamia.domain.services.CrudService

/**
 *
 * @author Mario Serrano Leones
 */
@CMSAction
class RemoveUserContactInfoAction implements SiteAction {

	@Autowired
	private CrudService crudService

    @Override
    String getName() {
		return "removeUserContactInfo"
    }

	@Override
    void actionPerformed(ActionEvent evt) {
		ModelAndView mv = evt.modelAndView
        Long id = (Long) evt.data
        UserContactInfo userContactInfo = crudService.find(UserContactInfo.class, id)

        try {
			if (userContactInfo.user.equals(UserHolder.get().current)) {
				crudService.delete(userContactInfo)
                CMSUtil.addSuccessMessage("Direccion de contacto elimidada exitosamente", evt.redirectAttributes)
            } else {
				CMSUtil.addErrorMessage("Direccion de contacto NO pertenece a este usuario -.-", evt.redirectAttributes)
            }
		} catch (Exception e) {
			CMSUtil.addWarningMessage("Direccion de contacto utilizada, no puede ser borrada", evt.redirectAttributes)

        }

	}

}

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

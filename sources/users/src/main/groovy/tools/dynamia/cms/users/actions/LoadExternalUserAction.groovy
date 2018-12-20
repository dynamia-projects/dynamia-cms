/*
 * Copyright (C) 2009 - 2019 Dynamia Soluciones IT S.A.S - NIT 900302344-1
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
import tools.dynamia.cms.users.api.UserDTO
import tools.dynamia.cms.users.services.UserService
import tools.dynamia.domain.services.CrudService

@CMSAction
class LoadExternalUserAction implements SiteAction {

    @Autowired
    private UserService service

    @Autowired
    private CrudService crudService


    @Override
    String getName() {
        return "loadExternalUser"
    }

    @Override
    void actionPerformed(ActionEvent evt) {

        ModelAndView mv = new ModelAndView("users/external")
        String id = evt.request.getParameter("id")


        if (id != null && !id.empty) {


            UserDTO dto = service.loadExternalUser(evt.site, id)
            if (dto != null) {
                mv.addObject("externalUser", dto)
                evt.request.session.setAttribute("externalUser", dto)
                CMSUtil.addSuccessMessage("Por favor verifique los datos antes de continuar", mv)
            } else {
                CMSUtil.addErrorMessage("No se encontro cliente con identificacion: " + id, mv)
            }

        } else {
            CMSUtil.addErrorMessage("Ingrese identificacion de cliente", mv)
        }

    }
}

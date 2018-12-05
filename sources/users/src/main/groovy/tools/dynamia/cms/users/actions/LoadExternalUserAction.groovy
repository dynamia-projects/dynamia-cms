/*
 *  Copyright (c) 2018 Dynamia Soluciones IT SAS and the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
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

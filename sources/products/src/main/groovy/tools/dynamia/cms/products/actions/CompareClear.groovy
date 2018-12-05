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
package tools.dynamia.cms.products.actions

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.view.RedirectView
import tools.dynamia.cms.core.CMSUtil
import tools.dynamia.cms.core.actions.ActionEvent
import tools.dynamia.cms.core.actions.SiteAction
import tools.dynamia.cms.core.api.CMSAction
import tools.dynamia.domain.services.CrudService
import tools.dynamia.integration.Containers

/**
 *
 * @author Mario Serrano Leones
 */
@CMSAction
class CompareClear implements SiteAction {

    @Autowired
    private CrudService crudService
    @Autowired
    private tools.dynamia.cms.products.services.ProductsService service

    @Override
    String getName() {
        return "compareClear"
    }

    @Override
    void actionPerformed(ActionEvent evt) {

        tools.dynamia.cms.products.ProductCompareList list = Containers.get().findObject(tools.dynamia.cms.products.ProductCompareList.class)
        list.clear()

        ModelAndView mv = evt.modelAndView
        CMSUtil.addSuccessMessage("Se ha limpiado la lista de comparacion.", evt.redirectAttributes)
        String redirect = "/"
        mv.view = new RedirectView(redirect, true, true, false)
    }

}

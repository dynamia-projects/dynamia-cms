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
package tools.dynamia.cms.products.actions

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.view.RedirectView
import tools.dynamia.cms.core.CMSUtil
import tools.dynamia.cms.core.actions.ActionEvent
import tools.dynamia.cms.core.actions.SiteAction
import tools.dynamia.cms.core.api.CMSAction
import tools.dynamia.cms.products.domain.Product
import tools.dynamia.domain.services.CrudService
import tools.dynamia.integration.Containers

/**
 *
 * @author Mario Serrano Leones
 */
@CMSAction
class AddProductToCompareList implements SiteAction {

    @Autowired
    private CrudService crudService
    @Autowired
    private tools.dynamia.cms.products.services.ProductsService service

    @Override
    String getName() {
        return "addProductToCompareList"
    }

    @Override
    void actionPerformed(ActionEvent evt) {
        Long id = (Long) evt.data

        Product product = crudService.find(Product.class, id)
        tools.dynamia.cms.products.ProductCompareList list = Containers.get().findObject(tools.dynamia.cms.products.ProductCompareList.class)
        list.add(product)

        ModelAndView mv = evt.modelAndView
        tools.dynamia.cms.products.ProductsUtil.setupDefaultVars(evt.site, mv)

        if (list.products.size() == 1) {
            CMSUtil.addSuccessMessage("Agregue otros productos de la misma categoria o similares para comparar", evt.redirectAttributes)
        }
        String redirect = "/store/compare/" + list.toString()
        mv.view = new RedirectView(redirect, true, true, false)
    }

}

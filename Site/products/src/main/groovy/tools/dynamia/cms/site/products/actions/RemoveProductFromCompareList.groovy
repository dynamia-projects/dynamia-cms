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
package tools.dynamia.cms.site.products.actions

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.view.RedirectView
import tools.dynamia.cms.site.core.CMSUtil
import tools.dynamia.cms.site.core.actions.ActionEvent
import tools.dynamia.cms.site.core.actions.SiteAction
import tools.dynamia.cms.site.core.api.CMSAction
import tools.dynamia.cms.site.products.ProductCompareList
import tools.dynamia.cms.site.products.ProductsUtil
import tools.dynamia.cms.site.products.domain.Product
import tools.dynamia.cms.site.products.services.ProductsService
import tools.dynamia.domain.services.CrudService
import tools.dynamia.integration.Containers

/**
 *
 * @author Mario Serrano Leones
 */
@CMSAction
class RemoveProductFromCompareList implements SiteAction {

    @Autowired
    private CrudService crudService
    @Autowired
    private ProductsService service

    @Override
    String getName() {
        return "removeProductFromCompareList"
    }

    @Override
    void actionPerformed(ActionEvent evt) {
        Long id = (Long) evt.data

        Product product = crudService.find(Product.class, id)
        ProductCompareList list = Containers.get().findObject(ProductCompareList.class)
        list.remove(product)

        ModelAndView mv = evt.modelAndView
        String action = "/store/compare/" + list.toString()
        ProductsUtil.setupDefaultVars(evt.site, mv)
        if (list.products.size() == 1) {
            CMSUtil.addSuccessMessage("Agregue otros productos de la misma categoria o similares para comparar", evt.redirectAttributes)
        } else if (list.products.empty) {
            action = "/"
        }
        mv.view = new RedirectView(action, true, true, false)

    }

}

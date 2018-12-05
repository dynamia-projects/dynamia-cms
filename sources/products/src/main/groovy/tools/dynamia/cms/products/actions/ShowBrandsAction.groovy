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
import tools.dynamia.cms.core.actions.ActionEvent
import tools.dynamia.cms.core.actions.SiteAction
import tools.dynamia.cms.core.api.CMSAction
import tools.dynamia.cms.products.ProductsUtil
import tools.dynamia.cms.products.services.ProductsService

/**
 *
 * @author Mario Serrano Leones
 */
@CMSAction
class ShowBrandsAction implements SiteAction {

    @Autowired
    private ProductsService service

    @Override
    String getName() {
        return "showProductBrands"
    }

    @Override
    void actionPerformed(ActionEvent evt) {
        ModelAndView mv = evt.modelAndView
        mv.addObject("title", "Nuestras marcas")
        mv.addObject("brandlist", service.getBrands(evt.site))

        ProductsUtil.setupProductsVar(Collections.EMPTY_LIST, mv)
    }

}

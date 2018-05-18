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
import tools.dynamia.cms.core.CMSUtil
import tools.dynamia.cms.core.actions.ActionEvent
import tools.dynamia.cms.core.actions.SiteAction
import tools.dynamia.cms.core.api.CMSAction
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.products.domain.Product
import tools.dynamia.commons.StringUtils
import tools.dynamia.domain.services.CrudService

/**
 *
 * @author Mario Serrano Leones
 */
@CMSAction
class CompareProducts implements SiteAction {

    @Autowired
    private tools.dynamia.cms.products.services.ProductsService service
    @Autowired
    private CrudService crudService

    @Override
    String getName() {
        return "compareProducts"
    }

    @Override
    void actionPerformed(ActionEvent evt) {
        String[] idsString = (String[]) evt.data
        List<Long> ids = new ArrayList<>()
        for (String id : idsString) {
            ids.add(new Long(id))
        }

        List<Product> products = service.getProductsById(ids)
        tools.dynamia.cms.products.ProductCompareGrid grid = new tools.dynamia.cms.products.ProductCompareGrid(service, products)
        grid.loadData()

        ModelAndView mv = evt.modelAndView
        mv.addObject("comparegrid", grid)
        mv.addObject("title", buildTitle(products, evt.site))
        tools.dynamia.cms.products.ProductsUtil.setupDefaultVars(evt.site, mv)
    }

    private String buildTitle(List<Product> products, Site site) {
        CMSUtil utils = new CMSUtil(site)
        List<String> names = new ArrayList<>()
        for (Product product : products) {
            names.add(utils.cropText(product.name, 30).replace(".", "").trim().toUpperCase())
        }
        return StringUtils.arrayToDelimitedString(names.toArray(), " vs ")
    }

}

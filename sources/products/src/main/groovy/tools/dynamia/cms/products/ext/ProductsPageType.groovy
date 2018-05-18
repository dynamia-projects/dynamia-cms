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
package tools.dynamia.cms.products.ext

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.servlet.ModelAndView
import tools.dynamia.cms.core.actions.SiteActionManager
import tools.dynamia.cms.core.api.CMSExtension
import tools.dynamia.cms.pages.PageContext
import tools.dynamia.cms.pages.api.PageTypeExtension
import tools.dynamia.cms.pages.domain.PageParameter
import tools.dynamia.domain.services.CrudService

/**
 *
 * @author Mario Serrano Leones
 */
@CMSExtension
class ProductsPageType implements PageTypeExtension {

    @Autowired
    private tools.dynamia.cms.products.services.ProductsService service

    @Autowired
    private CrudService crudService

    @Override
    String getId() {
        return "products"
    }

    @Override
    String getName() {
        return "Products List Page"
    }

    @Override
    String getDescription() {
        return "A product list page, you cant specified a category id using page parameters"
    }

    @Override
    String getDescriptorId() {
        return "ProductGridConfig"
    }

    @Override
    void setupPage(PageContext context) {
        ModelAndView mv = context.modelAndView
        mv.addObject("subtitle", context.page.subtitle)

        PageParameter categoryParam = context.getParameter("category")

        if (categoryParam != null) {
            Long id = new Long(categoryParam.value)
            SiteActionManager.performAction("loadProductCategory", mv, context.request, id)
            mv.viewName = "products/products"
        } else {
            tools.dynamia.cms.products.ProductsUtil.setupProductsVar(service.getFeaturedProducts(context.site), context.modelAndView)
            mv.viewName = "products/main"
            SiteActionManager.performAction("showMainProductPage", mv, context.request)

        }

    }

}

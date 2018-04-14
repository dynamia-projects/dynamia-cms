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
import tools.dynamia.cms.site.core.CMSUtil
import tools.dynamia.cms.site.core.actions.ActionEvent
import tools.dynamia.cms.site.core.actions.SiteAction
import tools.dynamia.cms.site.core.api.CMSAction
import tools.dynamia.cms.site.core.domain.Site
import tools.dynamia.cms.site.products.ProductSearchForm
import tools.dynamia.cms.site.products.ProductsUtil
import tools.dynamia.cms.site.products.domain.Product
import tools.dynamia.cms.site.products.domain.ProductCategory
import tools.dynamia.cms.site.products.services.ProductsService
import tools.dynamia.domain.services.CrudService

/**
 *
 * @author Mario Serrano Leones
 */
@CMSAction
class SearchProductsAction implements SiteAction {

    @Autowired
    private ProductsService service

    @Autowired
    private CrudService crudService

    @Override
    String getName() {
        return "searchProducts"
    }

    @Override
    void actionPerformed(ActionEvent evt) {
        ModelAndView mv = evt.modelAndView
        Site site = evt.site

        List<Product> products = null
        if (evt.data instanceof String) {
            String query = (String) evt.data
            products = service.find(site, query)
        } else if (evt.data instanceof ProductSearchForm) {
            ProductSearchForm form = (ProductSearchForm) evt.data
            products = service.filterProducts(site, form)
            mv.addObject("prd_searchForm", form)
            if (form.categoryId != null) {
                ProductCategory category = crudService.find(ProductCategory.class, form.categoryId)
                List<ProductCategory> subcategories = service.getSubcategories(category)

                mv.addObject("prd_category", category)
                mv.addObject("prd_parentCategory", category.parent)
                mv.addObject("prd_subcategories", subcategories)
            }
        }

        if (products == null) {
            products = service.getFeaturedProducts(site)
            mv.addObject("title", "Ingrese los campos de busqueda")
        } else if (!products.empty) {
            mv.addObject("title", products.size() + " productos encontrados")
        } else {
            mv.addObject("title", " No se encontraron productos para la busqueda avanzada")
        }

        products = CMSUtil.setupPagination(products, evt.request, mv)
        ProductsUtil.setupProductsVar(products, mv)

    }

}

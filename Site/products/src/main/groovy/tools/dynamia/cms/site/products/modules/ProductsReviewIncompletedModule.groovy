/* 
 * Copyright 2017 Dynamia Soluciones IT SAS and the original author or authors.
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
package tools.dynamia.cms.site.products.modules

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.cms.site.core.api.AbstractModule
import tools.dynamia.cms.site.core.api.CMSModule
import tools.dynamia.cms.site.core.api.ModuleContext
import tools.dynamia.cms.site.core.domain.ModuleInstance
import tools.dynamia.cms.site.products.domain.ProductReview
import tools.dynamia.cms.site.products.domain.ProductsSiteConfig
import tools.dynamia.cms.site.products.dto.ProductsReviewResponse
import tools.dynamia.cms.site.products.services.ProductsService
import tools.dynamia.cms.site.users.UserHolder

/**
 * Created by Mario on 18/11/2014.
 */
@CMSModule
class ProductsReviewIncompletedModule extends AbstractModule {

    @Autowired
    private ProductsService service

    ProductsReviewIncompletedModule() {
        super("products_incomplete_reviews", "Products Reviews Incompleted", "products/modules/incompleteReviews")
        setDescription("Show a list of incomplete Product Reviews")
        putMetadata("author", "Mario Serrano Leones")
        putMetadata("version", "1.0")
        putMetadata("created at", "09-08-2017")
        setVariablesNames("reviews", "hasReviews", "count")
        setCacheable(false)

    }

    @Override
    void init(ModuleContext context) {
        System.out.println("Loading Products Reviews")
        ModuleInstance mod = context.getModuleInstance()

        List<ProductReview> reviews
        if (UserHolder.get().isAuthenticated()) {
            reviews = service.getIncompleteProductReviews(UserHolder.get().getCurrent())

            if ((reviews == null || reviews.isEmpty()) && UserHolder.get().getCurrent().getIdentification() != null) {
                try {
                    ProductsSiteConfig config = service.getSiteConfig(context.getSite())
                    ProductsReviewResponse response = service.requestExternalReviews(config, "ID" + UserHolder.get().getCurrent().getIdentification())
                    reviews = service.getExternalProductReviews(context.getSite(), response, UserHolder.get().getCurrent())
                } catch (Exception e) {
                    e.printStackTrace()
                }

            }

        } else {
            reviews = Collections.EMPTY_LIST
        }

        mod.addObject("reviews", reviews)
        mod.addObject("count", reviews.size())
        mod.addObject("hasReviews", !reviews.isEmpty())

    }

}
/*
 * Copyright (C) 2009 - 2020 Dynamia Soluciones IT S.A.S - NIT 900302344-1
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
package tools.dynamia.cms.products.modules

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.cms.core.api.AbstractModule
import tools.dynamia.cms.core.api.CMSModule
import tools.dynamia.cms.core.api.ModuleContext
import tools.dynamia.cms.core.domain.ModuleInstance
import tools.dynamia.cms.products.domain.ProductReview
import tools.dynamia.cms.products.domain.ProductsSiteConfig
import tools.dynamia.cms.users.UserHolder

/**
 * Created by Mario on 18/11/2014.
 */
@CMSModule
class ProductsReviewIncompletedModule extends AbstractModule {

    @Autowired
    private tools.dynamia.cms.products.services.ProductsService service

    ProductsReviewIncompletedModule() {
        super("products_incomplete_reviews", "Products Reviews Incompleted", "products/modules/incompleteReviews")
        description = "Show a list of incomplete Product Reviews"
        putMetadata("author", "Mario Serrano Leones")
        putMetadata("version", "1.0")
        putMetadata("created at", "09-08-2017")
        setVariablesNames("reviews", "hasReviews", "count")
        cacheable = false

    }

    @Override
    void init(ModuleContext context) {
        System.out.println("Loading Products Reviews")
        ModuleInstance mod = context.moduleInstance

        List<ProductReview> reviews
        if (UserHolder.get().authenticated) {
            reviews = service.getIncompleteProductReviews(UserHolder.get().current)

            if ((reviews == null || reviews.empty) && UserHolder.get().current.identification != null) {
                try {
                    ProductsSiteConfig config = service.getSiteConfig(context.site)
                    tools.dynamia.cms.products.dto.ProductsReviewResponse response = service.requestExternalReviews(config, "ID" + UserHolder.get().current.identification)
                    reviews = service.getExternalProductReviews(context.site, response, UserHolder.get().current)
                } catch (Exception e) {
                    e.printStackTrace()
                }

            }

        } else {
            reviews = Collections.EMPTY_LIST
        }

        mod.addObject("reviews", reviews)
        mod.addObject("count", reviews.size())
        mod.addObject("hasReviews", !reviews.empty)

    }

}

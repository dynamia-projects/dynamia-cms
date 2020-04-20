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
package tools.dynamia.cms.products.ext

import org.springframework.web.servlet.ModelAndView
import tools.dynamia.cms.core.actions.SiteActionManager
import tools.dynamia.cms.core.api.CMSExtension
import tools.dynamia.cms.pages.PageContext
import tools.dynamia.cms.pages.api.PageTypeExtension

/**
 *
 * @author Mario Serrano Leones
 */
@CMSExtension
class BrandsPageType implements PageTypeExtension {

    @Override
    String getId() {
        return "products-brands"
    }

    @Override
    String getName() {
        return "Product Brands List Page"
    }

    @Override
    String getDescription() {
        return "A brand list page, you cant specified a brand id using page parameters"
    }

    @Override
    String getDescriptorId() {
        return "BrandGridConfig"
    }

    @Override
    void setupPage(PageContext context) {
        ModelAndView mv = context.modelAndView
        mv.addObject("subtitle", context.page.subtitle)

        SiteActionManager.performAction("showBrands", mv, context.request)

    }

}

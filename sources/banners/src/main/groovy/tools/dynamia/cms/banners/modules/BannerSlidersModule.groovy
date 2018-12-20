/*
 * Copyright (C) 2009 - 2019 Dynamia Soluciones IT S.A.S - NIT 900302344-1
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
package tools.dynamia.cms.banners.modules

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.cms.banners.domain.Banner
import tools.dynamia.cms.banners.services.BannerService
import tools.dynamia.cms.core.JavaScriptResource
import tools.dynamia.cms.core.StyleSheetResource
import tools.dynamia.cms.core.api.AbstractModule
import tools.dynamia.cms.core.api.CMSModule
import tools.dynamia.cms.core.api.ModuleContext
import tools.dynamia.cms.core.domain.ModuleInstanceParameter

@CMSModule
class BannerSlidersModule extends AbstractModule {

    private final static String PARAM_CATEGORY_ID = "category"

    @Autowired
    private BannerService service

    BannerSlidersModule() {
        super("banner_sliders", "Banners Slider", "banners/modules/bannersliders")
        addResource(new JavaScriptResource("jquery.flexslider", "banners/js/jquery.flexslider.js"))
        addResource(new JavaScriptResource("banner_sliders", "banners/js/banners.sliders.js"))
        addResource(new StyleSheetResource("flexslider", "banners/css/flexslider.css"))
        setVariablesNames("banners")
    }

    @Override
    void init(ModuleContext context) {

        ModuleInstanceParameter categoryId = context.getParameter(PARAM_CATEGORY_ID)
        if (categoryId != null) {
            try {

                List<Banner> banners = service.getBannersByCategory(new Long(categoryId.value))
                context.moduleInstance.addObject("banners", banners)

            } catch (NumberFormatException e) {
                // ignore
            }
        }

    }

}

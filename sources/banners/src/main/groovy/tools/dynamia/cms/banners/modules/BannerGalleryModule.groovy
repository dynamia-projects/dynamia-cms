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
import tools.dynamia.cms.banners.services.BannerService
import tools.dynamia.cms.core.JavaScriptResource
import tools.dynamia.cms.core.StyleSheetResource
import tools.dynamia.cms.core.api.AbstractModule
import tools.dynamia.cms.core.api.CMSModule
import tools.dynamia.cms.core.api.ModuleContext
import tools.dynamia.cms.core.domain.ModuleInstanceParameter

@CMSModule
class BannerGalleryModule extends AbstractModule {

	private static final String PARAM_USE_IMAGES_FOLDER = "useImagesFolder"

    private final static String PARAM_CATEGORY_ID = "category"
    private final static String PARAM_THUMBNAIL_WIDTH = "thumbnailWidth"
    private final static String PARAM_THUMBNAIL_HEIGHT = "thumbnailHeight"

    @Autowired
	private BannerService service

    BannerGalleryModule() {
		super("banners_gallery", "Gallery", "banners/modules/bannersgallery")
        description = "Create a photo gallery using banners images or folder"
        addResource(new JavaScriptResource("jquery.blueimp-gallery", "banners/js/jquery.blueimp-gallery.min.js"))
        addResource(new StyleSheetResource("blueimp-gallery", "banners/css/blueimp-gallery.min.css"))
        setVariablesNames("banners", "width", "height")
    }

	@Override
    void init(ModuleContext context) {

		ModuleInstanceParameter categoryId = context.getParameter(PARAM_CATEGORY_ID)

        if (categoryId != null) {
			try {
				List<tools.dynamia.cms.banners.domain.Banner> banners = null
                if (context.isTrue(PARAM_USE_IMAGES_FOLDER)) {
					banners = service.createBannersFromCategory(new Long(categoryId.value))
                } else {
					banners = service.getBannersByCategory(new Long(categoryId.value))
                }

                context.moduleInstance.addObject("banners", banners)
                context.moduleInstance.addObject("width", context.getParameterValue(PARAM_THUMBNAIL_WIDTH, "200"))
                context.moduleInstance.addObject("height",
						context.getParameterValue(PARAM_THUMBNAIL_HEIGHT, "200"))

            } catch (NumberFormatException e) {
				// ignore
			}
		}

	}

}

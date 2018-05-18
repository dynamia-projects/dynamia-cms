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

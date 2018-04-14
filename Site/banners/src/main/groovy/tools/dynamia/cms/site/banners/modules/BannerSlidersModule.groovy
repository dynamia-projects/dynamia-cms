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
package tools.dynamia.cms.site.banners.modules;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import tools.dynamia.cms.site.banners.domain.Banner;
import tools.dynamia.cms.site.banners.services.BannerService;
import tools.dynamia.cms.site.core.JavaScriptResource;
import tools.dynamia.cms.site.core.StyleSheetResource;
import tools.dynamia.cms.site.core.api.AbstractModule;
import tools.dynamia.cms.site.core.api.CMSModule;
import tools.dynamia.cms.site.core.api.ModuleContext;
import tools.dynamia.cms.site.core.domain.ModuleInstanceParameter;

@CMSModule
public class BannerSlidersModule extends AbstractModule {

	private final static String PARAM_CATEGORY_ID = "category";

	@Autowired
	private BannerService service;

	public BannerSlidersModule() {
		super("banner_sliders", "Banners Slider", "banners/modules/bannersliders");
		addResource(new JavaScriptResource("jquery.flexslider", "banners/js/jquery.flexslider.js"));
		addResource(new JavaScriptResource("banner_sliders", "banners/js/banners.sliders.js"));
		addResource(new StyleSheetResource("flexslider", "banners/css/flexslider.css"));
		setVariablesNames("banners");
	}

	@Override
	public void init(ModuleContext context) {

		ModuleInstanceParameter categoryId = context.getParameter(PARAM_CATEGORY_ID);
		if (categoryId != null) {
			try {

				List<Banner> banners = service.getBannersByCategory(new Long(categoryId.getValue()));
				context.getModuleInstance().addObject("banners", banners);

			} catch (NumberFormatException e) {
				// ignore
			}
		}

	}

}

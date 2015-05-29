package com.dynamia.cms.site.banners.modules;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dynamia.cms.site.banners.domain.Banner;
import com.dynamia.cms.site.banners.services.BannerService;
import com.dynamia.cms.site.core.api.AbstractModule;
import com.dynamia.cms.site.core.api.CMSModule;
import com.dynamia.cms.site.core.api.ModuleContext;
import com.dynamia.cms.site.core.domain.ModuleInstanceParameter;

@CMSModule
public class BannerSlidersModule extends AbstractModule {

	private final static String PARAM_CATEGORY_ID = "category";

	@Autowired
	private BannerService service;

	public BannerSlidersModule() {
		super("banner_sliders", "Banners Slider", "banners/modules/bannersliders");
	}

	@Override
	public void init(ModuleContext context) {

		ModuleInstanceParameter categoryId = context.getParameters(PARAM_CATEGORY_ID);
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

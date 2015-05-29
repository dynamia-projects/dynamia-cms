package com.dynamia.cms.site.banners;

import com.dynamia.cms.site.banners.domain.Banner;
import com.dynamia.cms.site.banners.domain.BannerCategory;
import com.dynamia.cms.site.core.api.AdminModule;
import com.dynamia.cms.site.core.api.AdminModuleOption;
import com.dynamia.cms.site.core.api.CMSModule;

@CMSModule
public class BannerAdminContent implements AdminModule {

	@Override
	public String getGroup() {
		return "Content";
	}

	@Override
	public String getName() {
		return "Site Content";
	}

	@Override
	public AdminModuleOption[] getOptions() {
		return new AdminModuleOption[]{
			new AdminModuleOption("banners", "Banners", Banner.class),
			new AdminModuleOption("bannersCategory", "Banners Categories", BannerCategory.class)
		};
	}

}

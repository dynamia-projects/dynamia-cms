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
package com.dynamia.cms.site.banners;

import com.dynamia.cms.site.banners.domain.Banner;
import com.dynamia.cms.site.banners.domain.BannerCategory;
import com.dynamia.cms.site.core.api.AdminModule;
import com.dynamia.cms.site.core.api.AdminModuleOption;
import com.dynamia.cms.site.core.api.CMSModule;

@CMSModule
public class BannerAdminModule implements AdminModule {

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
			new AdminModuleOption("banners", "Banners", Banner.class,true,true,"photo",true),
			new AdminModuleOption("bannersCategory", "Banners Categories", BannerCategory.class)
		};
	}

}

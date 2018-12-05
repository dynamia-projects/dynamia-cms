/*
 *  Copyright (c) 2018 Dynamia Soluciones IT SAS and the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package tools.dynamia.cms.banners

import tools.dynamia.cms.banners.domain.Banner
import tools.dynamia.cms.banners.domain.BannerCategory
import tools.dynamia.cms.core.api.AdminModule
import tools.dynamia.cms.core.api.AdminModuleOption
import tools.dynamia.cms.core.api.CMSModule

@CMSModule
class BannerAdminModule implements AdminModule {

    @Override
    String getGroup() {
        return "banners"
    }

    @Override
    String getName() {
        return "Banners"
    }

    @Override
    String getImage() {
        return "fa-photo"
    }

    @Override
    AdminModuleOption[] getOptions() {
        return [
                new AdminModuleOption("banners", "Items", Banner.class, true, true, "photo", true),
                new AdminModuleOption("bannersCategory", "Categories", BannerCategory.class)
        ]
    }

}

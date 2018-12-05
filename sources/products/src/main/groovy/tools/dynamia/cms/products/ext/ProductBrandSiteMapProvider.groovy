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

package tools.dynamia.cms.products.ext

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.cms.core.CMSUtil
import tools.dynamia.cms.core.api.CMSExtension
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.core.sitemap.SiteMapProvider
import tools.dynamia.cms.core.sitemap.SiteMapURL

@CMSExtension
class ProductBrandSiteMapProvider implements SiteMapProvider {

    @Autowired
    private tools.dynamia.cms.products.services.ProductsService service

    @Override
    List<SiteMapURL> get(Site site) {
        List<SiteMapURL> urls = new ArrayList<>()

        SiteMapURL url = new SiteMapURL(CMSUtil.getSiteURL(site, "store/brands"))
        url.name = "Brands"

        urls.add(url)
        service.getBrands(site).stream().map { c -> createURL(site, c) }.forEach { urls << it }

        return urls

    }

    private SiteMapURL createURL(Site site, tools.dynamia.cms.products.domain.ProductBrand b) {
        SiteMapURL url = new SiteMapURL(CMSUtil.getSiteURL(site, String.format("store/brands/%s", b.alias)))
        url.name = b.name
        url.description = b.description

        return url
    }

}

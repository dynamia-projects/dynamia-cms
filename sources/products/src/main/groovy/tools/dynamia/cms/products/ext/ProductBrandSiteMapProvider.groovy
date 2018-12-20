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

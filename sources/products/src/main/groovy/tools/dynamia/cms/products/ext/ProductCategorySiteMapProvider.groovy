/*
 * Copyright (C) 2009 - 2020 Dynamia Soluciones IT S.A.S - NIT 900302344-1
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
import tools.dynamia.cms.core.sitemap.SiteMapFrecuency
import tools.dynamia.cms.core.sitemap.SiteMapProvider
import tools.dynamia.cms.core.sitemap.SiteMapURL
import tools.dynamia.cms.products.domain.ProductCategory
import tools.dynamia.cms.products.services.ProductsService

@CMSExtension
class ProductCategorySiteMapProvider implements SiteMapProvider {

    @Autowired
    private ProductsService service

    @Override
    List<SiteMapURL> get(Site site) {
        return service.getCategories(site).findResults { it.active ? createURL(site, it) : null }
    }

    private SiteMapURL createURL(Site site, ProductCategory category) {
        SiteMapURL url = new SiteMapURL(CMSUtil.getSiteURL(site, category))
        url.changeFrequency = SiteMapFrecuency.daily
        url.priority = SiteMapURL.HIGH
        url.name = category.name
        url.description = category.description
        if (category.parent != null) {
            url.category = category.parent.name
        }
        return url
    }

}

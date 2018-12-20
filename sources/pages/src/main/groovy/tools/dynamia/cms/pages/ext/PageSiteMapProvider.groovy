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

package tools.dynamia.cms.pages.ext

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.cms.core.CMSUtil
import tools.dynamia.cms.core.api.CMSExtension
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.core.sitemap.SiteMapProvider
import tools.dynamia.cms.core.sitemap.SiteMapURL
import tools.dynamia.cms.pages.domain.Page

@CMSExtension
class PageSiteMapProvider implements SiteMapProvider {

    @Autowired
    private tools.dynamia.cms.pages.services.PageService service

    @Override
    List<SiteMapURL> get(Site site) {

        List<SiteMapURL> urls = new ArrayList<>()

        service.getPages(site).stream().filter { p -> p.published }.map { p -> createURL(site, p) }.forEach {
            urls << it
        }

        service.getPagesCategories(site).forEach { p ->
            if (p.alias != null && !p.alias.empty) {
                SiteMapURL url = new SiteMapURL(CMSUtil.getSiteURL(site, "category/" + p.alias))
                url.name = p.name
                url.description = p.description
                urls.add(url)
            }
        }

        return urls
    }

    private SiteMapURL createURL(Site site, Page p) {
        SiteMapURL url = new SiteMapURL(CMSUtil.getSiteURL(site, p.alias), p.lastUpdate)
        url.name = p.title
        url.imageURL = p.imageURL
        url.description = p.summary
        if (p.category != null) {
            url.category = p.category.name
        }
        return url
    }

}

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

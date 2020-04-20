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

package tools.dynamia.cms.blog.ext

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.cms.blog.services.BlogService
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.core.sitemap.SiteMapFrecuency
import tools.dynamia.cms.core.sitemap.SiteMapProvider
import tools.dynamia.cms.core.sitemap.SiteMapURL
import tools.dynamia.domain.services.AbstractService
import tools.dynamia.integration.sterotypes.Provider

import static tools.dynamia.cms.core.CMSUtil.getSiteURL

@Provider
class BlogSiteMapProvider extends AbstractService implements SiteMapProvider {


    @Autowired
    private BlogService blogService

    @Override
    List<SiteMapURL> get(Site site) {

        List<SiteMapURL> urls = [new SiteMapURL(getSiteURL(site, "blogs"))]

        def blogs = blogService.getBlogs(site)
        blogs?.each { blog ->
            urls << new SiteMapURL(getSiteURL(site, blog), blog.lastUpdate, SiteMapFrecuency.daily)
                    .name(blog.title)
                    .description(blog.description)
                    .category(blog.mainAuthor.toString())


            def categories = blogService.getCategories(blog)
            categories?.each { cat ->
                urls << new SiteMapURL(getSiteURL(site, cat), cat.lastUpdate, SiteMapFrecuency.daily)
                        .name(cat.name)
                        .description(cat.description)
                        .category(cat.name)

                def posts = blogService.findByTilleAndTagAndCategory(blog, null, null, cat)
                posts?.each { post ->
                    urls << new SiteMapURL(getSiteURL(site, post), post.lastUpdate, SiteMapFrecuency.daily)
                            .name(post.title)
                            .description(post.summary)
                            .imageURL(post.imageURL)
                            .category(cat.name)
                }
            }
        }
        return urls
    }
}

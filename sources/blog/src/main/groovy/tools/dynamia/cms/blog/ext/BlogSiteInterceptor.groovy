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

package tools.dynamia.cms.blog.ext

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.servlet.ModelAndView
import tools.dynamia.cms.blog.domain.Blog
import tools.dynamia.cms.blog.services.BlogService
import tools.dynamia.cms.core.api.CMSExtension
import tools.dynamia.cms.core.api.SiteRequestInterceptorAdapter
import tools.dynamia.cms.core.domain.Site

@CMSExtension
class BlogSiteInterceptor extends SiteRequestInterceptorAdapter {
    @Autowired
    private BlogService blogService


    @Override
    protected void afterRequest(Site site, ModelAndView modelAndView) {
        Blog blog = modelAndView.getModel().get("blog") as Blog
        if (blog != null) {
            modelAndView.addObject("blog_categories", blogService.getCategories(blog))
            modelAndView.addObject("blog_authors", blogService.findAuthors(blog))
            modelAndView.addObject("blog_archive", blogService.getArchiveSummary(blog))
        }
    }
}

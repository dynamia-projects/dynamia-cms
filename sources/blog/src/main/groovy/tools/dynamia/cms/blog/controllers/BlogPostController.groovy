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

package tools.dynamia.cms.blog.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import tools.dynamia.cms.blog.BlogException
import tools.dynamia.cms.blog.domain.Blog
import tools.dynamia.cms.blog.domain.BlogPost
import tools.dynamia.cms.blog.services.BlogService
import tools.dynamia.cms.core.SiteContext
import tools.dynamia.cms.core.SiteNotFoundException
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.core.services.SiteService
import tools.dynamia.navigation.PageNotFoundException

import javax.servlet.http.HttpServletRequest

@Controller
@RequestMapping("/blogs")
class BlogPostController {

    @Autowired
    private SiteService siteService
    @Autowired
    private BlogService blogService

    @GetMapping("/{blog}/{year}/{month}/{post}")
    ModelAndView post(@PathVariable("blog") String blogAlias, @PathVariable("year") int year,
                      @PathVariable("month") int month, @PathVariable("post") String postAlias, HttpServletRequest request) {

        Site site = siteService.getSite(request)
        Blog blog = blogService.findBlog(site, blogAlias)

        if (blog == null) {
            throw new BlogException(HttpStatus.NOT_FOUND, "blog $blogAlias not found")
        }

        BlogPost blogPost = blogService.findPost(blog, year, month, postAlias)
        if (blogPost == null) {
            throw new BlogException(HttpStatus.NOT_FOUND, "blog post $postAlias not found")
        }

        ModelAndView mv = new ModelAndView("blog/post")
        mv.addObject("blog", "blog")
        mv.addObject("blog_post", blogPost)
        mv.addObject("blog_comments", blogService.getComments(blogPost))

    }
}

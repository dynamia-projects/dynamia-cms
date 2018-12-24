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
import tools.dynamia.cms.blog.domain.BlogCategory
import tools.dynamia.cms.blog.domain.BlogPost
import tools.dynamia.cms.blog.services.BlogService
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.core.services.SiteService

import javax.servlet.http.HttpServletRequest

@Controller
@RequestMapping("/blogs")
class BlogController {

    @Autowired
    private SiteService siteService
    @Autowired
    private BlogService blogService


    @GetMapping
    ModelAndView main(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("blog/index")
        Site site = siteService.getSite(request)

        mv.addObject("blogs", blogService.getBlogs(site))
        mv.addObject("main_posts", blogService.findMainPosts(site))

        return mv
    }

    @GetMapping("/{blog}/{year}/{month}/{post}")
    ModelAndView post(@PathVariable("blog") String blogAlias, @PathVariable("year") int year,
                      @PathVariable("month") int month, @PathVariable("post") String postAlias, HttpServletRequest request) {

        Site site = siteService.getSite(request)
        Blog blog = getBlog(site, blogAlias)

        BlogPost blogPost = blogService.findPost(blog, year, month, postAlias)
        if (blogPost == null) {
            throw new BlogException(HttpStatus.NOT_FOUND, "blog post $postAlias not found")
        }

        ModelAndView mv = new ModelAndView("blog/post")
        mv.addObject("blog", "blog")
        mv.addObject("blog_post", blogPost)
        mv.addObject("blog_year", year)
        mv.addObject("blog_month", month)
        mv.addObject("blog_comments", blogService.getComments(blogPost))

        return mv

    }


    @GetMapping("/{blog}/{year}/{month}")
    ModelAndView archive(@PathVariable("blog") String blogAlias, @PathVariable("year") int year,
                         @PathVariable("month") int month, HttpServletRequest request) {

        Site site = siteService.getSite(request)
        Blog blog = getBlog(site, blogAlias)


        ModelAndView mv = new ModelAndView("blog/posts")
        mv.addObject("blog", "blog")
        mv.addObject("blog_year", year)
        mv.addObject("blog_month", month)
        mv.addObject("blog_posts", blogService.findBlogArchive(blog, year, month))


        return mv

    }

    @GetMapping("/{blog}/{category}")
    ModelAndView categoryPosts(@PathVariable("blog") String blogAlias, @PathVariable("category") String categoyAlias, HttpServletRequest request) {

        Site site = siteService.getSite(request)
        Blog blog = getBlog(site, blogAlias)
        BlogCategory category = blogService.findCategory(blog, categoyAlias)

        ModelAndView mv = new ModelAndView("blog/posts")
        mv.addObject("blog", "blog")
        mv.addObject("blog_category", category)
        mv.addObject("blog_posts", blogService.findByTilleAndTagAndCategory(blog, null, null, category))
        return mv
    }

    @GetMapping("/{blog}/tag/{tag}")
    ModelAndView tagPosts(@PathVariable("blog") String blogAlias, @PathVariable("tag") String tag, HttpServletRequest request) {

        Site site = siteService.getSite(request)
        Blog blog = getBlog(site, blogAlias)


        ModelAndView mv = new ModelAndView("blog/posts")
        mv.addObject("blog", "blog")
        mv.addObject("blog_tag", tag)
        mv.addObject("blog_posts", blogService.findByTilleAndTagAndCategory(blog, null, tag, null))
        return mv
    }

    private Blog getBlog(Site site, String blogAlias) {
        Blog blog = blogService.findBlog(site, blogAlias)

        if (blog == null) {
            throw new BlogException(HttpStatus.NOT_FOUND, "blog $blogAlias not found")
        }
        blog
    }
}

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

package tools.dynamia.cms.blog.services

import org.springframework.scheduling.annotation.Async
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import tools.dynamia.cms.blog.BlogArchive
import tools.dynamia.cms.blog.BlogPostCommentTree
import tools.dynamia.cms.blog.domain.Blog
import tools.dynamia.cms.blog.domain.BlogCategory
import tools.dynamia.cms.blog.domain.BlogPost
import tools.dynamia.cms.core.domain.ContentAuthor
import tools.dynamia.cms.core.domain.Site

interface BlogService {

    List<BlogPost> findBlogArchive(Blog blog, int year, int month)

    List<BlogPost> findRecentPost(Blog blog)

    List<BlogPost> findRecentPost(ContentAuthor author)

    List<BlogPost> findMainPosts(Site site)

    List<BlogPost> findByTilleAndTagAndCategory(Blog blog, String title, String tag, BlogCategory category)

    List<BlogPost> search(Site site, String text)

    BlogPostCommentTree getComments(BlogPost post)

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void updateStatistics(Blog blog)

    Blog findBlog(Site site, String alias)

    BlogPost findPost(Blog blog, int year, int month, String alias)

    List<BlogCategory> getCategories(Blog blog)

    List<Blog> getBlogs(Site site)

    BlogCategory findCategory(Blog blog, String alias)

    List<ContentAuthor> findAuthors(Blog blog)

    List<BlogArchive> getArchiveSummary(Blog blog)

    List<BlogPost> getRelatedPosts(BlogPost post)

    BlogPost getNextPost(BlogPost post)

    BlogPost getPreviousPost(BlogPost post)
}
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

package tools.dynamia.cms.blog.services.impl

import org.springframework.scheduling.annotation.Async
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import tools.dynamia.cms.blog.BlogPostCommentTree
import tools.dynamia.cms.blog.domain.*
import tools.dynamia.cms.blog.services.BlogService
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.domain.query.QueryParameters
import tools.dynamia.domain.services.AbstractService
import tools.dynamia.integration.sterotypes.Service

import static tools.dynamia.domain.query.QueryConditions.like

@Service
class BlogServiceImpl extends AbstractService implements BlogService {

    private static final int PAGINATION_SIZE = 10


    @Override
    List<BlogPost> findBlogArchive(Blog blog, int year, int month) {
        return crudService().find(BlogPost, QueryParameters.with("blog", blog).add("year", year).add("month", month))
    }

    @Override
    List<BlogPost> findRecentPost(Blog blog) {
        return crudService().find(BlogPost, QueryParameters.with("blog", blog).paginate(PAGINATION_SIZE))
    }

    @Override
    List<BlogPost> findMainPosts(Site site) {
        return crudService().find(BlogPost, QueryParameters.with("blog.site", site).paginate(PAGINATION_SIZE * 2))
    }

    @Override
    List<BlogPost> findByTilleAndTagAndCategory(Blog blog, String title, String tag, BlogCategory category) {

        def params = QueryParameters.with("blog", blog)

        if (title) {
            params.add("title", like(title))
        }

        if (tag) {
            params.add("tags", like(tag))
        }
        if (category) {
            params.add("category", category)
        }

        return crudService().find(BlogPost, params)
    }

    @Override
    List<BlogPost> search(Site site, String text) {
        return crudService().executeQuery("""
                select p from $BlogPost.name p where p.blog.site = :site and p.published = true and (
                       p.title like :text or 
                       p.tags like :text or 
                       p.category.name like :text or
                       p.summary like :text 
                       )""", QueryParameters.with("site", site).add("text", like(text)))
    }


    @Override
    BlogPostCommentTree getComments(BlogPost post) {
        def comments = crudService().find(BlogPostComment, QueryParameters.with("post", post).add("status", CommentStatus.VALID))
        return BlogPostCommentTree.build(comments)
    }


    @Override
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void updateStatistics(Blog blog) {


        crudService().execute("update $BlogPost.name p set p.lastUpdate = :lastUpdate, p.commentsCount = (select count(c) from $BlogPostComment.name c where c.post.id = p.id and c.statud = :status where p.blog = :blog ",
                QueryParameters.with("blog", "blog")
                        .add("status", CommentStatus.VALID)
                        .add("lastUpdate", new Date()))
        crudService().execute("update $BlogCategory.name c set c.lastUpdate = :lastUpdate, c.postCount = (select count(p) from $BlogPost.name p where p.category.id = c.id and p.published = true) and c.blog = :blog",
                QueryParameters.with("blog", blog)
                        .add("lastUpdate", new Date()))

        crudService().execute("""
                            update $Blog.name b set b.lastUpdate = :lastUpdate, b.categoriesCount = (select count(c) from $BlogCategory.name c where c.blog = b),
                                                    b.postCount = (select count(p) from $BlogPost.name p where p.blog = b),
                                                    b.commentsCount = (select count(c) from $BlogPostComment.name c where c.blog = b),
                                                    b.subscriberCount = (select coun(s) $BlogSubscriber.name s were s.blog = b)
                            where b.id = :id                            
                            """,
                QueryParameters.with("id", blog.id)
                        .add("lastUpdate", new Date()))
    }

    @Override
    Blog findBlog(Site site, String alias) {
        return crudService().findSingle(Blog, QueryParameters.with("site", site).add("alias", alias))
    }

    @Override
    BlogPost findPost(Blog blog, int year, int month, String alias) {
        return crudService().findSingle(BlogPost, QueryParameters.with("blog", blog)
                .add("year", year)
                .add("month", month)
                .add("alias", alias)
                .add("published", true))
    }

    @Override
    List<BlogCategory> getCategories(Blog blog) {
        return crudService().find(BlogCategory, QueryParameters.with("blog", blog))
    }

    @Override
    List<Blog> getBlogs(Site site) {
        return crudService().find(Blog, "site", site)
    }

    @Override
    BlogCategory findCategory(Blog blog, String alias) {
        return crudService().findSingle(BlogCategory, "alias", alias)
    }
}

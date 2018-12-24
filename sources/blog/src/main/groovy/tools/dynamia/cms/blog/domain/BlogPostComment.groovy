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

package tools.dynamia.cms.blog.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import tools.dynamia.cms.blog.BlogElement
import tools.dynamia.cms.core.api.URIable
import tools.dynamia.cms.core.domain.SiteBaseEntity
import tools.dynamia.cms.users.domain.User
import tools.dynamia.domain.contraints.NotEmpty

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.ManyToOne
import javax.persistence.OneToOne
import javax.persistence.Table
import javax.validation.constraints.NotNull

@Entity
@Table(name = "blg_comments")
class BlogPostComment extends SiteBaseEntity implements BlogElement, URIable {

    @OneToOne
    @NotNull
    Blog blog

    @ManyToOne
    @JsonIgnore
    BlogPost post

    @NotNull
    @NotEmpty(message = "Enter name")
    String userName
    @NotEmpty(message = "Enter email")
    String userEmail
    String userWebSite
    @OneToOne
    @JsonIgnore
    User user

    @Column(length = 1000)
    String content

    int depth = 0
    Long thread
    /**
     * This path are the comments id separated by comma.  path == '0' means top level comment
     */
    @Column(length = 1000)
    String path = "0"
    @Enumerated(EnumType.STRING)
    CommentStatus status = CommentStatus.NEW
    boolean notifyNewReplies
    boolean notifyNewComments
    boolean reply

    void setPost(BlogPost post) {
        this.post = post
        this.blog = post?.blog
    }

    BlogPostComment newReply(User replyUser) {
        def reply = new BlogPostComment(blog: blog, post: post, depth: depth + 1, path: path + ",$id", reply: true)

        if (depth == 0) {
            reply.thread = id
        } else {
            reply.thread = thread
        }

        if (replyUser != null) {
            reply.user = replyUser
            reply.userName = replyUser.fullName
            reply.userEmail = replyUser.username
            reply.userWebSite = replyUser.website
        }
        return reply
    }

    BlogPostComment newReply() {
        return newReply(null)
    }

    @Override
    String toURI() {
        return "${post.toURI()}#${id}"
    }
}

enum CommentStatus {
    NEW, SPAM, VALID, DELETED
}

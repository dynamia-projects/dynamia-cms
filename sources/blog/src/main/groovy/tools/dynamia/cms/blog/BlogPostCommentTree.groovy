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

package tools.dynamia.cms.blog

import tools.dynamia.cms.blog.domain.BlogPostComment

import java.util.stream.Collectors

class BlogPostCommentTree {

    List<BlogPostCommentTreeNode> nodes = []

    static BlogPostCommentTree build(List<BlogPostComment> comments) {
        def tree = new BlogPostCommentTree()

        if (comments) {
            tree.nodes = comments.stream()
                    .filter { it.depth == 0 }
                    .map { new BlogPostCommentTreeNode(it) }
                    .collect(Collectors.toList())
            tree.nodes.each { loadChildren(it, comments) }
        }
        return tree
    }


    private static void loadChildren(BlogPostCommentTreeNode parent, List<BlogPostComment> comments) {
        def subpath = parent.comment.path + ",$parent.comment.id"

        comments.each { cmt ->
            if (cmt.path == subpath) {
                def child = new BlogPostCommentTreeNode(cmt)
                parent.children << child
                loadChildren(child, comments)
            }
        }
    }

}

class BlogPostCommentTreeNode {

    BlogPostComment comment
    List<BlogPostCommentTreeNode> children = []

    BlogPostCommentTreeNode(BlogPostComment comment) {
        this.comment = comment
    }
}


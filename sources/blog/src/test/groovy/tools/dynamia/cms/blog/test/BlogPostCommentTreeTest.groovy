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

package tools.dynamia.cms.blog.test

import org.junit.Assert
import org.junit.Test
import tools.dynamia.cms.blog.BlogPostCommentTree
import tools.dynamia.cms.blog.BlogPostCommentTreeNode
import tools.dynamia.cms.blog.domain.BlogPostComment

class BlogPostCommentTreeTest {

    private Long id = 0

    @Test
    void shoudBuildTree() {
        def random = new Random()
        List<BlogPostComment> comments = []

        (1..10).each {
            id++
            def cmt = new BlogPostComment(id: id, content: "comment_$it")
            comments << cmt
            generateReplies(cmt, random.nextInt(10)).each { rp1 ->
                comments << rp1
                int chance = random.nextInt(10)
                if (chance > 5) {
                    generateReplies(rp1, random.nextInt(chance)).each { rp2 ->
                        comments << rp2
                    }
                }
            }

        }

        BlogPostCommentTree tree = BlogPostCommentTree.build(comments)
        printTree(tree.nodes)

        Assert.assertTrue(!tree.nodes.empty)
    }

    def printTree(List<BlogPostCommentTreeNode> nodes) {
        nodes.each { n ->
            println(n.comment.id+":: "+ ("----" * n.comment.depth) + " Path $n.comment.path |  Thread $n.comment.thread | CONTENT: $n.comment.content ")
            printTree(n.children)
        }
    }

    def generateReplies(BlogPostComment comment, int replies) {
        List<BlogPostComment> result = []
        (1..replies).each {
            id++
            def reply = comment.newReply()
            reply.id = id
            reply.content = "$comment.content > subcontent$it"
            result << reply
        }
        return result
    }
}

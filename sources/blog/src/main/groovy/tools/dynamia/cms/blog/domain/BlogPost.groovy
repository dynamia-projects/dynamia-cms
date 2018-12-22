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

import org.apache.poi.ss.usermodel.DateUtil
import tools.dynamia.cms.blog.BlogElement
import tools.dynamia.cms.core.Aliasable
import tools.dynamia.cms.core.domain.Content
import tools.dynamia.cms.core.domain.ContentAuthor
import tools.dynamia.commons.DateTimeUtils
import tools.dynamia.domain.OrderBy
import tools.dynamia.domain.contraints.NotEmpty

import javax.persistence.Basic
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Lob
import javax.persistence.OneToOne
import javax.persistence.Table
import javax.persistence.Temporal
import javax.persistence.TemporalType
import javax.validation.constraints.NotNull

@Entity
@Table(name = "blg_posts")
@OrderBy("postDate desc")
class BlogPost extends Content implements Aliasable, BlogElement {

    @OneToOne
    @NotNull
    Blog blog

    @OneToOne
    @NotNull
    BlogCategory category

    @OneToOne
    @NotNull
    ContentAuthor author

    @NotEmpty
    @Column(length = 500)
    String title

    @Lob
    @Basic(fetch = FetchType.LAZY)
    String content

    @Column(length = 1000)
    String summary

    @Column(length = 1000)
    String tags

    String imageURL
    String imageTitle

    boolean published = true

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    Date postDate = new Date()

    boolean commentsDisabled
    long commentsCount

    @Column(name = "postAlias")
    String alias


    @Column(name = "postYear")
    int year
    @Column(name="postMonth")
    int month

    BlogPost() {
        setPostDate(new Date())
    }

    void setPostDate(Date postDate) {
        if (postDate != null) {
            this.postDate = postDate
            this.year = DateTimeUtils.getYear(postDate)
            this.month = DateTimeUtils.getMonth(postDate)
        }
    }

    @Override
    String aliasSource() {
        title
    }
}

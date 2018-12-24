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

import tools.dynamia.cms.blog.BlogElement
import tools.dynamia.cms.core.domain.SiteBaseEntity
import tools.dynamia.domain.contraints.Email
import tools.dynamia.domain.contraints.NotEmpty

import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name="blg_subscribers")
class BlogSubscriber extends SiteBaseEntity implements BlogElement{


    @ManyToOne
    Blog blog

    @NotEmpty
    @Email(message = "Enter valid email address")
    String email
    String name
    @Enumerated(EnumType.STRING)
    @NotNull
    SubscriberStatus status = SubscriberStatus.NEW
    @Temporal(TemporalType.TIMESTAMP)
    Date statusDate

    void setStatus(@NotNull SubscriberStatus status) {
        if (this.status != status) {
            this.status = status
            this.statusDate = new Date()
        }

    }
}

enum SubscriberStatus {
    NEW, VERIFIED, UNSUBSCRIBED
}

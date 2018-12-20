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

package tools.dynamia.cms.users.domain

import tools.dynamia.cms.core.domain.SiteBaseEntity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.OneToOne
import javax.persistence.Table
import javax.validation.constraints.NotNull

@Entity
@Table(name = "usr_logs")
class UserLog extends SiteBaseEntity {

    @OneToOne
    @NotNull
    private User user
    @Column(length = 2000)
    private String event

    UserLog() {
    }

    UserLog(User user, String event) {
        this.user = user
        this.event = event
        site = user.site
    }

    User getUser() {
        return user
    }

    void setUser(User user) {
        this.user = user
    }

    String getEvent() {
        return event
    }

    void setEvent(String event) {
        this.event = event
    }
}

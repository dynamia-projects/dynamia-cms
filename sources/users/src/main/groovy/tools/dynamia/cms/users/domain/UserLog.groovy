/*
 *  Copyright (c) 2018 Dynamia Soluciones IT SAS and the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
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

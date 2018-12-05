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
package tools.dynamia.cms.users

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import tools.dynamia.app.ApplicationUserInfo
import tools.dynamia.cms.core.SiteContext
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.users.api.UserProfile
import tools.dynamia.cms.users.domain.User
import tools.dynamia.cms.users.domain.UserSiteConfig
import tools.dynamia.cms.users.ext.CustomerChangeListener
import tools.dynamia.cms.users.services.UserService
import tools.dynamia.integration.Containers

/**
 *
 * @author Mario Serrano Leones
 */
@Component("userHolder")
@Scope("session")
class UserHolder implements Serializable {

    protected User user
    protected User customer

    private Date timestamp

    @Autowired
    private UserService service

    static UserHolder get() {
        try {
            return Containers.get().findObject(UserHolder.class)
        } catch (Exception e) {
            UserHolder userHolder = new UserHolder()
            userHolder.init(User.createMock())
            return userHolder
        }
    }

    boolean isAuthenticated() {
        return user != null && user.id != null
    }

    boolean isAdmin() {
        return authenticated && user.profile == UserProfile.ADMIN
    }

    boolean isSeller() {
        return authenticated && user.profile == UserProfile.SELLER
    }

    boolean isEditor() {
        return authenticated && user.profile == UserProfile.EDITOR
    }

    String getUserName() {
        return user.username.toLowerCase()
    }

    String getFullName() {
        if (authenticated) {
            return user.fullName
        } else {
            return ""
        }
    }

    User getCurrent() {
        return user
    }

    Date getTimestamp() {
        return timestamp
    }

    UserProfile getProfile() {
        if (current != null) {
            return current.profile
        } else {
            return null
        }
    }

    void init(User user) {
        if (user != null) {
            this.user = user
            this.timestamp = new Date()

            ApplicationUserInfo.get().date = UserHolder.get().current.creationDate
            ApplicationUserInfo.get().fullName = UserHolder.get().current.fullName
            ApplicationUserInfo.get().profilePath = "/users/account"

            String image = UserHolder.get().current.userphoto
            if (image == null) {
                image = "/zkau/web/tools/images/no-user-photo.jpg"
            } else {
                image = "/resources/users/images/$image"
            }

            ApplicationUserInfo.get().image = image
        }
    }

    void update(User user) {
        if (user != null && this.user != null && this.user == user) {
            this.user = user
        }
    }

    void setCustomer(User customer) {
        this.customer = customer
        if (customer != null) {
            Containers.get().findObjects(CustomerChangeListener.class).forEach { l -> l.onCustomerChange(customer) }
        }
    }

    User getCustomer() {
        return customer
    }

    UserSiteConfig getConfig() {
        Site site = SiteContext.get().current
        if (site != null) {
            return service.getSiteConfig(site)
        }
        return new UserSiteConfig()
    }
}

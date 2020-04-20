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

/* 
 * Copyright 2016 Dynamia Soluciones IT SAS and the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tools.dynamia.cms.site.users;

import java.io.Serializable;
import java.util.Date;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import tools.dynamia.cms.site.users.domain.User;
import tools.dynamia.cms.site.users.domain.enums.UserProfile;

import tools.dynamia.integration.Containers;

/**
 *
 * @author Mario Serrano Leones
 */
@Component("userHolder")
@Scope("session")
public class UserHolder implements Serializable {

    protected User user;

    private Date timestamp;

    public static UserHolder get() {
        try {
            return Containers.get().findObject(UserHolder.class);
        } catch (Exception e) {
            UserHolder userHolder = new UserHolder();
            userHolder.init(User.createMock());
            return userHolder;
        }
    }

    public boolean isAuthenticated() {
        return user != null && user.getId() != null;
    }

    public boolean isAdmin() {
        return isAuthenticated() && user.getProfile() == UserProfile.ADMIN;
    }

    public String getUserName() {
        return user.getUsername().toLowerCase();
    }

    public String getFullName() {
        if (isAuthenticated()) {
            return user.getFullName();
        } else {
            return "";
        }
    }

    public User getCurrent() {
        return user;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    void init(User user) {
        if (user != null) {
            this.user = user;
            this.timestamp = new Date();
        }
    }

    public void update(User user) {
        if (user != null && this.user != null && this.user.equals(user)) {
            this.user = user;
        }
    }
}

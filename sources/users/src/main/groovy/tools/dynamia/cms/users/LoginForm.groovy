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
package tools.dynamia.cms.users

import tools.dynamia.cms.core.domain.Site
import tools.dynamia.domain.contraints.NotEmpty

import javax.validation.constraints.NotNull

/**
 *
 * @author Mario Serrano Leones
 */
class LoginForm implements Serializable {

    @NotEmpty
    private String username
    @NotEmpty
    private String password
    @NotNull
    private Site site

    LoginForm() {
    }

    LoginForm(Site site) {
        this.site = site
    }

    Site getSite() {
        return site
    }

    void setSite(Site site) {
        this.site = site
    }

    String getUsername() {
        return username
    }

    void setUsername(String username) {
        this.username = username
    }

    String getPassword() {
        return password
    }

    void setPassword(String password) {
        this.password = password
    }

}

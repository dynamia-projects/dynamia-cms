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
package tools.dynamia.cms.users.services

import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.users.UserForm
import tools.dynamia.cms.users.api.UserDTO
import tools.dynamia.cms.users.domain.User
import tools.dynamia.cms.users.domain.UserContactInfo
import tools.dynamia.cms.users.domain.UserSiteConfig

/**
 * @author Mario Serrano Leones
 */
interface UserService {

    User getUser(Site site, String name, String identification)

    void saveUser(UserForm user)

    User getUser(Site site, String name)

    void changePassword(UserForm form)

    void checkAdminUser(Site site)

    List<UserContactInfo> getContactInfos(User current)

    void resetPassword(Site site, String username)

    void resetPassword(User user, String newpassword, String newpassword2)

    UserSiteConfig getSiteConfig(Site site)

    void setupPassword(User user, String newPassword)

    List<User> getUserCustomers(User current)

    User getByExternalRef(Site site, String customer)

    void enableUser(User user)

    void disableUser(User user)

    User getUserByValidationKey(Site site, String key)

    UserDTO loadExternalUser(Site site, String identification)
}

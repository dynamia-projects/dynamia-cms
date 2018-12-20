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

package tools.dynamia.cms.users.services.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import tools.dynamia.cms.users.api.UserDTO
import tools.dynamia.cms.users.api.UsersDatasource
import tools.dynamia.cms.users.domain.User
import tools.dynamia.cms.users.domain.UserSiteConfig
import tools.dynamia.cms.users.services.UserService
import tools.dynamia.cms.users.services.UserSyncService
import tools.dynamia.commons.StringUtils
import tools.dynamia.domain.services.CrudService
import tools.dynamia.web.util.HttpRemotingServiceClient

@Service
class UserSyncServiceImpl implements UserSyncService {

    @Autowired
    private CrudService crudService

    @Autowired
    private UserService service

    /*
     * (non-Javadoc)
     *
     * @see
     * tools.dynamia.cms.site.users.services.impl.UserSyncService#syncUsers(
     * Site, java.util.Map)
     */

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void syncUsers(UserSiteConfig cfg, Map<String, String> params) {

        if (cfg.synchronizationEnabled && cfg.datasourceURL != null) {

            UsersDatasource datasource = HttpRemotingServiceClient.build(UsersDatasource.class)
                    .setServiceURL(cfg.datasourceURL)
                    .setUsername(cfg.datasourceUsername)
                    .setPassword(cfg.datasourcePassword)
                    .getProxy()

            List<UserDTO> remoteUsers = datasource.getUsers(params)
            if (remoteUsers != null && !remoteUsers.empty) {
                sync(cfg, remoteUsers, params)
            }
        }
    }

    private void sync(UserSiteConfig cfg, List<UserDTO> remoteUsers, Map<String, String> params) {
        for (UserDTO remoteUser : remoteUsers) {
            try {
                User localUser = getLocalUser(remoteUser)
                if (localUser == null) {
                    localUser = new User()
                    localUser.site = cfg.site
                }
                localUser.sync(remoteUser)

                if (localUser.password == null || localUser.password.empty) {
                    String password = remoteUser.externalRef
                    if (password == null || password.empty) {
                        password = StringUtils.randomString()
                    }
                    service.setupPassword(localUser, password)
                }

                if (remoteUser.relatedUser != null && !remoteUser.relatedUser.empty) {
                    User relatedUser = crudService.findSingle(User.class, "externalRef", remoteUser.relatedUser)
                    localUser.relatedUser = relatedUser
                }

                crudService.save(localUser)
            } catch (Exception e) {
                System.out.println("Error Sync: " + remoteUser.email + "  - " + e.message)
            }
        }
    }

    private User getLocalUser(UserDTO dto) {

        return crudService.findSingle(User.class, "username", dto.email)

    }
}

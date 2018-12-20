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

import org.springframework.context.ApplicationEvent
import org.springframework.context.ApplicationListener
import org.springframework.security.authentication.event.AuthenticationSuccessEvent
import tools.dynamia.cms.users.domain.User
import tools.dynamia.cms.users.domain.UserLog
import tools.dynamia.domain.services.CrudService
import tools.dynamia.integration.Containers

class SpringSecurtyApplicationListener implements ApplicationListener {


    void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof AuthenticationSuccessEvent) {
            AuthenticationSuccessEvent evt = (AuthenticationSuccessEvent) event
            fireOnUserLogin(evt)
        }
    }

    private void fireOnUserLogin(AuthenticationSuccessEvent evt) {
        Collection<LoginListener> listeners = Containers.get().findObjects(LoginListener.class)
        if (listeners != null) {
            User user = (User) evt.authentication.principal
            UserHolder.get().init(user)
            CrudService crudService = Containers.get().findObject(CrudService.class)
            if (crudService != null) {
                crudService.executeWithinTransaction {
                    crudService.create(new UserLog(user, "Login"))
                }
            }

            for (LoginListener listener : listeners) {
                listener.onLoginSuccess(user)
            }
        }
    }
}

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
package com.dynamia.cms.site.users;

import java.util.Collection;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;

import com.dynamia.cms.site.users.domain.User;

import tools.dynamia.integration.Containers;


class SpringSecurtyApplicationListener implements ApplicationListener {

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof AuthenticationSuccessEvent) {
            AuthenticationSuccessEvent evt = (AuthenticationSuccessEvent) event;
            fireOnUserLogin(evt);
        }
    }

    private void fireOnUserLogin(AuthenticationSuccessEvent evt) {
        Collection<LoginListener> listeners = Containers.get().findObjects(LoginListener.class);
        if (listeners != null) {
            User usuario = (User) evt.getAuthentication().getPrincipal();
            UserHolder.get().init(usuario);

            for (LoginListener listener : listeners) {
                listener.onLoginSuccess(usuario);
            }
        }
    }
}

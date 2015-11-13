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

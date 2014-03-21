/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.users;

import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.users.domain.User;
import com.dynamia.cms.site.users.services.UserService;
import com.dynamia.tools.domain.ValidationError;
import com.dynamia.tools.domain.contraints.EmailValidator;
import com.dynamia.tools.domain.services.CrudService;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.token.Sha512DigestUtils;
import org.springframework.stereotype.Component;

/**
 *
 * @author mario
 */

public class UsersAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserService userService;
    
    
    @Autowired
    private CrudService crudService;

    @Override
    public Authentication authenticate(Authentication a) throws AuthenticationException {

        String username = a.getName();
        String password = (String) a.getCredentials();
        Site site = null;
        if (a.getDetails() instanceof AuthenticationDetails) {
            site = ((AuthenticationDetails) a.getDetails()).getSite();
        }

        if (site == null) {
            throw new SiteNotFoundException("No site", null);
        }

        site = crudService.reload(site);
        
        if(site.getParent()!=null){
            site = site.getParent();
        }
        
        if (username.isEmpty() || password.isEmpty()) {
            throw new BadCredentialsException("Ingrese email y password para acceder");
        }

        EmailValidator emailValidator = new EmailValidator();
        if (!emailValidator.isValid(username, null)) {
            throw new BadCredentialsException("Ingrese direccion valida de email");
        }

        password = Sha512DigestUtils.shaHex(username + ":" + password);

        User user = userService.getUser(site, username);
        if (user == null) {
            throw new UserNotFoundException("Usuario [" + username + "] no encontrado en [" + site.getName() + "]");
        }

        if (!password.equals(user.getPassword())) {
            throw new InvalidPasswordException("Password invalido");
        }

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        Authentication auth = new SiteUsernamePasswordAuthenticationToken(site, user, password, authorities);
        
        return auth;

    }

    @Override
    public boolean supports(Class<?> type) {
        return true;
    }

}

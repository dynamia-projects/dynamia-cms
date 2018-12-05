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
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.token.Sha512DigestUtils
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.users.api.UserProfile
import tools.dynamia.cms.users.domain.User
import tools.dynamia.cms.users.services.UserService
import tools.dynamia.domain.contraints.EmailValidator
import tools.dynamia.domain.services.CrudService

/**
 *
 * @author Mario Serrano Leones
 */

class UsersAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserService userService

    @Autowired
    private CrudService crudService

    @Override
    Authentication authenticate(Authentication a) throws AuthenticationException {

        String username = a.name
        String password = (String) a.credentials
        Site site = null
        if (a.details instanceof AuthenticationDetails) {
            site = ((AuthenticationDetails) a.details).site
        }

        if (site == null) {
            throw new SiteNotFoundException("No site", null)
        }

        site = crudService.reload(site)

        if (site.parent != null) {
            site = site.parent
        }

        if (username.empty || password.empty) {
            throw new BadCredentialsException("Ingrese email y password para acceder")
        }

        EmailValidator emailValidator = new EmailValidator()
        if (!emailValidator.isValid(username, null)) {
            throw new BadCredentialsException("Ingrese direccion valida de email")
        }

        password = Sha512DigestUtils.shaHex(username + ":" + password)

        User user = userService.getUser(site, username)
        if (user == null) {
            throw new UserNotFoundException("Usuario [" + username + "] no encontrado en [" + site.name + "]")
        }

        if (!user.enabled) {
            throw new UserNotFoundException("El usuario [" + username + "] esta desactivado")
        }

        if (password != user.password) {
            throw new InvalidPasswordException("Password invalido")
        }

        if (user.profile != UserProfile.ADMIN && user.profile != UserProfile.EDITOR && site.offline) {
            throw new UserNotFoundException("Sitio fuera de linea: $site.offlineMessage")
        }

        Collection<? extends GrantedAuthority> authorities = user.authorities

        Authentication auth = new SiteUsernamePasswordAuthenticationToken(site, user, password, authorities)

        return auth

    }

    @Override
    boolean supports(Class<?> type) {
        return true
    }

}

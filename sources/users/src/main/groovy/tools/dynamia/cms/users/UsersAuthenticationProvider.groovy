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

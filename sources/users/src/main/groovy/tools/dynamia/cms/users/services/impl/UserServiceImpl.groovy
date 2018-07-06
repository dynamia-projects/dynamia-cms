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
package tools.dynamia.cms.users.services.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.security.core.token.Sha512DigestUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.mail.MailMessage
import tools.dynamia.cms.mail.MailServiceException
import tools.dynamia.cms.mail.domain.MailTemplate
import tools.dynamia.cms.mail.services.MailService
import tools.dynamia.cms.users.PasswordsNotMatchException
import tools.dynamia.cms.users.UserForm
import tools.dynamia.cms.users.api.UserDTO
import tools.dynamia.cms.users.api.UserProfile
import tools.dynamia.cms.users.api.UsersDatasource
import tools.dynamia.cms.users.domain.User
import tools.dynamia.cms.users.domain.UserContactInfo
import tools.dynamia.cms.users.domain.UserSiteConfig
import tools.dynamia.cms.users.services.UserService
import tools.dynamia.commons.MapBuilder
import tools.dynamia.commons.StringUtils
import tools.dynamia.domain.ValidationError
import tools.dynamia.domain.query.QueryConditions
import tools.dynamia.domain.query.QueryParameters
import tools.dynamia.domain.services.CrudService
import tools.dynamia.domain.services.ValidatorService
import tools.dynamia.web.util.HttpRemotingServiceClient

/**
 * @author Mario Serrano Leones
 */
@Service
class UserServiceImpl implements UserService {

    private static final String CACHE_NAME = "users"

    @Autowired
    private CrudService crudService

    @Autowired
    private MailService mailService

    @Autowired
    private ValidatorService validatorService

    @Override
    User getUser(Site site, String name) {
        QueryParameters qp = QueryParameters.with("site", site).add("username", QueryConditions.eq(name))
                .setAutocreateSearcheableStrings(false)
        return crudService.findSingle(User.class, qp)
    }

    @Override
    User getUser(Site site, String name, String identification) {
        QueryParameters qp = QueryParameters.with("site", site).add("username", QueryConditions.eq(name))
                .add("identification", identification)
                .setAutocreateSearcheableStrings(false)
        return crudService.findSingle(User.class, qp)
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void saveUser(UserForm form) {
        User user = form.data

        UserSiteConfig config = getSiteConfig(form.site)
        if (!config.registrationEnabled) {
            throw new ValidationError("User registration is not enabled for this site")
        }

        if (user.id == null) {
            user.site = form.site
            user.fullName
            validatorService.validate(user)

            User alreadyUser = getUser(user.site, user.username)
            if (alreadyUser != null) {
                throw new ValidationError("Ya existe un usuario con el mismo email ingresado: " + user.username)
            }

            if (!user.password.equals(form.passwordConfirm)) {
                throw new PasswordsNotMatchException(user)
            }

            user.password = Sha512DigestUtils.shaHex(user.username + ":" + user.password)

            if (config.registrationValidated) {
                user.enabled = false
            }

            if (config.requireEmailActivation) {
                user.startEmailValidation()
            }

            user = crudService.create(user)

            if (config.requireEmailActivation) {
                notifyValidationRequired(user)
            }

            notifyUserRegistration(user)

        } else {
            User actualUser = crudService.find(User.class, user.id)
            actualUser.contactInfo = user.contactInfo
            actualUser.firstName = user.firstName
            actualUser.lastName = user.lastName
            actualUser.fullName = user.fullName
            actualUser.identification = user.identification

            validatorService.validate(actualUser)
            crudService.update(actualUser)
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void changePassword(UserForm form) {

        validatorService.validate(form)

        User user = crudService.find(User.class, form.data.id)
        String currentPassword = Sha512DigestUtils.shaHex(user.username + ":" + form.currentPassword)

        if (!user.password.equals(currentPassword)) {
            throw new ValidationError("El password actual ingresado es incorrecto.")
        }

        if (!form.data.password.equals(form.passwordConfirm)) {
            throw new PasswordsNotMatchException(user)
        }

        String newPassword = form.data.password
        setupPassword(user, newPassword)
        crudService.save(user)
    }

    @Override
    void setupPassword(User user, String newPassword) {
        user.password = Sha512DigestUtils.shaHex(user.username + ":" + newPassword)
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void resetPassword(Site site, String username) {

        String templateName = "ResetPasswordTemplate"
        QueryParameters params = QueryParameters.with("name", templateName).add("site", site)

        MailTemplate mailTemplate = crudService.findSingle(MailTemplate.class, params)
        if (mailTemplate == null) {
            throw new ValidationError(
                    "En estos momentos no podemos reiniciar su password, por favor intente mas tarde")
        }

        User user = getUser(site, username)
        if (user == null) {
            throw new ValidationError("El usuario [" + username + "] no existe en este sitio web")
        }

        String newPassword = StringUtils.randomString().substring(0, 7)
        System.out.println("NEW PASSWORD " + newPassword)
        setupPassword(user, newPassword)
        crudService.save(user)

        MailMessage message = new MailMessage(mailTemplate)
        message.to = username
        message.templateModel.put("user", user)
        message.templateModel.put("newpassword", newPassword)
        message.site = site

        try {
            mailService.send(message)
        } catch (MailServiceException e) {
            throw new ValidationError(e.message, e)
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void resetPassword(User user, String newpassword, String newpassword2) {

        if (user == null) {
            throw new ValidationError("El usuario no existe en este sitio web")
        }

        if (newpassword == null || newpassword.empty || newpassword2 == null || newpassword2.empty) {
            throw new ValidationError("Ingrese nuevo password")
        }

        if (!newpassword.equals(newpassword2)) {
            throw new ValidationError("El nuevo password ingresado no coincide")
        }

        setupPassword(user, newpassword)
        crudService.save(user)

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void checkAdminUser(Site site) {
        String adminUsername = "admin@" + site.key + ".login"
        User adminUser = getUser(site, adminUsername)
        if (adminUser == null) {
            User user = new User()
            user.username = adminUsername
            user.firstName = "Admin"
            user.lastName = site.name
            user.password = Sha512DigestUtils.shaHex(user.username + ":admin" + site.key)
            user.site = site
            user.profile = UserProfile.ADMIN
            user.fullName = "Admin $site.name"
            crudService.create(user)
        } else if (adminUser.profile != UserProfile.ADMIN || adminUser.password == null) {
            adminUser.profile = UserProfile.ADMIN
            adminUser.password = Sha512DigestUtils.shaHex(adminUser.username + ":admin" + site.key)
            crudService.update(adminUser)
        }
    }

    @Override
    List<UserContactInfo> getContactInfos(User user) {
        return crudService.find(UserContactInfo.class, "user", user)
    }

    @Override
    @Transactional
    @Cacheable(value = UserServiceImpl.CACHE_NAME, key = "'cfg'+#site.key")
    UserSiteConfig getSiteConfig(Site site) {
        UserSiteConfig config = crudService.findSingle(UserSiteConfig.class, "site", site)
        if (config == null) {
            config = new UserSiteConfig()
            config.site = site
            config = crudService.create(config)
        }
        return config
    }

    @Override
    List<User> getUserCustomers(User user) {

        List<User> customers = null

        customers = crudService.find(User.class, QueryParameters.with("profile", UserProfile.USER)
                .add("relatedUser", user).add("enabled", true).orderBy("fullName"))

        if (customers == null) {
            customers = Collections.EMPTY_LIST
        }

        return customers
    }

    @Override
    User getByExternalRef(Site site, String externalRef) {
        return crudService.findSingle(User.class, QueryParameters.with("site", site)
                .add("externalRef", QueryConditions.eq(externalRef)).setAutocreateSearcheableStrings(false))
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void enableUser(User user) {
        if (user != null) {
            user.enabled = true
            crudService.update(user)

            notifyUserRegistration(user)
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void disableUser(User user) {
        if (user != null) {
            user.enabled = false
            crudService.update(user)
        }
    }

    @Override
    User getUserByValidationKey(Site site, String key) {
        return crudService.findSingle(User.class,
                QueryParameters.with("site", site).add("validationKey", QueryConditions.eq(key)))
    }

    private void notifyValidationRequired(User user) {
        UserSiteConfig config = getSiteConfig(user.site)

        if (config != null && config.emailValidationTemplate != null) {
            MailMessage msg = new MailMessage()
            msg.template = config.emailValidationTemplate
            msg.templateModel = MapBuilder.put("user", user)
            msg.to = user.username
            msg.mailAccount = config.mailAccount

            mailService.send(msg)

        }
    }

    private void notifyUserRegistration(User user) {
        UserSiteConfig config = getSiteConfig(user.site)

        if (config != null && config.registrationCompletedTemplate != null
                && config.registrationPendingTemplate != null) {
            MailMessage msg = new MailMessage()
            msg.mailAccount = config.mailAccount
            if (user.enabled) {
                msg.template = config.registrationCompletedTemplate
            } else {
                msg.template = config.registrationPendingTemplate
            }
            msg.templateModel = MapBuilder.put("user", user)

            if (user.contactInfo.email != null) {
                msg.to = user.contactInfo.email
            } else {
                msg.to = user.username
            }

            mailService.send(msg)

        }

    }

    @Override
    UserDTO loadExternalUser(Site site, String identification) {
        UserDTO user = null
        UserSiteConfig config = getSiteConfig(site)
        if (config != null && config.datasourceURL != null && !config.datasourceURL.empty) {
            UsersDatasource datasource = HttpRemotingServiceClient.build(UsersDatasource.class)
                    .setServiceURL(config.datasourceURL)
                    .getProxy()


            if (datasource != null) {
                user = datasource.getUser(identification)
            }
        }

        return user
    }

}

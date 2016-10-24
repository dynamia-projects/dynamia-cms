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
package tools.dynamia.cms.site.users.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.token.Sha512DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import tools.dynamia.cms.site.core.domain.Site;
import tools.dynamia.cms.site.mail.MailMessage;
import tools.dynamia.cms.site.mail.domain.MailTemplate;
import tools.dynamia.cms.site.mail.services.MailService;
import tools.dynamia.cms.site.users.PasswordsNotMatchException;
import tools.dynamia.cms.site.users.UserForm;
import tools.dynamia.cms.site.users.api.UserProfile;
import tools.dynamia.cms.site.users.domain.User;
import tools.dynamia.cms.site.users.domain.UserContactInfo;
import tools.dynamia.cms.site.users.domain.UserSiteConfig;
import tools.dynamia.cms.site.users.services.UserService;

import tools.dynamia.commons.StringUtils;
import tools.dynamia.domain.ValidationError;
import tools.dynamia.domain.query.QueryParameters;
import tools.dynamia.domain.services.CrudService;
import tools.dynamia.domain.services.ValidatorService;

/**
 *
 * @author Mario Serrano Leones
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private CrudService crudService;

	@Autowired
	private MailService mailService;

	@Autowired
	private ValidatorService validatorService;

	@Override
	public User getUser(Site site, String name) {
		QueryParameters qp = QueryParameters.with("site", site).add("username", name);
		return crudService.findSingle(User.class, qp);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveUser(UserForm form) {
		User user = form.getData();

		if (user.getId() == null) {
			user.setSite(form.getSite());
			user.getFullName();
			validatorService.validate(user);

			User alreadyUser = getUser(user.getSite(), user.getUsername());
			if (alreadyUser != null) {
				throw new ValidationError("Ya existe un usuario con el mismo email ingresado: " + user.getUsername());
			}

			if (!user.getPassword().equals(form.getPasswordConfirm())) {
				throw new PasswordsNotMatchException(user);
			}

			user.setPassword(Sha512DigestUtils.shaHex(user.getUsername() + ":" + user.getPassword()));

			crudService.create(user);

		} else {
			User actualUser = crudService.find(User.class, user.getId());
			actualUser.setContactInfo(user.getContactInfo());
			actualUser.setFirstName(user.getFirstName());
			actualUser.setLastName(user.getLastName());
			actualUser.setFullName(user.getFullName());
			actualUser.setIdentification(user.getIdentification());

			validatorService.validate(actualUser);
			crudService.update(actualUser);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void changePassword(UserForm form) {

		validatorService.validate(form);

		User user = crudService.find(User.class, form.getData().getId());
		String currentPassword = Sha512DigestUtils.shaHex(user.getUsername() + ":" + form.getCurrentPassword());

		if (!user.getPassword().equals(currentPassword)) {
			throw new ValidationError("El password actual ingresado es incorrecto.");
		}

		if (!form.getData().getPassword().equals(form.getPasswordConfirm())) {
			throw new PasswordsNotMatchException(user);
		}

		String newPassword = form.getData().getPassword();
		setupPassword(user, newPassword);
		crudService.save(user);
	}

	@Override
	public void setupPassword(User user, String newPassword) {
		user.setPassword(Sha512DigestUtils.shaHex(user.getUsername() + ":" + newPassword));
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void resetPassword(Site site, String username) {

		String templateName = "ResetPasswordTemplate";
		QueryParameters params = QueryParameters.with("name", templateName).add("site", site);

		MailTemplate mailTemplate = crudService.findSingle(MailTemplate.class, params);
		if (mailTemplate == null) {
			throw new ValidationError(
					"En estos momentos no podemos reiniciar su password, por favor intente mas tarde");
		}

		User user = getUser(site, username);
		if (user == null) {
			throw new ValidationError("El usuario [" + username + "] no existe en este sitio web");
		}

		String newPassword = StringUtils.randomString().substring(0, 7);
		System.out.println("NEW PASSWORD " + newPassword);
		setupPassword(user, newPassword);
		crudService.save(user);

		MailMessage message = new MailMessage(mailTemplate);
		message.setTo(username);
		message.getTemplateModel().put("user", user);
		message.getTemplateModel().put("newpassword", newPassword);

		mailService.send(message);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void resetPassword(User user, String newpassword, String newpassword2) {

		if (user == null) {
			throw new ValidationError("El usuario no existe en este sitio web");
		}

		if (newpassword == null || newpassword.isEmpty() || newpassword2 == null || newpassword2.isEmpty()) {
			throw new ValidationError("Ingrese nuevo password");
		}

		if (!newpassword.equals(newpassword2)) {
			throw new ValidationError("El nuevo password ingresado no coincide");
		}

		setupPassword(user, newpassword);
		crudService.save(user);

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void checkAdminUser(Site site) {
		String adminUsername = "admin@" + site.getKey() + ".login";
		User adminUser = getUser(site, adminUsername);
		if (adminUser == null) {
			User user = new User();
			user.setUsername(adminUsername);
			user.setFirstName("Admin");
			user.setLastName(site.getName());
			user.setPassword(Sha512DigestUtils.shaHex(user.getUsername() + ":admin" + site.getKey()));
			user.setSite(site);
			user.setProfile(UserProfile.ADMIN);
			crudService.create(user);
		} else if (adminUser.getProfile() != UserProfile.ADMIN) {
			adminUser.setProfile(UserProfile.ADMIN);
			crudService.update(adminUser);
		}
	}

	@Override
	public List<UserContactInfo> getContactInfos(User user) {
		return crudService.find(UserContactInfo.class, "user", user);
	}

	@Override
	@Transactional
	public UserSiteConfig getSiteConfig(Site site) {
		UserSiteConfig config = crudService.findSingle(UserSiteConfig.class, "site", site);
		if (config == null) {
			config = new UserSiteConfig();
			config.setSite(site);
			config = crudService.create(config);
		}
		return config;
	}

	@Override
	public List<User> getUserCustomers(User user) {

		List<User> customers = null;

		customers = crudService.find(User.class, QueryParameters.with("profile", UserProfile.USER)
				.add("relatedUser", user).add("enabled", true).orderBy("fullName"));

		if (customers == null || customers.isEmpty()) {
			customers = crudService.find(User.class,
					QueryParameters.with("profile", UserProfile.USER).add("enabled", true).orderBy("fullName"));
		}

		return customers;
	}

}

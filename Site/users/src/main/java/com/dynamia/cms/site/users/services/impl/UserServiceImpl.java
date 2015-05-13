/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.users.services.impl;

import java.util.List;

import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.users.PasswordsNotMatchException;
import com.dynamia.cms.site.users.UserForm;
import com.dynamia.cms.site.users.domain.Profile;
import com.dynamia.cms.site.users.domain.User;
import com.dynamia.cms.site.users.domain.UserContactInfo;
import com.dynamia.cms.site.users.domain.UserProfile;
import com.dynamia.cms.site.users.services.UserService;
import com.dynamia.tools.domain.ValidationError;
import com.dynamia.tools.domain.query.QueryParameters;
import com.dynamia.tools.domain.services.CrudService;
import com.dynamia.tools.domain.services.ValidatorService;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.token.Sha512DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author mario_2
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private CrudService crudService;

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

			UserProfile basicProfile = new UserProfile(getDefaultProfile(user.getSite()), user);
			basicProfile.setSite(user.getSite());
			crudService.create(basicProfile);
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

		user.setPassword(Sha512DigestUtils.shaHex(user.getUsername() + ":" + form.getData().getPassword()));
		crudService.save(user);

	}

	private Profile getDefaultProfile(Site site) {
		QueryParameters qp = QueryParameters.with("site", site).add("internalName", "ROLE_USER");
		Profile roleUser = crudService.findSingle(Profile.class, qp);
		if (roleUser == null) {
			roleUser = new Profile();
			roleUser.setName("User");
			roleUser.setSite(site);
			roleUser.setEditable(false);
			roleUser = crudService.create(roleUser);
		}

		return roleUser;
	}

	private Profile getAdminProfile(Site site) {
		QueryParameters qp = QueryParameters.with("site", site).add("internalName", "ROLE_ADMIN");
		Profile roleAdmin = crudService.findSingle(Profile.class, qp);
		if (roleAdmin == null) {
			roleAdmin = new Profile();
			roleAdmin.setName("Admin");
			roleAdmin.setSite(site);
			roleAdmin.setEditable(false);
			roleAdmin = crudService.create(roleAdmin);
		}

		return roleAdmin;
	}

	private Profile getEditorProfile(Site site) {
		QueryParameters qp = QueryParameters.with("site", site).add("internalName", "ROLE_EDITOR");
		Profile roleAdmin = crudService.findSingle(Profile.class, qp);
		if (roleAdmin == null) {
			roleAdmin = new Profile();
			roleAdmin.setName("Editor");
			roleAdmin.setSite(site);
			roleAdmin.setEditable(false);
			roleAdmin = crudService.create(roleAdmin);
		}

		return roleAdmin;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void checkAdminUser(Site site) {
		String adminUsername = "admin@" + site.getKey() + ".login";
		User adminUser = getUser(site, adminUsername);
		if (adminUser == null) {
			User user = new User();
			user.setUsername(adminUsername);
			user.setFullName("Administrator " + site.getName());
			user.setPassword(Sha512DigestUtils.shaHex(user.getUsername() + ":admin" + site.getKey()));
			user.setSite(site);
			crudService.create(user);

			UserProfile basicProfile = new UserProfile(getDefaultProfile(site), user);
			basicProfile.setSite(site);
			crudService.create(basicProfile);

			UserProfile adminProfile = new UserProfile(getAdminProfile(site), user);
			adminProfile.setSite(site);
			crudService.create(adminProfile);

			UserProfile editorProfile = new UserProfile(getEditorProfile(site), user);
			editorProfile.setSite(site);
			crudService.create(editorProfile);
		} else {

			if (!adminUser.hasProfile("ROLE_ADMIN")) {
				UserProfile adminProfile = new UserProfile(getAdminProfile(site), adminUser);
				adminProfile.setSite(site);
				crudService.create(adminProfile);
			}

		}
	}

	@Override
	public List<UserContactInfo> getContactInfos(User user) {
		return crudService.find(UserContactInfo.class, "user", user);
	}

}

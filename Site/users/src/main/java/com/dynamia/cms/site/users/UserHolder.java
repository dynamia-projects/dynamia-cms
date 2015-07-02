/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.users;

import com.dynamia.cms.site.users.domain.User;

import com.dynamia.tools.integration.Containers;
import java.io.Serializable;
import java.util.Date;

import org.springframework.context.annotation.Scope;

import org.springframework.stereotype.Component;

/**
 *
 * @author mario
 */
@Component("userHolder")
@Scope("session")
public class UserHolder implements Serializable {

	protected User user;

	private Date timestamp;

	public static UserHolder get() {
		try {
			return Containers.get().findObject(UserHolder.class);
		} catch (Exception e) {
			UserHolder userHolder = new UserHolder();
			userHolder.init(User.createMock());
			return userHolder;
		}
	}

	public boolean isAuthenticated() {
		return user != null && user.getId() != null;
	}

	public boolean isSuperadmin() {
		return isAuthenticated() && user.isSuperadmin();
	}

	public String getUserName() {
		return user.getUsername().toLowerCase();
	}

	public String getFullName() {
		if (isAuthenticated()) {
			return user.getFullName();
		} else {
			return "";
		}
	}

	public boolean hasProfile(String profileName) {
		return user.getProfile(profileName) != null;
	}

	public User getCurrent() {
		return user;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	void init(User user) {
		if (user != null) {
			this.user = user;
			this.timestamp = new Date();
		}
	}

	public void update(User user) {
		if (user != null && this.user != null && this.user.equals(user)) {
			this.user = user;
		}
	}
}

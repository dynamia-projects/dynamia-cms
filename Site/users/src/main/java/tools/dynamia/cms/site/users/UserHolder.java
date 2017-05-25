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
package tools.dynamia.cms.site.users;

import java.io.Serializable;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import tools.dynamia.app.ApplicationUserInfo;
import tools.dynamia.cms.site.core.SiteContext;
import tools.dynamia.cms.site.core.domain.Site;
import tools.dynamia.cms.site.users.api.UserProfile;
import tools.dynamia.cms.site.users.domain.User;
import tools.dynamia.cms.site.users.domain.UserSiteConfig;
import tools.dynamia.cms.site.users.ext.CustomerChangeListener;
import tools.dynamia.cms.site.users.services.UserService;
import tools.dynamia.integration.Containers;
import tools.dynamia.web.util.HttpUtils;

/**
 *
 * @author Mario Serrano Leones
 */
@Component("userHolder")
@Scope("session")
public class UserHolder implements Serializable {

	protected User user;
	protected User customer;

	private Date timestamp;

	@Autowired
	private UserService service;

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

	public boolean isAdmin() {
		return isAuthenticated() && user.getProfile() == UserProfile.ADMIN;
	}

	public boolean isSeller() {
		return isAuthenticated() && user.getProfile() == UserProfile.SELLER;
	}

	public boolean isEditor() {
		return isAuthenticated() && user.getProfile() == UserProfile.EDITOR;
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

	public User getCurrent() {
		return user;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public UserProfile getProfile() {
		if (getCurrent() != null) {
			return getCurrent().getProfile();
		} else {
			return null;
		}
	}

	void init(User user) {
		if (user != null) {
			this.user = user;
			this.timestamp = new Date();

			ApplicationUserInfo.get().setDate(UserHolder.get().getCurrent().getCreationDate());
			ApplicationUserInfo.get().setFullName(UserHolder.get().getCurrent().getFullName());
			String image = UserHolder.get().getCurrent().getUserphoto();
			if (image == null) {
				image = HttpUtils.getServerPath() + "/images/user.png";

			}

			ApplicationUserInfo.get().setImage(image);
		}
	}

	public void update(User user) {
		if (user != null && this.user != null && this.user.equals(user)) {
			this.user = user;
		}
	}

	public void setCustomer(User customer) {
		this.customer = customer;
		if (customer != null) {
			Containers.get().findObjects(CustomerChangeListener.class).forEach(l -> l.onCustomerChange(customer));
		}
	}

	public User getCustomer() {
		return customer;
	}

	public UserSiteConfig getConfig() {
		Site site = SiteContext.get().getCurrent();
		if (site != null) {
			return service.getSiteConfig(site);
		}
		return new UserSiteConfig();
	}
}

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
package tools.dynamia.cms.site.users.services;

import java.util.List;

import tools.dynamia.cms.site.core.domain.Site;
import tools.dynamia.cms.site.users.UserForm;
import tools.dynamia.cms.site.users.domain.User;
import tools.dynamia.cms.site.users.domain.UserContactInfo;
import tools.dynamia.cms.site.users.domain.UserSiteConfig;

/**
 *
 * @author Mario Serrano Leones
 */
public interface UserService {

	public void saveUser(UserForm user);

	public User getUser(Site site, String name);

	public void changePassword(UserForm form);

	void checkAdminUser(Site site);

	public List<UserContactInfo> getContactInfos(User current);

	public void resetPassword(Site site, String username);

	public void resetPassword(User user, String newpassword, String newpassword2);

	UserSiteConfig getSiteConfig(Site site);

	void setupPassword(User user, String newPassword);

	public List<User> getUserCustomers(User current);

	public User getByExternalRef(Site site, String customer);

}

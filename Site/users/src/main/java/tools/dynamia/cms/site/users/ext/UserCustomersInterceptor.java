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
package tools.dynamia.cms.site.users.ext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import tools.dynamia.cms.site.core.api.CMSExtension;
import tools.dynamia.cms.site.core.api.SiteRequestInterceptorAdapter;
import tools.dynamia.cms.site.core.domain.Site;
import tools.dynamia.cms.site.users.UserHolder;
import tools.dynamia.cms.site.users.services.UserService;

/**
 *
 * @author Mario Serrano Leones
 */
@CMSExtension
public class UserCustomersInterceptor extends SiteRequestInterceptorAdapter {

	@Autowired
	private UserService service;

	@Override
	protected void afterRequest(Site site, ModelAndView modelAndView) {
		if (UserHolder.get().isSeller() && modelAndView.getModelMap().get("userCustomers") == null) {
			modelAndView.addObject("userCustomers", service.getUserCustomers(UserHolder.get().getCurrent()));
		}
	}

}
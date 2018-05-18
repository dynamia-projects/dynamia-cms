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
package tools.dynamia.cms.users.ext

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.servlet.ModelAndView
import tools.dynamia.cms.core.api.CMSExtension
import tools.dynamia.cms.core.api.SiteRequestInterceptorAdapter
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.users.UserHolder
import tools.dynamia.cms.users.domain.User
import tools.dynamia.cms.users.actions.UserMenuAction
import tools.dynamia.cms.users.actions.UserMenuActionEnableable

/**
 *
 * @author Mario Serrano Leones
 */
@CMSExtension
class UserMenuInterceptor extends SiteRequestInterceptorAdapter {

	@Autowired
	private List<UserMenuAction> allActions

    @Override
	protected void afterRequest(Site site, ModelAndView modelAndView) {
		List<UserMenuAction> orderedActions = new ArrayList<>()

        if (UserHolder.get().authenticated) {
			for (UserMenuAction action : allActions) {
				if (action instanceof UserMenuActionEnableable) {
					User currentUser = UserHolder.get().current
                    if (((UserMenuActionEnableable) action).isEnabled(currentUser)) {
						orderedActions.add(action)
                    }
				} else {
					orderedActions.add(action)

                }
			}

			Collections.sort(orderedActions, new Comparator<UserMenuAction>() {

				@Override
                int compare(UserMenuAction t, UserMenuAction t1) {
					Integer ua = t.order
                    Integer ub = t1.order
                    return ua.compareTo(ub)
                }
			})

            modelAndView.addObject("site", UserHolder.get().current.site)
        }

		modelAndView.addObject("userMenuActions", orderedActions)

    }

}

/*
 * Copyright (C) 2009 - 2019 Dynamia Soluciones IT S.A.S - NIT 900302344-1
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
	void afterRequest(Site site, ModelAndView modelAndView) {
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

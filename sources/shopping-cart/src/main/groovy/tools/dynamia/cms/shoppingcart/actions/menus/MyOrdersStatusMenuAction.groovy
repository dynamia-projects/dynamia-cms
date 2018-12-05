/*
 *  Copyright (c) 2018 Dynamia Soluciones IT SAS and the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package tools.dynamia.cms.shoppingcart.actions.menus

import tools.dynamia.cms.core.api.CMSAction
import tools.dynamia.cms.users.actions.UserMenuActionEnableable
import tools.dynamia.cms.users.domain.User

@CMSAction
class MyOrdersStatusMenuAction implements UserMenuActionEnableable {

	@Override
    String getLabel() {
		return "Estado de cuenta"
    }

	@Override
    String getDescription() {
		return "Muestra el estado actual de la cartera"
    }

	@Override
    String getIcon() {
		// TODO Auto-generated method stub
		return null
    }

	@Override
    int getOrder() {
		return 5
    }

	@Override
    String action() {
		return "shoppingcart/orders/status"
    }

	@Override
    boolean isEnabled(User currentUser) {
		return currentUser.site.corporateSite
    }

}

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
package tools.dynamia.cms.menus.services

import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.menus.MenuContext

/**
 *
 * @author Mario Serrano Leones
 */
interface MenuService {

	tools.dynamia.cms.menus.domain.Menu getMainMenu(Site site)

    MenuContext setupMenuItem(tools.dynamia.cms.menus.domain.MenuItem menuItem)

    tools.dynamia.cms.menus.domain.Menu getMenu(Site site, String alias)

    tools.dynamia.cms.menus.domain.Menu getMenu(Site site, Long id)

    tools.dynamia.cms.menus.api.MenuItemType getMenuItemType(tools.dynamia.cms.menus.domain.MenuItem menuItem)

    List<tools.dynamia.cms.menus.domain.MenuItem> getItems(tools.dynamia.cms.menus.domain.Menu menu)

}

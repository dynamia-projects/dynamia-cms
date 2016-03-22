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
package com.dynamia.cms.site.menus.services;

import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.menus.MenuContext;
import com.dynamia.cms.site.menus.api.MenuItemType;
import com.dynamia.cms.site.menus.domain.Menu;
import com.dynamia.cms.site.menus.domain.MenuItem;

/**
 *
 * @author Mario Serrano Leones
 */
public interface MenuService {

	Menu getMainMenu(Site site);

	MenuContext setupMenuItem(MenuItem menuItem);

	public Menu getMenu(Site site, String alias);

	public Menu getMenu(Site site, Long id);

	MenuItemType getMenuItemType(MenuItem menuItem);

}

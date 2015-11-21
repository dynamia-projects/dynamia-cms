/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dynamia.cms.site.menus.services;

import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.menus.MenuContext;
import com.dynamia.cms.site.menus.api.MenuItemType;
import com.dynamia.cms.site.menus.domain.Menu;
import com.dynamia.cms.site.menus.domain.MenuItem;

/**
 *
 * @author mario
 */
public interface MenuService {

	Menu getMainMenu(Site site);

	MenuContext setupMenuItem(MenuItem menuItem);

	public Menu getMenu(Site site, String alias);

	public Menu getMenu(Site site, Long id);

	MenuItemType getMenuItemType(MenuItem menuItem);

}

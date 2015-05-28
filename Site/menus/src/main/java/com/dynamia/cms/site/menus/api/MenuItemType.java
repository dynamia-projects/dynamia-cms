/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dynamia.cms.site.menus.api;

import com.dynamia.cms.site.core.api.TypeExtension;
import com.dynamia.cms.site.menus.MenuContext;

/**
 *
 * @author mario
 */
public interface MenuItemType extends TypeExtension {

	public void setupMenuItem(MenuContext context);

}

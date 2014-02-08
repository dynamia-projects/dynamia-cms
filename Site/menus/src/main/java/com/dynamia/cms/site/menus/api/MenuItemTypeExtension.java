/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dynamia.cms.site.menus.api;

import com.dynamia.cms.site.menus.MenuContext;

/**
 *
 * @author mario
 */
public interface MenuItemTypeExtension {
    
    public String getId();
    
    public String getName();
    
    public String getDescription();
    
    public String getHref(MenuContext context);
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.menus;

import com.dynamia.cms.site.menus.domain.Menu;
import com.dynamia.cms.site.menus.domain.MenuItem;
import java.io.Serializable;

/**
 *
 * @author mario
 */
public class MenuContext implements Serializable{

    private MenuItem menuItem;

    private Menu parent;

    public MenuContext(MenuItem menuItem, Menu parent) {
        this.menuItem = menuItem;
        this.parent = parent;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public Menu getParent() {
        return parent;
    }

}

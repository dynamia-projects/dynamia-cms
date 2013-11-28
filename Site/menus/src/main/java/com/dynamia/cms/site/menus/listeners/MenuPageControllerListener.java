/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dynamia.cms.site.menus.listeners;

import com.dynamia.cms.site.menus.domain.Menu;
import com.dynamia.cms.site.menus.domain.MenuItem;
import com.dynamia.cms.site.menus.services.MenuService;
import com.dynamia.cms.site.pages.PageControllerListener;
import com.dynamia.cms.site.pages.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author mario
 */
@Component
public class MenuPageControllerListener implements PageControllerListener{

    @Autowired
    private MenuService service;
    
    @Override
    public void onPageLoaded(Page page, ModelAndView mv) {
        
        Menu menu = service.getMainMenu(page.getSite());
        mv.addObject("menus", menu.getItems());
        
        for (MenuItem menuItem : menu.getItems()) {
            if(page.equals(menuItem.getPage())){
                mv.addObject("activeMenu",menuItem);
            }
        }
    }
    
}

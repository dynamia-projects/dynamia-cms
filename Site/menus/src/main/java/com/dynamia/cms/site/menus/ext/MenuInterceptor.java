/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.menus.ext;

import com.dynamia.cms.site.core.api.CMSExtension;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.core.api.SiteRequestInterceptorAdapter;
import com.dynamia.cms.site.menus.domain.Menu;
import com.dynamia.cms.site.menus.domain.MenuItem;
import com.dynamia.cms.site.menus.services.MenuService;
import com.dynamia.cms.site.pages.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author mario
 */
@CMSExtension
public class MenuInterceptor extends SiteRequestInterceptorAdapter {

    @Autowired
    private MenuService service;

    @Override
    protected void afterRequest(Site site, ModelAndView mv) {
        Menu menu = service.getMainMenu(site);
        if (menu != null) {
            mv.addObject("menu", menu);
            mv.addObject("menuitems", menu.getItems());

            Page page = (Page) mv.getModel().get("page");
            if (page != null) {
                for (MenuItem menuItem : menu.getItems()) {
                    /* if (page.equals(menuItem.getPage())) {
                     mv.addObject("activeMenu", menuItem);
                     }
                     */
                }

            }
        }
    }

}

package com.dynamia.cms.site.menus.ext;

import java.util.ArrayList;
import java.util.List;

import com.dynamia.cms.site.core.api.CMSExtension;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.core.api.SiteRequestInterceptorAdapter;
import com.dynamia.cms.site.menus.MenuContext;
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
			List<MenuItem> items = new ArrayList<>(menu.getItems());
			for (MenuItem menuItem : items) {
				service.setupMenuItem(menuItem);
			}

			mv.addObject("menu", menu);
			mv.addObject("menuitems", items);
		}
	}

}

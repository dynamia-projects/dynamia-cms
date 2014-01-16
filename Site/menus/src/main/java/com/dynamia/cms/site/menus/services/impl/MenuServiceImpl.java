/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.menus.services.impl;

import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.menus.domain.Menu;
import com.dynamia.cms.site.menus.services.MenuService;
import com.dynamia.tools.domain.query.QueryParameters;
import com.dynamia.tools.domain.services.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 *
 * @author mario
 */
@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private CrudService crudService;

    @Override
    @Cacheable(value = "menus", key = "#site.key")
    public Menu getMainMenu(Site site) {
        QueryParameters qp = QueryParameters.with("site", site);
        return crudService.findSingle(Menu.class, qp);
    }
}

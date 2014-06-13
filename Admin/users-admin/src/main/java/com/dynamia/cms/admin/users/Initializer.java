/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.admin.users;

import com.dynamia.cms.site.core.services.SiteService;
import com.dynamia.cms.site.users.services.UserService;
import com.dynamia.tools.integration.sterotypes.Component;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

/**
 *
 * @author mario_2
 */
@Component
@Scope("session")
public class Initializer {

    @Autowired
    private UserService userService;
    @Autowired
    private SiteService siteService;

    public void init(HttpServletRequest request) {

        if (userService != null && request != null && siteService != null) {
            userService.checkAdminUser(siteService.getSite(request));
        }
    }

}

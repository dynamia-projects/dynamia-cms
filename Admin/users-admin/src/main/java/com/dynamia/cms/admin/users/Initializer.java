/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.admin.users;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.core.services.SiteService;
import com.dynamia.cms.site.users.services.UserService;

import tools.dynamia.integration.sterotypes.Component;

/**
 *
 * @author Mario Serrano Leones
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
			Site site = siteService.getSite(request);
			if (site != null) {
				userService.checkAdminUser(site);
			}
		}
	}

}

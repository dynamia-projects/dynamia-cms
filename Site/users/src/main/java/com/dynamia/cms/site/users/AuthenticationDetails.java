/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.users;

import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.core.services.SiteService;
import com.dynamia.tools.integration.Containers;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

/**
 *
 * @author mario_2
 */
public class AuthenticationDetails extends WebAuthenticationDetails {

    private Site site;

    public AuthenticationDetails(HttpServletRequest request) {
        super(request);
        SiteService service = Containers.get().findObject(SiteService.class);
        this.site = service.getSite(request);
        if (this.site == null) {
            this.site = service.getMainSite();
        }
    }

    public Site getSite() {
        return site;
    }

}

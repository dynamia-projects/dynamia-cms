/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.core.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.core.services.SiteService;

/**
 *
 * @author mario
 */
@Controller
public class SiteController {

    @Autowired
    private SiteService service;

    @ResponseBody
    @RequestMapping("/google{siteid}.html")
    public String googleSiteVerification(@PathVariable String siteid, HttpServletRequest request) {
        Site site = service.getSite(request);
        if (site == null) {
            site = service.getMainSite();
        }

        String fullId = "google" + siteid + ".html";
        if (!fullId.equals(site.getGoogleSiteVerification())) {
            fullId = "unknown";
        }
        return "google-site-verification: " + fullId;

    }
}

/* 
 * Copyright 2016 Dynamia Soluciones IT SAS and the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
 * @author Mario Serrano Leones
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

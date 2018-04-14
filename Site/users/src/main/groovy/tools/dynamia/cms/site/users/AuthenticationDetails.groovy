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
package tools.dynamia.cms.site.users

import org.springframework.security.web.authentication.WebAuthenticationDetails
import tools.dynamia.cms.site.core.domain.Site
import tools.dynamia.cms.site.core.services.SiteService
import tools.dynamia.integration.Containers

import javax.servlet.http.HttpServletRequest

/**
 *
 * @author Mario Serrano Leones
 */
class AuthenticationDetails extends WebAuthenticationDetails {

    private Site site

    AuthenticationDetails(HttpServletRequest request) {
        super(request)
        SiteService service = Containers.get().findObject(SiteService.class)
        this.site = service.getSite(request)
        if (this.site == null) {
            this.site = service.getMainSite()
        }
    }

    Site getSite() {
        return site
    }

}

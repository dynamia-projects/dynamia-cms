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
package tools.dynamia.cms.site.users;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import tools.dynamia.cms.site.core.domain.Site;

/**
 *
 * @author Mario Serrano Leones
 */
public class SiteUsernamePasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private Site site;

    public SiteUsernamePasswordAuthenticationToken(Site site, UsernamePasswordAuthenticationToken originalToken) {
        super(originalToken.getPrincipal(), originalToken.getCredentials(), originalToken.getAuthorities());
        this.site = site;
    }

    public SiteUsernamePasswordAuthenticationToken(Site site, Object principal, Object credentials) {
        super(principal, credentials);
        this.site = site;
    }

    public SiteUsernamePasswordAuthenticationToken(Site site, Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
        this.site = site;
    }

    public Site getSite() {
        return site;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.users;

import com.dynamia.cms.site.core.domain.Site;
import java.util.Collection;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 *
 * @author mario_2
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

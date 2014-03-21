/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.core.api;

import com.dynamia.cms.site.core.domain.Site;

/**
 *
 * @author mario_2
 */
public interface SiteAware {

    public Site getSite();

    public void setSite(Site site);

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.cms.site.core;

import tools.dynamia.cms.site.core.domain.Site;

/**
 *
 * @author mario
 */
public interface SiteInitializer {

    public void init(Site site);

    public void postInit(Site site);
}

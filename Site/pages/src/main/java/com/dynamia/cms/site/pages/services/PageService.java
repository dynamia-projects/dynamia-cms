/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.pages.services;

import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.pages.domain.Page;

/**
 *
 * @author mario
 */
public interface PageService {

    Page loadPage(Site site, String alias);

    Page loadPageByUUID(String uuid);

}

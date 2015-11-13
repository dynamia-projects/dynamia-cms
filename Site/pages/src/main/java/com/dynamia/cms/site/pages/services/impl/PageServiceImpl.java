/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.pages.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.pages.domain.Page;
import com.dynamia.cms.site.pages.services.PageService;

import tools.dynamia.domain.query.QueryParameters;
import tools.dynamia.domain.services.CrudService;

/**
 *
 * @author mario
 */
@Service
public class PageServiceImpl implements PageService {

    @Autowired
    private CrudService crudService;

    @Override
    @Cacheable("pages")
    public Page loadPage(Site site, String alias) {
        QueryParameters qp = QueryParameters.with("alias", alias);
        qp.add("site", site);
        return crudService.findSingle(Page.class, qp);
    }

    @Override
    @Cacheable("pages")
    public Page loadPageByUUID(String uuid) {
        return crudService.findSingle(Page.class, "uuid", uuid);
    }
}

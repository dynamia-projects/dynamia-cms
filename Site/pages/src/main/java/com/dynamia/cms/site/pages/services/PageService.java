/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.pages.services;

import java.util.List;

import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.pages.domain.Page;
import com.dynamia.cms.site.pages.domain.PageCategory;

/**
 *
 * @author mario
 */
public interface PageService {

	Page loadPage(Site site, String alias);

	Page loadPageByUUID(String uuid);

	List<Page> getPages(Site site);

	int publishPages();

	int unpublishPages();

	List<Page> getPages(Site site, PageCategory category);

	List<Page> getPagesWithoutCategory(Site site);

	List<PageCategory> getPagesCategories(Site site);

}

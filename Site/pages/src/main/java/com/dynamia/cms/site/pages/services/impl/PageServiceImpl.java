/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.pages.services.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.pages.domain.Page;
import com.dynamia.cms.site.pages.domain.PageCategory;
import com.dynamia.cms.site.pages.services.PageService;

import tools.dynamia.domain.query.QueryCondition;
import tools.dynamia.domain.query.QueryConditions;
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
		qp.add("published", true);
		return crudService.findSingle(Page.class, qp);
	}

	@Override
	@Cacheable("pages")
	public Page loadPageByUUID(String uuid) {
		return crudService.findSingle(Page.class, "uuid", uuid);
	}

	@Override
	@Cacheable(value = "pages", key = "'pages'+#site.key")
	public List<Page> getPages(Site site) {
		return crudService.find(Page.class, QueryParameters.with("site", site)
				.add("published", true)
				.orderBy("title", true));
	}

	@Override
	@Cacheable(value = "pages", key = "'pagesWC'+#site.key")
	public List<Page> getPagesWithoutCategory(Site site) {
		return crudService.find(Page.class, QueryParameters.with("site", site)
				.add("published", true)
				.add("category", QueryConditions.isNull())
				.orderBy("title", true));
	}

	@Override
	@Cacheable(value = "pages", key = "'pages'+#site.key+'cat'+#category.id")
	public List<Page> getPages(Site site, PageCategory category) {
		return crudService.find(Page.class, QueryParameters.with("site", site)
				.add("category", category)
				.add("published", true)
				.orderBy("title", true));
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public int unpublishPages() {
		return crudService.batchUpdate(Page.class, "published", false,
				QueryParameters.with("neverExpire", false)
						.add("published", true)
						.add("endDate", QueryConditions.geqt(new Date())));
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public int publishPages() {
		return crudService.batchUpdate(Page.class, "published", true,
				QueryParameters.with("published", false)
						.add("startDate", QueryConditions.geqt(new Date()))
						.add("endDate", QueryConditions.lt(new Date())));
	}

	@Override
	public List<PageCategory> getPagesCategories(Site site) {
		return crudService.find(PageCategory.class, QueryParameters.with("site", site).orderBy("name", true));

	}
}

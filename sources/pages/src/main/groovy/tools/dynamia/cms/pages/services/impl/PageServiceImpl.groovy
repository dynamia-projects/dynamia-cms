/*
 * Copyright (C) 2009 - 2019 Dynamia Soluciones IT S.A.S - NIT 900302344-1
 * Colombia - South America
 * All Rights Reserved.
 *
 * DynamiaCMS is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License (LGPL v3) as
 * published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DynamiaCMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with DynamiaCMS.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
package tools.dynamia.cms.pages.services.impl

import org.jsoup.Jsoup
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import tools.dynamia.cms.core.StringParser
import tools.dynamia.cms.core.StringParsers
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.pages.domain.Page
import tools.dynamia.cms.pages.domain.PageCategory
import tools.dynamia.cms.pages.services.PageService
import tools.dynamia.domain.query.QueryConditions
import tools.dynamia.domain.query.QueryParameters
import tools.dynamia.domain.services.CrudService

/**
 *
 * @author Mario Serrano Leones
 */
@Service
class PageServiceImpl implements PageService {

	@Autowired
	private CrudService crudService

	private static final String CACHE_NAME = "pages"

	@Override
	@Cacheable(PageServiceImpl.CACHE_NAME)
	Page loadPage(Site site, String alias) {
		QueryParameters qp = QueryParameters.with("alias", QueryConditions.eq(alias))
		qp.add("site", site)
		qp.add("published", true)

		return crudService.findSingle(Page.class, qp)

	}

	@Override
	@Cacheable(PageServiceImpl.CACHE_NAME)
	Page loadPageByUUID(String uuid) {
		return crudService.findSingle(Page.class, "uuid", uuid)
	}

	@Override
	@Cacheable(value = PageServiceImpl.CACHE_NAME, key = "'pages'+#site.key")
	List<Page> getPages(Site site) {
		return crudService.find(Page.class,
				QueryParameters.with("site", site).add("published", true).orderBy("creationDate"))
	}

	@Override
	@Cacheable(value = PageServiceImpl.CACHE_NAME, key = "'pagesWC'+#site.key")
	List<Page> getPagesWithoutCategory(Site site) {
		return crudService.find(Page.class, QueryParameters.with("site", site).add("published", true)
				.add("category", QueryConditions.isNull()).orderBy("creationDate"))
	}

	@Override
	@Cacheable(value = PageServiceImpl.CACHE_NAME, key = "'pages'+#site.key+'cat'+#category.id")
	List<Page> getPages(Site site, PageCategory category) {
		return crudService.find(Page.class,
				QueryParameters.with("site", site).add("category", category).add("category.hidden", false)
						.add("published", true).orderBy("creationDate").paginate(category.paginationSize))

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	int unpublishPages() {
		return crudService.batchUpdate(Page.class, "published", false, QueryParameters.with("neverExpire", false)
				.add("published", true).add("endDate", QueryConditions.geqt(new Date())))
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	int publishPages() {
		return crudService.batchUpdate(Page.class, "published", true, QueryParameters.with("published", false)
				.add("startDate", QueryConditions.geqt(new Date())).add("endDate", QueryConditions.lt(new Date())))
	}

	@Override
	@Cacheable(value = PageServiceImpl.CACHE_NAME, key = "'categories'+#site.key")
	List<PageCategory> getPagesCategories(Site site) {
		return crudService.find(PageCategory.class, QueryParameters.with("site", site).orderBy("name"))
	}

	/**
	 *
	 * @param site
	 * @param alias
	 * @return
	 */
	@Cacheable(value = PageServiceImpl.CACHE_NAME, key = "'cat'+#alias+#site.key")
	@Override
	PageCategory getPageCategoryByAlias(Site site, String alias) {
		return crudService.findSingle(PageCategory.class, QueryParameters.with("site", site).add("alias", alias))
	}

	@Override
	void generateSummary(Page entity) {
		String summary = Jsoup.parse(entity.content).text()
		if (summary.length() > 2000) {
			summary = summary.substring(0, 1996) + "..."
		}
        entity.summary = summary
    }

	@Override
	void generateImageURL(Page entity) {
		String imageURL = Jsoup.parse(entity.content).select("img[src]").attr("src")
        entity.imageURL = imageURL
    }

	/**
	 *
	 * @param site
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@Override
	List<Page> findPagesByDate(Site site, Date startDate, Date endDate) {
		return crudService.find(Page.class,
				QueryParameters.with("site", site).add("published", true)
						.add("creationDate", QueryConditions.between(startDate, endDate)).add("category.hidden", false)
						.add("category.indexableByDates", true))

	}

	
	@Override
	String parsePageContent(Page page, Map<String, Object> model) {
		String content = null
		if (page != null) {
			if (page.templateEngine != null) {
				StringParser parser = StringParsers.get(page.templateEngine)
				if (parser != null) {
					content = parser.parse(page.content, model)
				} else {
					content = page.content
				}
			}
		}

		return content
	}

}

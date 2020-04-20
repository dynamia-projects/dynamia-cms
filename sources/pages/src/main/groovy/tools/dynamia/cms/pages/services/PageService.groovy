/*
 * Copyright (C) 2009 - 2020 Dynamia Soluciones IT S.A.S - NIT 900302344-1
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
package tools.dynamia.cms.pages.services

import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.pages.domain.Page
import tools.dynamia.cms.pages.domain.PageCategory

/**
 *
 * @author Mario Serrano Leones
 */
interface PageService {

    Page loadPage(Site site, String alias)

    Page loadPageByUUID(String uuid)

    List<Page> getPages(Site site)

    int publishPages()

    int unpublishPages()

    List<Page> getPages(Site site, PageCategory category)

    List<Page> getPagesWithoutCategory(Site site)

    List<PageCategory> getPagesCategories(Site site)

    void generateSummary(Page entity)

    void generateImageURL(Page entity)

    /**
     * Find all published pages in indexable by date categories
     *
     * @param site
     * @param startDate
     * @param endDate
     * @return
     */
    List<Page> findPagesByDate(Site site, Date startDate, Date endDate)

    /**
     * 
     * @param site
     * @param alias
     * @return
     */
    PageCategory getPageCategoryByAlias(Site site, String alias)

    /**
     * 
     * @param page
     * @param model
     * @return
     */
	String parsePageContent(Page page, Map<String, Object> model)

}

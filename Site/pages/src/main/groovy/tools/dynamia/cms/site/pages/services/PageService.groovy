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
package tools.dynamia.cms.site.pages.services

import tools.dynamia.cms.site.core.domain.Site
import tools.dynamia.cms.site.pages.domain.Page
import tools.dynamia.cms.site.pages.domain.PageCategory

/**
 *
 * @author Mario Serrano Leones
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

    public void generateSummary(Page entity);

    void generateImageURL(Page entity);

    /**
     * Find all published pages in indexable by date categories
     *
     * @param site
     * @param startDate
     * @param endDate
     * @return
     */
    public List<Page> findPagesByDate(Site site, Date startDate, Date endDate);

    /**
     * 
     * @param site
     * @param alias
     * @return
     */
    PageCategory getPageCategoryByAlias(Site site, String alias);

    /**
     * 
     * @param page
     * @param model
     * @return
     */
	String parsePageContent(Page page, Map<String, Object> model);

}

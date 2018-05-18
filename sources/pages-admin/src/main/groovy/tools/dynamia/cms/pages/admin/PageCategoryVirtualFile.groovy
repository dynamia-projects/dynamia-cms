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
package tools.dynamia.cms.pages.admin

import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.pages.domain.Page
import tools.dynamia.cms.pages.domain.PageCategory
import tools.dynamia.cms.pages.services.PageService
import tools.dynamia.io.VirtualFile

class PageCategoryVirtualFile extends VirtualFile {

    private PageCategory pageCategory
    private PageService pageService
    private Site site
    private List<PageCategoryVirtualFile> subdirectories = new ArrayList<>()

    PageCategoryVirtualFile(String name, Site site, PageService pageService) {
        super(name)
        setName(name)
        this.pageService = pageService
        this.site = site
        init()
    }

    PageCategoryVirtualFile(PageService pageService, PageCategory pageCategory) {
        super(pageCategory.name)
        name = pageCategory.name
        this.pageCategory = pageCategory
        this.pageService = pageService
        init()
    }

    private void init() {
        directory = true
        canWrite = false
    }

    /**
     *
     */
    private static final long serialVersionUID = -4309978768150061535L

    @Override
    List<File> getChildren() {
        List<Page> pages = Collections.EMPTY_LIST
        if (pageCategory != null) {
            pages = pageService.getPages(pageCategory.site, pageCategory)
        } else if (site != null) {
            pages = pageService.getPagesWithoutCategory(site)
        }

        List<File> children = new ArrayList<>()
        children.addAll(subdirectories)
        children.addAll(pages.collect { new PageVirtualFile(it) })

        return children
    }

    void addSubdirectory(PageCategoryVirtualFile directory) {
        subdirectories.add(directory)
    }

    List<PageCategoryVirtualFile> getSubdirectories() {
        return subdirectories
    }

}

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

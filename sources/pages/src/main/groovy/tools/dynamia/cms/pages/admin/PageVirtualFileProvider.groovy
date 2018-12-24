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

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.cms.core.SiteContext
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.pages.domain.PageCategory
import tools.dynamia.cms.pages.services.PageService
import tools.dynamia.integration.sterotypes.Provider
import tools.dynamia.io.VirtualFile
import tools.dynamia.io.VirtualFileProvider

@Provider
class PageVirtualFileProvider implements VirtualFileProvider {

	@Autowired
	private PageService service

    @Override
    List<VirtualFile> getVirtualFiles() {
		List<VirtualFile> files = new ArrayList<>()
        Site site = SiteContext.get().current
        if (site != null) {

			PageCategoryVirtualFile pagesVF = new PageCategoryVirtualFile("pages", site, service)

            List<PageCategory> categories = service.getPagesCategories(site)
            for (PageCategory pageCategory : categories) {
				pagesVF.addSubdirectory(new PageCategoryVirtualFile(service, pageCategory))
            }

			
			files.add(pagesVF)
		} 
		
		return files
    }

}

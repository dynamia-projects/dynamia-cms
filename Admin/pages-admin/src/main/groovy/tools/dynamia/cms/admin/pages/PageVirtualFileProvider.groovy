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
package tools.dynamia.cms.admin.pages

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.cms.site.core.SiteContext
import tools.dynamia.cms.site.core.domain.Site
import tools.dynamia.cms.site.pages.domain.PageCategory
import tools.dynamia.cms.site.pages.services.PageService
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

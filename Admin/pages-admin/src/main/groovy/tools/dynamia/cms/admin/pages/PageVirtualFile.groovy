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

import tools.dynamia.cms.site.core.CMSUtil
import tools.dynamia.cms.site.core.api.URLProvider
import tools.dynamia.cms.site.pages.domain.Page
import tools.dynamia.io.VirtualFile

class PageVirtualFile extends VirtualFile implements URLProvider {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L
    private Page page

    PageVirtualFile(Page page) {
		super(page.alias)
        this.page = page
        name = page.title +".html"
        canWrite = false
        directory = false

    }

	@Override
    String getURL() {
		return CMSUtil.getSiteURL(page.site, page.alias)
    }

	@Override
    boolean isFile() {
		return true
    }
	
	@Override
    long lastModified() {
		return page.creationDate.time
    }
}

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
package tools.dynamia.cms.pages.admin

import tools.dynamia.cms.core.CMSUtil
import tools.dynamia.cms.core.api.URLProvider
import tools.dynamia.cms.pages.domain.Page
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

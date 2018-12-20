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

package tools.dynamia.cms.admin.ui.ext

import org.zkoss.zul.A
import org.zkoss.zul.Label
import org.zkoss.zul.Vlayout
import tools.dynamia.cms.core.CMSUtil
import tools.dynamia.cms.core.SiteContext
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.integration.sterotypes.Provider
import tools.dynamia.io.FileInfo
import tools.dynamia.modules.filemanager.FileManagerPreviewExtension

@Provider
class ViewResourceLinkPreviewExtension implements FileManagerPreviewExtension {

	@Override
    Object getView(FileInfo selectedFile) {
		if(selectedFile.directory){
			return null
        }
		

		Site site = SiteContext.get().current
        Vlayout layout = new Vlayout()
        layout.appendChild(new Label("URL"))
        layout.sclass = "container"
        layout.width = "100%"

        A absolute = new A(CMSUtil.getResourceURL(site, selectedFile.file))
        absolute.href = absolute.label
        absolute.target = "_blank"
        absolute.style = "word-wrap: break-word"
        absolute.tooltiptext = "Open in new tab"
        layout.appendChild(absolute)

        layout.appendChild(new Label("Relative Path"))
        A relative = new A(CMSUtil.getResourceRelativePath(site, selectedFile.file))
        relative.href = absolute.href
        relative.target = "_blank"
        relative.style = "word-wrap: break-word"
        relative.tooltiptext = "Open in new tab"
        layout.appendChild(relative)

        return layout

    }

}

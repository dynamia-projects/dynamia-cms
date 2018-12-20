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
package tools.dynamia.cms.admin.ui.actions

import org.zkoss.zul.Messagebox
import tools.dynamia.actions.ActionEvent
import tools.dynamia.actions.InstallAction
import tools.dynamia.cms.core.CMSUtil
import tools.dynamia.cms.core.SiteContext
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.domain.services.CrudService
import tools.dynamia.integration.Containers
import tools.dynamia.io.FileInfo
import tools.dynamia.modules.filemanager.FileManagerAction
import tools.dynamia.ui.MessageType
import tools.dynamia.ui.UIMessages

@InstallAction
class ViewResourceLinkAction extends FileManagerAction {

    ViewResourceLinkAction() {
        image = "link"
        name = "View Link"
        position = 20
        menuSupported = true

    }

    @Override
    void actionPerformed(ActionEvent evt) {
        List<FileInfo> files = new ArrayList<>()

        if (evt.data instanceof FileInfo) {
            files.add((FileInfo) evt.data)
        } else if (evt.data instanceof List) {
            files.addAll((Collection<? extends FileInfo>) evt.data)
        }

        if (!files.isEmpty()) {
            try {

                CrudService crudService = Containers.get().findObject(CrudService.class)
                Site site = SiteContext.get().current
                site = crudService.reload(site)

                String url = ""

                for (FileInfo file : files) {
                    url = "$url${CMSUtil.getSiteURL(site, CMSUtil.getResourceURL(site, file.file))}\n"
                }
                url = url.replace("//", "/")
                Messagebox.show(url, "Link", Messagebox.OK, null)
            } catch (Exception e) {
                e.printStackTrace()
            }
        } else {
            UIMessages.showMessage("Selecte a file first", MessageType.ERROR)
        }

    }

}

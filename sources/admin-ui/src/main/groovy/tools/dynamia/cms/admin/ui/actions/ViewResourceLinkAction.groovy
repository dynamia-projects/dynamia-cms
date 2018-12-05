/*
 *  Copyright (c) 2018 Dynamia Soluciones IT SAS and the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
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
        position = Double.MAX_VALUE
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

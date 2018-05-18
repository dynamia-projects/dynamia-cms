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
package tools.dynamia.cms.admin.ui.ui

import org.zkoss.zk.ui.WrongValueException
import org.zkoss.zk.ui.event.Event
import org.zkoss.zk.ui.event.Events
import org.zkoss.zul.Bandbox
import tools.dynamia.actions.FastAction
import tools.dynamia.cms.core.CMSUtil
import tools.dynamia.cms.core.DynamiaCMS
import tools.dynamia.cms.core.SiteContext
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.pages.services.PageService
import tools.dynamia.commons.StringUtils
import tools.dynamia.integration.Containers
import tools.dynamia.io.FileInfo
import tools.dynamia.modules.filemanager.FileManager
import tools.dynamia.ui.MessageType
import tools.dynamia.ui.UIMessages
import tools.dynamia.viewers.impl.DefaultViewDescriptor
import tools.dynamia.zk.BindingComponentIndex
import tools.dynamia.zk.ComponentAliasIndex
import tools.dynamia.zk.util.ZKUtil
import tools.dynamia.zk.viewers.ui.Viewer

class ResourceSelector extends Bandbox {

    private static final String CMS_FILE_MANAGER_DESCRIPTOR = "CMSFileManagerDescriptor"
    /**
     *
     */
    private static final long serialVersionUID = 1L

    static {
        ComponentAliasIndex.instance.add(ResourceSelector.class)
        BindingComponentIndex.instance.put("value", ResourceSelector.class)
    }

    private boolean useRelativePath

    ResourceSelector() {
        initUI()
        initEvents()
    }

    private void initUI() {
        buttonVisible = true
        autodrop = false
        open = false
    }

    private void initEvents() {
        addEventListener(Events.ON_OPEN, { showResourceSelector() })

    }

    private void showResourceSelector() {
        Site site = SiteContext.get().current

        PageService service = Containers.get().findObject(PageService.class)
        Viewer viewer = new Viewer(new DefaultViewDescriptor(ResourceSelector.class, "fileManager", false))

        FileManager view = (FileManager) viewer.view
        view.rootDirectory = DynamiaCMS.getSitesResourceLocation(site).toFile()

        viewer.addAction(new FastAction("Select Resource", {
            FileInfo fileInfo = view.value
            if (fileInfo != null) {
                if (useRelativePath) {
                    value = CMSUtil.getResourceRelativePath(SiteContext.get().current, fileInfo.file)
                } else {
                    value = CMSUtil.getResourceURL(SiteContext.get().current, fileInfo.file)
                }
                viewer.parent.detach()
                UIMessages.showMessage("Resource selected")
            } else {
                UIMessages.showMessage("No resource selected", MessageType.WARNING)
            }
        }))

        ZKUtil.showDialog("Select Resource", viewer, "90%", "90%")
    }

    @Override
    void setValue(String value) throws WrongValueException {
        if (!StringUtils.equals(getValue(), value)) {
            super.setValue(value)
            Events.postEvent(new Event(Events.ON_CHANGE, this, value))
        }
    }

    boolean isUseRelativePath() {
        return useRelativePath
    }

    void setUseRelativePath(boolean useRelativePath) {
        this.useRelativePath = useRelativePath
    }

}

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
package tools.dynamia.cms.admin.ui

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

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
package tools.dynamia.cms.admin.ui.controllers

import org.springframework.stereotype.Controller
import org.zkoss.bind.annotation.*
import org.zkoss.zk.ui.Component
import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.select.Selectors
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zk.ui.util.Clients
import tools.dynamia.cms.core.CMSUtil
import tools.dynamia.cms.core.SiteContext
import tools.dynamia.io.FileInfo
import tools.dynamia.modules.filemanager.FileManager
import tools.dynamia.ui.MessageType
import tools.dynamia.ui.UIMessages

@Controller
class FileBrowserVM {

    @Wire("#fileMgr")
    private FileManager fileMgr

    private String baseUrl
    private int ckEditorFuncNum
    private String ckEditor

    @Init
    void init(@ExecutionParam("CKEditor") String ckeditor,
              @ExecutionParam("CKEditorFuncNum") int ckEditorFuncNum,
              @ExecutionParam("baseUrl") String baseUrl) {

        this.baseUrl = baseUrl
        this.ckEditorFuncNum = ckEditorFuncNum
        this.ckEditor = ckeditor

    }

    @AfterCompose
    void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false)
    }

    @Command
    void select() {
        FileInfo resource = fileMgr.value
        if (resource != null) {
            String path = CMSUtil.getResourceRelativePath(SiteContext.get().current, resource.file)
            String script = "window.opener.CKEDITOR.tools.callFunction(" +
                    ckEditorFuncNum + ", '" + Executions.current.encodeURL(path) + "'); window.close(); "

            Clients.evalJavaScript(script)
        } else {
            UIMessages.showMessage("No file selected", MessageType.ERROR)
        }

    }

}

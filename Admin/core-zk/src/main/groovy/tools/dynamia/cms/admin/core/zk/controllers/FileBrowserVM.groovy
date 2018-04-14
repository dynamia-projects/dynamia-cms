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
package tools.dynamia.cms.admin.core.zk.controllers

import org.springframework.stereotype.Controller
import org.zkoss.bind.annotation.*
import org.zkoss.zk.ui.Component
import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.select.Selectors
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zk.ui.util.Clients
import tools.dynamia.cms.site.core.CMSUtil
import tools.dynamia.cms.site.core.SiteContext
import tools.dynamia.cms.site.pages.services.PageService
import tools.dynamia.integration.Containers
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
        PageService pageService = Containers.get().findObject(PageService.class)

    }

	@Command
    void select() {
		FileInfo resource = fileMgr.value
        if (resource != null) {
			String path = CMSUtil.getResourceURL(SiteContext.get().current, resource.file)
            String script = "window.opener.CKEDITOR.tools.callFunction(" +
					ckEditorFuncNum + ", '" + Executions.current.encodeURL(path) + "'); window.close(); "

            Clients.evalJavaScript(script)
        } else {
			UIMessages.showMessage("No file selected", MessageType.ERROR)
        }

	}

}

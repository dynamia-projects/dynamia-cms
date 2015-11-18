package com.dynamia.cms.admin.core.zk.controllers;

import org.springframework.stereotype.Controller;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;

import com.dynamia.cms.site.core.CMSUtil;
import com.dynamia.cms.site.core.SiteContext;
import com.dynamia.cms.site.pages.services.PageService;
import com.dynamia.modules.filemanager.FileManager;

import tools.dynamia.integration.Containers;
import tools.dynamia.io.FileInfo;
import tools.dynamia.ui.MessageType;
import tools.dynamia.ui.UIMessages;

@Controller
public class FileBrowserVM {

	@Wire("#fileMgr")
	private FileManager fileMgr;

	private String baseUrl;
	private int ckEditorFuncNum;
	private String ckEditor;

	@Init
	public void init(@ExecutionParam("CKEditor") String ckeditor,
			@ExecutionParam("CKEditorFuncNum") int ckEditorFuncNum,
			@ExecutionParam("baseUrl") String baseUrl) {

		this.baseUrl = baseUrl;
		this.ckEditorFuncNum = ckEditorFuncNum;
		this.ckEditor = ckeditor;

	}

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		PageService pageService = Containers.get().findObject(PageService.class);
		
	}

	@Command
	public void select() {
		FileInfo resource = fileMgr.getValue();
		if (resource != null) {
			String path = CMSUtil.getResourceURL(SiteContext.get().getCurrent(), resource.getFile());
			String script = "window.opener.CKEDITOR.tools.callFunction(" +
					ckEditorFuncNum + ", '" + Executions.getCurrent().encodeURL(path) + "'); window.close(); ";

			Clients.evalJavaScript(script);
		} else {
			UIMessages.showMessage("No file selected", MessageType.ERROR);
		}

	}

}

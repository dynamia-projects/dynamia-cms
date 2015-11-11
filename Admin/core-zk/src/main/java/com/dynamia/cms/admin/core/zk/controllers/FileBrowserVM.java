package com.dynamia.cms.admin.core.zk.controllers;

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

import com.dynamia.modules.filemanager.FileManager;
import com.dynamia.tools.integration.sterotypes.Controller;
import com.dynamia.tools.web.ui.MessageType;
import com.dynamia.tools.web.ui.UIMessages;

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
	}

	@Command
	public void select() {
		String filePath = fileMgr.getSelectedFilePath();
		if (filePath != null && !filePath.isEmpty()) {
			filePath = filePath.replace("\\", "/");

			String separator = "";
			if (!baseUrl.endsWith("/")) {
				separator = "/";
			}

			String path = baseUrl + separator + filePath;

			String script = "window.opener.CKEDITOR.tools.callFunction(" +
					ckEditorFuncNum + ", '" + Executions.getCurrent().encodeURL(path) + "'); window.close(); ";

			Clients.evalJavaScript(script);
		} else {
			UIMessages.showMessage("No file selected", MessageType.ERROR);
		}

	}

}

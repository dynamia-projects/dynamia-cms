package com.dynamia.cms.admin.core.zk.actions;

import static com.dynamia.tools.viewers.ViewDescriptorBuilder.field;
import static com.dynamia.tools.viewers.ViewDescriptorBuilder.viewDescriptor;

import java.io.File;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Window;

import com.dynamia.cms.admin.core.zk.actions.model.Folder;
import com.dynamia.modules.filemanager.FileManager;
import com.dynamia.modules.filemanager.FileManagerAction;
import com.dynamia.tools.viewers.ViewDescriptor;
import com.dynamia.tools.viewers.zk.ui.Viewer;
import com.dynamia.tools.web.actions.ActionEvent;
import com.dynamia.tools.web.actions.InstallAction;
import com.dynamia.tools.web.ui.MessageType;
import com.dynamia.tools.web.ui.UIMessages;
import com.dynamia.tools.web.util.ZKUtil;

@InstallAction
public class AddFolderResourceAction extends FileManagerAction {

	public AddFolderResourceAction() {
		setName("New Directory");

		setImage("add-folder");
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		final FileManager fileManager = (FileManager) evt.getSource();

		Folder form = new Folder();
		if (fileManager.getCurrentDirectory() != null) {
			form.setParent(fileManager.getCurrentDirectory().getName());
		}

		ViewDescriptor descriptor = viewDescriptor("form", Folder.class, true)
				.sortFields("name", "parent", "root")
				.fields(
						field("parent", "Parent", "label"))
				.layout("columns", "1")
				.build();

		final Viewer viewer = new Viewer(descriptor, form);
		viewer.setWidth("600px");

		final Window window = ZKUtil.createWindow("New Directory");

		window.appendChild(viewer);

		Button btn = new Button("Create Directory");
		btn.setStyle("float:right");
		window.appendChild(btn);
		btn.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				Folder form = (Folder) viewer.getValue();
				File parent = fileManager.getRootDirectory();
				if (!form.isRoot() && fileManager.getCurrentDirectory() != null) {
					parent = fileManager.getCurrentDirectory();
				}
				if (form.getName() != null && !form.getName().isEmpty()) {

					File newdir = new File(parent, form.getName());
					if (!newdir.exists()) {
						newdir.mkdirs();
						window.detach();
						UIMessages.showMessage("Directory " + form.getName() + " created successfully");
						fileManager.reloadSelected();
					} else {
						UIMessages.showMessage("Already exists  directory with name " + form.getName(), MessageType.ERROR);
					}
				} else {
					UIMessages.showMessage("Enter directory name", MessageType.ERROR);
				}
			}
		});

		window.doModal();

	}

}

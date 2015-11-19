package com.dynamia.cms.admin.core.zk.ui;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Bandbox;

import com.dynamia.cms.site.core.CMSUtil;
import com.dynamia.cms.site.core.DynamiaCMS;
import com.dynamia.cms.site.core.SiteContext;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.pages.services.PageService;
import com.dynamia.modules.filemanager.FileManager;

import tools.dynamia.actions.FastAction;
import tools.dynamia.integration.Containers;
import tools.dynamia.io.FileInfo;
import tools.dynamia.ui.MessageType;
import tools.dynamia.ui.UIMessages;
import tools.dynamia.viewers.impl.DefaultViewDescriptor;
import tools.dynamia.zk.BindingComponentIndex;
import tools.dynamia.zk.ComponentAliasIndex;
import tools.dynamia.zk.util.ZKUtil;
import tools.dynamia.zk.viewers.ui.Viewer;

public class ResourceSelector extends Bandbox {

	private static final String CMS_FILE_MANAGER_DESCRIPTOR = "CMSFileManagerDescriptor";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static {
		ComponentAliasIndex.getInstance().add(ResourceSelector.class);
		BindingComponentIndex.getInstance().put("value", ResourceSelector.class);
	}

	public ResourceSelector() {
		initUI();
		initEvents();
	}

	private void initUI() {
		setButtonVisible(true);
		setAutodrop(false);
		setOpen(false);
	}

	private void initEvents() {
		addEventListener(Events.ON_OPEN, e -> showResourceSelector());

	}

	private void showResourceSelector() {
		Site site = SiteContext.get().getCurrent();

		PageService service = Containers.get().findObject(PageService.class);
		Viewer viewer = new Viewer(new DefaultViewDescriptor(ResourceSelector.class, "fileManager", false));

		FileManager view = (FileManager) viewer.getView();
		view.setRootDirectory(DynamiaCMS.getSitesResourceLocation(site).toFile());

		viewer.addAction(new FastAction("Select Resource", evt -> {
			FileInfo fileInfo = view.getValue();
			if (fileInfo != null) {
				setValue(CMSUtil.getResourceURL(SiteContext.get().getCurrent(), fileInfo.getFile()));
				viewer.getParent().detach();
				UIMessages.showMessage("Resource selected");
			} else {
				UIMessages.showMessage("No resource selected", MessageType.WARNING);
			}
		}));

		ZKUtil.showDialog("Select Resource", viewer, "90%", "90%");
	}

	@Override
	public void setValue(String value) throws WrongValueException {
		if (getValue() != value) {
			super.setValue(value);
			Events.postEvent(new Event(Events.ON_CHANGE, this, value));
		}
	}

}

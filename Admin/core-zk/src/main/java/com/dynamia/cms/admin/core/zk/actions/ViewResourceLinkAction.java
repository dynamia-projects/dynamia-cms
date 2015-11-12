package com.dynamia.cms.admin.core.zk.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Messagebox;

import com.dynamia.cms.site.core.SiteContext;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.core.domain.SiteDomain;
import com.dynamia.modules.filemanager.FileManagerAction;
import com.dynamia.tools.domain.services.CrudService;
import com.dynamia.tools.integration.Containers;
import com.dynamia.tools.io.FileInfo;
import com.dynamia.tools.web.actions.ActionEvent;
import com.dynamia.tools.web.actions.InstallAction;
import com.dynamia.tools.web.ui.MessageType;
import com.dynamia.tools.web.ui.UIMessages;

@InstallAction
public class ViewResourceLinkAction extends FileManagerAction {

	public ViewResourceLinkAction() {
		setImage("link");
		setName("View Link");
		setPosition(Double.MAX_VALUE);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		List<FileInfo> files = new ArrayList<>();

		if (evt.getData() instanceof FileInfo) {
			files.add((FileInfo) evt.getData());
		} else if (evt.getData() instanceof List) {
			files.addAll((Collection<? extends FileInfo>) evt.getData());
		}

		if (!files.isEmpty()) {
			try {
				CrudService crudService = Containers.get().findObject(CrudService.class);
				Site site = SiteContext.get().getCurrent();
				site = crudService.reload(site);
				SiteDomain domain = site.getAcceptedDomains().get(0);
				String port = "";
				if (domain.getPort() > 0) {
					port = ":" + domain.getPort();
				}
				String url = "";
				for (FileInfo file : files) {
					url = url + getFileURL(file, site, domain, port) + "\n";
				}

				Messagebox.show(url, "Link", Messagebox.OK, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			UIMessages.showMessage("Selecte a file first", MessageType.ERROR);
		}

	}

	private String getFileURL(FileInfo file, Site site, SiteDomain domain, String port) {
		String path = file.getFile().getPath();
		path = path.substring(path.indexOf(site.getKey()) + site.getKey().length() + 1);

		String url = String.format("http://%s%s/resources/%s", domain.getName(), port, path);
		url = url.trim().replace(" ", "%20").replace("\\", "/");
		url = Executions.getCurrent().encodeURL(url);
		return url;
	}

}

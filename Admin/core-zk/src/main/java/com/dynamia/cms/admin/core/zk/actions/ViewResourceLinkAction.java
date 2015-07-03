package com.dynamia.cms.admin.core.zk.actions;

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
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		FileInfo file = (FileInfo) evt.getData();
		
		
		if (file != null) {
			try {
				CrudService crudService = Containers.get().findObject(CrudService.class);
				Site site = SiteContext.get().getCurrent();
				site = crudService.reload(site);
				SiteDomain domain = site.getAcceptedDomains().get(0);
				String port = "";
				if (domain.getPort() > 0) {
					port = ":" + domain.getPort();
				}
				String path = file.getFile().getPath();
				path = path.substring(path.indexOf(site.getKey())+site.getKey().length()+1);

				String url = String.format("http://%s%s/resources/%s", domain.getName(), port, path);
				Messagebox.show(url,"Link",Messagebox.OK,null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			UIMessages.showMessage("Selecte a file first", MessageType.ERROR);
		}

	}

}

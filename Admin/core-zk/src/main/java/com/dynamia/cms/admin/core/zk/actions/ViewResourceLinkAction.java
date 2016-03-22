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
package com.dynamia.cms.admin.core.zk.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Messagebox;

import com.dynamia.cms.site.core.SiteContext;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.core.domain.SiteDomain;

import tools.dynamia.actions.ActionEvent;
import tools.dynamia.actions.InstallAction;
import tools.dynamia.domain.services.CrudService;
import tools.dynamia.integration.Containers;
import tools.dynamia.io.FileInfo;
import tools.dynamia.modules.filemanager.FileManagerAction;
import tools.dynamia.ui.MessageType;
import tools.dynamia.ui.UIMessages;

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

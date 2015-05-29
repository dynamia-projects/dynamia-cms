/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.admin.core.zk.actions;

import org.springframework.beans.factory.annotation.Autowired;

import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.core.services.SiteService;
import com.dynamia.tools.commons.ApplicableClass;
import com.dynamia.tools.web.actions.InstallAction;
import com.dynamia.tools.web.crud.AbstractCrudAction;
import com.dynamia.tools.web.crud.CrudActionEvent;
import com.dynamia.tools.web.crud.CrudState;
import com.dynamia.tools.web.ui.MessageType;
import com.dynamia.tools.web.ui.UIMessages;

/**
 *
 * @author mario_2
 */
@InstallAction
public class ClearSiteCacheAction extends AbstractCrudAction {

	@Autowired
	private SiteService service;

	public ClearSiteCacheAction() {
		setName("Clear Cache");
		setImage("refresh");
		setMenuSupported(true);
	}

	@Override
	public void actionPerformed(CrudActionEvent evt) {
		Site site = (Site) evt.getData();
		if (site != null) {
			service.clearCache(site);
			UIMessages.showMessage("Site Cache cleared successfull");
		} else {
			UIMessages.showMessage("Select site", MessageType.WARNING);
		}
	}

	@Override
	public CrudState[] getApplicableStates() {
		return CrudState.get(CrudState.READ);
	}

	@Override
	public ApplicableClass[] getApplicableClasses() {
		return ApplicableClass.get(Site.class);
	}

	

}

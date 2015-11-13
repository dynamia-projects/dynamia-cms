/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.admin.core.zk.actions;

import org.springframework.beans.factory.annotation.Autowired;

import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.core.services.SiteService;

import tools.dynamia.actions.InstallAction;
import tools.dynamia.commons.ApplicableClass;
import tools.dynamia.crud.AbstractCrudAction;
import tools.dynamia.crud.CrudActionEvent;
import tools.dynamia.crud.CrudState;
import tools.dynamia.ui.MessageType;
import tools.dynamia.ui.UIMessages;

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

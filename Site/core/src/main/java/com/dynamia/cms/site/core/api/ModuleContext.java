/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dynamia.cms.site.core.api;

import com.dynamia.cms.site.core.domain.ModuleInstance;
import com.dynamia.cms.site.core.domain.ModuleInstanceParameter;
import com.dynamia.cms.site.core.domain.Site;

/**
 *
 * @author mario
 */
public class ModuleContext {

	private ModuleInstance moduleInstance;
	private Site site;

	public ModuleContext(ModuleInstance moduleInstance) {
		super();
		this.moduleInstance = moduleInstance;
		this.site = moduleInstance.getSite();
	}

	public ModuleInstance getModuleInstance() {
		return moduleInstance;
	}

	public ModuleInstanceParameter getParameters(String name) {
		return moduleInstance.getParameter(name);
	}

	public Site getSite() {
		return site;
	}

}

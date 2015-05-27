package com.dynamia.cms.admin.core.zk.controllers;

import com.dynamia.cms.site.core.domain.ModuleInstance;
import com.dynamia.cms.site.core.domain.ModuleInstanceParameter;
import com.dynamia.tools.web.crud.CrudController;

public class ModuleInstanceParameterController extends CrudController<ModuleInstanceParameter> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3698793765557086771L;

	@Override
	public void newEntity() {
		// TODO Auto-generated method stub
		super.newEntity();

		try {
			getEntity().setModuleInstance((ModuleInstance) getParameter("moduleInstance"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
